package kr.crud.crudproject.task.service;

import kr.crud.crudproject.person.model.Person;
import kr.crud.crudproject.person.model.Role;
import kr.crud.crudproject.person.repository.PersonRepository;
import kr.crud.crudproject.task.dto.TaskRequest;
import kr.crud.crudproject.task.model.Task;
import kr.crud.crudproject.task.model.TaskStatus;
import kr.crud.crudproject.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final PersonRepository personRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, PersonRepository personRepository) {
        this.taskRepository = taskRepository;
        this.personRepository = personRepository;
    }

    public Task create(TaskRequest taskRequest, Authentication authentication) {
        String email = authentication.getName();
        Person actor = personRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Actor not found: " + email));

        boolean isAdmin = actor.getRole() == Role.ADMIN;

        Long targetPersonId;
        if (isAdmin) {
            if (taskRequest.getPersonId() == null) {
                throw new IllegalArgumentException("personId is required for admin");
            }
            targetPersonId = taskRequest.getPersonId();
        } else {
            targetPersonId = actor.getId();
        }

        Person target = personRepository.findById(targetPersonId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + targetPersonId));

        Task t = new Task();
        t.setTitle(taskRequest.getTitle());
        t.setDescription(taskRequest.getDescription());
        t.setStatus(TaskStatus.TO_DO);
        t.setPerson(target);
        return taskRepository.save(t);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByPerson(Long personId) {
        return taskRepository.findByPersonId(personId);
    }

    public Task updateStatus(Long taskId, TaskStatus taskStatus) {
        Task task = taskRepository.findById(taskId).orElseThrow(()
                -> new IllegalArgumentException("Task not found with id: " + taskId));

        task.setStatus(taskStatus);
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task not found with id: " + taskId);
        }

        taskRepository.deleteById(taskId);
    }

    public Long getPersonIdByEmail(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email))
                .getId();
    }

    public List<Task> getTasksByEmail(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return taskRepository.findByPersonId(person.getId());
    }
}
