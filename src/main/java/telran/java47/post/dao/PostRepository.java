package telran.java47.post.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.java47.post.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {
	
	List<Post> findAllByAuthor(String author);

    List<Post> findAllByTagsIn(List<String> tags);

    List<Post> findAllByDateCreatedBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

}
