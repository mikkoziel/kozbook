package com.kozbook.server.service;

import com.kozbook.server.entity.model.User;
import com.kozbook.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listAllUser() {
        return userRepository.findAll();
    }

    public User saveUser(User user) throws ValidationException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ValidationException("Username exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUser(Integer id) {
        return userRepository.findById(id).get();
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public void checkIfValidPassword(User user, String password) throws Exception{
        if(!passwordEncoder.matches(password, user.getPassword())){
            logger.info("Wrong");
            throw new Exception();
        }
        logger.info("Right");
    }

    public void changeUserPassword(User user, String password){
        String encodedPassword = passwordEncoder.encode(password).replace("{bcrypt}", "");
        user.setPassword(encodedPassword);
        this.userRepository.save(user);
    }
}
