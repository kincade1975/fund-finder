package hr.betaware.fundfinder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;

import hr.betaware.fundfinder.security.csrf.CsrfHeaderFilter;
import hr.betaware.fundfinder.security.csrf.CsrfRequestMatcher;
import hr.betaware.fundfinder.security.csrf.CsrfTokenRepository;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Configuration
	@Order(1)
	public static class WebConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		private CsrfRequestMatcher csrfRequestMatcher;

		@Autowired
		private CsrfTokenRepository csrfTokenRepository;

		@Autowired
		private CsrfHeaderFilter csrfHeaderFilter;

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(new ShaPasswordEncoder());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.csrf().csrfTokenRepository(csrfTokenRepository.getRepository())
			.requireCsrfProtectionMatcher(csrfRequestMatcher)
			.and()
			.addFilterAfter(csrfHeaderFilter.getFilter(), CsrfFilter.class);

			http
			.authorizeRequests()
			.antMatchers("/index.html", "/webjars/**", "/css/**", "/font-awesome/**", "/fonts/**", "/img/**", "/js/**", "/views/**", "/").permitAll()
			.anyRequest().authenticated();

			http
			.formLogin().loginPage("/index.html").loginProcessingUrl("/login").defaultSuccessUrl("/index.html", true);

			http
			.logout().permitAll();
		}

	}

}
