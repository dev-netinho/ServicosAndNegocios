package com.deefy.group2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.deefy.group2.dto.request.ListeningHistoryRequest;
import com.deefy.group2.service.impl.ListeningHistoryServiceImpl;
import com.deefy.group2.repository.*;
import com.deefy.group2.mapper.*;

@SpringBootApplication
public class DeefyGroup2Application {

    public static void main(String[] args) {
        SpringApplication.run(DeefyGroup2Application.class, args);
    }
}
