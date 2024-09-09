import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import PostList from '../components/PostList';

const HomePage = () => {
  return (
    <div className="container mt-4">
      <div className="row">
        {/* Cột bên trái */}
        <div className="col-md-3">
          <div className="sidebar bg-light p-3">
            <h4>Sidebar Left</h4>
            {/* Nội dung sidebar bên trái */}
          </div>
        </div>

        {/* Cột giữa */}
        <div className="col-md-6 ">
            <h1 className="text-center mb-4">Welcome to the Social Network</h1>
            <PostList />
        
        </div>

        {/* Cột bên phải */}
        <div className="col-md-3">
          <div className="sidebar bg-light p-3">
            <h4>Sidebar Right</h4>
            {/* Nội dung sidebar bên phải */}
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
