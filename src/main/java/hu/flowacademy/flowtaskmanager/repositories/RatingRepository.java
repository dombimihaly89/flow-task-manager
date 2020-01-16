package hu.flowacademy.flowtaskmanager.repositories;

import hu.flowacademy.flowtaskmanager.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
