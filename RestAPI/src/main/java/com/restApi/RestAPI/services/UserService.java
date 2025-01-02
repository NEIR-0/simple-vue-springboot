package com.restApi.RestAPI.services;

import com.restApi.RestAPI.config.JwtUtil;
import com.restApi.RestAPI.dto.UserDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
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

    public List<UserDTO> getAllUsers(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Users> usersPage;
        if (search != null && !search.isEmpty()) {
            usersPage = userRepository.findByEmailContainingIgnoreCase(search, pageRequest);
        } else {
            usersPage = userRepository.findAll(pageRequest);
        }

        return usersPage.getContent()
                .stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsersWithoutAdmin(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Users> usersPage;
        if (search != null && !search.isEmpty()) {
            usersPage = userRepository.findByEmailContainingIgnoreCase(search, pageRequest);
        } else {
            usersPage = userRepository.findAll(pageRequest);
        }

        return usersPage.getContent()
                .stream()
                .filter(user -> !"admin".equalsIgnoreCase(user.getRole())) // Filter out admin
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getRole()))
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

    public ResponseDTOOutput findByEmail(Users inputUser){
        Optional<Users> matchingUser = userRepository.findByEmail(inputUser.getEmail());
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        if(matchingUser.isPresent()){
            Users dataUser = matchingUser.get();
            if(passwordEncoder.matches(inputUser.getPassword(), dataUser.getPassword())){
                String token = "Bearer " + jwtUtil.generateToken(dataUser.getEmail(), dataUser.getId(), dataUser.getRole());
                responseStatus.setMsg(token);
                responseStatus.setStatus(dataUser.getRole());
                return responseStatus;
            } else {
                responseStatus.setMsg("email / password invalid");
                responseStatus.setStatus("failed");
                return responseStatus;
            }
        }else{
            responseStatus.setMsg("email / password invalid");
            responseStatus.setStatus("failed");
            return responseStatus;
        }
    }

    public ResponseDTOOutput emailChecker(String inputUser){
        Optional<Users> matchingUser = userRepository.findByEmail(inputUser);
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        if (matchingUser.isPresent()) {
            responseStatus.setMsg("email already exist");
            responseStatus.setStatus("failed");
            return responseStatus;
        } else {
            responseStatus.setMsg("email available");
            responseStatus.setStatus("success");
            return responseStatus;
        }
    }
}
