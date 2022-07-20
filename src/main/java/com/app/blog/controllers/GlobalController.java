package com.app.blog.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.dto.PostDTO;
import com.app.blog.dto.UpdatePostDTO;
import com.app.blog.models.Posts;
import com.app.blog.models.Users;
import com.app.blog.repository.PostRepository;
import com.app.blog.util.EntitiyHawk;
import com.app.blog.util.PostMapper;

import io.jsonwebtoken.Claims;
import org.springframework.web.server.ResponseStatusException;

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
@RequestMapping("/api")
public class GlobalController extends EntitiyHawk {

    @PostMapping("/publish")
    public ResponseEntity publishPost(@RequestBody PostDTO postDTO){

        return this.genericSuccess();
    }

    @GetMapping("/getPost")
    public ResponseEntity getAllPosts(){

        return this.genericSuccess(0);
    }

    @GetMapping("/getPostCount")
    public ResponseEntity getPostCount(){
        return this.genericSuccess(0);
    }

    @GetMapping("/getPostByUser/{userId}")
    public ResponseEntity getPostByUser(){
        return this.genericSuccess(0);
    }

    @PostMapping("/updatePost")
    public ResponseEntity updatePost(@RequestBody PostDTO postDTO){
        return this.genericSuccess();
    }

    @GetMapping("/getPost/{postID}")
    public ResponseEntity getPostById(){
        return this.genericSuccess(0);
    }

    @GetMapping("/deletePost/{postID}")
    public ResponseEntity deletePostById(){
        return this.genericSuccess(0);
    }

}
