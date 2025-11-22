package kr.crud.crudproject.task.service;

import kr.crud.crudproject.person.model.Person;
import kr.crud.crudproject.person.repository.PersonRepository;
import kr.crud.crudproject.task.dto.TaskRequest;
import kr.crud.crudproject.task.model.Task;
import kr.crud.crudproject.task.model.TaskStatus;
import kr.crud.crudproject.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    public Task createTaskForUser(TaskRequest request, String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        return saveNewTask(request, person);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByPerson(Long personId) {
        return taskRepository.findByPersonId(personId);
    }

    public Task updateStatus(Long taskId, TaskStatus taskStatus) {
        Task task = taskRepository.findById(taskId).orElseThrow(()
                -> new IllegalArgumentException("Task not foun with id" + taskId));

        task.setStatus(taskStatus);
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task not found with id" + taskId);
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

    public Task create(TaskRequest request, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_ADMIN"));

        Person targetPerson;
        if (isAdmin && request.getPersonId() != null) {
            targetPerson = personRepository.findById(request.getPersonId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getPersonId()));
        } else {
            String email = authentication.getName();
            targetPerson = personRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        }

        return saveNewTask(request, targetPerson);
    }

    private Task saveNewTask(TaskRequest request, Person person) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription() == null ? "" : request.getDescription());
        task.setPerson(person);
        task.setStatus(TaskStatus.TO_DO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
}
