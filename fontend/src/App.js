import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import PrivateRoute from './components/PrivateRoute';

const App = () => {
  return (
    <Router>
      <Routes>
        {/* Trang chủ */}
        <Route path="/" element={<HomePage />} />
        
        {/* Trang đăng nhập */}
        
        {/* Các route được bảo vệ */}
        {/* <Route element={<PrivateRoute />}>
          <Route path="/add-post" element={<AddPostForm />} />
        </Route> */}
      </Routes>
    </Router>
  );
};

export default App;
