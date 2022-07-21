package com.app.blog.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.app.blog.repository.UserRepository;
import com.app.blog.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtility;


    @PostMapping("/publish")
    public ResponseEntity publishPost(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody PostDTO postDTO){
        if(postDTO==null)
            return this.genericError("body should not be empty");
        if(postDTO.getBody()==null||postDTO.getBody().isEmpty())
            return this.genericError("body should not be empty");

        String token = jwtUtility.getTokenFromHeader(authHeader);
        String userId = jwtUtility.getUserId(token);

        Users user = new Users();
        user.setUserId(Integer.parseInt(userId));
        Posts post = new Posts();
        post.setPostTitle(postDTO.getTitle());
        post.setPostBody(postDTO.getBody());
        post.setPublishedBy(user);

        postRepository.save(post);
        return this.genericSuccess("Published");

    }

    @GetMapping("/getPost")
    public ResponseEntity getAllPosts(){

        List<Posts> allPosts= postRepository.findAll();//new ArrayList<>();
//        List<UpdatePostDTO> allPostDTOs = new ArrayList<>();
//        for (Posts post:
//                allPosts) {
//            UpdatePostDTO postDTO = new UpdatePostDTO();
//            postDTO.setPost_id(post.getPostId());
//            postDTO.setBody(post.getPostBody());
//            postDTO.setTitle(post.getPostTitle());
//            allPostDTOs.add(postDTO);
//        }

        List<Map> allPostDTOs = new ArrayList<>();
        for (Posts post:
                allPosts) {
//            UpdatePostDTO postDTO = new UpdatePostDTO();
//            postDTO.setPost_id(post.getPostId());
//            postDTO.setBody(post.getPostBody());
//            postDTO.setTitle(post.getPostTitle());

            PostMapper postMapper = new PostMapper();
            allPostDTOs.add(postMapper.postDetailsToMap(post));
        }


        return this.genericSuccess(allPostDTOs);
    }

    @GetMapping("/getPostCount")
    public ResponseEntity getPostCount(){

        return this.genericSuccess(postRepository.findAll().size());
    }

    @GetMapping("/getPostByUser/{userId}")
    public ResponseEntity getPostByUser(@PathVariable("userId") Integer userId){
        Optional<Users> optionalUsers = userRepository.findById(userId);


        if (!optionalUsers.isPresent())
            return this.genericError("No posts by user Id " + userId);


        List<Posts> allPosts= optionalUsers.get().getPostsList();
        List<Map> allPostDTOs = new ArrayList<>();
        for (Posts post:
                allPosts) {
            allPostDTOs.add(new PostMapper().postDetailsToMap(post));
        }

        if(allPostDTOs.size()==0)
            return this.genericError("No posts by user Id " + userId);

        return this.genericSuccess(allPostDTOs);
    }

    @PostMapping("/updatePost")
    public ResponseEntity updatePost(@RequestBody UpdatePostDTO updatePostDTO){
        Posts post = postRepository.findById(updatePostDTO.getPost_id())
                .orElseThrow(() -> new IllegalStateException("Post Not Found"));

        if(updatePostDTO.getTitle()!=null && updatePostDTO.getTitle().length() > 0 &&
                !Objects.equals(updatePostDTO.getTitle(), post.getPostTitle())){
            post.setPostTitle(updatePostDTO.getTitle());
        }

        if(updatePostDTO.getBody()!=null && updatePostDTO.getBody().length() > 0 &&
                !Objects.equals(updatePostDTO.getBody(), post.getPostBody())){
            post.setPostBody(updatePostDTO.getBody());
        }

        postRepository.save(post);
        return this.genericSuccess("Post updated");
    }

    @GetMapping("/getPost/{postId}")
    public ResponseEntity getPostById(@PathVariable("postId") Integer postId){

        boolean exists = postRepository.existsById(postId);
        if (!exists) {
            return this.genericError("Post Not Found");
        }

        Posts post = postRepository.findById(postId).orElseThrow(()-> new IllegalStateException("Post Not Found!"));
        return this.genericSuccess(new PostMapper().postDetailsToMap(post));

    }

    @GetMapping("/deletePost/{postId}")
    public ResponseEntity deletePostById(@PathVariable("postId") Integer postId){

        boolean exists = postRepository.existsById(postId);
        if (!exists) {
            return this.genericError("Post Not Found");
        }

        postRepository.deleteById(postId);

        return this.genericSuccess("Post Deleted");
    }

}
