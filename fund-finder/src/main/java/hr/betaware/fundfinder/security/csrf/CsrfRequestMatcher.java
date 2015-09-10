package hr.betaware.fundfinder.security.csrf;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class CsrfRequestMatcher implements RequestMatcher {

	private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

	@Override
	public boolean matches(HttpServletRequest request) {
		if (allowedMethods.matcher(request.getMethod()).matches()) {
			return false;
		}

		String requestURI = request.getRequestURI();
		if (requestURI.equals("/info")
				|| requestURI.equals("/health")
				|| requestURI.equals("/metrics")
				|| requestURI.equals("/shutdown")
				|| requestURI.equals("/env")
				|| requestURI.startsWith("/registration")
				|| requestURI.startsWith("/password")
				|| requestURI.equals("/login")) {
			return false;
		}

		return true;
	}

}
