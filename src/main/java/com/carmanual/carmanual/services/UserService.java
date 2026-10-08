package com.carmanual.carmanual.services;

import jakarta.persistence.EntityNotFoundException;
import com.carmanual.carmanual.models.User;
import com.carmanual.carmanual.repositories.UserRepository;
import com.carmanual.carmanual.services.exception.DataBindingViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public void delete(Long id) {
        findById(id);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new DataBindingViolationException(
                    "Não é possivel excluir pois há entidades relacionadas!", exception);
        }
    }
}
