// src/hooks/useAuth.js
import axios from 'axios';
import { useEffect, useState } from 'react';
export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userRole, setUserRole] = useState('');

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const response = await axios.get('/api/auth/status'); // Sử dụng axios instance
        setIsAuthenticated(response.data.isAuthenticated);
        setUserRole(response.data.role);
      } catch (error) {
        setIsAuthenticated(false);
        setUserRole('');
      }
    };

    checkAuth();
  }, []);

  return { isAuthenticated, userRole };
};
