package com.echomap.cherryblossomclean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.swing.*;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
public class CherryBlossomCleanApplication {

  public static void main(String[] args) {
    SpringApplication.run(CherryBlossomCleanApplication.class, args);
  }
}
