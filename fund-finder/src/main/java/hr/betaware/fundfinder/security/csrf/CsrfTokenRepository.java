package hr.betaware.fundfinder.security.csrf;

import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class CsrfTokenRepository {

	private static HttpSessionCsrfTokenRepository repository;

	public CsrfTokenRepository() {
		repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
	}

	public HttpSessionCsrfTokenRepository getRepository() {
		return repository;
	}

}
