package edu.leicester.co2103.repo;

import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103.domain.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

    public List<Session> findAll();
    public Optional<Session> findById(long id);

    public Optional<Session> deleteById(long id);

}
