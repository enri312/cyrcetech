import React, { useState } from 'react';
import { PlusCircle, HardDrive, User, Edit, Trash2, Save, X } from 'lucide-react';
import { Equipment, DeviceType, DeviceTypeDisplay, Customer } from '../types';
import { GlassCard, Button, Input } from '../components/ui';

interface EquipmentViewProps {
    t: (key: any) => string;
    equipment: Equipment[];
    customers: Customer[];
    createEquipment: (equip: Partial<Equipment>) => Promise<Equipment>;
    updateEquipmentData: (id: string, equip: Partial<Equipment>) => Promise<Equipment>;
    deleteEquipment: (id: string) => Promise<void>;
}

export const EquipmentView: React.FC<EquipmentViewProps> = ({
    t, equipment, customers, createEquipment, updateEquipmentData, deleteEquipment
}) => {
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [editingId, setEditingId] = useState<string | null>(null);
    const [formData, setFormData] = useState<Partial<Equipment>>({});
    const [isLoading, setIsLoading] = useState(false);

    const handleEdit = (equip: Equipment) => {
        setEditingId(equip.id);
        setFormData(equip);
        setIsFormOpen(true);
    };

    const handleNew = () => {
        setEditingId(null);
        setFormData({ deviceType: DeviceType.NOTEBOOK });
        setIsFormOpen(true);
    };

    const handleCancel = () => {
        setIsFormOpen(false);
        setEditingId(null);
        setFormData({});
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.brand || !formData.model || !formData.deviceType) return;

        setIsLoading(true);
        try {
            if (editingId) {
                await updateEquipmentData(editingId, formData);
            } else {
                await createEquipment(formData);
            }
            handleCancel();
        } catch (error) {
            console.error('Error saving equipment:', error);
        } finally {
            setIsLoading(false);
        }
    };

    if (isFormOpen) {
        return (
            <div className="p-8 max-w-4xl mx-auto animate-fade-in">
                <div className="flex justify-between items-center mb-8">
                    <h2 className="text-3xl font-bold">{editingId ? t('edit') : t('newEquipment')}</h2>
                </div>
                <GlassCard className="p-8">
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <label className="block text-gray-400 text-sm mb-1">{t('deviceType')}</label>
                                <select
                                    className="w-full bg-space-800/50 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-neon-blue"
                                    value={formData.deviceType}
                                    onChange={e => setFormData({ ...formData, deviceType: e.target.value as DeviceType })}
                                >
                                    {Object.values(DeviceType).map(type => (
                                        <option key={type} value={type}>
                                            {DeviceTypeDisplay[type]?.icon} {DeviceTypeDisplay[type]?.name || type}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="space-y-2">
                                <label className="block text-gray-400 text-sm mb-1">{t('clientData')}</label>
                                <select
                                    className="w-full bg-space-800/50 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-neon-blue"
                                    value={formData.customerId || ''}
                                    onChange={e => setFormData({ ...formData, customerId: e.target.value })}
                                >
                                    <option value="">Select Customer</option>
                                    {customers.map(c => (
                                        <option key={c.id} value={c.id}>{c.name}</option>
                                    ))}
                                </select>
                            </div>

                            <Input
                                label={t('brand')}
                                value={formData.brand || ''}
                                onChange={e => setFormData({ ...formData, brand: e.target.value })}
                                required
                            />
                            <Input
                                label={t('model')}
                                value={formData.model || ''}
                                onChange={e => setFormData({ ...formData, model: e.target.value })}
                                required
                            />
                            <Input
                                label={t('serial')}
                                value={formData.serialNumber || ''}
                                onChange={e => setFormData({ ...formData, serialNumber: e.target.value })}
                            />
                            <Input
                                label={t('condition')}
                                value={formData.physicalCondition || ''}
                                onChange={e => setFormData({ ...formData, physicalCondition: e.target.value })}
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
                <h2 className="text-3xl font-bold">{t('equipmentTitle')}</h2>
                <Button size="sm" onClick={handleNew}><PlusCircle size={16} /> {t('newEquipment')}</Button>
            </div>
            {equipment.length === 0 ? (
                <GlassCard>
                    <p className="text-gray-500 text-center py-8">{t('noEquipment')}</p>
                </GlassCard>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {equipment.map(equip => (
                        <GlassCard key={equip.id} className="hover:bg-white/10">
                            <div className="flex justify-between items-start mb-4">
                                <div className="p-3 bg-orange-500/20 rounded-lg text-orange-400">
                                    <HardDrive size={24} />
                                </div>
                                <span className="px-2 py-1 bg-white/10 rounded text-xs">
                                    {DeviceTypeDisplay[equip.deviceType]?.icon} {DeviceTypeDisplay[equip.deviceType]?.name || equip.deviceType}
                                </span>
                            </div>
                            <h3 className="text-xl font-bold mb-1">{equip.brand} {equip.model}</h3>
                            <p className="text-gray-400 text-sm">S/N: {equip.serialNumber}</p>
                            <p className="text-gray-500 text-xs mt-1">{equip.physicalCondition}</p>
                            {equip.customerName && (
                                <p className="text-xs text-neon-blue mt-2 flex items-center gap-1">
                                    <User size={12} /> {equip.customerName}
                                </p>
                            )}
                            <div className="flex justify-end gap-2 mt-4 pt-4 border-t border-white/10">
                                <Button variant="ghost" size="sm" className="text-neon-blue" onClick={() => handleEdit(equip)}>
                                    <Edit size={16} />
                                </Button>
                                <Button variant="ghost" size="sm" className="text-red-400" onClick={() => deleteEquipment(equip.id)}>
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
