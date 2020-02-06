package hu.flowacademy.flowtaskmanager.models.TaskDTO;

import hu.flowacademy.flowtaskmanager.models.Post;
import hu.flowacademy.flowtaskmanager.models.Rating;
import hu.flowacademy.flowtaskmanager.models.Task;
import hu.flowacademy.flowtaskmanager.models.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaskDTO {

    private Long id;

    private Task.Type type;

    private String title;

    private String content;

    private Task.Difficulty difficulty;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private Long mentorId;

    private List<Rating> ratings;

    private List<Long> userIds;

    private List<Long> postIds;

    public void taskDTOFromTask(Task task) {
        this.id = task.getId();
        this.type = task.getType();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.difficulty = task.getDifficulty();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.deletedAt = task.getDeletedAt();
        if (task.getMentor() != null) {
            this.mentorId = task.getMentor().getId();
        } else this.mentorId = null;
        this.ratings = task.getRatings();
        if (task.getUsers() == null) {
            this.userIds = null;
        } else {
            this.userIds = task.getUsers().stream().map(x -> x.getId()).collect(Collectors.toList());
        }
        if (task.getPosts() == null) {
            this.postIds = null;
        } else {
            this.postIds = task.getPosts().stream().map(x -> x.getId()).collect(Collectors.toList());
        }
    }

}
