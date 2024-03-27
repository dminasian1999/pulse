package telran.java51.pulse.service;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import lombok.RequiredArgsConstructor;
import telran.java51.pulse.dto.PulseDto;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class PulseService {

	final StreamBridge streamBridge;
//	int minPulse= 40;
//	int maxPulse= 110; 

//	@Bean
//	 static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
//		return new PropertySourcesPlaceholderConfigurer();
//	}
	
	@Bean
	Consumer<PulseDto> dispatchData(@Value("${minPulse.name}")String minPulse,@Value("${maxPulse.name}")String maxPulse) {
		return data -> { 
			if (data.getPayload() < Integer.parseInt(minPulse)) {
				streamBridge.send("lowPulse-out-0", data);
				return;
			}
			if (data.getPayload() > Integer.parseInt(maxPulse)) {
				streamBridge.send("highPulse-out-0", data);
				return;
			}
			long delay = System.currentTimeMillis() - data.getTimestamp();
			System.out.println("delay: " + delay + ", id: " + data.getId() + ", pulse: " + data.getPayload());
		};
	}
}
