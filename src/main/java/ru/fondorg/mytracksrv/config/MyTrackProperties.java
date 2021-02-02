package ru.fondorg.mytracksrv.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mytrack")
public class MyTrackProperties {
}
