package it.uniroma3.siw.controller;


import it.uniroma3.siw.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

@Component
public class UserValidator implements Validator {

    final Integer MAX_NAME_LENGTH = 100;
    final Integer MIN_NAME_LENGTH = 2;


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    } // specifica la classe su cui opera il validator

    @Override
    public void validate(Object o, Errors errors) {

        User user = (User) o;
        String firstName = user.getName().trim();
        String lastName = user.getSurname().trim();

        if(firstName.isEmpty()){
            errors.rejectValue("firstname","required");
        }
        else if(firstName.length() < MIN_NAME_LENGTH || firstName.length() > MAX_NAME_LENGTH){
            errors.rejectValue("firstName","size");
        }

        if(lastName.isEmpty()){
            errors.rejectValue("lastname","required");
        }
        else if(firstName.length() < MIN_NAME_LENGTH || firstName.length() > MAX_NAME_LENGTH){
            errors.rejectValue("lastName","size");
        }

    }
}
