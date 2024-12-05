package com.restApi.RestAPI.services;

import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public String createUser(Users user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "Berhasil create user";
    }

    public Users updateUser(Long id, Users user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public String findByEmail(Users inputUser){
        Optional<Users> matchingUser = userRepository.findByEmail(inputUser.getEmail());
        if(matchingUser.isPresent()){
            Users dataUser = matchingUser.get();
            if(passwordEncoder.matches(inputUser.getPassword(), dataUser.getPassword())){
                return "Login berhasil";
            } else {
                return "email / password invalid";
            }
        }else{
            return "email / password invalid";
        }
    }
}
