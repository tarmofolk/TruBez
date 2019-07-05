package com.tarmofolk.trubez.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/*в классе собран обработчик ошибок*/
public class ErrorController {

    /**  получает на вход список ошибок и обрабатывает*/
    static Map<String, String> getErrors(BindingResult bindingResult) {
        /** мэп с ошибками
         * в качестве ключа используем имя поля + слово "Error
         * а в качестве значения дефолт мессадж"*/
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage);

        /**получаем лист ошибок и через стримAPI преобразуем в мэп с ошибками*/
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
