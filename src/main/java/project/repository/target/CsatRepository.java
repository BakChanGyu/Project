package project.repository.target;

import project.target.student.csat.Csat;

import java.util.List;
import java.util.Optional;

public interface CsatRepository {

    Csat save(Csat target);
    List<Csat> findAll();
    Optional<Csat> findByIdCode(String csatIdCode);
    String findName(String csatIdCode);
    Csat update(Csat target);
    void delete(String csatIdCode);
}
