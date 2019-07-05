package com.tarmofolk.trubez.controller;


import com.tarmofolk.trubez.domain.Employee;
import com.tarmofolk.trubez.domain.User;
import com.tarmofolk.trubez.repos.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


// контроллер мэпингов главной страницы

@Controller
public class MainController {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Value("${upload.path}")                                // проверяем есть ли директория куда сохранять файлы
    private String uploadPath;                             // ищет в пропертис директорию путь и вставляет в переменную


    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }


    @GetMapping("/main")                                       //при входе на мэпинг main
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
                       Model model) {                             // в модель складываются данные прежде чем вернуть
        Iterable<Employee> employees = employeeRepo.findAll();      // пр интерфейсу итеребл все что нашли в репозитории

        if (filter != null && !filter.isEmpty()) {               // если фильтр вообще задан и фильтр не пустой
            employees = employeeRepo.findByFirm(filter);            // пп все что находим по тэгу фильтр
        } else {
            employees = employeeRepo.findAll();                    // иначе все кладем
        }
        model.addAttribute("employees", employees);    // в поле модели кладем то, что нашли в репозитории по логике выше, оно отобразится в вьюхе
        model.addAttribute("filter", filter);        // в поле filter  модели кладем то, что получили от пользователя по value = filter

        return "main";                                            //возвращает пользователю страницу main  со всеми полями
    }



 /**    обрабатываем поля формы ввода. форма будет отправляться на main
  *     принимаем на вход текущего пользователя
  *                       сообщение (аннотация проверяет на валидность??)
  *                       список сообщений и ошибок валидации (всегда должен идти перед аргументом model)
  *                       модель куда кладем все (UI)
  *                       необязательный параметр файл картинки (загружается в виде кучки фрагментов)
  *    обрабатываем исключения хз какое */

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @Valid Employee employee,
                      BindingResult bindingResult,
                      Model model,
                      @RequestParam("file") MultipartFile file
    ) throws IOException {

       employee.setAuthor(user);

        /**если список ошибок имеет эти самые ошибки то делаем обработку ошибок*/
        if (bindingResult.hasErrors()) {
           /** мэп с ошибками. получает на вход список ошибок и обрабатывает*/
            Map<String, String> errorMap = ErrorController.getErrors(bindingResult);

            /** помещаем в модель все ошибки
             *  помещаем в модель сообщение*/
            model.mergeAttributes(errorMap);
            model.addAttribute("employee", employee);

        /** иначе продолжаем собирать*/
        } else {

            // только если мы пытаемся загружать файл и имя файла задано
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);              // директория где лежит

                // обезопасим что директории не существует
                if (!uploadDir.exists()) {                          // если она не существует
                    uploadDir.mkdir();                               // то создаем
                }

                // обезопасим от коллизии имен
                String uuidFile = UUID.randomUUID().toString();      // универсальный уникальный идентификатор делает рандомный префикс
                String resultFilename = uuidFile + "." + file.getOriginalFilename();  // результирующий файл


                file.transferTo(new File(uploadPath + "/" + resultFilename));

                employee.setFilename(resultFilename);
            }
            /**подставляем в поле  нулевое значение, чтобы убрать из полей значения если нормально создали сообщение*/
            model.addAttribute("message", null);
// сохраняем в репозиторий message Repo
            employeeRepo.save(employee);
        }

        Iterable<Employee> employees = employeeRepo.findAll();     // п.м  достаем все из репозитрия

        model.addAttribute("employees", employees);   // кладем в модель (со всеми полями внутри)

        return "main";                                          //возвращает пользователю страницу main со всеми полями

    }

}