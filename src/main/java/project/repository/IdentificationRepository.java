package project.repository;

import project.identification.IdentificationTarget;

import java.util.List;

public interface IdentificationRepository {

    IdentificationTarget save(IdentificationTarget member);
    List<IdentificationTarget> findAll();
    IdentificationTarget updateTable(IdentificationTarget member);
    void delete(IdentificationTarget member);

    int countId(Long id);
    int countLoginID(String loginId);
}
