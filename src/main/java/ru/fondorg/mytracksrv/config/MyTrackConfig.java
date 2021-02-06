package ru.fondorg.mytracksrv.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MyTrackProperties.class)
public class MyTrackConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
