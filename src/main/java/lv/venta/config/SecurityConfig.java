package lv.venta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
	public SecurityFilterChain configureUrlssecurity(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(auth -> auth
		.requestMatchers("/send-email").hasAnyAuthority("USER","ADMIN")
		.requestMatchers("/crud/eparakstalogs/show/all").hasAnyAuthority("ADMIN")
		.requestMatchers("/crud/eparakstalogs/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/eparakstalogs/delete/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/eparakstalogs/insert").hasAuthority("ADMIN")
		.requestMatchers("/crud/maciburezultati/show/all").hasAuthority("ADMIN")
		.requestMatchers("/crud/maciburezultati/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/maciburezultati/delete/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/maciburezultati/insert").hasAuthority("ADMIN")
		.requestMatchers("/crud/sertifikati/show/all").hasAuthority("ADMIN")
		.requestMatchers("/crud/sertifikati/show/**").hasAuthority("ADMIN")
		.requestMatchers("/crud/sertifikati/insert").hasAuthority("ADMIN"));
		
		
		
		http.formLogin(auth -> auth.permitAll());
		
		http.csrf(auth -> auth.disable());
		
		return http.build();
		
		
	}
	
	

}
