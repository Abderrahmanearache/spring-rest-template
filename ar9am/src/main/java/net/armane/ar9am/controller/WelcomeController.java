package net.armane.ar9am.controller;

import net.armane.ar9am.Security.account.RoleValue;
import net.armane.ar9am.entity.Task;
import net.armane.ar9am.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WelcomeController {

    @Autowired
    TaskRepository taskRepository;
    @Value("image.directory")
    String doss1ierc;

    @GetMapping(path = {"/", "/whoami"})
    public String whoami() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hi , you are loged as " + authentication.getPrincipal() +
                " your autorities are " + authentication.getAuthorities()
                ;
    }


    @GetMapping(path = "/tasks")
    @PreAuthorize("hasAnyAuthority({@roles.ADMIN,@roles.ASSISTANT})")
    public List<Task> tasks() {
        return taskRepository.findAll();
    }


    @PostMapping(path = "/tasks")
    @PreAuthorize("hasAuthority({@roles.ADMIN })")
    public Task save(@RequestBody Task s) {
        return taskRepository.save(s);
    }

}
