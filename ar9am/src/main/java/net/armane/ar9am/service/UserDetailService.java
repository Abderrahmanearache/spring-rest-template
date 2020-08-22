package net.armane.ar9am.service;

import net.armane.ar9am.Security.account.User;
import net.armane.ar9am.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountManager accountManager;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = accountManager.findUserByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User Not Found with username: " + username);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole().name())));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
