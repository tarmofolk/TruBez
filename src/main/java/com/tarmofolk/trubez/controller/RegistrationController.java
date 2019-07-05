package com.tarmofolk.trubez.controller;


import com.tarmofolk.trubez.domain.User;
import com.tarmofolk.trubez.domain.dto.CaptchaResponseDto;
import com.tarmofolk.trubez.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;




/** контроллер для регистрации*/

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";


    @Autowired
    private UserService userService;

    /** автоварим метод, который позволяет общаться с внешними приложениями при помощи рест запросов (с гугл капчой).
     */
    @Autowired
    private RestTemplate restTemplate;

    /** из пропертей достаем секретный ключ гугловой рекаптчи  */
    @Value("${recaptcha.secret}")
    private String secret;



    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }


     /**добавление пользователей
      * поле подтверждения пароля
      * гугловская каптчаОтвет
      * валидируем юзера
      * список сообщений и ошибок валидации (всегда должен идти перед аргументом model)
      * модель куда кладем все (UI)
      * */
    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {

        /** заполняем шаблон в котором содержится 1 начало урла, 2 сикрет и 3 ответ/ (п.п. полный урл)
         *  отправляем пост запросом через рест шаблон:*/
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        /** если респонс неуспешный,  то надо сообщить пользоватею (добавить в модель ошибку) */
        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }

        /** переменная проверки пустого подтверждения пароля */
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        /** Проверяем чтобы поле подтверждения пароля было не пустым
         * если нет то добавляем в модель атрибут ошибка пароля */
        if (isConfirmEmpty) {
            model.addAttribute("passwordError", "Password confirmation cannot be empty");
        }

        /**Проверяем сходятся ли 2 пароля которые ввел пользователь при регистрации
         * если нет то добавляем в модель атрибут ошибка пароля*/
        if (user.getPassword() !=null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Password are different!");
        }
        /**если пусто подвержение пароля или список ошибок имеет эти самые ошибки, или не прошла каптча, то делаем обработку ошибок*/
        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {

        /** мэп с ошибками. получает на вход список ошибок и обрабатывает точно такое же в юзер контроллере*/
            Map<String, String> errorMap = ErrorController.getErrors(bindingResult);

            /** сливаем воедино и помещаем в модель все ошибки
             *  и возвращаем на страницу регистрации*/
            model.mergeAttributes(errorMap);
            return "registration";
        }
        /**     если пользователь уже есть
         * помещаем в модель ошибку что юзер уже есть
        * и возвращаем обратно на регистрацию
        * а при успешной регистрации делаем редирект на страницу логина*/
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }










   /** сюда будет перекидывать ссылка из полученного письма кодом*/
    @GetMapping("/activate/{code}")
   public String activate (Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        /**сообщаем контроллеру как прошла активация
         * если успешно, то выводим сообщение и говорим, что успешно (во вьюху логин), иначе наоборот*/
        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "user successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "activation code is not found!");
        }
        return  "login";

    }

}
