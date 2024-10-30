package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SessionRestController {

    @Autowired
    SessionRepository repo;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ConvenorRepository convenorRepository;

    // Delete all sessions. Endpoint 18
    @DeleteMapping("/sessions")
    public ResponseEntity<?> deleteSessions() {

        List<Session> sessions = repo.findAll();
        if (sessions.isEmpty()){
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("No sessions found"), HttpStatus.NOT_FOUND);
        }
        for (Session s : sessions){
            repo.deleteById(s.getId());
        }
        return ResponseEntity.ok(null);
    }

    // Endpoint 19-20
    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions(@RequestParam(value = "convenor", required = false) Long id,
                                         @RequestParam(value = "module", required = false) String code) {

        List<Session> sessions = new ArrayList<>();
        System.out.println(id);
        System.out.println(code);

        // if both params not null
        if (id!= null && code != "") {
            Convenor convenor = convenorRepository.findById(id).get();
            List<Module> modules = convenor.getModules();
            for (Module m : modules){
                if (m.equals(moduleRepository.findByCode(code).get())){
                    sessions = m.getSessions();
                    return new ResponseEntity<List<Session>>(sessions, HttpStatus.OK);
                    }
                }
        }
        // if convenor is null and code is not null
        else if (code!= ""){
            Module module = moduleRepository.findByCode(code).get();
            sessions = module.getSessions();

            return new ResponseEntity<List<Session>>(sessions, HttpStatus.OK);
        }
        // if module is null
        else if (id!= null){
            Convenor convenor = convenorRepository.findById(id).get();
            List<Module> modules = convenor.getModules();
            for (Module m : modules) {
                List<Session> moduleSessions = m.getSessions();
                for (Session s : moduleSessions) {
                    sessions.add(s);
                }
            }
            return new ResponseEntity<List<Session>>(sessions, HttpStatus.OK);
        }
        return new ResponseEntity<ErrorInfo>(new ErrorInfo("Specify correctly"), HttpStatus.NOT_FOUND);
    }
}
