package com.tarmofolk.trubez.service;

import com.tarmofolk.trubez.domain.Role;
import com.tarmofolk.trubez.domain.User;
import com.tarmofolk.trubez.repos.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/** это бизнес - логика. сюда нужно вставлять всю логику из контроллеров
 *  сервис дает понять спрингу что это компонет.*/


@Service
public class UserService implements UserDetailsService {

    //   @Autowired
    //   private UserRepo userRepo;
    //   замена автовайреда
    private final UserRepo userRepo;
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override        // ищу в юзер репо по имени юзера, проверяю, что юзер есть, иначе эксепшн
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException ("User not found");
        }
        return user;
    }


    public boolean addUser (User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());    // пп находим в репо юзера по имени

        if (userFromDb != null) {                                         // если ползователь уже есть
            return false;
        }
        user.setActive(true);                                             // делаем активным
        user.setRoles(Collections.singleton(Role.USER));                  // пристваиваем роль, ожидается на вход сет ролей, но мы делаем одну роль - юзер
        user.setActivationCode(UUID.randomUUID().toString());             // даем юзеру через универсальный уникальный идентификатор рандомный текст, код, цифры хз???
        user.setPassword(passwordEncoder.encode(user.getPassword()));    // шифруем пароль через енкодер

        userRepo.save(user);                                              // сохраняем в бд
        sendMessage(user);

        return true;
    }


   /**  отправляем пользователю оповещение*/
    private void sendMessage(User user) {

        if (!StringUtils.isEmpty(user.getEmail()))  {                     // проверяем через стрингУтилс если есть емаил
            String message = String.format(                              // создадим сообщение через св-во стринга форматированная строка
                    "Hello, %s! \n" +                                    // привет, имя пользователя, перевод строки
                            // пользователь в своей почте перейдет по ссылке сайт\ активэйт \ код активации

                            "Welcome. Please, visit next link: http://localhost/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
                                                                          // то мэил сендер отправляет сообщение
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    /**проверяем активацию юзера*/

    public boolean activateUser(String code) {
        /**метод ищет юзера по активэйшн коду в репозитории*/
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        /**установим актив.код в нулл, это значит что юзер подтвердил его*/
        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    /**находим юзеров в репозитории*/
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**сохранение юзера*/
    public void saveUser(User user, String username, Map<String, String> form) {
        // устанавливае новое имя пользователя
        user.setUsername(username);

        // устанавиваем новую роль юзеру
        // сохраняем в виде списка ролей
        // получаем  список этих ролей из енама
        // приводим к стоковому виду
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        // берем пользователя и очищаем все его роли
        user.getRoles().clear();

        // проверяем что форма содержит роли для данного пользователя
        // берем форму. получаем список ключей. и итерируем по ней
        // если в форме присутствует роль то в интерфейсе у нее утановлен флажок
        for (String key : form.keySet()) {
            // проверяем что роли содержат ключ
            if (roles.contains(key)) {
                // тогда нашему пользователю добавляем роль
                // берем пользователя. добавляем из енама роль по ключу
                user.getRoles().add(Role.valueOf(key));
            }
        }
        // сохраняем юзера в репо
        userRepo.save(user);
    }

    }

