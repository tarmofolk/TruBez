package com.tarmofolk.trubez.controller;


import com.tarmofolk.trubez.domain.Role;
import com.tarmofolk.trubez.domain.User;
import com.tarmofolk.trubez.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


// контроллер пользователей

@Controller
@RequestMapping("/user")                      // все мэпинги ниже бдут иметь префикс юзер а дальше \ свое
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * будет перед каждым выполнением этого метода проверять наличие у юзера админских прав
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    // мэпинг для редактора пользователя
    @GetMapping("{user}")                      // кроме мэпинга user будет добавляться ID пользователя
    public String userEditForm(@PathVariable User user,  // получаем пользователя из бд
                               Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());    // чтобы вывсти список ролей из енама роли (все его значения)
        return "userEdit";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    // Метод сохранения в бд
    @PostMapping
    public String userSave(
            // получаем с сервера параметры
            // имя
            @RequestParam String username,
            // форма в виде мэпа с ключами и значениями в виде строки хз что это
            @RequestParam Map<String, String> form,
            // по параметру юзер айди пользователя из бд
            @RequestParam("userId") User user
    ) {

        userService.saveUser(user, username, form);

        // делаем редирект на список пользователей
        return "redirect:/user";
    }

}
