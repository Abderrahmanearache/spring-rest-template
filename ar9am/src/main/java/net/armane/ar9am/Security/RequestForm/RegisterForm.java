package net.armane.ar9am.Security.RequestForm;


import lombok.Data;

@Data
public class RegisterForm {
    String username;
    String password;
    String repassword;
    String email;
}
