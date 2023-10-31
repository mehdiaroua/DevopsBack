package tn.esprit.devops_project;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {
   @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // This allows CORS for all endpoints
                        .allowedOrigins("http://192.168.33.10:8083") // Replace with your Angular app's domain
                        .allowedMethods("GET", "POST"); // Specify the HTTP methods you want to allow
            }
        };
    }
}
