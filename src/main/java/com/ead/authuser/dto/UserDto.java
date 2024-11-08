package com.ead.authuser.dto;

import com.ead.authuser.validation.UserNameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto  {

    public interface UserView {
        public static interface  RegistrationPost {}
        public static interface  UserPut {}
        public static interface  PasswordPut {}
        public static interface  ImagePut {}
    }

    private UUID userId;

    @NotBlank(message = "O Campo nao pode ser nulo ou vazio!", groups = {UserView.RegistrationPost.class})
    @Size(min = 4, max = 50, groups = {UserView.RegistrationPost.class})
    @JsonView({UserView.RegistrationPost.class})
    @UserNameConstraint(message = "Campo noa pode conter espaços ou estar em branco", groups = {UserView.RegistrationPost.class}) //Criando a propria validation
    private String userName;

    @Email(message = "Email Inválido", groups = {UserView.RegistrationPost.class})
    @NotBlank(message = "O Campo nao pode ser nulo ou vazio!", groups = {UserView.RegistrationPost.class})
    @Size(min = 4, max = 50, groups = {UserView.RegistrationPost.class})
    @JsonView({UserView.RegistrationPost.class})
    private String email;

    @NotBlank(message = "O Campo nao pode ser nulo ou vazio!", groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @NotBlank(message = "O Campo nao pode ser nulo ou vazio!", groups = {UserView.PasswordPut.class})
    @JsonView({UserView.PasswordPut.class})
    @Size(min = 6, max = 20, groups = {UserView.PasswordPut.class})
    private String oldPassword;

    @NotBlank(message = "O Campo nao pode ser nulo ou vazio!", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @NotBlank(message = "O Campo nao pode ser nulo ou vazio!", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @NotBlank(message = "O Campo nao pode ser nulo ou vazio!", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @NotBlank(message = "O Campo nao pode ser nulo ou vazio!", groups = {UserView.ImagePut.class})
    @JsonView({UserView.ImagePut.class})
    private String imageUrl;

    private List<UserNotificationDto> notifications;

}
