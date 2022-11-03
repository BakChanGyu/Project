package project.repository.target;

import project.target.student.csat.Csat;
import project.target.student.toeic.Toeic;

import java.util.List;
import java.util.Optional;

public interface ToeicRepository {

    Toeic save(Toeic target);
    List<Toeic> findAll();
    Optional<Toeic> findByIdCode(String toeicIdCode);
    String findName(String toeicIdCode);
    Toeic update(Toeic target);
    void delete(String toeicIdCode);

    void setIsUpdated(String toeicIdCode);
}
