package it.uniroma3.siw.controller.validator;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CredentialsValidator implements Validator {

    @Autowired
    CredentialsService credentialsService;

    final Integer MAX_USERNAME_LENGTH = 20;
    final Integer MIN_USERNAME_LENGTH = 4;
    final Integer MAX_PASSWORD_LENGTH = 20;
    final Integer MIN_PASSWORD_LENGTH = 6;

    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Credentials credentials = (Credentials) o;
        String userName = credentials.getUsername().trim();
        String password= credentials.getPassword().trim();


        if(userName.isEmpty()){
            errors.rejectValue("userName","required");
        }
        else if(userName.length() < MIN_USERNAME_LENGTH || userName.length() > MAX_USERNAME_LENGTH){
            errors.rejectValue("userName","size");
        }
        else if(this.credentialsService.getCredentials(userName) != null){
            errors.rejectValue("userName", "duplicate");
        }

        if(password.isEmpty()){
            errors.rejectValue("password","required");
        }
        else if(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH){
            errors.rejectValue("password","size");
        }

    }
}
