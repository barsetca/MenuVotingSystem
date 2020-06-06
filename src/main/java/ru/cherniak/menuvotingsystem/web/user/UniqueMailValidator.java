package ru.cherniak.menuvotingsystem.web.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.cherniak.menuvotingsystem.HasIdAndEmail;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.repository.user.UserRepository;
import ru.cherniak.menuvotingsystem.web.ExceptionInfoHandler;


@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        User dbUser = userRepository.getByEmail(user.getEmail().toLowerCase());
        if (dbUser != null && !dbUser.getId().equals(user.getId())) {
            errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
        }
    }
}
