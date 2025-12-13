import React, { useState } from 'react';
import { PlusCircle, User, CreditCard, Phone, Edit, Trash2, Save, X } from 'lucide-react';
import { Customer, Language } from '../types';
import { GlassCard, Button, Input } from '../components/ui';

interface ClientsViewProps {
    t: (key: any) => string;
    customers: Customer[];
    createCustomer: (customer: Partial<Customer>) => Promise<Customer>;
    updateCustomerData: (id: string, customer: Partial<Customer>) => Promise<Customer>;
    deleteCustomer: (id: string) => Promise<void>;
}

export const ClientsView: React.FC<ClientsViewProps> = ({ t, customers, createCustomer, updateCustomerData, deleteCustomer }) => {
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [editingId, setEditingId] = useState<string | null>(null);
    const [formData, setFormData] = useState<Partial<Customer>>({});
    const [isLoading, setIsLoading] = useState(false);

    const handleEdit = (customer: Customer) => {
        setEditingId(customer.id);
        setFormData(customer);
        setIsFormOpen(true);
    };

    const handleNew = () => {
        setEditingId(null);
        setFormData({});
        setIsFormOpen(true);
    };

    const handleCancel = () => {
        setIsFormOpen(false);
        setEditingId(null);
        setFormData({});
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.name) return;

        setIsLoading(true);
        try {
            if (editingId) {
                await updateCustomerData(editingId, formData);
            } else {
                await createCustomer(formData);
            }
            handleCancel();
        } catch (error) {
            console.error('Error saving customer:', error);
        } finally {
            setIsLoading(false);
        }
    };

    if (isFormOpen) {
        return (
            <div className="p-8 max-w-4xl mx-auto animate-fade-in">
                <div className="flex justify-between items-center mb-8">
                    <h2 className="text-3xl font-bold">{editingId ? t('edit') : t('newClient')}</h2>
                </div>
                <GlassCard className="p-8">
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <Input
                                label={t('name')}
                                value={formData.name || ''}
                                onChange={e => setFormData({ ...formData, name: e.target.value })}
                                required
                            />
                            <Input
                                label={t('taxId')}
                                value={formData.taxId || ''}
                                onChange={e => setFormData({ ...formData, taxId: e.target.value })}
                            />
                            <Input
                                label={t('phone')}
                                value={formData.phone || ''}
                                onChange={e => setFormData({ ...formData, phone: e.target.value })}
                            />
                            <Input
                                label={t('address')}
                                value={formData.address || ''}
                                onChange={e => setFormData({ ...formData, address: e.target.value })}
                            />
                        </div>
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
                <h2 className="text-3xl font-bold">{t('clientsTitle')}</h2>
                <Button size="sm" onClick={handleNew}><PlusCircle size={16} /> {t('newClient')}</Button>
            </div>
            {customers.length === 0 ? (
                <GlassCard>
                    <p className="text-gray-500 text-center py-8">{t('noClients')}</p>
                </GlassCard>
            ) : (
                <div className="grid gap-4">
                    {customers.map(customer => (
                        <GlassCard key={customer.id} className="flex justify-between items-center hover:bg-white/10">
                            <div className="flex items-center gap-4">
                                <div className="p-3 bg-neon-blue/20 rounded-lg text-neon-blue">
                                    <User size={24} />
                                </div>
                                <div>
                                    <h3 className="text-lg font-bold">{customer.name}</h3>
                                    <div className="flex gap-4 text-sm text-gray-400">
                                        <span className="flex items-center gap-1"><CreditCard size={14} /> {customer.taxId}</span>
                                        <span className="flex items-center gap-1"><Phone size={14} /> {customer.phone}</span>
                                    </div>
                                    <p className="text-xs text-gray-500 mt-1">{customer.address}</p>
                                </div>
                            </div>
                            <div className="flex gap-2">
                                <Button variant="ghost" size="sm" className="text-neon-blue" onClick={() => handleEdit(customer)}>
                                    <Edit size={16} />
                                </Button>
                                <Button variant="ghost" size="sm" className="text-red-400" onClick={() => deleteCustomer(customer.id)}>
                                    <Trash2 size={16} />
                                </Button>
                            </div>
                        </GlassCard>
                    ))}
                </div>
            )}
        </div>
    );
};
