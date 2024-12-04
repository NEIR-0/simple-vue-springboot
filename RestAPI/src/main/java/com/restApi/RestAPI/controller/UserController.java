package com.restApi.RestAPI.controller;

import com.restApi.RestAPI.interfaceList.CardDTO;
import com.restApi.RestAPI.interfaceList.JobDTO;
import com.restApi.RestAPI.interfaceList.UserDTO;
import com.restApi.RestAPI.model.User;
import com.restApi.RestAPI.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//logs:
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Transactional
    public List<User> getAllUsersCustomSorted() {
        // return userRepository.findAll(); // basic
        //  return userRepository.findAllUsersSortedByName(); // manual menggunakan repository
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "name")); // ini pakai bawaan jpa
    }

    @GetMapping("/with-jobs")
    public List<UserDTO> getAllUsers() {
        Logger logger = LoggerFactory.getLogger(getClass());

        // Memanggil method repository untuk mendapatkan list users
        List<User> users = userRepository.findAllWithJobs();

        // Melakukan logging untuk melihat isi dari users
        logger.info("Isi users: {}", users);

        // Logging lebih detail untuk melihat properties dari user jika perlu
        users.forEach(user -> logger.info("User ID: {}, Name: {}, Jobs: {}",
                user.getId(), user.getName(), user.getJobs().size()));

        return users.stream().map(user -> new UserDTO(
                user.getId(),
                user.getName(),
                user.getCard() != null ? new CardDTO(
                        user.getCard().getCardNumber(),
                        user.getCard().getJoinDate().toString()) : null,
                user.getJobs().stream().map(job -> new JobDTO(
                        job.getId(),
                        job.getJobTitle(),
                        job.getSalary()
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setName(userDetails.getName());
            return userRepository.save(existingUser);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
