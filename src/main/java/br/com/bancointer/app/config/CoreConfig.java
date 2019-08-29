package br.com.bancointer.app.config;

import br.com.bancointer.app.model.Job;
import br.com.bancointer.app.repository.JobRepository;
import br.com.bancointer.app.service.JobService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Elvis Fernandes on 27/06/19
 */
@Configuration
@ComponentScan(basePackageClasses = JobService.class)
@EnableJpaRepositories(basePackageClasses = JobRepository.class)
@EntityScan(basePackageClasses = Job.class)
public class CoreConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
