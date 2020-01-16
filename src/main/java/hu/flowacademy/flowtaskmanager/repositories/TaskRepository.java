package hu.flowacademy.flowtaskmanager.repositories;

import hu.flowacademy.flowtaskmanager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
