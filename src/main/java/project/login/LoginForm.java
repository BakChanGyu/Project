package project.login;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonAutoDetect
public class LoginForm {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
