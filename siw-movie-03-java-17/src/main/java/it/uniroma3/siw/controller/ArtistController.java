package it.uniroma3.siw.controller;

import it.uniroma3.siw.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistRepository artistRepository;
	@Autowired
	private ArtistService artistService;
	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model,Artist artist) {
		model.addAttribute("artist", new Artist());
		model.addAttribute("attori",  artist.getActorOf());
		return "admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	
	@PostMapping("/admin/artist")
	public String newArtist(@Valid  @ModelAttribute("artist") Artist artist, BindingResult bindingResult, @RequestParam("artistImage") MultipartFile multipartFile,Model model) {
		if (!bindingResult.hasErrors()) {
			this.artistService.createNewArtisits(artist,multipartFile);
			model.addAttribute("artist", artist);
			model.addAttribute("attori",  artist.getActorOf());
			return "artist.html";
		} else {
			model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNewArtist.html"; 
		}
	}

	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model,Artist artist) {
		model.addAttribute("artist", this.artistService.findArtistById(id));
		model.addAttribute("directed", artist.getDirectorOf());
		model.addAttribute("acted", artist.getActorOf());
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {

		model.addAttribute("artists", this.artistRepository.findAll());

		return "artists.html";
	}
}
