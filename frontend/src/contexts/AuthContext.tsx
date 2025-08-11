
import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';
import { useNotificationContext } from './NotificationContext';
import apiClient from '../utils/api';

interface User {
  id: string;
  email: string;
  nickname: string;
}

interface AuthContextType {
  isAuthenticated: boolean;
  user: User | null;
  accessToken: string | null;
  refreshToken: string | null;
  login: (email: string, password: string) => Promise<boolean>;
  logout: () => void;
  updateUser: (userData: Partial<User>) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<User | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [refreshToken, setRefreshToken] = useState<string | null>(null);
  const { showSuccess, showInfo } = useNotificationContext();

  useEffect(() => {
    const storedAccessToken = localStorage.getItem('accessToken');
    const storedRefreshToken = localStorage.getItem('refreshToken');
    const storedUser = localStorage.getItem('user');
    
    if (storedAccessToken && storedRefreshToken && storedUser) {
      setAccessToken(storedAccessToken);
      setRefreshToken(storedRefreshToken);
      setUser(JSON.parse(storedUser));
      setIsAuthenticated(true);
    }
  }, []);


  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      const data = await apiClient.post('/user/signIn', { email, password }, { skipAuth: true });
        
        // JWT 토큰 저장
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        setAccessToken(data.accessToken);
        setRefreshToken(data.refreshToken);
        
        // JWT 디코딩하여 사용자 정보 추출
        const payload = JSON.parse(atob(data.accessToken.split('.')[1]));
        const userData = {
          id: payload.sub || payload.id,
          email: payload.email || email,
          nickname: payload.nickname || payload.name || email.split('@')[0]
        };
        
        localStorage.setItem('user', JSON.stringify(userData));
        setIsAuthenticated(true);
        setUser(userData);
        showSuccess('환영합니다!', '성공적으로 로그인되었습니다.');
        return true;
    } catch (error: any) {
      if (error.message === 'Failed to fetch') {
        throw new Error('서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.');
      }
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    setAccessToken(null);
    setRefreshToken(null);
    setIsAuthenticated(false);
    setUser(null);
    showInfo('로그아웃', '안전하게 로그아웃되었습니다.');
  };

  const updateUser = (userData: Partial<User>) => {
    if (user) {
      setUser({ ...user, ...userData });
    }
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, accessToken, refreshToken, login, logout, updateUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
