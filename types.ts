
export enum DeviceType {
  NOTEBOOK = 'Notebook',
  SMARTPHONE = 'Celular',
  DESKTOP = 'PC de Mesa'
}

export enum TicketStatus {
  PENDING = 'Pendiente',
  DIAGNOSING = 'En Diagnóstico', // Added from PDF
  IN_PROGRESS = 'En Reparación',
  READY = 'Listo',
  DELIVERED = 'Entregado'
}

export interface Customer {
  id: string;
  name: string;
  taxId: string; // Cédula or RUC (from PDF)
  address: string;
  phone: string;
  email?: string;
}

export interface SparePart { // New Module from PDF
  id: string;
  name: string;
  price: number;
  stock: number;
  provider: string;
}

export interface Ticket {
  id: string;
  customer: Customer;
  deviceType: DeviceType;
  brand: string; // Added from PDF
  model: string;
  serialNumber: string; // Added from PDF
  physicalCondition: string; // Added from PDF
  problemDescription: string;
  observations: string; 
  status: TicketStatus;
  amountPaid: number;
  estimatedCost: number; // Renamed from totalCost to match PDF
  dateCreated: string;
  aiDiagnosis?: string;
  technician?: string; // Added role
}

export type ViewState = 'LOGIN' | 'DASHBOARD' | 'NEW_TICKET' | 'LIST_TICKETS' | 'INVENTORY_PARTS' | 'HISTORY';

export type Language = 'es' | 'en';