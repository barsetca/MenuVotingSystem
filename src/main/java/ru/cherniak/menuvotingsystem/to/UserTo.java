package ru.cherniak.menuvotingsystem.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UserTo extends BaseTo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Email
    @NotBlank
    @Size(min = 3, max = 100, message = "length must be between 3 and 100 characters")
    private String email;

    @NotBlank
    @Size(min = 6, max = 100, message = "length must be between 6 and 100 characters")
    private String password;

    public UserTo() {
    }

    public UserTo(Long id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;

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

     @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
