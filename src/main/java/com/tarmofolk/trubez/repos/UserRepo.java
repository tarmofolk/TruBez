package com.tarmofolk.trubez.repos;

import com.tarmofolk.trubez.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;



    // хранилище юзеров

public interface UserRepo extends JpaRepository<User, Long> {

    /** возвращает пользоваетеля*/
    User findByUsername(String username);

    /**ищет юзера по активэйшн коду*/
    User findByActivationCode(String code);
}
