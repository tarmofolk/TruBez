package com.tarmofolk.trubez.domain;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


// объект базы данных (сущность) то, что хранится в бд

@Entity
public class Employee {
    @Id                                                        // объясняет, что поле id - идентификатор
    @GeneratedValue(strategy = GenerationType.AUTO)            // спринг сам решает как и в каком виде будут храниться id. я в бд не лазаю
    private Long id;                                           // нужен, чтобы различать записи в таблице

    @NotBlank(message = "Please, fill the full name of person")
    private String fullName;

    @Length(max = 255, message = "Title of firm too long")
    private String firm;

    // указываем как хранится в базе данных юзер
    @ManyToOne(
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")                             // в базе данных колонка будет наываться user_id. если не указать оно будет называтся по умолчанию author_id
    private User author;

    private String filename;                                  // файл загружаемый



    public Employee(String fullName, String firm, User user) {
        super();
        this.author = user;
        this.fullName = fullName;
        this.firm = firm;
    }

    public String getAuthorName() {

        // если автор не равен 0, то из класса User вызываем getUsername

        return author != null ? author.getUsername() : "<none>";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setText(String fullName) {
        this.fullName = fullName;
    }

    public String getFirm() {
        return firm;
    }

    public void setTag(String firm) {
        this.firm = firm;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
