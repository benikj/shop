package vb.shop.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import vb.shop.model.User;

public interface UserService extends UserDetailsService {
    User findById(String id);
    User registerUser(User user);
}
