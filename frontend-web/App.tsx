
import React, { useState, useEffect } from 'react';
import {
  LayoutDashboard,
  PlusCircle,
  List,
  LogOut,
  Wrench,
  User,
  Users,
  Smartphone,
  Monitor,
  Laptop,
  HardDrive,
  Search,
  Sparkles,
  CheckCircle,
  Clock,
  Phone,
  Box as BoxIcon,
  History,
  CreditCard,
  FileText,
  Globe,
  Trash2,
  Edit,
  BrainCircuit
} from 'lucide-react';

import { DeviceType, DeviceTypeDisplay, TicketStatus, TicketStatusDisplay, Ticket, ViewState, Language, Customer, SparePart, Equipment, Invoice, PaymentStatus } from './types';
import { GlassCard, Input, TextArea, Button, StatusBadge, LanguageToggle } from './components/ui';
// Ollama local AI service for repair diagnosis
import { getRepairDiagnosis } from './services/ollamaService';
// Backend API services
import { ticketApi, sparePartApi, customerApi, equipmentApi, invoiceApi, TicketDTO, SparePartDTO, CustomerDTO, EquipmentDTO, InvoiceDTO } from './services/api';
import { NewTicketView } from './views/NewTicketView';
import { ClientsView } from './views/ClientsView';
import { EquipmentView } from './views/EquipmentView';
import { InvoicesView } from './views/InvoicesView';

// --- I18N CONFIGURATION ---
const TRANSLATIONS = {
  es: {
    loginTitle: "Ingresar al Sistema",
    user: "Usuario",
    pass: "Contraseña",
    menuMain: "Inicio",
    menuNew: "Registrar Orden",
    menuList: "Órdenes Activas",
    menuParts: "Repuestos",
    menuHistory: "Historial Técnico",
    logout: "Salir",
    dashboardTitle: "Panel Principal",
    toFix: "Pendientes / Diagnóstico",
    toDeliver: "Listos para Entregar",
    delivered: "Entregados",
    pendingList: "En Taller",
    readyList: "Para Retirar",
    noPending: "No hay equipos pendientes.",
    noReady: "No hay equipos para entregar.",
    registerTitle: "Nueva Orden de Reparación",
    clientData: "Datos del Cliente",
    name: "Nombre Completo",
    taxId: "Cédula / RUC",
    address: "Dirección",
    phone: "Teléfono",
    deviceData: "Datos del Equipo",
    deviceType: "Tipo",
    brand: "Marca",
    model: "Modelo",
    serial: "Nº Serie",
    condition: "Estado Físico",
    amountPaid: "Seña / Abono ($)",
    problemDesc: "Motivo de Ingreso / Problema",
    consultAI: "Diagnóstico IA",
    aiRec: "Sugerencia (Deepseek)",
    observations: "Observaciones / Diagnóstico",
    cancel: "Cancelar",
    register: "Crear Orden",
    inventoryTitle: "Listado de Órdenes",
    partsTitle: "Gestión de Repuestos",
    historyTitle: "Historial de Reparaciones",
    searchPlaceholder: "Buscar por cliente, serial...",
    problem: "Problema",
    obs: "Obs",
    total: "Costo Est.",
    edit: "Gestionar",
    deliver: "Entregar",
    menuClients: "Clientes",
    menuEquipment: "Equipos",
    menuInvoices: "Facturas",
    clientsTitle: "Gestión de Clientes",
    equipmentTitle: "Gestión de Equipos",
    invoicesTitle: "Gestión de Facturas",
    newClient: "Nuevo Cliente",
    newEquipment: "Nuevo Equipo",
    newInvoice: "Nueva Factura",
    noClients: "No hay clientes registrados.",
    noEquipment: "No hay equipos registrados.",
    noInvoices: "No hay facturas.",
    actions: "Acciones",
    delete: "Eliminar",
    status: "Estado"
  },
  en: {
    loginTitle: "System Login",
    user: "Username",
    pass: "Password",
    menuMain: "Home",
    menuNew: "New Order",
    menuList: "Active Orders",
    menuParts: "Spare Parts",
    menuHistory: "Tech History",
    logout: "Logout",
    dashboardTitle: "Main Dashboard",
    toFix: "Pending / Diagnosis",
    toDeliver: "Ready to Deliver",
    delivered: "Delivered",
    pendingList: "In Workshop",
    readyList: "Ready for Pickup",
    noPending: "No pending devices.",
    noReady: "No devices ready.",
    registerTitle: "New Repair Order",
    clientData: "Customer Data",
    name: "Full Name",
    taxId: "Tax ID / ID",
    address: "Address",
    phone: "Phone",
    deviceData: "Device Data",
    deviceType: "Type",
    brand: "Brand",
    model: "Model",
    serial: "Serial No.",
    condition: "Condition",
    amountPaid: "Deposit ($)",
    problemDesc: "Issue / Problem",
    consultAI: "AI Diagnosis",
    aiRec: "Suggestion (Deepseek)",
    observations: "Observations / Diagnosis",
    cancel: "Cancel",
    register: "Create Order",
    inventoryTitle: "Order List",
    partsTitle: "Spare Parts Management",
    historyTitle: "Repair History",
    searchPlaceholder: "Search customer, serial...",
    problem: "Problem",
    obs: "Note",
    total: "Est. Cost",
    edit: "Manage",
    deliver: "Deliver",
    menuClients: "Clients",
    menuEquipment: "Equipment",
    menuInvoices: "Invoices",
    clientsTitle: "Client Management",
    equipmentTitle: "Equipment Management",
    invoicesTitle: "Invoice Management",
    newClient: "New Client",
    newEquipment: "New Equipment",
    newInvoice: "New Invoice",
    noClients: "No clients registered.",
    noEquipment: "No equipment registered.",
    noInvoices: "No invoices.",
    actions: "Actions",
    delete: "Delete",
    status: "Status"
  }
};

