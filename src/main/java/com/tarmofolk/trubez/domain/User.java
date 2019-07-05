package com.tarmofolk.trubez.domain;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;




@Entity                                               // объект базы данных (сущность) то, что хранится в бд
@Table(name = "usr")                                  // будет храниться в таблице с именем usr
public class User implements UserDetails {


    /** объясняет, что поле id - идентификатор
     * нужен, чтобы различать записи в таблице*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**предупереждает юзера, что юзернейм не может быть пустым*/
    @NotBlank(message = "Username cannot be empty")
    private String username;

    /**предупереждает юзера, что пароль не может быть пустым*/
    @NotBlank(message = "Password cannot be empty")
    private String password;

    private boolean active;

    /** проверяем, что это именно емэил
     * предупереждает юзера, что email не может быть пустым*/
    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    private String activationCode;

    // не делаем доп структуру чтобы хранить роли. анот упрощает все это. подгружаем жадным способомн
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
        // данное поле будет храниться в отдельной таблице для которой мы не описывали мэпинг
        // эта таблица с именем user_role будет соединяться с основной черед user_id
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)                      // храним этот енам в виде строки
    private Set<Role> roles;                          // содержит набор (сет) полей роли (админ, пользователь, бухгалтер и т.д.)


    // проверяет является ли пользоваетль админом
    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    // проверяет является ли пользоваетль продаваном
    public boolean isSalesman() {
        return roles.contains(Role.SALESMAN);
    }
    // проверяет является ли пользоваетль бухалтером
    public boolean isBooker() {
        return roles.contains(Role.BOOKER);
    }






    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return getRoles();
    }

    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return isActive();
    }

}
