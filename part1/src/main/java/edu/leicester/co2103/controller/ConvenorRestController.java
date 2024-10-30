package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ConvenorRestController {

    @Autowired
    ConvenorRepository repo;

    @Autowired
    ModuleRepository moduleRepo;

    @Autowired
    SessionRepository sessionRepo;

    // Get all convenors. Endpoint 1
    @GetMapping("/convenors")
    public ResponseEntity<List<Convenor>> listAllConvenors() {
        List<Convenor> convenors = repo.findAll();
        if (convenors.isEmpty()) {
            return new ResponseEntity<List<Convenor>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Convenor>>(convenors, HttpStatus.OK);
    }

    // Create a new convenor. Endpoint 2
    @PostMapping("/convenors")
    public ResponseEntity<?> createConvenor(@RequestBody Convenor convenor, UriComponentsBuilder ucBuilder) {
        if (repo.existsById(convenor.getId())) {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor named " + convenor.getName() + " already exists."),
                    HttpStatus.CONFLICT);
        }
        repo.save(convenor);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/convenors/{id}").buildAndExpand(convenor.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    // Get a specific convenor. Endpoint 3
    @GetMapping("/convenors/{id}")
    public ResponseEntity<?> getConvenor(@PathVariable("id") Long id) {

        if (repo.findById(id).isPresent()) {
            Convenor convenor = repo.findById(id).get();
            return new ResponseEntity<Convenor>(convenor, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with " + id + " not found"), HttpStatus.NOT_FOUND);
    }

    // Update a specific convenor. Endpoint 4
    @PutMapping("/convenors/{id}")
    public ResponseEntity<?> updateConvenor(@PathVariable("id") Long id, @RequestBody Convenor newConvenor) {

        if (repo.findById(id).isPresent()) {
            Convenor currentConvenor = repo.findById(id).get();
            currentConvenor.setName(newConvenor.getName());
            currentConvenor.setPosition(newConvenor.getPosition());

            currentConvenor.getModules().clear();
            currentConvenor.getModules().addAll(newConvenor.getModules());

            repo.save(currentConvenor);
            return new ResponseEntity<Convenor>(currentConvenor, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found."), HttpStatus.NOT_FOUND);

    }

    // Delete a specific convenor. Endpoint 5
    @DeleteMapping("/convenors/{id}")
    public ResponseEntity<?> deleteConvenor(@PathVariable("id") Long id) {

        if (repo.findById(id).isPresent()) {
            Convenor convenor = repo.findById(id).get();
            List<Module> modules = convenor.getModules();

            for (Module m : modules){
                List<Convenor> convenors = repo.findAll();

                for (Convenor c : convenors){
                    if (convenors.size() == 1){
                        repo.deleteById(id);
                    } else{
                        if (!c.equals(convenor)){
                            if (c.getModules().contains(m)){
                                convenor.getModules().remove(m);
                                repo.delete(convenor);
                            } else{
                                String code = m.getCode();
                                moduleRepo.deleteByCode(code);
                                repo.delete(convenor);
                            }
                        }
                    }
                }
            }

            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    //Get specific convenors modules. Endpoint 6
    @GetMapping("/convenors/{id}/modules")
    public ResponseEntity<?> getConvenorModules(@PathVariable("id") Long id) {

        if (repo.findById(id).isPresent()) {
            Convenor convenor = repo.findById(id).get();
            List<Module> modules = convenor.getModules();
            return new ResponseEntity<>(modules, HttpStatus.OK);

        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with " + id + " modules are not available"), HttpStatus.NOT_FOUND);
    }
}