// --- MOCK DATA ---
const INITIAL_TICKETS: Ticket[] = [
  {
    id: '1001',
    customer: { id: 'c1', name: 'Juan Perez', taxId: '4.555.666', address: 'Av. Siempre Viva 123', phone: '0981-555-0101' },
    deviceType: DeviceType.NOTEBOOK,
    brand: 'Dell',
    model: 'XPS 15',
    serialNumber: 'JX-2292',
    physicalCondition: 'Rayones en tapa',
    problemDescription: 'Pantalla azul al iniciar.',
    observations: 'Posible fallo de RAM.',
    status: TicketStatus.DIAGNOSING,
    amountPaid: 50000,
    estimatedCost: 150000,
    dateCreated: '2023-10-25'
  },
  {
    id: '1002',
    customer: { id: 'c2', name: 'Maria Gomez', taxId: '3.222.111', address: 'Centro', phone: '0971-555-0202' },
    deviceType: DeviceType.SMARTPHONE,
    brand: 'Apple',
    model: 'iPhone 13',
    serialNumber: 'SN-9992',
    physicalCondition: 'Impecable',
    problemDescription: 'Cambio de batería.',
    observations: '',
    status: TicketStatus.READY,
    amountPaid: 0,
    estimatedCost: 80000,
    dateCreated: '2023-10-26'
  }
];

const INITIAL_PARTS: SparePart[] = [
  { id: '1', name: 'SSD Kingston 480GB', price: 45000, stock: 5, minStock: 2, provider: 'Global Electronics' },
  { id: '2', name: 'Pasta Térmica Arctic MX-4', price: 15000, stock: 12, minStock: 5, provider: 'PC Parts PY' },
  { id: '3', name: 'Pantalla iPhone 11', price: 60000, stock: 2, minStock: 2, provider: 'Apple Fix' }
];

// --- HOOKS ---
interface TicketSystemHook {
  tickets: Ticket[];
  parts: SparePart[];
  customers: Customer[];
  equipment: Equipment[];
  invoices: Invoice[];
  formData: Partial<Ticket>;
  loading: boolean;
  error: string | null;
  createTicket: () => boolean;
  updateForm: (updates: Partial<Ticket>) => void;
  updateCustomer: (updates: Partial<Customer>) => void;
  refreshData: () => Promise<void>;
  deleteCustomer: (id: string) => Promise<void>;
  deleteEquipment: (id: string) => Promise<void>;
  createCustomer: (customer: Omit<Customer, 'id'>) => Promise<Customer>;
  updateCustomerData: (id: string, customer: Partial<Customer>) => Promise<Customer>;
  createEquipment: (equipment: Omit<Equipment, 'id'>) => Promise<Equipment>;
  updateEquipmentData: (id: string, equipment: Partial<Equipment>) => Promise<Equipment>;
  createInvoice: (invoice: Omit<Invoice, 'id'>) => Promise<Invoice>;
  updateInvoiceData: (id: string, invoice: Partial<Invoice>) => Promise<Invoice>;
}

