package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO  postDTO, MultipartFile images, MultipartFile videos);
    Path getByFilename(String filename);
    List<PostDTO> getAllPostDesc();
}
