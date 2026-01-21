package lv.venta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lv.venta.security.TwoFactorAuthenticationSuccessHandler;
import lv.venta.service.impl.MyUserDetailsManegerServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public MyUserDetailsManegerServiceImpl loadMyUserdetailsManager() {
		return new MyUserDetailsManegerServiceImpl();
	}
	
	@Bean
	public DaoAuthenticationProvider loadDaoAuthProvider() {
		PasswordEncoder passEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		MyUserDetailsManegerServiceImpl service = loadMyUserdetailsManager();
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passEncoder);
		provider.setUserDetailsService(service);
		
		return provider;
	}
	
	@Bean
	public AuthenticationSuccessHandler twoFactorAuthenticationSuccessHandler() {
		return new TwoFactorAuthenticationSuccessHandler();
	}
	
	@Bean
	public SecurityFilterChain configureUrlssecurity(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(auth -> auth
		.requestMatchers("/send-email").hasAnyAuthority("USER","ADMIN")
		
		// 2FA endpoints
		.requestMatchers("/2fa/**").authenticated()
		// 2FA endpoints - allow access when authenticated
		.requestMatchers("/2fa/setup", "/2fa/enable", "/2fa/disable", "/2fa/debug").authenticated()
		.requestMatchers("/2fa/verify").permitAll() // Allow verify page access even during login


		.requestMatchers("/crud/eparakstalogs/show/all").hasAnyAuthority("ADMIN")
		.requestMatchers("/crud/eparakstalogs/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/eparakstalogs/delete/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/eparakstalogs/insert").hasAuthority("ADMIN")
		
		.requestMatchers("/crud/maciburezultati/insert").hasAuthority("ADMIN")
		
		.requestMatchers("/crud/sertifikati/insert").hasAuthority("ADMIN")
		.requestMatchers("/filter/sertifikati/**").hasAuthority("ADMIN")
        .requestMatchers("/filter/sertifikati/form").hasAuthority("ADMIN")
        .requestMatchers("/filter/sertifikati/dalibnieks", "/filter/sertifikati/dalibnieks/**")
        .hasRole("ADMIN")
        .requestMatchers("/filter/sertifikati/kurss/**").hasAuthority("ADMIN")//
        .requestMatchers("/filter/sertifikati/kurss").hasAuthority("ADMIN") //n
        .requestMatchers("/filter/sertifikati/search").hasAuthority("ADMIN") //n
        .requestMatchers("/cache-status").hasAuthority("ADMIN") //
        .requestMatchers("/cache-clear").hasAuthority("ADMIN") //n
		
		.requestMatchers("/crud/eparakstalogs/update/**").hasAuthority("ADMIN")  //n
		
		.requestMatchers("/crud/maciburezultati/show/all").hasAuthority("ADMIN")
		.requestMatchers("/crud/maciburezultati/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/maciburezultati/delete/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/maciburezultati/update/**").hasAnyAuthority("ADMIN")
		
		.requestMatchers("/crud/sertifikati/show/all").hasAuthority("ADMIN") //
		.requestMatchers("/crud/sertifikati/show/**").hasAuthority("ADMIN") //
		.requestMatchers("/crud/sertifikati/update/**").hasAuthority("ADMIN") //n
		.requestMatchers("/crud/sertifikati/delete/**").hasAuthority("ADMIN") //n

		.requestMatchers("/statistika/**").hasAuthority("ADMIN")

		.requestMatchers("/pdf/**").hasAuthority("ADMIN") //
        
        .anyRequest().authenticated()
		);
		
		http.formLogin(form -> form
			.permitAll()
			.successHandler(twoFactorAuthenticationSuccessHandler())
		);
		
		http.csrf(auth -> auth.disable());
	
				http.logout(logout -> logout
			.logoutSuccessUrl("/login?logout")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
			.permitAll()
		);

				// Enable session management
		http.sessionManagement(session -> session
			.maximumSessions(1)
			.maxSessionsPreventsLogin(false)
		);

		return http.build();
	}
}