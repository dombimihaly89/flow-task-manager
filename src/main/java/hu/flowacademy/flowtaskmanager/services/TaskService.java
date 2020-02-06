package hu.flowacademy.flowtaskmanager.services;

import hu.flowacademy.flowtaskmanager.exceptions.ValidationException;
import hu.flowacademy.flowtaskmanager.models.Rating;
import hu.flowacademy.flowtaskmanager.models.Task;
import hu.flowacademy.flowtaskmanager.models.TaskDTO.TaskDTO;
import hu.flowacademy.flowtaskmanager.models.User;
import hu.flowacademy.flowtaskmanager.models.userDTO.UserRegisterDTO;
import hu.flowacademy.flowtaskmanager.models.userDTO.UserResponseDTO;
import hu.flowacademy.flowtaskmanager.repositories.RatingRepository;
import hu.flowacademy.flowtaskmanager.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private RatingRepository ratingRepository;

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(null);
    }

    public Task saveTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.taskFromTaskDTO(taskDTO);
        mentorValidator(taskDTO);
        task.setMentor(userService.findUserById(taskDTO.getMentorId()));
        task.setCreatedAt(LocalDateTime.now());
        // task.setUsers(taskDTO.getUserIds().stream().map(x -> userService.findUserById(x)).collect(Collectors.toList()));
        return taskRepository.save(task);
    }

    public Task addRating(Long id, Integer rating, Long userId) {
        Task task =  findTaskById(id);
        if (task == null) throw new ValidationException("There is no user with this ID.");
        User user = userService.findUserById(userId);
        Rating newRating = new Rating(rating, user);
        boolean findUserRating = task.getRatings().stream().map(x -> x.getUser().getId()).anyMatch(x -> x == userId);
        if (findUserRating) {
            throw new ValidationException("This user already rated this task");
        }
        ratingRepository.save(newRating);
        task.getRatings().add(newRating);
        return task;
    }


    public Task updateTask(TaskDTO taskDTO) {
        Task task = findTaskById(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return task;
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }


    public void savePostToTask(Long taskId, Long postId) {
        Task task = findTaskById(taskId);
        task.getPosts().add(postService.findPostById(postId));
    }

    public void mentorValidator(TaskDTO taskDTO) {
        if (userService.findUserById(taskDTO.getMentorId()).getRole() != User.Role.MENTOR) {
            throw new ValidationException("The mentor has to be a user with MENTOR role.");
        }
    }
}
