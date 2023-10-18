package monitoring.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.dto.EmailNotificationData;
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailDataProviderImpl implements EmailDataProvider {
	
	final RestTemplate restTemplate;
	@Value("${app.email.provider.api.url:data}")
	String urlApi;
	@Value("${app.email.provider.host:localhost}")
	String host;
	@Value("${app.email.provider.port:8080}")
	int port;
	
	@Override
	public EmailNotificationData getData(long patientId) {
		String url = getUrl(patientId);
		log.trace("full url is {}", url);
		EmailNotificationData res = null;
		try {
			ResponseEntity<EmailNotificationData> responseEntity =
					restTemplate.exchange(url, HttpMethod.GET, null,
							EmailNotificationData.class);
			res = responseEntity.getBody();
		} catch (Exception e) {
			log.error("error: {}", e.getMessage());
		}
		return res;
	}
	private String getUrl(long patientId) {
		
		return String.format("http://%s:%d/%s/%d", host,port,urlApi,patientId);
	}

}
