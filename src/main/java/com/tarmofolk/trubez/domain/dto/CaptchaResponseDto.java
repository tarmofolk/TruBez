package com.tarmofolk.trubez.domain.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/** класс тупо для передачи данных от чужой программы моей
 *  в полученном jsonе будем игнорировать неизвестные поля, а брать только описанные ниже */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {

    /** поля из гугловой документации, которые нам возваратит в ответе
     * успешно или нет
     * набор ошибок (так как джава не поддерживает тире, добавляем аннотацию элиос, которая описывает реальное название, которое пришлет гугл)*/
    @JsonProperty("success")
    private boolean success;
    @JsonAlias("error-codes")
    private Set<String> errorCodes;




    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
