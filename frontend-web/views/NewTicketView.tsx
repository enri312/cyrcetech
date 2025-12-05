import React from 'react';
import { User, Monitor, Wrench, BrainCircuit, Sparkles } from 'lucide-react';
import { DeviceType, DeviceTypeDisplay, Ticket, Language, Customer } from '../types';
import { GlassCard, Input, TextArea, Button, LanguageToggle } from '../components/ui';

interface NewTicketViewProps {
    t: (key: any) => string;
    lang: Language;
    setLang: (l: Language) => void;
    formData: Partial<Ticket>;
    updateCustomer: (updates: Partial<Customer>) => void;
    updateForm: (updates: Partial<Ticket>) => void;
    handleAiDiagnosis: () => void;
    isAiLoading: boolean;
    onSubmit: () => void;
    onCancel: () => void;
}

export const NewTicketView: React.FC<NewTicketViewProps> = ({
    t, lang, setLang, formData, updateCustomer, updateForm, handleAiDiagnosis, isAiLoading, onSubmit, onCancel
}) => {
    return (
        <div className="p-8 max-w-4xl mx-auto animate-fade-in">
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-3xl font-bold">{t('registerTitle')}</h2>
                <LanguageToggle lang={lang} setLang={setLang} />
            </div>

            <GlassCard className="p-8">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-4">
                        <h3 className="text-neon-blue font-semibold flex items-center gap-2 mb-4">
                            <User size={18} /> {t('clientData')}
                        </h3>
                        <Input
                            label={t('name')}
                            value={formData.customer?.name}
                            onChange={e => updateCustomer({ name: e.target.value })}
                        />
                        <Input
                            label={t('taxId')}
                            value={formData.customer?.taxId}
                            onChange={e => updateCustomer({ taxId: e.target.value })}
                        />
                        <Input
                            label={t('address')}
                            value={formData.customer?.address}
                            onChange={e => updateCustomer({ address: e.target.value })}
                        />
                        <Input
                            label={t('phone')}
                            value={formData.customer?.phone}
                            onChange={e => updateCustomer({ phone: e.target.value })}
                        />
                    </div>

                    <div className="space-y-4">
                        <h3 className="text-neon-blue font-semibold flex items-center gap-2 mb-4">
                            <Monitor size={18} /> {t('deviceData')}
                        </h3>
                        <div className="grid grid-cols-2 gap-2">
                            <div className="mb-4">
                                <label className="block text-gray-400 text-sm mb-1">{t('deviceType')}</label>
                                <select
                                    className="w-full bg-space-800/50 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-neon-blue"
                                    value={formData.deviceType}
                                    onChange={e => updateForm({ deviceType: e.target.value as DeviceType })}
                                >
                                    {Object.values(DeviceType).map(type => (
                                        <option key={type} value={type}>
                                            {DeviceTypeDisplay[type]?.icon} {DeviceTypeDisplay[type]?.name || type}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <Input
                                label={t('brand')}
                                value={formData.brand || ''}
                                onChange={e => updateForm({ brand: e.target.value })}
                            />
                        </div>

                        <div className="grid grid-cols-2 gap-2">
                            <Input
                                label={t('model')}
                                value={formData.model || ''}
                                onChange={e => updateForm({ model: e.target.value })}
                            />
                            <Input
                                label={t('serial')}
                                value={formData.serialNumber || ''}
                                onChange={e => updateForm({ serialNumber: e.target.value })}
                            />
                        </div>
                        <Input
                            label={t('condition')}
                            value={formData.physicalCondition || ''}
                            onChange={e => updateForm({ physicalCondition: e.target.value })}
                        />
                        <Input
                            label={t('amountPaid')}
                            type="number"
                            value={formData.amountPaid || ''}
                            onChange={e => updateForm({ amountPaid: parseFloat(e.target.value) })}
                        />
                    </div>
                </div>

                <div className="mt-8 space-y-4">
                    <h3 className="text-neon-blue font-semibold flex items-center gap-2 mb-4">
                        <Wrench size={18} /> {t('problemDesc')}
                    </h3>

                    <TextArea
                        label={t('problemDesc')}
                        rows={3}
                        value={formData.problemDescription || ''}
                        onChange={e => updateForm({ problemDescription: e.target.value })}
                    />

                    <div className="flex justify-end mb-4">
                        <Button
                            type="button"
                            variant="secondary"
                            onClick={handleAiDiagnosis}
                            isLoading={isAiLoading}
                            className="text-sm py-2 hover:bg-neon-blue/10 hover:border-neon-blue/50"
                        >
                            <BrainCircuit size={16} className="text-neon-blue" /> {t('consultAI')}
                        </Button>
                    </div>

                    {formData.aiDiagnosis && (
                        <div className="bg-neon-blue/10 border border-neon-blue/20 rounded-lg p-4 mb-4 text-sm text-gray-300">
                            <h4 className="font-bold text-neon-blue mb-2 flex items-center gap-2">
                                <Sparkles size={14} /> {t('aiRec')}
                            </h4>
                            <div className="whitespace-pre-line">{formData.aiDiagnosis}</div>
                        </div>
                    )}

                    <TextArea
                        label={t('observations')}
                        rows={2}
                        value={formData.observations || ''}
                        onChange={e => updateForm({ observations: e.target.value })}
                    />

                    <div className="flex justify-end gap-4 mt-8 pt-6 border-t border-white/10">
                        <Button variant="secondary" type="button" onClick={onCancel}>{t('cancel')}</Button>
                        <Button type="button" onClick={onSubmit}>{t('register')}</Button>
                    </div>
                </div>
            </GlassCard>
        </div>
    );
};
