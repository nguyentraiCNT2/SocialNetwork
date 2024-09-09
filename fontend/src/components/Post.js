import PropTypes from 'prop-types';
import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const Post = ({ post }) => {
  return (
    <div className="card mb-4 post_item">
        <p className="card-text"><small className="text-muted">Posted by {post.user.fullName}</small></p>
        <p className="card-text"><small className="text-muted">{new Date(post.createdAt).toLocaleDateString()}</small></p>
        <p className="card-text">{post.content}</p>

      {post.imageUrl && (
        <img src={`http://localhost:8080/images/${post.imageUrl}`} alt="Post" className=" images_post" />
      )}
      <div className="card-body">
      </div>
    </div>
  );
};

Post.propTypes = {
  post: PropTypes.shape({
    content: PropTypes.string.isRequired,
    imageUrl: PropTypes.string,
    author: PropTypes.string.isRequired,
    createdAt: PropTypes.string.isRequired,
  }).isRequired,
};

export default Post;
