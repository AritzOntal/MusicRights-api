package com.svalero.music.rights.service;

import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.repository.MusicianRepository;
import com.svalero.music.rights.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    private UserRepository userRepository;
    private MusicianRepository musicianRepository;

    public UserService(MusicianRepository musicianRepository, UserRepository userRepository) { //MEJOR CON CONSTRUCTOR QUE CON AUTOWIRED
        this.musicianRepository = musicianRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> find(String name) {
        userRepository.findByUsername(name);
        return ResponseEntity.ok().build();
    }
}
