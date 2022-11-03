package project.repository.target;

import project.target.missing.Missing;

import java.util.List;
import java.util.Optional;

public interface MissingRepository {

    Missing save(Missing target);
    List<Missing> findAll();
    Optional<Missing> findByIdCode(String missingIdCode);
    String findName(String missingIdCode);
    Missing update(Missing target);
    void delete(String missingIdCode);
    void setIsUpdated(String missingIdCode);
}
