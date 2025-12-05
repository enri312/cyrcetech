
// === ENUMS (matching backend) ===

export enum DeviceType {
  NOTEBOOK = 'NOTEBOOK',
  SMARTPHONE = 'SMARTPHONE',
  MONITOR = 'MONITOR',
  TABLET = 'TABLET',
  CONSOLE = 'CONSOLE',
  PRINTER = 'PRINTER',
  OTHER = 'OTHER'
}

export const DeviceTypeDisplay: Record<DeviceType, { name: string; icon: string }> = {
  [DeviceType.NOTEBOOK]: { name: 'Notebook', icon: '💻' },
  [DeviceType.SMARTPHONE]: { name: 'Smartphone', icon: '📱' },
  [DeviceType.MONITOR]: { name: 'Monitor', icon: '🖥️' },
  [DeviceType.TABLET]: { name: 'Tablet', icon: '📲' },
  [DeviceType.CONSOLE]: { name: 'Consola', icon: '🎮' },
  [DeviceType.PRINTER]: { name: 'Impresora', icon: '🖨️' },
  [DeviceType.OTHER]: { name: 'Otro', icon: '🔧' }
};

export enum TicketStatus {
  PENDING = 'PENDING',
  DIAGNOSING = 'DIAGNOSING',
  IN_PROGRESS = 'IN_PROGRESS',
  WAITING_PARTS = 'WAITING_PARTS',
  READY = 'READY',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export const TicketStatusDisplay: Record<TicketStatus, { name: string; color: string }> = {
  [TicketStatus.PENDING]: { name: 'Pendiente', color: '#FFC107' },
  [TicketStatus.DIAGNOSING]: { name: 'Diagnosticando', color: '#2196F3' },
  [TicketStatus.IN_PROGRESS]: { name: 'En Progreso', color: '#FF9800' },
  [TicketStatus.WAITING_PARTS]: { name: 'Esperando Repuestos', color: '#9C27B0' },
  [TicketStatus.READY]: { name: 'Listo', color: '#4CAF50' },
  [TicketStatus.DELIVERED]: { name: 'Entregado', color: '#00BCD4' },
  [TicketStatus.CANCELLED]: { name: 'Cancelado', color: '#F44336' }
};

export enum PaymentStatus {
  PENDING = 'PENDING',
  PAID = 'PAID',
  OVERDUE = 'OVERDUE',
  CANCELLED = 'CANCELLED'
}

export enum PaymentMethod {
  CASH = 'CASH',
  CREDIT_CARD = 'CREDIT_CARD',
  DEBIT_CARD = 'DEBIT_CARD',
  BANK_TRANSFER = 'BANK_TRANSFER',
  DIGITAL_WALLET = 'DIGITAL_WALLET',
  OTHER = 'OTHER'
}

// === INTERFACES (matching backend) ===

export interface Customer {
  id: string;
  name: string;
  taxId: string;
  address: string;
  phone: string;
  formattedPhone?: string;
}

export interface Equipment {
  id: string;
  deviceType: DeviceType;
  brand: string;
  model: string;
  serialNumber: string;
  physicalCondition: string;
  customerId: string;
  customerName?: string;
}

export interface SparePart {
  id: string;
  name: string;
  price: number;
  stock: number;
  minStock: number;
  provider: string;
  lowStock?: boolean;
}

export interface Ticket {
  id: string;
  customer: Customer;
  equipment?: Equipment;
  deviceType: DeviceType;
  brand: string;
  model: string;
  serialNumber: string;
  physicalCondition: string;
  problemDescription: string;
  observations: string;
  status: TicketStatus;
  amountPaid: number;
  estimatedCost: number;
  dateCreated: string;
  dateCompleted?: string;
  aiDiagnosis?: string;
}

export interface Invoice {
  id: string;
  invoiceNumber: string;
  ticketId: string;
  customerId: string;
  customerName?: string;
  issueDate: string;
  dueDate: string;
  laborCost: number;
  partsCost: number;
  taxAmount: number;
  totalAmount: number;
  paymentStatus: PaymentStatus;
  paymentMethod?: PaymentMethod;
  notes?: string;
}

// === VIEW TYPES ===

export type ViewState = 'LOGIN' | 'DASHBOARD' | 'NEW_TICKET' | 'LIST_TICKETS' | 'INVENTORY_PARTS' | 'HISTORY' | 'CLIENTS' | 'EQUIPMENT' | 'INVOICES';

export type Language = 'es' | 'en';