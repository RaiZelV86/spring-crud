package kr.crud.crudproject.task.repository;

import kr.crud.crudproject.task.model.Task;
import kr.crud.crudproject.task.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository  extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);

    List<Task> findByPersonId(Long personId);
    List<Task> findByStatus(TaskStatus taskStatus);

}
