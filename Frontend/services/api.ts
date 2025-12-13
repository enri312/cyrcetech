// API Service to connect with Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Generic fetch helper
async function fetchApi<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const token = localStorage.getItem('token');
    const headers: HeadersInit = {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options?.headers,
    };

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers,
    });

    if (response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/'; // Force reload/redirect to login
        throw new Error('Session expired');
    }

    if (!response.ok) {
        // Try to parse error message from JSON response
        try {
            const errorBody = await response.json();
            throw new Error(errorBody.message || `API Error: ${response.status}`);
        } catch (e) {
            throw new Error(`API Error: ${response.status} ${response.statusText}`);
        }
    }

    return response.json();
}

// === AUTH ===
export interface LoginRequest {
    email: string;
    password?: string;
}

export interface RegisterRequest {
    fullName: string;
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    userId: string;
    email: string;
    role: string;
}

export const authApi = {
    login: (data: LoginRequest) =>
        fetchApi<AuthResponse>('/auth/login', { method: 'POST', body: JSON.stringify(data) }),
    register: (data: RegisterRequest) =>
        fetchApi<AuthResponse>('/auth/register', { method: 'POST', body: JSON.stringify(data) }),
};

// === CUSTOMERS ===
export interface CustomerDTO {
    id: string;
    name: string;
    taxId: string;
    address: string;
    phone: string;
    formattedPhone?: string;
}

export interface CreateCustomerRequest {
    name: string;
    taxId: string;
    address: string;
    phone: string;
}

export const customerApi = {
    getAll: () => fetchApi<CustomerDTO[]>('/customers'),
    getById: (id: string) => fetchApi<CustomerDTO>(`/customers/${id}`),
    create: (data: CreateCustomerRequest) =>
        fetchApi<CustomerDTO>('/customers', { method: 'POST', body: JSON.stringify(data) }),
    update: (id: string, data: Partial<CreateCustomerRequest>) =>
        fetchApi<CustomerDTO>(`/customers/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id: string) =>
        fetchApi<void>(`/customers/${id}`, { method: 'DELETE' }),
    search: (query: string) => fetchApi<CustomerDTO[]>(`/customers/search?q=${encodeURIComponent(query)}`),
};

// === EQUIPMENT ===
export interface EquipmentDTO {
    id: string;
    deviceType: string;
    brand: string;
    model: string;
    serialNumber: string;
    physicalCondition: string;
    customerId: string;
    customerName?: string;
}

export interface CreateEquipmentRequest {
    deviceType: string;
    brand: string;
    model: string;
    serialNumber: string;
    physicalCondition: string;
    customerId: string;
}

export const equipmentApi = {
    getAll: () => fetchApi<EquipmentDTO[]>('/equipment'),
    getById: (id: string) => fetchApi<EquipmentDTO>(`/equipment/${id}`),
    getByCustomer: (customerId: string) => fetchApi<EquipmentDTO[]>(`/equipment/customer/${customerId}`),
    create: (data: CreateEquipmentRequest) =>
        fetchApi<EquipmentDTO>('/equipment', { method: 'POST', body: JSON.stringify(data) }),
    update: (id: string, data: Partial<CreateEquipmentRequest>) =>
        fetchApi<EquipmentDTO>(`/equipment/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id: string) =>
        fetchApi<void>(`/equipment/${id}`, { method: 'DELETE' }),
    search: (query: string) => fetchApi<EquipmentDTO[]>(`/equipment/search?q=${encodeURIComponent(query)}`),
};

// === TICKETS ===
export interface TicketDTO {
    id: string;
    customerId: string;
    customerName?: string;
    equipmentId: string;
    equipmentSummary?: string;
    problemDescription: string;
    observations: string;
    status: string;
    amountPaid: number;
    estimatedCost: number;
    dateCreated: string;
    dateCompleted?: string;
}

export interface CreateTicketRequest {
    customerId: string;
    equipmentId: string;
    problemDescription: string;
    observations?: string;
    amountPaid?: number;
    estimatedCost?: number;
}

