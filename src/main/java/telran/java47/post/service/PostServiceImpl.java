package telran.java47.post.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java47.post.dao.PostRepository;
import telran.java47.post.dto.DatePeriodDto;
import telran.java47.post.dto.NewCommentDto;
import telran.java47.post.dto.NewPostDto;
import telran.java47.post.dto.PostDto;
import telran.java47.post.dto.exceptions.PostNotFoundException;
import telran.java47.post.model.Comment;
import telran.java47.post.model.Post;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	final PostRepository postRepository;
	final ModelMapper modelMapper;

	@Override
	public PostDto addNewPost(String author, NewPostDto newPostDto) {
		Post post = modelMapper.map(newPostDto, Post.class);
		post.setAuthor(author);

		Post savedPost = postRepository.save(post);
		return modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto findPostById(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException());
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto removePost(String id) {
		return postRepository.findById(id).map(post -> {
			postRepository.delete(post);
			return modelMapper.map(post, PostDto.class);
		}).orElseThrow(() -> new PostNotFoundException());
	}

	@Override
	public PostDto updatePost(String id, NewPostDto newPostDto) {
		return postRepository.findById(id).map(post -> {
			post.setTitle(newPostDto.getTitle());
			post.setContent(newPostDto.getContent());
			post.setTags(newPostDto.getTags());
			Post updatedPost = postRepository.save(post);
			return modelMapper.map(updatedPost, PostDto.class);
		}).orElseThrow(() -> new PostNotFoundException());
	}

	@Override
	public PostDto addComment(String id, String author, NewCommentDto newCommentDto) {
		return postRepository.findById(id).map(post -> {
			Comment comment = new Comment(author, newCommentDto.getMessage());
			post.getComments().add(comment);
			Post updatedPost = postRepository.save(post);
			return modelMapper.map(updatedPost, PostDto.class);
		}).orElseThrow(() -> new PostNotFoundException());
	}

	@Override
	public void addLike(String id) {
		postRepository.findById(id).ifPresent(post -> {
			post.addLike();
			postRepository.save(post);
		});
	}

	@Override
	public Iterable<PostDto> findPostByAuthor(String author) {
		return postRepository.findAllByAuthor(author).stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PostDto> findPostsByTags(List<String> tags) {
		return postRepository.findAllByTagsIn(tags).stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto) {
		return postRepository
				.findAllByDateCreatedBetween(datePeriodDto.getDateFrom().atStartOfDay(),
						datePeriodDto.getDateTo().atStartOfDay().plusDays(1))
				.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
	}
}
