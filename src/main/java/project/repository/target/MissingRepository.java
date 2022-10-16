package project.repository.target;

import project.target.missing.Missing;

import java.util.List;
import java.util.Optional;

public interface MissingRepository {

    Missing save(Missing target);
    List<Missing> findAll();
    Optional<Missing> findByIdCode(String idCode);
    String findName(String idCode);
    Missing update(Missing target);
    void delete(String idCode);
}
