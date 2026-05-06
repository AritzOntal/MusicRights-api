package com.svalero.music.rights;

import com.svalero.music.rights.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class MusicRights {

    public static void main(String[] args)  {
        SpringApplication.run(MusicRights.class, args);
    }
}