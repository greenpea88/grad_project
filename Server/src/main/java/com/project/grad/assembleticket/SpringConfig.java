package com.project.grad.assembleticket;

import com.project.grad.assembleticket.domain.repository.ShowRepository;
import com.project.grad.assembleticket.service.ShowService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final ShowRepository showRepository;

    public SpringConfig(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    @Bean
    public ShowService ShowService() {
        return new ShowService(showRepository);
    }
}
