package hu.flowacademy.flowtaskmanager.controllers;

import hu.flowacademy.flowtaskmanager.models.Post;
import hu.flowacademy.flowtaskmanager.models.Task;
import hu.flowacademy.flowtaskmanager.models.TaskDTO.TaskDTO;
import hu.flowacademy.flowtaskmanager.models.User;
import hu.flowacademy.flowtaskmanager.models.postDTO.PostDTO;
import hu.flowacademy.flowtaskmanager.models.userDTO.UserRegisterDTO;
import hu.flowacademy.flowtaskmanager.models.userDTO.UserResponseDTO;
import hu.flowacademy.flowtaskmanager.services.PostService;
import hu.flowacademy.flowtaskmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/findall")
    public ResponseEntity<List<PostDTO>> findAllPosts() {
        List<Post> listOfPosts = postService.findAllPosts();

        List<PostDTO> listOfDTOs = listOfPosts.stream().map(p -> {
            PostDTO postDTO = new PostDTO();
            postDTO.postDTOfromPost(p);
            return postDTO;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(listOfDTOs);
    }

    @GetMapping("/findPostsByTaskId/{id}")
    public ResponseEntity<List<PostDTO>> findPostsByTaskId(@PathVariable Long id) {
        List<Post> listOfPosts = postService.findPostsByTaskId(id);

        List<PostDTO> listOfDTOs = listOfPosts.stream().map(p -> {
            PostDTO postDTO = new PostDTO();
            postDTO.postDTOfromPost(p);
            return postDTO;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(listOfDTOs);
    }

    @GetMapping("/findUserByPostId/{id}")
    public ResponseEntity<UserResponseDTO> findUserByPostId(@PathVariable Long id) {
        User user = postService.findUserByPostId(id);
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.userDTOFromUser(user);
        return ResponseEntity.ok(userDTO);
    }


    @PostMapping
    public ResponseEntity<PostDTO> saveTask(@RequestBody PostDTO postDTO) {
        Post post = postService.savePost(postDTO);
        postDTO.postDTOfromPost(post);
        return ResponseEntity.ok(postDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok().build();
    }

}
