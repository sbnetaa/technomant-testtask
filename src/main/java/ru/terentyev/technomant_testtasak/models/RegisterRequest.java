package ru.terentyev.technomant_testtasak.models;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterRequest extends AbstractDTO {

    private String username;
    private String password;
    private String passwordConfirm;
}
