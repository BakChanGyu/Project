package project.repository;

import project.manager.Manager;

import java.util.Optional;

public interface ManagerRepository {
    Optional<Manager> findById(Long id);
}