export const ticketApi = {
    getAll: () => fetchApi<TicketDTO[]>('/tickets'),
    getById: (id: string) => fetchApi<TicketDTO>(`/tickets/${id}`),
    getActive: () => fetchApi<TicketDTO[]>('/tickets/active'),
    getByStatus: (status: string) => fetchApi<TicketDTO[]>(`/tickets/status/${status}`),
    create: (data: CreateTicketRequest) =>
        fetchApi<TicketDTO>('/tickets', { method: 'POST', body: JSON.stringify(data) }),
    update: (id: string, data: Partial<CreateTicketRequest & { status: string }>) =>
        fetchApi<TicketDTO>(`/tickets/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id: string) =>
        fetchApi<void>(`/tickets/${id}`, { method: 'DELETE' }),
    search: (query: string) => fetchApi<TicketDTO[]>(`/tickets/search?q=${encodeURIComponent(query)}`),
};

// === SPARE PARTS ===
export interface SparePartDTO {
    id: string;
    name: string;
    price: number;
    stock: number;
    minStock: number;
    provider: string;
    lowStock: boolean;
}

export interface CreateSparePartRequest {
    name: string;
    price: number;
    stock: number;
    minStock?: number;
    provider: string;
}

export const sparePartApi = {
    getAll: () => fetchApi<SparePartDTO[]>('/spare-parts'),
    getById: (id: string) => fetchApi<SparePartDTO>(`/spare-parts/${id}`),
    getLowStock: () => fetchApi<SparePartDTO[]>('/spare-parts/low-stock'),
    create: (data: CreateSparePartRequest) =>
        fetchApi<SparePartDTO>('/spare-parts', { method: 'POST', body: JSON.stringify(data) }),
    update: (id: string, data: Partial<CreateSparePartRequest>) =>
        fetchApi<SparePartDTO>(`/spare-parts/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id: string) =>
        fetchApi<void>(`/spare-parts/${id}`, { method: 'DELETE' }),
    search: (query: string) => fetchApi<SparePartDTO[]>(`/spare-parts/search?q=${encodeURIComponent(query)}`),
};

// === INVOICES ===
export interface InvoiceDTO {
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
    paymentStatus: string;
    paymentMethod?: string;
    notes?: string;
}

export interface CreateInvoiceRequest {
    ticketId: string;
    laborCost: number;
    partsCost?: number;
    taxRate?: number;
    dueDate: string;
    notes?: string;
}

export const invoiceApi = {
    getAll: () => fetchApi<InvoiceDTO[]>('/invoices'),
    getById: (id: string) => fetchApi<InvoiceDTO>(`/invoices/${id}`),
    getByTicket: (ticketId: string) => fetchApi<InvoiceDTO>(`/invoices/ticket/${ticketId}`),
    getPending: () => fetchApi<InvoiceDTO[]>('/invoices/pending'),
    getPaid: () => fetchApi<InvoiceDTO[]>('/invoices/paid'),
    create: (data: CreateInvoiceRequest) =>
        fetchApi<InvoiceDTO>('/invoices', { method: 'POST', body: JSON.stringify(data) }),
    update: (id: string, data: Partial<CreateInvoiceRequest & { paymentStatus: string; paymentMethod: string }>) =>
        fetchApi<InvoiceDTO>(`/invoices/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id: string) =>
        fetchApi<void>(`/invoices/${id}`, { method: 'DELETE' }),
};

// === AUDIT LOGS ===
export interface AuditLogDTO {
    id: string;
    userId: string;
    username: string;
    userRole: string;
    userRoleDisplayName?: string;
    action: string;
    actionDisplayName?: string;
    entityType: string;
    entityId?: string;
    timestamp: string;
    details: string;
    ipAddress?: string;
}

export const auditApi = {
    getAll: () => fetchApi<AuditLogDTO[]>('/audit'),
    getByUser: (userId: string) => fetchApi<AuditLogDTO[]>(`/audit/user/${userId}`),
    getByEntityType: (entityType: string) => fetchApi<AuditLogDTO[]>(`/audit/entity/${entityType}`),
    getByRole: (role: string) => fetchApi<AuditLogDTO[]>(`/audit/role/${role}`),
    getByDateRange: (startDate: string, endDate: string) =>
        fetchApi<AuditLogDTO[]>(`/audit/date-range?startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`),
};

// === BILLING ===
export interface BillingInvoiceDTO {
    id: string;
    invoiceNumber: string;
    issueDate: string;
    totalAmount: number;
    paidAmount: number;
    balance: number;
    paymentStatus: string;
    paymentMethod?: string;
}

export interface BillingSummary {
    totalInvoiced: number;
    totalCollected: number;
    pendingBalance: number;
    invoices: BillingInvoiceDTO[];
}

export const billingApi = {
    // Get daily billing report
    getDaily: (date: string) => fetchApi<BillingInvoiceDTO[]>(`/billing/daily/${date}`),

    // Get monthly billing report
    getMonthly: (year: number, month: number) => fetchApi<BillingInvoiceDTO[]>(`/billing/monthly/${year}/${month}`),

    // Get yearly billing report
    getYearly: (year: number) => fetchApi<BillingInvoiceDTO[]>(`/billing/yearly/${year}`),
};
