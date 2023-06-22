package it.uniroma3.siw.service;


import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private MovieRepository movieRepository;
@Autowired
ImageRepository imageRepository;
    @Transactional
    public boolean createNewArtisit(Artist artist) {
        boolean res = false;
        if (!artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
            res = true;
            artistRepository.save(artist);
        }
        return res;

    }

    @Transactional
    public Artist createNewArtisits(Artist artist, MultipartFile multipartFile) {
        try {

            artist.setImage(imageRepository.save(new Image(multipartFile.getBytes())));

        }
        catch (IOException e){}
        this.artistRepository.save(artist);
        return artist;

    }
    public Artist saveArtist(Artist artist) {
        return this.artistRepository.save(artist);
    }
    public Artist findArtistById(Long id) {
        return this.artistRepository.findById(id).orElse(null);
    }
    @Transactional
    public Iterable<Artist>findALLArtisit(){
        return artistRepository.findAll();
    }
    public List<Artist> findActorNotInMovie(Long movieId){
        List<Artist>actorsToAdd=new ArrayList<>();
        for (Artist a: artistRepository.findActorsNotInMovie(movieId)){
            actorsToAdd.add(a);
        }
        return  actorsToAdd;
    }
    @Transactional
    public Artist addArtistPhoto(MultipartFile multipartFile, Artist artist) throws IOException {


        artist.setImage(imageRepository.save(new Image(multipartFile.getBytes())));

        this.saveArtist(artist);


        return artist;
    }

}
