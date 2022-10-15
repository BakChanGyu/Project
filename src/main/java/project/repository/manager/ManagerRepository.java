package project.repository.manager;

import project.manager.Manager;

import java.util.Optional;

public interface ManagerRepository {
    Optional<Manager> findById(Long id);
}
