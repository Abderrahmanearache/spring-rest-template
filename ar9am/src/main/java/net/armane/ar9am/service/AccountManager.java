package net.armane.ar9am.service;

import net.armane.ar9am.Security.account.Role;
import net.armane.ar9am.Security.account.RoleValue;
import net.armane.ar9am.Security.account.User;
import net.armane.ar9am.repository.RoleRepository;
import net.armane.ar9am.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AccountManager {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addRoleToUser(String username, String rolename) {
        User user = userRepository.findByUsername(username);
        user.getRoles().add(roleRepository.findByRole(RoleValue.valueOf(rolename)));
    }

    public void addRoleToUser(User user, RoleValue role) {
        addRoleToUser(user.getUsername(), role.name());
    }

    public void addRoleToUser(User user, String role) {
        addRoleToUser(user.getUsername(), role);
    }

    public void addRoleToUser(String username, RoleValue role) {
        addRoleToUser(username, role.name());
    }


    public void deleteAllUser() {
        userRepository.deleteAll();
    }
}
