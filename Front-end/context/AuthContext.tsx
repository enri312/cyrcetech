import React, { createContext, useState, useContext, useEffect } from 'react';
import { authApi, AuthResponse, LoginRequest, RegisterRequest } from '../services/api';

interface User {
    id: string;
    email: string;
    role: string;
    fullName?: string; // We might want to store this
}

interface AuthContextType {
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
    login: (data: LoginRequest) => Promise<void>;
    register: (data: RegisterRequest) => Promise<void>;
    logout: () => void;
    loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    // Init auth from localStorage
    useEffect(() => {
        const storedToken = localStorage.getItem('token');
        const storedUser = localStorage.getItem('user');

        if (storedToken && storedUser) {
            setToken(storedToken);
            try {
                setUser(JSON.parse(storedUser));
            } catch (e) {
                console.error("Error parsing stored user", e);
                localStorage.removeItem('user');
            }
        }
        setLoading(false);
    }, []);

    const login = async (data: LoginRequest) => {
        const response = await authApi.login(data);
        handleAuthResponse(response);
    };

    const register = async (data: RegisterRequest) => {
        const response = await authApi.register(data);
        handleAuthResponse(response);
    };

    const handleAuthResponse = (response: AuthResponse) => {
        const userData: User = {
            id: response.userId,
            email: response.email,
            role: response.role
        };

        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(userData));

        setToken(response.token);
        setUser(userData);
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setToken(null);
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{
            user,
            token,
            isAuthenticated: !!token,
            login,
            register,
            logout,
            loading
        }}>
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
