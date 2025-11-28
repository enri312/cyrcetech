import React, { ReactNode } from 'react';

// --- Glass Card ---
interface GlassCardProps {
  children: ReactNode;
  className?: string;
  delay?: boolean;
}

export const GlassCard: React.FC<GlassCardProps> = ({ children, className = '', delay = false }) => {
  return (
    <div className={`
      relative backdrop-blur-xl bg-white/5 border border-white/10 shadow-2xl rounded-2xl p-6
      transition-all duration-500 hover:bg-white/10 hover:shadow-neon-blue/20 hover:border-white/20
      animate-float ${delay ? 'animate-float-delayed' : ''}
      ${className}
    `}>
      {children}
    </div>
  );
};

// --- Input Field ---
interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
}

export const Input: React.FC<InputProps> = ({ label, className, ...props }) => (
  <div className="mb-4 group">
    <label className="block text-gray-400 text-sm mb-1 group-focus-within:text-neon-blue transition-colors duration-300">
      {label}
    </label>
    <input
      {...props}
      className={`
        w-full bg-space-800/50 border border-white/10 rounded-lg px-4 py-3 text-white placeholder-gray-600
        focus:outline-none focus:border-neon-blue focus:ring-1 focus:ring-neon-blue
        transition-all duration-300
        ${className}
      `}
    />
  </div>
);

// --- Text Area ---
interface TextAreaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label: string;
}

export const TextArea: React.FC<TextAreaProps> = ({ label, className, ...props }) => (
  <div className="mb-4 group">
    <label className="block text-gray-400 text-sm mb-1 group-focus-within:text-neon-blue transition-colors duration-300">
      {label}
    </label>
    <textarea
      {...props}
      className={`
        w-full bg-space-800/50 border border-white/10 rounded-lg px-4 py-3 text-white placeholder-gray-600
        focus:outline-none focus:border-neon-blue focus:ring-1 focus:ring-neon-blue
        transition-all duration-300 resize-none
        ${className}
      `}
    />
  </div>
);

// --- Button ---
interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger' | 'ghost';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
}

export const Button: React.FC<ButtonProps> = ({ 
  children, 
  variant = 'primary', 
  size = 'md',
  isLoading, 
  className, 
  ...props 
}) => {
  const baseStyles = "rounded-lg font-semibold transition-all duration-300 transform active:scale-95 flex items-center justify-center gap-2";
  
  const sizes = {
    sm: "px-3 py-1.5 text-sm",
    md: "px-6 py-3 text-base",
    lg: "px-8 py-4 text-lg"
  };

  const variants = {
    primary: "bg-gradient-to-r from-blue-600 to-neon-blue text-white shadow-lg hover:shadow-neon-blue/50 border border-transparent hover:border-white/20",
    secondary: "bg-white/5 border border-white/10 text-white hover:bg-white/10 hover:border-white/30",
    danger: "bg-gradient-to-r from-red-600 to-red-500 text-white hover:shadow-red-500/50",
    ghost: "bg-transparent text-gray-400 hover:text-white"
  };

  return (
    <button
      disabled={isLoading}
      className={`${baseStyles} ${sizes[size]} ${variants[variant]} ${isLoading ? 'opacity-70 cursor-not-allowed' : ''} ${className}`}
      {...props}
    >
      {isLoading ? (
        <span className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
      ) : children}
    </button>
  );
};

// --- Badge ---
export const StatusBadge: React.FC<{ status: string }> = ({ status }) => {
  let colors = "bg-gray-500/20 text-gray-300 border-gray-500/50";
  // Check for Spanish statuses (from types.ts)
  if (status === 'Pendiente') colors = "bg-yellow-500/20 text-yellow-300 border-yellow-500/50";
  if (status === 'En Reparación') colors = "bg-blue-500/20 text-blue-300 border-blue-500/50";
  if (status === 'Listo para Entregar') colors = "bg-green-500/20 text-green-300 border-green-500/50";
  if (status === 'Entregado') colors = "bg-gray-800 text-gray-500 border-gray-700";

  // Fallback for English statuses
  if (status === 'Pending') colors = "bg-yellow-500/20 text-yellow-300 border-yellow-500/50";
  if (status === 'In Progress') colors = "bg-blue-500/20 text-blue-300 border-blue-500/50";
  if (status === 'Ready to Deliver') colors = "bg-green-500/20 text-green-300 border-green-500/50";
  if (status === 'Delivered') colors = "bg-gray-800 text-gray-500 border-gray-700";

  return (
    <span className={`px-3 py-1 rounded-full text-xs font-medium border ${colors}`}>
      {status}
    </span>
  );
};