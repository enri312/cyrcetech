import React, { useState } from 'react';
import { PlusCircle, FileText, User, Edit, Trash2, Save, X, DollarSign, Calendar } from 'lucide-react';
import { Invoice, PaymentStatus, PaymentMethod, Customer } from '../types';
import { GlassCard, Button, Input, TextArea, StatusBadge } from '../components/ui';

interface InvoicesViewProps {
    t: (key: any) => string;
    invoices: Invoice[];
    customers: Customer[];
    createInvoice: (invoice: Partial<Invoice>) => Promise<Invoice>;
    updateInvoiceData: (id: string, invoice: Partial<Invoice>) => Promise<Invoice>;
}

export const InvoicesView: React.FC<InvoicesViewProps> = ({
    t, invoices, customers, createInvoice, updateInvoiceData
}) => {
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [editingId, setEditingId] = useState<string | null>(null);
    const [formData, setFormData] = useState<Partial<Invoice>>({});
    const [isLoading, setIsLoading] = useState(false);

    const handleEdit = (invoice: Invoice) => {
        setEditingId(invoice.id);
        setFormData(invoice);
        setIsFormOpen(true);
    };

    const handleNew = () => {
        setEditingId(null);
        setFormData({
            issueDate: new Date().toISOString().split('T')[0],
            paymentStatus: PaymentStatus.PENDING,
            paymentMethod: PaymentMethod.CASH,
            laborCost: 0,
            partsCost: 0,
            taxAmount: 0,
            totalAmount: 0
        });
        setIsFormOpen(true);
    };

    const handleCancel = () => {
        setIsFormOpen(false);
        setEditingId(null);
        setFormData({});
    };

    const calculateTotal = (labor: number, parts: number, tax: number) => {
        return (labor || 0) + (parts || 0) + (tax || 0);
    };

    const handleCostChange = (field: 'laborCost' | 'partsCost' | 'taxAmount', value: string) => {
        const numValue = parseFloat(value) || 0;
        const newData = { ...formData, [field]: numValue };
        newData.totalAmount = calculateTotal(
            field === 'laborCost' ? numValue : newData.laborCost || 0,
            field === 'partsCost' ? numValue : newData.partsCost || 0,
            field === 'taxAmount' ? numValue : newData.taxAmount || 0
        );
        setFormData(newData);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.customerId) return;

        setIsLoading(true);
        try {
            if (editingId) {
                await updateInvoiceData(editingId, formData);
            } else {
                await createInvoice(formData);
            }
            handleCancel();
        } catch (error) {
            console.error('Error saving invoice:', error);
        } finally {
            setIsLoading(false);
        }
    };

    if (isFormOpen) {
        return (
            <div className="p-8 max-w-4xl mx-auto animate-fade-in">
                <div className="flex justify-between items-center mb-8">
                    <h2 className="text-3xl font-bold">{editingId ? t('edit') : t('newInvoice')}</h2>
                </div>
                <GlassCard className="p-8">
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <label className="block text-gray-400 text-sm mb-1">{t('clientData')}</label>
                                <select
                                    className="w-full bg-space-800/50 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-neon-blue"
                                    value={formData.customerId || ''}
                                    onChange={e => setFormData({ ...formData, customerId: e.target.value })}
                                    required
                                >
                                    <option value="">Select Customer</option>
                                    {customers.map(c => (
                                        <option key={c.id} value={c.id}>{c.name}</option>
                                    ))}
                                </select>
                            </div>

                            <Input
                                label="Invoice Number"
                                value={formData.invoiceNumber || ''}
                                onChange={e => setFormData({ ...formData, invoiceNumber: e.target.value })}
                            />

                            <Input
                                label="Issue Date"
                                type="date"
                                value={formData.issueDate || ''}
                                onChange={e => setFormData({ ...formData, issueDate: e.target.value })}
                            />

                            <Input
                                label="Due Date"
                                type="date"
                                value={formData.dueDate || ''}
                                onChange={e => setFormData({ ...formData, dueDate: e.target.value })}
                            />

                            <div className="space-y-2">
                                <label className="block text-gray-400 text-sm mb-1">{t('status')}</label>
                                <select
                                    className="w-full bg-space-800/50 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-neon-blue"
                                    value={formData.paymentStatus}
                                    onChange={e => setFormData({ ...formData, paymentStatus: e.target.value as PaymentStatus })}
                                >
                                    {Object.values(PaymentStatus).map(status => (
                                        <option key={status} value={status}>{status}</option>
                                    ))}
                                </select>
                            </div>

                            <div className="space-y-2">
                                <label className="block text-gray-400 text-sm mb-1">Payment Method</label>
                                <select
                                    className="w-full bg-space-800/50 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-neon-blue"
                                    value={formData.paymentMethod}
                                    onChange={e => setFormData({ ...formData, paymentMethod: e.target.value as PaymentMethod })}
                                >
                                    {Object.values(PaymentMethod).map(method => (
                                        <option key={method} value={method}>{method}</option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        <div className="border-t border-white/10 pt-6">
                            <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
                                <DollarSign size={18} className="text-green-400" /> Financial Details
                            </h3>
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                                <Input
                                    label="Labor Cost"
                                    type="number"
                                    value={formData.laborCost || ''}
                                    onChange={e => handleCostChange('laborCost', e.target.value)}
                                />
                                <Input
                                    label="Parts Cost"
                                    type="number"
                                    value={formData.partsCost || ''}
                                    onChange={e => handleCostChange('partsCost', e.target.value)}
                                />
                                <Input
                                    label="Tax Amount"
                                    type="number"
                                    value={formData.taxAmount || ''}
                                    onChange={e => handleCostChange('taxAmount', e.target.value)}
                                />
                            </div>
                            <div className="mt-4 flex justify-end">
                                <div className="text-xl font-bold">
                                    Total: <span className="text-green-400">${formData.totalAmount?.toLocaleString()}</span>
                                </div>
                            </div>
                        </div>

                        <TextArea
                            label={t('obs')}
                            rows={2}
                            value={formData.notes || ''}
                            onChange={e => setFormData({ ...formData, notes: e.target.value })}
                        />

                        <div className="flex justify-end gap-4 pt-4 border-t border-white/10">
                            <Button type="button" variant="secondary" onClick={handleCancel} disabled={isLoading}>
                                <X size={18} className="mr-2" /> {t('cancel')}
                            </Button>
                            <Button type="submit" isLoading={isLoading}>
                                <Save size={18} className="mr-2" /> {t('register')}
                            </Button>
                        </div>
                    </form>
                </GlassCard>
            </div>
        );
    }

    return (
        <div className="p-8 max-w-7xl mx-auto animate-fade-in">
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-3xl font-bold">{t('invoicesTitle')}</h2>
                <Button size="sm" onClick={handleNew}><PlusCircle size={16} /> {t('newInvoice')}</Button>
            </div>
            {invoices.length === 0 ? (
                <GlassCard>
                    <p className="text-gray-500 text-center py-8">{t('noInvoices')}</p>
                </GlassCard>
            ) : (
                <div className="grid gap-4">
                    {invoices.map(invoice => (
                        <GlassCard key={invoice.id} className="flex flex-col md:flex-row justify-between items-center gap-4 hover:bg-white/10">
                            <div className="flex items-center gap-4 w-full md:w-auto">
                                <div className="p-3 bg-green-500/20 rounded-lg text-green-400">
                                    <FileText size={24} />
                                </div>
                                <div>
                                    <h3 className="text-lg font-bold">#{invoice.invoiceNumber}</h3>
                                    <div className="flex items-center gap-2 text-sm text-gray-400">
                                        <User size={14} /> {invoice.customerName}
                                    </div>
                                    <div className="flex items-center gap-4 mt-1 text-xs text-gray-500">
                                        <span className="flex items-center gap-1"><Calendar size={12} /> {invoice.issueDate}</span>
                                        <span className={`px-2 py-0.5 rounded-full ${invoice.paymentStatus === PaymentStatus.PAID ? 'bg-green-500/20 text-green-400' :
                                                invoice.paymentStatus === PaymentStatus.PENDING ? 'bg-yellow-500/20 text-yellow-400' : 'bg-red-500/20 text-red-400'
                                            }`}>
                                            {invoice.paymentStatus}
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div className="flex items-center gap-6 w-full md:w-auto justify-between md:justify-end">
                                <div className="text-right">
                                    <p className="text-xs text-gray-500">Total Amount</p>
                                    <p className="text-xl font-bold text-green-400">${invoice.totalAmount.toLocaleString()}</p>
                                </div>
                                <div className="flex gap-2">
                                    <Button variant="ghost" size="sm" className="text-neon-blue" onClick={() => handleEdit(invoice)}>
                                        <Edit size={16} />
                                    </Button>
                                </div>
                            </div>
                        </GlassCard>
                    ))}
                </div>
            )}
        </div>
    );
};
