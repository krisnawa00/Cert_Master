package lv.venta.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lv.venta.model.MyUser;

public class MyUserDetails implements UserDetails{
	
	private MyUser myUser;
	
	public MyUserDetails(MyUser myUser) {
		this.myUser = myUser;
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority auth = new SimpleGrantedAuthority(myUser.getAuthority().getTitle());
		Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
		auths.add(auth);
		return auths;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return myUser.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return myUser.getUsername();
	}
	
}