package project.repository;

import project.identification.IdentificationTarget;

import java.util.List;

public interface IdentificationRepository {

    IdentificationTarget save(IdentificationTarget member);
    List<IdentificationTarget> findAll();
    void delete(IdentificationTarget member);
    IdentificationTarget updateByMissingcode(IdentificationTarget member);

    int countId(Long id);
    int countLoginID(String loginId);
}
