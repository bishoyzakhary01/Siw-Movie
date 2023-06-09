package it.uniroma3.siw.controller;

import it.uniroma3.siw.SessionData;
import it.uniroma3.siw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public  class GlobalController
{

    @Autowired
    private SessionData sessionData;

    @ModelAttribute("userDetails")
    public User getUser(){

        try {
            return this.sessionData.getLoggedUser();
        }
        catch(ClassCastException e){
            return null;
        }
    }

}