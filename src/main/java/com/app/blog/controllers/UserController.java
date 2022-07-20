package com.app.blog.controllers;

import com.app.blog.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.app.blog.dto.LoginDto;
import com.app.blog.dto.RegisterUserDTO;
import com.app.blog.models.Users;
import com.app.blog.repository.UserRepository;
import com.app.blog.util.EntitiyHawk;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 1460344
 */
@RestController
@RequestMapping("/")
public class UserController extends EntitiyHawk {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTUtils jwtUtils;

	@PostMapping("/register")
	public ResponseEntity signup(@RequestBody RegisterUserDTO registerUserDTO)
	{
		if(registerUserDTO.getEmail()==null || registerUserDTO.getEmail().isEmpty())
			return this.genericError("email Email cannot be blank");
		Users user = new Users();
		user.setUserName(registerUserDTO.getName());
		user.setPassword(registerUserDTO.getPassword());
		user.setEmail(registerUserDTO.getEmail());
		
		userRepository.save(user);
		return this.genericSuccess("User Registered");
	}
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginDto loginDto) throws Exception {
		System.out.println("inside login....before");
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					loginDto.getEmail(), loginDto.getPassword()));
			System.out.println("inside login....between");
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (BadCredentialsException e){
//			throw  new Exception("Incorrect Username or Password!", e);
			return this.genericError("Invalid Username or Password");
		}

		Users user = userRepository.findByEmail(loginDto.getEmail()) .orElseThrow(() -> new IllegalStateException("User Not Found"));
		final String token = jwtUtils.CreateJWTToken(user);

        return this.genericSuccess(token);
	}

	@GetMapping ("/hello")
	public String Hello(){
		return "Hola Mundo";
	}
}
