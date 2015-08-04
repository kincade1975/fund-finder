package hr.betaware.fundfinder.security;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Configuration
	@Order(1)
	public static class WebConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		private CsrfMatcher csrfRequestMatcher = new CsrfMatcher();

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(new ShaPasswordEncoder());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.csrf().csrfTokenRepository(SecurityConfiguration.csrfTokenRepository())
			.requireCsrfProtectionMatcher(csrfRequestMatcher)
			.and()
			.addFilterAfter(SecurityConfiguration.csrfHeaderFilter(), CsrfFilter.class);

			http
			.authorizeRequests()
			.antMatchers("/index.html", "/webjars/**", "/css/**", "/font-awesome/**", "/fonts/**", "/img/**", "/js/**", "/views/**", "/").permitAll()
			.anyRequest().authenticated();

			http
			.formLogin();

			http.logout().permitAll();
		}

	}

	static Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if ((cookie == null) || ((token != null) && !token.equals(cookie.getValue()))) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}

				filterChain.doFilter(request, response);
			}
		};
	}

	static CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	static class CsrfMatcher implements RequestMatcher {

		private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

		@Override
		public boolean matches(HttpServletRequest request) {
			if (allowedMethods.matcher(request.getMethod()).matches()) {
				return false;
			}

			String requestURI = request.getRequestURI();
			if (requestURI.equals("/login")) {
				return false;
			}

			return true;
		}

	}


}
