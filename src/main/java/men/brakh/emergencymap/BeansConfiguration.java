package men.brakh.emergencymap;

import men.brakh.emergencymap.http.APICommunication;
import men.brakh.emergencymap.http.api.NominatimAPICommunication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean(name="apiBean")
    public APICommunication apiCommunication() {
        return new NominatimAPICommunication();
    }
}
