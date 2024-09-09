package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.config.FileStorageConfig;
import org.ninhngoctuan.backend.dto.PostDTO;
import org.ninhngoctuan.backend.entity.PostEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.PostRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.ninhngoctuan.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {
    @Value("${images.dir}")
    private String imagesDir;
    @Value("${videos.dir}")
    private String videosDir;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public PostDTO createPost(PostDTO postDTO, MultipartFile images, MultipartFile videos) {
        try {
            // Chuyển đổi DTO thành Entity
            PostEntity post = modelMapper.map(postDTO, PostEntity.class);

            // Tìm user và đảm bảo user tồn tại
            UserEntity user = userRepository.findByUserId(post.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("Không có tài khoản nào có id là: " + post.getUser().getUserId()));

            // Tạo đường dẫn lưu file
            Path uploadPathImages = Paths.get(imagesDir);
            Path uploadPathVideos = Paths.get(videosDir);

            // Tạo thư mục nếu chưa tồn tại
            try {
                if (!Files.exists(uploadPathImages)) {
                    Files.createDirectories(uploadPathImages);
                }
                if (!Files.exists(uploadPathVideos)) {
                    Files.createDirectories(uploadPathVideos);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create directories. Please check permissions.", e);
            }

            // Xử lý ảnh
            if (images != null && !images.isEmpty()) {
                try {
                    String imageFileName = UUID.randomUUID().toString()+"_"+ images.getOriginalFilename();
                    if (imageFileName == null || imageFileName.isEmpty()) {
                        throw new RuntimeException("File name is invalid.");
                    }
                    Path imageFilePath = uploadPathImages.resolve(imageFileName);
                    Files.copy(images.getInputStream(), imageFilePath, StandardCopyOption.REPLACE_EXISTING);
                    post.setImageUrl(imageFileName);
                } catch (IOException e) {
                    throw new RuntimeException("Could not store file " + images.getOriginalFilename() + ". Please try again!", e);
                }
            }

            // Xử lý video (nếu cần)
            if (videos != null && !videos.isEmpty()) {
                try {
                    String videoFileName = UUID.randomUUID().toString()+"_"+videos.getOriginalFilename();
                    if (videoFileName == null || videoFileName.isEmpty()) {
                        throw new RuntimeException("File name is invalid.");
                    }
                    Path videoFilePath = uploadPathVideos.resolve(videoFileName);
                    Files.copy(videos.getInputStream(), videoFilePath, StandardCopyOption.REPLACE_EXISTING);
                    // Bạn có thể lưu URL video nếu cần thiết
                } catch (IOException e) {
                    throw new RuntimeException("Could not store file " + videos.getOriginalFilename() + ". Please try again!", e);
                }
            }

            // Cập nhật thời gian và user
            post.setCreatedAt(new java.util.Date());
            post.setUser(user);

            // Lưu bài viết vào cơ sở dữ liệu
            PostEntity savedPost = postRepository.save(post);
            return modelMapper.map(savedPost, PostDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating the post: " + e.getMessage(), e);
        }
    }

    @Override
    public Path getByFilename(String filename) {
        return  Paths.get(imagesDir).resolve(filename);
    }

    @Override
    public List<PostDTO> getAllPostDesc() {
        List<PostEntity> postEntities = postRepository.findAll();
        List<PostDTO> list = new ArrayList<>();
            for (PostEntity post: postEntities){
                PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                list.add(postDTO);
            }
        return list;
    }
}
