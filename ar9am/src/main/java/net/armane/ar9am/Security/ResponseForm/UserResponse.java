package net.armane.ar9am.Security.ResponseForm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    Long id;
    String username;
    String email;
    Collection<String> roles;
    String Autorization;
}
