package com.app.blog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.blog.models.Users;
import com.app.blog.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	/*
	 * public CustomUserDetailService(UserRepository userRepository) {
	 * this.userRepository = userRepository; }
	 */

//    @Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Users user = userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("no user found"+username));
//		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), true, true, true, true, getAuthorities("ROLE_USER"));
//
////		return new User("admin","pass", new ArrayList<>());
//	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Users user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("no user found"+email));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, getAuthorities("ROLE_USER"));

//		return new User("admin","pass", new ArrayList<>());
	}



	private Collection<? extends GrantedAuthority> getAuthorities(String role_user) {
		
		return Collections.singletonList(new SimpleGrantedAuthority(role_user));
	}

	
}
