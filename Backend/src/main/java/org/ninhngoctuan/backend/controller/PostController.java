package org.ninhngoctuan.backend.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.ninhngoctuan.backend.dto.PostDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.service.PostService;
import org.ninhngoctuan.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Value("${images.dir}")
    private String imagesDir;
    @Value("${videos.dir}")
    private String videosDir;
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestParam("content") String content,
                                        @RequestParam(value = "images",required = false)MultipartFile images,
                                        @RequestParam(value = "videos",required = false) MultipartFile videos){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDTO userDTO = userService.info(authentication.getName());
            PostDTO postDTO = new PostDTO();
            postDTO.setContent(content);
            postDTO.setUser(userDTO);
        PostDTO post =    postService.createPost(postDTO, images,videos);
            return ResponseEntity.ok(post);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            File file = Paths.get(imagesDir).resolve(filename).toFile();
            if (file.exists()) {
                Resource resource = new FileSystemResource(file);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // Set the correct media type
                headers.setContentType(MediaType.IMAGE_PNG); // Set the correct media type
                headers.setContentType(MediaType.IMAGE_GIF); // Set the correct media type
                headers.setContentDispositionFormData("attachment", filename);
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAll(){
        try {
            List<PostDTO> list = postService.getAllPostDesc();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
}
