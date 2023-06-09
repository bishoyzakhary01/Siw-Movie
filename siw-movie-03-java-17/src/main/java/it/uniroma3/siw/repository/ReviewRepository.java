package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review,Long> {

public boolean existsByAuthorAndTitleAndRatingAndText(String author,String title,Integer rating, String text);

    public Review findByAuthor(String author);



}
