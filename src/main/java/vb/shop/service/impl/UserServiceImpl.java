package vb.shop.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vb.shop.model.User;
import vb.shop.model.exception.UserAlreadyExistException;
import vb.shop.model.exception.UserNotFoundException;
import vb.shop.repository.UserRepository;
import vb.shop.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(String id) {
        return this.userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException());
    }

    @Override
    public User registerUser(User user) {
            if (this.userRepository.existsById(user.getUsername())){
                throw new UserAlreadyExistException();
            }
        return this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findById(username)
                .orElseThrow(()->new UsernameNotFoundException(username));
    }
}
