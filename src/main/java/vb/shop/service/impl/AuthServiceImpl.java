package vb.shop.service.impl;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vb.shop.model.Role;
import vb.shop.model.User;
import vb.shop.model.exception.InvalidArgumentException;
import vb.shop.model.exception.InvalidUserCredentials;
import vb.shop.model.exception.PasswordsDoNotMatchException;
import vb.shop.repository.RoleRepository;
import vb.shop.repository.UserRepository;
import vb.shop.service.AuthService;
import vb.shop.service.UserService;

import javax.annotation.PostConstruct;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }




    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentUser().getUsername();
    }


    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() ||password==null ||password.isEmpty()){
            throw new InvalidArgumentException();
        }

        return this.userRepository.findByUsernameAndPassword(username,password).orElseThrow(InvalidUserCredentials::new);

    }

    @Override
    public User register(String username, String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)){
            throw new PasswordsDoNotMatchException();
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role role= this.roleRepository.findByName("ROLE_USER");
        return this.userService.registerUser(user);
    }



    @PostConstruct
    public void init() {
        if (!this.userRepository.existsById("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(this.passwordEncoder.encode("admin"));
            admin.setRoles(this.roleRepository.findAll());
            this.userRepository.save(admin);
        }
    }
}
