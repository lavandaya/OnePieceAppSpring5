package be.kdg.programming5.onepiece.presentation.viewmodel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterViewModel {

    @NotBlank(message = "{register.username.required}")
    @Size(min = 3, max = 50, message = "{register.username.size}")
    private String username;

    @NotBlank(message = "{register.email.required}")
    @Email(message = "{register.email.invalid}")
    private String email;

    @NotBlank(message = "{register.password.required}")
    @Size(min = 8, max = 72, message = "{register.password.size}")
    private String password;

    @NotBlank(message = "{register.confirmPassword.required}")
    private String confirmPassword;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}