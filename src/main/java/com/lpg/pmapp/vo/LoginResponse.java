package com.lpg.pmapp.vo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author olatundeonigbinde
 */
@Getter
@Setter
public class LoginResponse extends ServiceResponse {

    private String sessionToken;
    private String firstname, lastname, phoneNumber, email;

    public LoginResponse(int code) {
        super(code);
    }
    public LoginResponse(int code, String message) {
        super(code, message);
    }
}
