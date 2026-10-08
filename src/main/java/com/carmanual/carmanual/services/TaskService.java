package com.carmanual.carmanual.services;

import jakarta.persistence.EntityNotFoundException;
import com.carmanual.carmanual.models.Task;
import com.carmanual.carmanual.repositories.TaskRepository;
import com.carmanual.carmanual.repositories.UserRepository;
import com.carmanual.carmanual.services.exception.DataBindingViolationException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> findAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    public Task save(Task task) {
        if (task.getUser() == null || task.getUser().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario obrigatorio");
        }
        task.setUser(userRepository.findById(task.getUser().getId())
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + task.getUser().getId())));
        return taskRepository.save(task);
    }

    public Task update(Long id, Task task) {
        findById(id);
        task.setId(id);
        return save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com ID: " + id));
    }

    public void delete(Long id) {
        findById(id);
        try {
            taskRepository.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new DataBindingViolationException(
                    "Não é possível excluir pois há entidades relacionadas", exception);
        }
    }
}
