package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
public class ModuleRestController {

    @Autowired
    ModuleRepository repo;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    ConvenorRepository convenorRepository;

    // Get all modules. Endpoint 7
    @GetMapping("/modules")
    public ResponseEntity<List<Module>> listAllConvenors() {
        List<Module> modules = repo.findAll();
        if (modules.isEmpty()) {
            return new ResponseEntity<List<Module>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Module>>(modules, HttpStatus.OK);
    }

    // Create a new module. Endpoint 8
    @PostMapping("/modules")
    public ResponseEntity<?> createModule(@RequestBody Module module, UriComponentsBuilder ucBuilder) {
        if (repo.existsByCode(module.getCode())) {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module named " + module.getTitle() + " already exists."),
                    HttpStatus.CONFLICT);
        }
        repo.save(module);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/convenors/{id}").buildAndExpand(module.getCode()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    // Get a specific Module. Endpoint 9
    @GetMapping("/modules/{code}")
    public ResponseEntity<?> getModule(@PathVariable("code") String code) {

        if (repo.findByCode(code).isPresent()) {
            Module module = repo.findByCode(code).get();
            return new ResponseEntity<Module>(module, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with " + code + " not found"), HttpStatus.NOT_FOUND);
    }

    // Patch a modules level. Endpoint 10
    @PatchMapping("/modules/{code}")
    public ResponseEntity<?> patchModule(@PathVariable("code") String code, @RequestBody Map<String, Integer> body) {

        if (repo.findByCode(code).isPresent()) {
            Module currentModule = repo.findByCode(code).get();
            int level = body.get("level");
            currentModule.setLevel(level);
            repo.save(currentModule);

            return new ResponseEntity<Module>(currentModule, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module " + code + " not found."), HttpStatus.NOT_FOUND);
    }

    // Delete a specific Module. Endpoint 11
    @DeleteMapping("/modules/{code}")
    @Transactional
    public ResponseEntity<?> deleteModule(@PathVariable("code") String code) {

        if (repo.findByCode(code).isPresent()) {
            Module module = repo.findByCode(code).get();

            List<Convenor> convenors = convenorRepository.findAll();
            for (Convenor c : convenors) {
                if (c.getModules().contains(module)) {
                    c.getModules().remove(module);
                }
            }

            repo.deleteByCode(code);
            return ResponseEntity.ok(null);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with id " + code + " not found."), HttpStatus.NOT_FOUND);

    }

    // Get all sessions for a specific module. Endpoint 12
    @GetMapping("/modules/{code}/sessions")
    public ResponseEntity<?> getModuleSessions(@PathVariable("code") String code) {

        if (repo.findByCode(code).isPresent()) {
            Module module = repo.findByCode(code).get();
            List<Session> sessions = module.getSessions();

            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module " + code + " sessions are not available"), HttpStatus.NOT_FOUND);
    }

    //Create sessions in a module. Endpoint 13
    @PostMapping("/modules/{code}/sessions")
    public ResponseEntity<?> postModuleSessions(@PathVariable("code") String code, @RequestBody Session newSession) {

        if (repo.findByCode(code).isPresent()) {
            Module module = repo.findByCode(code).get();
            List<Session> sessions = module.getSessions();
            sessions.add(newSession);
            module.setSessions(sessions);
            repo.save(module);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module " + code + " doesn't exist"), HttpStatus.NOT_FOUND);
    }

    // Endpoint 14 put
    @PutMapping("/modules/{code}/sessions/{id}")
    @Transactional
    public ResponseEntity<?> updateSession(@PathVariable("code") String code, @PathVariable("id") long id, @RequestBody Session newSession) {
        if (repo.findByCode(code).isPresent()) {
            Module module = repo.findByCode(code).get();

            if (module.getSessions().contains(sessionRepository.findById(id).get())){
                Session currentSession = sessionRepository.findById(id).get();
                currentSession.setDatetime(newSession.getDatetime());
                currentSession.setDuration(newSession.getDuration());
                currentSession.setTopic(newSession.getTopic());

                repo.save(module);

                return new ResponseEntity<Module>(module, HttpStatus.OK);
            }
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session with id " + id + " not found."), HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with id " + code + " not found."), HttpStatus.NOT_FOUND);
    }


    // Endpoint 15 patch
    @PatchMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> patchSession(@PathVariable("code") String code, @PathVariable("id") long id, @RequestBody Map<String, Integer> body) {

        if (repo.existsByCode(code)) {
            Module module = repo.findByCode(code).get();
                if (module.getSessions().contains(sessionRepository.findById(id).get())) {
                    Session currentSession = sessionRepository.findById(id).get();
                    int duration = body.get("duration");
                    currentSession.setDuration(duration);
                    repo.save(module);
                    return new ResponseEntity<Module>(module, HttpStatus.OK);
                }else{
                    return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session " + id + " not in module selected."), HttpStatus.NOT_FOUND);
                    }
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module " + code + " not found."), HttpStatus.NOT_FOUND);
    }

    // Endpoint 16 delete session in module
    @DeleteMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable("code") String code, @PathVariable("id") long id) {

        if (repo.findByCode(code).isPresent()) {
            Module module = repo.findByCode(code).get();
            if (module.getSessions().contains(sessionRepository.findById(id).get())) {
                sessionRepository.deleteById(id);

                List<Session> sessions = module.getSessions();
                sessions.remove(sessionRepository.findById(id).get());
                module.setSessions(sessions);

                repo.save(module);
                return new ResponseEntity<Module>(module, HttpStatus.OK);
            } else{
                return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session " + id + " not in module selected."), HttpStatus.NOT_FOUND);
            }
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module " + code + " not found."), HttpStatus.NOT_FOUND);
    }


    // Get a specific session for a specific module. Endpoint 17
    @GetMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> getModuleSession(@PathVariable("code") String code, @PathVariable long id) {

        if (repo.findByCode(code).isPresent()) {
            Module module = repo.findByCode(code).get();
            if (sessionRepository.existsById(id)){
                if (module.getSessions().contains(sessionRepository.findById(id).get())) {
                    Session session = sessionRepository.findById(id).get();
                    return new ResponseEntity<>(session, HttpStatus.OK);
                }
            } else{
                return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session " + id + " does not exist"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module " + code + " does not contain session"), HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module " + code + " doesn't exist"), HttpStatus.NOT_FOUND);
    }
}

