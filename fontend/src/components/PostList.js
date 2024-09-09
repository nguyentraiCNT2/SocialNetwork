import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Post from './Post';
import '../stype/Postlist.css';
const PostList = () => {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await axios.get('http://localhost:8080/post/getall'); // Endpoint của API lấy bài viết
        setPosts(response.data);
      } catch (error) {
        console.error("Failed to fetch posts", error);
      }
    };

    fetchPosts();
  }, []);

  return (
    <div className="post-list align-items-center">
      {posts.length === 0 ? (
        <p className="text-center">No posts available</p>
      ) : (
        posts.map(post => (
          <Post key={post.postId} post={post} />
        ))
      )}
    </div>
  );
};

export default PostList;
