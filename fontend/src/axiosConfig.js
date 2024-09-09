// src/axiosConfig.js
import axios from 'axios';

// Tạo một instance Axios với cấu hình cơ bản
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/', // Cấu hình base URL cho server
});

export default axiosInstance;
