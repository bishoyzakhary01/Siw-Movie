package it.uniroma3.siw.controller.validator;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class ArtiistValidator implements Validator {

    @Autowired
    ArtistRepository artistRepository;
    @Override
    public void validate(Object o, Errors errors) {
        Artist artist = (Artist)o;
        if (artist.getName()!=null && artist.getSurname()!=null
                && artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
            errors.reject("Artist.duplicate");
        }
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return Artist.class.equals(aClass);
    }
}

