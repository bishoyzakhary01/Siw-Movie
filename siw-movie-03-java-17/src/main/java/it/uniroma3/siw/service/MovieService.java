package it.uniroma3.siw.service;


import it.uniroma3.siw.model.*;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class MovieService {


    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ArtistRepository artistRepository;
@Autowired
ImageRepository imageRepository;
    @Transactional
    public void updateMovie(Movie movie) {
        movieRepository.save(movie);
    }

    /*public List<Movie> search(String title, Integer year) {

        return movieRepository.findByTitleAndYear(title, year);
    }*/

    private List<Artist> actorsToAdd(Long movieId) {
        List<Artist> actorsToAdd = new ArrayList<>();

        for (Artist a : artistRepository.findActorsNotInMovie(movieId)) {
            actorsToAdd.add(a);
        }
        return actorsToAdd;
    }

    @Transactional
    public Movie createNewMovie(Movie movie, MultipartFile multipartFile) {
        try {

            movie.setImage(imageRepository.save(new Image(multipartFile.getBytes())));

        }
        catch (IOException e){}
         this.movieRepository.save(movie);
        return movie;

    }
//se solo lettua non serve     @Transactional


    public Movie findMovieById(Long id) {
        return this.movieRepository.findById(id).orElse(null);
    }

    public Iterable<Movie> findAllMovie() {

        return this.movieRepository.findAll();
    }

    public Movie saveMovie(Movie movie) {
        return this.movieRepository.save(movie);
    }


    public Movie saveDirectorToMovie(Long movieid, Long artistId) {
        Movie res = null;
        Artist director = this.artistRepository.findById(artistId).orElse(null);

        Movie movie = this.findMovieById(movieid);
        if (movie != null && director != null) {
            movie.setDirector(director);
            this.saveMovie(movie);
            res = movie;
        }
        return res;
    }

    public Iterable<Movie> findByYear(Integer year) {
        return this.movieRepository.findByYear(year);
    }

    public Movie saveActorToMovie(long movieid, long artistId ) {
        Movie movie = this.movieRepository.findById(movieid).orElse(null);
        Artist artist = this.artistRepository.findById(artistId).orElse(null);
        if (movie != null && artist != null) {

            Set<Artist> actors = movie.getActors();
            actors.add(artist);
            this.movieRepository.save(movie);
            // List<Artist>actorsToAdd=actorsToAdd(movieid);
            this.movieRepository.save(movie);
        }
        return movie;
    }
    public Movie deleteActorFromMovie(long movieId, long actorId ){
        Movie movie = this.movieRepository. findById(movieId).orElse(null);

        Artist actor = this.artistRepository.findById(actorId).orElse(null);
        if (movie != null && actor != null) {
            Set<Artist> actors = movie.getActors();
            actors.remove(actor);
            this.movieRepository.save(movie);
        }
        return movie;
    }
    @Transactional
    public Movie addMoviePhoto(MultipartFile multipartFile, Movie movie) throws IOException {


        movie.setImage(imageRepository.save(new Image(multipartFile.getBytes())));

        this.saveMovie(movie);


        return movie;
    }


    public String function(Model model, Movie movie, User user){
        Set<Artist> movieCast = new HashSet<>();
        if(movie.getActors() != null)
            movieCast.addAll(movie.getActors());
        movieCast.add(movie.getDirector());
        movieCast.remove(null);
        model.addAttribute("movieCast", movieCast);
        model.addAttribute("movie", movie);
        model.addAttribute("director", movie.getDirector());
        if(user != null && this.alreadyReviewed(movie.getReviews(),user.getUsername()))
            model.addAttribute("hasComment", true);
        else
            model.addAttribute("hasComment", false);
        model.addAttribute("review", new Review());
        model.addAttribute("reviews", movie.getReviews());
        return "movie.html";
    }

    @Transactional
    public boolean alreadyReviewed(Set<Review> reviews,String author){
        if(reviews != null)
            for(Review rev : reviews)
                if(rev.getAuthor().equals(author))
                    return true;
        return false;
    }


   /* public long count() {

        return movieRepository.count();
    }*/
}

