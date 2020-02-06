package hu.flowacademy.flowtaskmanager.services;

import hu.flowacademy.flowtaskmanager.models.Post;
import hu.flowacademy.flowtaskmanager.models.User;
import hu.flowacademy.flowtaskmanager.models.postDTO.PostDTO;
import hu.flowacademy.flowtaskmanager.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;


    public Post findPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> findPostsByTaskId(Long id) {
        return postRepository.findByTask(id);
    }

    public Post savePost(PostDTO postDTO) {
        Post post = new Post();
        post.postFromPostDTO(postDTO);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(userService.findUserById(postDTO.getUserId()));
        post.setTask(taskService.findTaskById(postDTO.getTaskId()));
        postRepository.save(post);
        taskService.savePostToTask(postDTO.getTaskId(), post.getId());
        return post;
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public User findUserByPostId(Long id) {
        return findPostById(id).getUser();
    }
}
