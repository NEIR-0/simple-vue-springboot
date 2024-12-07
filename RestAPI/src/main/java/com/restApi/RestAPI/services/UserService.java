package com.restApi.RestAPI.services;

import com.restApi.RestAPI.config.JwtUtil;
import com.restApi.RestAPI.dto.UserDTO;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public List<UserDTO> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);  // Mengatur halaman dan ukuran
        Page<Users> usersPage = userRepository.findAll(pageRequest);
        // return usersPage.getContent();
        return usersPage.getContent().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail()))
                .collect(Collectors.toList());
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
                return ("Bearer " + jwtUtil.generateToken(inputUser.getEmail(), inputUser.getId()));
            } else {
                return "email / password invalid";
            }
        }else{
            return "email / password invalid";
        }
    }
}
