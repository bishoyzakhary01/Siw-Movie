package it.uniroma3.siw.controller;


import java.io.IOException;
import java.util.List;

import it.uniroma3.siw.FileUploadUtil;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MovieController {
	@Autowired
	private MovieService movieService;

	@Autowired
	private ArtistService artistService;

	@Autowired
	private MovieValidator movieValidator;
@Autowired
private GlobalController globalController;

	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());

		return "admin/formNewMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		Movie movie=movieService.findMovieById(id);
		if(movie!=null) {
			model.addAttribute("movie", movie);
			model.addAttribute("review",new Review());
			return "admin/formUpdateMovie.html";
		}
		else{
			return "notFound.html";
		}
	}

	@GetMapping(value="/admin/indexMovie")
	public String indexMovie() {
		return "admin/indexMovie.html";
	}

	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		Model movies = model.addAttribute("movies", this.movieService.findAllMovie());
		return "admin/manageMovies.html";
	}

	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie= this.movieService.saveDirectorToMovie(movieId,directorId);
		if(movie!=null){
			model.addAttribute("movie",movie);
		}
		else{
			return "notFound.html";
		}
		return "admin/formUpdateMovie.html";
	}


	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistService.findALLArtisit());
		Movie movie=movieService.findMovieById(id);
		if(movie!=null){
			model.addAttribute("movie", movie);
			return "admin/directorsToAdd.html";
		}
		else{
			return "notFound.html";
		}
	}

	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, @RequestParam("movieImage") MultipartFile multipartFile, Model model) {

		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.movieService.createNewMovie(movie, multipartFile);
			model.addAttribute("movie", movie);
			return this.movieService.function(model, movie, globalController.getUser());
			/*return "movie.html";*/
		} else {
			return "admin/formNewMovie.html";
		}
	}

	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model,Review review) {
		Movie movie = movieService.findMovieById(id);

			model.addAttribute("movie", movie);
			model.addAttribute("reviews", movie.getReviews());

			//model.addAttribute("nummovies",movieService.count());
			return this.movieService.function(model, movie, (User) globalController.getUser());


	}

	@GetMapping("/movie")
	public String getMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "movies.html";
	}

	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies", this.movieService.findByYear(year));
		return "foundMovies.html";
	}

	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Artist> actorsToAdd = this.artistService.findActorNotInMovie(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		Movie movie=this.movieService.findMovieById(id);
		if(movie!=null){
			model.addAttribute("movie", movie);
			return "admin/actorsToAdd.html";
		}else{
			return "notFound.html";
		}
	}

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {

		Movie movie = this.movieService.saveActorToMovie(movieId, actorId);
		if (movie != null){
			List<Artist> actorsToAdd = this.artistService.findActorNotInMovie(movieId);
			model.addAttribute("movie", movie);
			model.addAttribute("actorsToAdd", actorsToAdd);
			model.addAttribute("actorsToAdd", movie.getActors());
			return "admin/actorsToAdd.html";
		}
		else{
			return "notFound.html";
		}
	}

	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = movieService.deleteActorFromMovie(movieId, actorId);
		if (movie != null) {


			List<Artist> actorsToAdd = artistService.findActorNotInMovie(movieId);

			model.addAttribute("movie", movie);
			model.addAttribute("actorsToAdd", actorsToAdd);
			model.addAttribute("actorsToAdd", movie.getActors());

			return "admin/actorsToAdd.html";
		} else {
			return "notFound.html";
		}
	}


}