function useTicketSystem(): TicketSystemHook {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [parts, setParts] = useState<SparePart[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [formData, setFormData] = useState<Partial<Ticket>>({
    customer: { id: '', name: '', taxId: '', address: '', phone: '' },
    deviceType: DeviceType.NOTEBOOK,
    status: TicketStatus.PENDING,
    amountPaid: 0,
    estimatedCost: 0
  });

  // Fetch tickets from API
  const fetchTickets = async () => {
    try {
      const data = await ticketApi.getAll();
      // Transform API data to local Ticket format
      const transformedTickets: Ticket[] = data.map((t: TicketDTO) => ({
        id: t.id,
        customer: {
          id: t.customerId,
          name: t.customerName || 'Sin cliente',
          taxId: '',
          address: '',
          phone: ''
        },
        deviceType: DeviceType.NOTEBOOK, // Default, API should provide this
        brand: '',
        model: t.equipmentSummary || '',
        serialNumber: '',
        physicalCondition: '',
        problemDescription: t.problemDescription,
        observations: t.observations,
        status: t.status as TicketStatus || TicketStatus.PENDING,
        amountPaid: t.amountPaid || 0,
        estimatedCost: t.estimatedCost || 0,
        dateCreated: t.dateCreated
      }));
      setTickets(transformedTickets);
    } catch (err) {
      console.error('Error fetching tickets:', err);
      setError('Error cargando tickets');
    }
  };

  // Fetch spare parts from API
  const fetchParts = async () => {
    try {
      const data = await sparePartApi.getAll();
      const transformedParts: SparePart[] = data.map((p: SparePartDTO) => ({
        id: p.id,
        name: p.name,
        price: p.price,
        stock: p.stock,
        minStock: p.minStock || 0,
        provider: p.provider
      }));
      setParts(transformedParts);
    } catch (err) {
      console.error('Error fetching parts:', err);
    }
  };

  const createTicket = async () => {
    if (!formData.customer?.name || !formData.model) return false;

    try {
      // First create customer if needed
      const customerResp = await customerApi.create({
        name: formData.customer.name,
        taxId: formData.customer.taxId || '',
        address: formData.customer.address || '',
        phone: formData.customer.phone || ''
      });

      // Then create ticket (simplified - would need equipment creation too)
      // For now, add locally
      const newTicket: Ticket = {
        id: Date.now().toString(),
        customer: { ...formData.customer, id: customerResp.id } as Customer,
        deviceType: formData.deviceType || DeviceType.NOTEBOOK,
        brand: formData.brand || 'Genérico',
        model: formData.model || 'Desconocido',
        serialNumber: formData.serialNumber || 'S/N',
        physicalCondition: formData.physicalCondition || 'Bueno',
        problemDescription: formData.problemDescription || '',
        observations: formData.observations || '',
        status: formData.status || TicketStatus.PENDING,
        amountPaid: formData.amountPaid || 0,
        estimatedCost: formData.estimatedCost || 0,
        dateCreated: new Date().toLocaleDateString(),
        aiDiagnosis: formData.aiDiagnosis
      };

      setTickets(prev => [newTicket, ...prev]);
      resetForm();
      return true;
    } catch (err) {
      console.error('Error creating ticket:', err);
      return false;
    }
  };

  const resetForm = () => {
    setFormData({
      customer: { id: '', name: '', taxId: '', address: '', phone: '' },
      deviceType: DeviceType.NOTEBOOK,
      brand: '',
      model: '',
      serialNumber: '',
      physicalCondition: '',
      status: TicketStatus.PENDING,
      amountPaid: 0,
      estimatedCost: 0,
      aiDiagnosis: ''
    });
  };

  const updateForm = (updates: Partial<Ticket>) => {
    setFormData(prev => ({ ...prev, ...updates }));
  };

  const updateCustomer = (updates: Partial<Customer>) => {
    setFormData(prev => ({
      ...prev,
      customer: { ...prev.customer!, ...updates }
    }));
  };

  const refreshData = () => {
    fetchTickets();
    fetchParts();
    fetchCustomers();
    fetchEquipment();
    fetchInvoices();
  };

  // Customers state and fetch
  const [customers, setCustomers] = useState<Customer[]>([]);

  const fetchCustomers = async () => {
    try {
      const data = await customerApi.getAll();
      setCustomers(data.map((c: CustomerDTO) => ({
        id: c.id,
        name: c.name,
        taxId: c.taxId,
        address: c.address,
        phone: c.phone
      })));
    } catch (err) {
      console.error('Error fetching customers:', err);
    }
  };

  const deleteCustomer = async (id: string) => {
    try {
      await customerApi.delete(id);
      setCustomers(prev => prev.filter(c => c.id !== id));
    } catch (err) {
      console.error('Error deleting customer:', err);
    }
  };

  const createCustomer = async (customer: Partial<Customer>) => {
    try {
      const newCustomer = await customerApi.create(customer as CustomerDTO);
      setCustomers(prev => [...prev, {
        id: newCustomer.id,
        name: newCustomer.name,
        taxId: newCustomer.taxId,
        address: newCustomer.address,
        phone: newCustomer.phone
      }]);
      return newCustomer;
    } catch (err) {
      console.error('Error creating customer:', err);
      throw err;
    }
  };

  const updateCustomerData = async (id: string, customer: Partial<Customer>) => {
    try {
      const updated = await customerApi.update(id, customer as CustomerDTO);
      setCustomers(prev => prev.map(c => c.id === id ? {
        id: updated.id,
        name: updated.name,
        taxId: updated.taxId,
        address: updated.address,
        phone: updated.phone
      } : c));
      return updated;
    } catch (err) {
      console.error('Error updating customer:', err);
      throw err;
    }
  };

  // Equipment state and fetch
  const [equipment, setEquipment] = useState<Equipment[]>([]);

  const fetchEquipment = async () => {
    try {
      const data = await equipmentApi.getAll();
      setEquipment(data.map((e: EquipmentDTO) => ({
        id: e.id,
        deviceType: e.deviceType as DeviceType,
        brand: e.brand,
        model: e.model,
        serialNumber: e.serialNumber,
        physicalCondition: e.physicalCondition,
        customerId: e.customerId,
        customerName: e.customerName
      })));
    } catch (err) {
      console.error('Error fetching equipment:', err);
    }
  };

  const deleteEquipment = async (id: string) => {
    try {
      await equipmentApi.delete(id);
      setEquipment(prev => prev.filter(e => e.id !== id));
    } catch (err) {
      console.error('Error deleting equipment:', err);
    }
  };

  const createEquipment = async (equip: Partial<Equipment>) => {
    try {
      const newEquip = await equipmentApi.create(equip as EquipmentDTO);
      setEquipment(prev => [...prev, {
        id: newEquip.id,
        deviceType: newEquip.deviceType as DeviceType,
        brand: newEquip.brand,
        model: newEquip.model,
        serialNumber: newEquip.serialNumber,
        physicalCondition: newEquip.physicalCondition,
        customerId: newEquip.customerId,
        customerName: newEquip.customerName
      }]);
      return newEquip;
    } catch (err) {
      console.error('Error creating equipment:', err);
      throw err;
    }
  };

  const updateEquipmentData = async (id: string, equip: Partial<Equipment>) => {
    try {
      const updated = await equipmentApi.update(id, equip as EquipmentDTO);
      setEquipment(prev => prev.map(e => e.id === id ? {
        id: updated.id,
        deviceType: updated.deviceType as DeviceType,
        brand: updated.brand,
        model: updated.model,
        serialNumber: updated.serialNumber,
        physicalCondition: updated.physicalCondition,
        customerId: updated.customerId,
        customerName: updated.customerName
      } : e));
      return updated;
    } catch (err) {
      console.error('Error updating equipment:', err);
      throw err;
    }
  };

  // Invoices state and fetch
  const [invoices, setInvoices] = useState<Invoice[]>([]);

  const fetchInvoices = async () => {
    try {
      const data = await invoiceApi.getAll();
      setInvoices(data.map((i: InvoiceDTO) => ({
        id: i.id,
        invoiceNumber: i.invoiceNumber,
        ticketId: i.ticketId,
        customerId: i.customerId,
        customerName: i.customerName,
        issueDate: i.issueDate,
        dueDate: i.dueDate,
        laborCost: i.laborCost,
        partsCost: i.partsCost,
        taxAmount: i.taxAmount,
        totalAmount: i.totalAmount,
        paymentStatus: i.paymentStatus as PaymentStatus,
        paymentMethod: i.paymentMethod,
        notes: i.notes
      })));
    } catch (err) {
      console.error('Error fetching invoices:', err);
    }
    const createInvoice = async (invoice: Partial<Invoice>) => {
      try {
        const newInvoice = await invoiceApi.create(invoice as InvoiceDTO);
        setInvoices(prev => [...prev, {
          id: newInvoice.id,
          invoiceNumber: newInvoice.invoiceNumber,
          ticketId: newInvoice.ticketId,
          customerId: newInvoice.customerId,
          customerName: newInvoice.customerName,
          issueDate: newInvoice.issueDate,
          dueDate: newInvoice.dueDate,
          laborCost: newInvoice.laborCost,
          partsCost: newInvoice.partsCost,
          taxAmount: newInvoice.taxAmount,
          totalAmount: newInvoice.totalAmount,
          paymentStatus: newInvoice.paymentStatus as PaymentStatus,
          paymentMethod: newInvoice.paymentMethod,
          notes: newInvoice.notes
        }]);
        return newInvoice;
      } catch (err) {
        console.error('Error creating invoice:', err);
        throw err;
      }
    };

    const updateInvoiceData = async (id: string, invoice: Partial<Invoice>) => {
      try {
        const updated = await invoiceApi.update(id, invoice as InvoiceDTO);
        setInvoices(prev => prev.map(i => i.id === id ? {
          id: updated.id,
          invoiceNumber: updated.invoiceNumber,
          ticketId: updated.ticketId,
          customerId: updated.customerId,
          customerName: updated.customerName,
          issueDate: updated.issueDate,
          dueDate: updated.dueDate,
          laborCost: updated.laborCost,
          partsCost: updated.partsCost,
          taxAmount: updated.taxAmount,
          totalAmount: updated.totalAmount,
          paymentStatus: updated.paymentStatus as PaymentStatus,
          paymentMethod: updated.paymentMethod,
          notes: updated.notes
        } : i));
        return updated;
      } catch (err) {
        console.error('Error updating invoice:', err);
        throw err;
      }
    };

    // Update useEffect to load all data
    useEffect(() => {
      const loadData = async () => {
        setLoading(true);
        await Promise.all([fetchTickets(), fetchParts(), fetchCustomers(), fetchEquipment(), fetchInvoices()]);
        setLoading(false);
      };
      loadData();
    }, []);

    return {
      tickets, parts, customers, equipment, invoices,
      formData, loading, error,
      createTicket, updateForm, updateCustomer, refreshData,
      deleteCustomer, deleteEquipment,
      createCustomer, updateCustomerData,
      createEquipment, updateEquipmentData,
      createInvoice, updateInvoiceData
    };
  }

  // --- COMPONENTS ---
  const TicketMiniItem: React.FC<{ ticket: Ticket, colorClass: string, labels: any }> = ({ ticket, colorClass, labels }) => (
    <div className="flex items-center justify-between p-3 bg-white/5 rounded-lg border border-white/5 mb-2 hover:bg-white/10 transition">
      <div className="flex items-center gap-3">
        <div className={`p-2 rounded-lg ${colorClass}`}>
          {ticket.deviceType === DeviceType.NOTEBOOK ? <Laptop size={16} /> :
            ticket.deviceType === DeviceType.SMARTPHONE ? <Smartphone size={16} /> :
              <Monitor size={16} />}
        </div>
        <div>
          <p className="font-bold text-sm">{ticket.customer.name}</p>
          <p className="text-xs text-gray-400">{ticket.brand} {ticket.model}</p>
        </div>
      </div>
      <div className="text-right">
        <span className="text-xs font-mono block text-gray-400">{ticket.dateCreated}</span>
        <StatusBadge status={ticket.status} />
      </div>
    </div>
  );



  // --- MAIN APPLICATION ---
  export default function App() {
    const [view, setView] = useState<ViewState>('LOGIN');
    const [user, setUser] = useState<{ name: string } | null>(null);
    const [lang, setLang] = useState<Language>('es');
    const [isAiLoading, setIsAiLoading] = useState(false);

    const {
      tickets, parts, customers, equipment, invoices,
      formData, loading, error,
      createTicket, updateForm, updateCustomer, refreshData,
      deleteCustomer, deleteEquipment,
      createCustomer, updateCustomerData,
      createEquipment, updateEquipmentData,
      createInvoice, updateInvoiceData
    } = useTicketSystem();
    const t = (key: keyof typeof TRANSLATIONS.es) => TRANSLATIONS[lang][key];

    const handleLogin = (e: React.FormEvent) => {
      e.preventDefault();
      setUser({ name: 'Técnico Admin' });
      setView('DASHBOARD');
    };

    const handleCreateTicketWrapper = () => {
      if (createTicket()) {
        setView('LIST_TICKETS');
      }
    };

    const handleAiDiagnosis = async () => {
      if (!formData.deviceType || !formData.problemDescription) {
        alert("Por favor ingrese el tipo de equipo y el problema.");
        return;
      }
      setIsAiLoading(true);
      // This now calls Ollama local
      const diagnosis = await getRepairDiagnosis(formData.deviceType, formData.problemDescription);
      updateForm({ aiDiagnosis: diagnosis });
      setIsAiLoading(false);
    };

    // --- VIEWS ---

    const LoginView = () => (
      <div className="min-h-screen flex items-center justify-center relative overflow-hidden">
        <div className="absolute top-1/4 left-1/4 w-64 h-64 bg-neon-blue/20 rounded-full blur-[100px] animate-pulse" />
        <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-neon-purple/20 rounded-full blur-[120px] animate-pulse" />

        <div className="absolute top-6 right-6 z-20">
          <LanguageToggle lang={lang} setLang={setLang} />
        </div>

        <GlassCard className="w-full max-w-md mx-4 text-center z-10">
          <div className="mb-8">
            <div className="w-20 h-20 bg-gradient-to-tr from-neon-blue to-neon-purple rounded-2xl mx-auto flex items-center justify-center shadow-lg shadow-neon-blue/50 mb-4 animate-float">
              <Wrench className="text-white w-10 h-10" />
            </div>
            <h1 className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-white to-gray-400">
              CyrceTech
            </h1>
          </div>

          <form onSubmit={handleLogin} className="space-y-6">
            <Input
              label={t('user')}
              placeholder="admin"
              defaultValue="admin"
            />
            <Input
              label={t('pass')}
              type="password"
              placeholder="••••••••"
              defaultValue="password"
            />
            <Button type="submit" className="w-full">
              {t('loginTitle')}
            </Button>
          </form>
        </GlassCard>
      </div>
    );

    const DashboardView = () => {
      const toFix = tickets.filter(t => t.status === TicketStatus.PENDING || t.status === TicketStatus.DIAGNOSING || t.status === TicketStatus.IN_PROGRESS);
      const toDeliver = tickets.filter(t => t.status === TicketStatus.READY);
      const completed = tickets.filter(t => t.status === TicketStatus.DELIVERED);

      return (
        <div className="p-8 max-w-7xl mx-auto animate-fade-in">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-3xl font-bold">{t('dashboardTitle')}</h2>
            <LanguageToggle lang={lang} setLang={setLang} />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <GlassCard className="border-l-4 border-l-yellow-500">
              <div className="flex justify-between items-start">
                <div>
                  <p className="text-gray-400 text-sm">{t('toFix')}</p>
                  <h3 className="text-4xl font-bold text-white mt-2">{toFix.length}</h3>
                </div>
                <div className="p-3 bg-yellow-500/20 rounded-lg text-yellow-500">
                  <Wrench size={24} />
                </div>
              </div>
            </GlassCard>

            <GlassCard className="border-l-4 border-l-neon-green" delay>
              <div className="flex justify-between items-start">
                <div>
                  <p className="text-gray-400 text-sm">{t('toDeliver')}</p>
                  <h3 className="text-4xl font-bold text-white mt-2">{toDeliver.length}</h3>
                </div>
                <div className="p-3 bg-neon-green/20 rounded-lg text-neon-green">
                  <CheckCircle size={24} />
                </div>
              </div>
            </GlassCard>

            <GlassCard className="border-l-4 border-l-blue-500" delay>
              <div className="flex justify-between items-start">
                <div>
                  <p className="text-gray-400 text-sm">{t('delivered')}</p>
                  <h3 className="text-4xl font-bold text-white mt-2">{completed.length}</h3>
                </div>
                <div className="p-3 bg-blue-500/20 rounded-lg text-blue-500">
                  <History size={24} />
                </div>
              </div>
            </GlassCard>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="space-y-4">
              <h3 className="text-xl font-semibold text-yellow-400 flex items-center gap-2">
                <Wrench size={20} /> {t('pendingList')}
              </h3>
              <GlassCard className="min-h-[200px]">
                {toFix.length === 0 ? (
                  <p className="text-gray-500 text-center py-4">{t('noPending')}</p>
                ) : (
                  toFix.map(tk => <TicketMiniItem key={tk.id} ticket={tk} colorClass="bg-yellow-500/20 text-yellow-500" labels={t} />)
                )}
              </GlassCard>
            </div>

            <div className="space-y-4">
              <h3 className="text-xl font-semibold text-neon-green flex items-center gap-2">
                <CheckCircle size={20} /> {t('readyList')}
              </h3>
              <GlassCard className="min-h-[200px]">
                {toDeliver.length === 0 ? (
                  <p className="text-gray-500 text-center py-4">{t('noReady')}</p>
                ) : (
                  toDeliver.map(tk => <TicketMiniItem key={tk.id} ticket={tk} colorClass="bg-neon-green/20 text-neon-green" labels={t} />)
                )}
              </GlassCard>
            </div>
          </div>
        </div>
      );
    };



    const TicketsListView = () => (
      <div className="p-8 max-w-7xl mx-auto animate-fade-in">
        <div className="flex justify-between items-center mb-8">
          <h2 className="text-3xl font-bold">{t('inventoryTitle')}</h2>
          <div className="flex gap-2 items-center">
            <LanguageToggle lang={lang} setLang={setLang} />
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500" size={18} />
              <input
                type="text"
                placeholder={t('searchPlaceholder')}
                className="pl-10 bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
              />
            </div>
          </div>
        </div>

        <div className="grid gap-4">
          {tickets.map((ticket) => (
            <GlassCard key={ticket.id} className="flex flex-col lg:flex-row gap-6 hover:bg-white/10 transition-colors">
              <div className={`absolute left-0 top-0 bottom-0 w-1 rounded-l-2xl ${ticket.status === TicketStatus.READY ? 'bg-neon-green' :
                ticket.status === TicketStatus.DELIVERED ? 'bg-gray-500' :
                  'bg-yellow-500'
                }`} />

              <div className="flex-1 space-y-2">
                <div className="flex items-center gap-3">
                  <span className="text-2xl font-bold">{ticket.customer.name}</span>
                  <StatusBadge status={ticket.status} />
                </div>
                <div className="flex flex-wrap gap-4 text-sm text-gray-400">
                  <span className="flex items-center gap-1"><CreditCard size={14} /> {ticket.customer.taxId}</span>
                  <span className="flex items-center gap-1"><Phone size={14} /> {ticket.customer.phone}</span>
                  <span className="flex items-center gap-1"><Clock size={14} /> {ticket.dateCreated}</span>
                </div>
                <div className="mt-4 p-3 bg-black/20 rounded-lg border border-white/5">
                  <p className="text-sm text-gray-300"><span className="text-neon-blue font-semibold">{t('problem')}:</span> {ticket.problemDescription}</p>
                  {ticket.observations && (
                    <p className="text-sm text-gray-400 mt-1"><span className="text-gray-500 font-semibold">{t('obs')}:</span> {ticket.observations}</p>
                  )}
                </div>
              </div>

              <div className="flex flex-col gap-3 min-w-[200px] border-l border-white/5 pl-0 lg:pl-6">
                <div className="flex items-center gap-2 text-gray-300">
                  {ticket.deviceType === DeviceType.NOTEBOOK ? <Laptop size={18} /> :
                    ticket.deviceType === DeviceType.SMARTPHONE ? <Smartphone size={18} /> :
                      <Monitor size={18} />}
                  <span className="font-medium">{ticket.brand} {ticket.model}</span>
                </div>
                <p className="text-xs text-gray-500">S/N: {ticket.serialNumber}</p>

                <div className="mt-auto">
                  <div className="flex justify-between text-sm mb-1">
                    <span className="text-gray-500">{t('amountPaid')}:</span>
                    <span className="text-green-400 font-mono">${ticket.amountPaid}</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-500">{t('total')}:</span>
                    <span className="text-white font-mono">${ticket.estimatedCost}</span>
                  </div>
                </div>

                <div className="flex gap-2 mt-2">
                  {ticket.status !== TicketStatus.DELIVERED && (
                    <Button variant="secondary" className="w-full text-xs py-2">{t('edit')}</Button>
                  )}
                  {ticket.status === TicketStatus.READY && (
                    <Button className="w-full text-xs py-2 !bg-neon-green/20 !text-neon-green hover:!bg-neon-green/30 border-neon-green/50">{t('deliver')}</Button>
                  )}
                </div>
              </div>
            </GlassCard>
          ))}
        </div>
      </div>
    );

    const PartsView = () => (
      <div className="p-8 max-w-7xl mx-auto animate-fade-in">
        <div className="flex justify-between items-center mb-8">
          <h2 className="text-3xl font-bold">{t('partsTitle')}</h2>
          <Button size="sm"><PlusCircle size={16} /> Agregar Repuesto</Button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {parts.map(part => (
            <GlassCard key={part.id} className="hover:bg-white/10">
              <div className="flex justify-between items-start mb-4">
                <div className="p-3 bg-neon-purple/20 rounded-lg text-neon-purple">
                  <BoxIcon size={24} />
                </div>
                <span className="px-2 py-1 bg-white/10 rounded text-xs">Stock: {part.stock}</span>
              </div>
              <h3 className="text-xl font-bold mb-1">{part.name}</h3>
              <p className="text-gray-400 text-sm mb-4">{part.provider}</p>
              <div className="flex justify-between items-center pt-4 border-t border-white/10">
                <span className="text-xl font-mono text-green-400">${part.price}</span>
                <Button variant="ghost" size="sm" className="text-neon-blue">Editar</Button>
              </div>
            </GlassCard>
          ))}
        </div>
      </div>
    );


    const NavButton = ({ active, onClick, icon, children }: any) => (
      <button
        onClick={onClick}
        className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-300 ${active ? 'bg-neon-blue/20 text-neon-blue shadow-lg shadow-neon-blue/10 border border-neon-blue/20' : 'text-gray-400 hover:bg-white/5 hover:text-white'
          }`}
      >
        {icon}
        <span className="font-medium">{children}</span>
      </button>
    );


    interface SidebarProps {
      view: ViewState;
      setView: (view: ViewState) => void;
      t: (key: any) => string;
      user: { name: string } | null;
    }

    const Sidebar = ({ view, setView, t, user }: SidebarProps) => (
      <div className="fixed left-0 top-0 h-full w-64 bg-space-800/80 backdrop-blur-md border-r border-white/5 p-6 z-20 hidden md:flex flex-col">
        <div className="flex items-center gap-3 mb-10">
          <div className="w-10 h-10 bg-gradient-to-tr from-neon-blue to-neon-purple rounded-lg flex items-center justify-center shadow-lg shadow-neon-blue/20">
            <Wrench className="text-white w-5 h-5" />
          </div>
          <span className="font-bold text-xl tracking-wide">CyrceTech</span>
        </div>

        <nav className="flex-1 space-y-2 overflow-y-auto pr-2 custom-scrollbar">
          <NavButton active={view === 'DASHBOARD'} onClick={() => setView('DASHBOARD')} icon={<LayoutDashboard size={20} />}>
            {t('menuMain')}
          </NavButton>
          <NavButton active={view === 'NEW_TICKET'} onClick={() => setView('NEW_TICKET')} icon={<PlusCircle size={20} />}>
            {t('menuNew')}
          </NavButton>
          <NavButton active={view === 'LIST_TICKETS'} onClick={() => setView('LIST_TICKETS')} icon={<List size={20} />}>
            {t('menuList')}
          </NavButton>

          <div className="border-t border-white/10 my-3" />

          <NavButton active={view === 'CLIENTS'} onClick={() => setView('CLIENTS')} icon={<Users size={20} />}>
            {t('menuClients')}
          </NavButton>
          <NavButton active={view === 'EQUIPMENT'} onClick={() => setView('EQUIPMENT')} icon={<HardDrive size={20} />}>
            {t('menuEquipment')}
          </NavButton>
          <NavButton active={view === 'INVENTORY_PARTS'} onClick={() => setView('INVENTORY_PARTS')} icon={<BoxIcon size={20} />}>
            {t('menuParts')}
          </NavButton>
          <NavButton active={view === 'INVOICES'} onClick={() => setView('INVOICES')} icon={<FileText size={20} />}>
            {t('menuInvoices')}
          </NavButton>

          <div className="border-t border-white/10 my-3" />

          <NavButton active={view === 'HISTORY'} onClick={() => setView('HISTORY')} icon={<History size={20} />}>
            {t('menuHistory')}
          </NavButton>
        </nav>

        <div className="pt-6 border-t border-white/10">
          <div className="flex items-center gap-3 mb-4 text-gray-400 px-4">
            <User size={16} />
            <span className="text-sm">{user?.name}</span>
          </div>
          <Button variant="ghost" className="w-full justify-start text-red-400 hover:text-red-300 hover:bg-red-500/10" onClick={() => setView('LOGIN')}>
            <LogOut size={18} className="mr-2" /> {t('logout')}
          </Button>
        </div>
      </div>
    );



    if (view === 'LOGIN') {
      return <LoginView />;
    }

    return (
      <div className="min-h-screen bg-space-900 text-white font-sans selection:bg-neon-blue/30">
        <div className="fixed top-0 left-0 w-full h-full overflow-hidden -z-10 pointer-events-none">
          <div className="absolute top-0 right-0 w-[500px] h-[500px] bg-blue-900/20 rounded-full blur-[128px]" />
          <div className="absolute bottom-0 left-0 w-[500px] h-[500px] bg-purple-900/20 rounded-full blur-[128px]" />
        </div>

        <Sidebar view={view} setView={setView} t={t} user={user} />

        <main className="md:ml-64 min-h-screen pb-12">
          <header className="md:hidden p-4 border-b border-white/10 flex justify-between items-center bg-space-900/80 backdrop-blur-md sticky top-0 z-30">
            <div className="flex items-center gap-2">
              <Wrench className="text-neon-blue" />
              <span className="font-bold">CyrceTech</span>
            </div>
            <Button variant="ghost" size="sm" onClick={() => setView('LOGIN')}><LogOut size={20} /></Button>
          </header>

          {view === 'DASHBOARD' && <DashboardView />}
          {view === 'NEW_TICKET' && (
            <NewTicketView
              t={t}
              lang={lang}
              setLang={setLang}
              formData={formData}
              updateCustomer={updateCustomer}
              updateForm={updateForm}
              handleAiDiagnosis={handleAiDiagnosis}
              isAiLoading={isAiLoading}
              onSubmit={handleCreateTicketWrapper}
              onCancel={() => setView('DASHBOARD')}
            />
          )}
          {view === 'LIST_TICKETS' && <TicketsListView />}
          {view === 'CLIENTS' && (
            <ClientsView
              t={t}
              customers={customers}
              createCustomer={createCustomer}
              updateCustomerData={updateCustomerData}
              deleteCustomer={deleteCustomer}
            />
          )}
          {view === 'EQUIPMENT' && (
            <EquipmentView
              t={t}
              equipment={equipment}
              customers={customers}
              createEquipment={createEquipment}
              updateEquipmentData={updateEquipmentData}
              deleteEquipment={deleteEquipment}
            />
          )}
          {view === 'INVENTORY_PARTS' && <PartsView />}
          {view === 'INVOICES' && (
            <InvoicesView
              t={t}
              invoices={invoices}
              customers={customers}
              createInvoice={createInvoice}
              updateInvoiceData={updateInvoiceData}
            />
          )}
          {view === 'HISTORY' && <TicketsListView />}
        </main>
      </div>
    );
  }
