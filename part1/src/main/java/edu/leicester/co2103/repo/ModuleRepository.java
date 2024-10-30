package edu.leicester.co2103.repo;

import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Session;
import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103.domain.Module;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends CrudRepository<Module, String> {
    public List<Module> findAll();
    public Optional<Module> findByCode(String code);
    public Optional<Module> deleteByCode(String code);
    boolean existsByCode(String code);
}
