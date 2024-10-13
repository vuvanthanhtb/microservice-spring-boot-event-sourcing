package com.thanhvv.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.thanhvv.user.data.User;
import com.thanhvv.user.data.UserRepository;
import com.thanhvv.user.model.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User user) {
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);

        newUser.setId(UUID.randomUUID().toString());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    public UserDto login(String username, String password) {
        User user = this.userRepository.findByUsername(username);
        UserDto userDto = new UserDto();

        if (user != null) {
            BeanUtils.copyProperties(user, userDto);
            if (passwordEncoder.matches(password, user.getPassword())) {
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .sign(algorithm);
                String refreshToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10080 * 60 * 1000))
                        .sign(algorithm);

                userDto.setToken(accessToken);
                userDto.setRefreshToken(refreshToken);
            }
        }
        return userDto;
    }
}
