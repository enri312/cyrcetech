import React, { useState, useEffect } from 'react';
import { Calendar, DollarSign, FileText, RefreshCw, TrendingUp, TrendingDown, Wallet } from 'lucide-react';
import { Language } from '../types';
import { billingApi, BillingInvoiceDTO } from '../services/api';
import { GlassCard, Button } from '../components/ui';

interface BillingViewProps {
    lang: Language;
    t: (key: string) => string;
}

type ReportType = 'DAILY' | 'MONTHLY' | 'YEARLY';

// Formatear moneda en Guaraníes
const formatCurrency = (amount: number): string => {
    return new Intl.NumberFormat('es-PY', {
        style: 'currency',
        currency: 'PYG',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(amount);
};

// Badge de estado de pago
const PaymentStatusBadge: React.FC<{ status: string; lang: Language }> = ({ status, lang }) => {
    const statusConfig: Record<string, { color: string; es: string; en: string }> = {
        PAID: { color: '#4CAF50', es: 'Pagado', en: 'Paid' },
        PENDING: { color: '#FF9800', es: 'Pendiente', en: 'Pending' },
        PARTIAL: { color: '#2196F3', es: 'Parcial', en: 'Partial' },
        OVERDUE: { color: '#F44336', es: 'Vencido', en: 'Overdue' }
    };
    const config = statusConfig[status] || statusConfig.PENDING;

    return (
        <span
            className="px-2 py-1 rounded-full text-xs font-semibold"
            style={{ backgroundColor: `${config.color}20`, color: config.color }}
        >
            {lang === 'es' ? config.es : config.en}
        </span>
    );
};

export const BillingView: React.FC<BillingViewProps> = ({ lang, t }) => {
    const [invoices, setInvoices] = useState<BillingInvoiceDTO[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Report type and filters
    const [reportType, setReportType] = useState<ReportType>('DAILY');
    const [selectedDate, setSelectedDate] = useState<string>(new Date().toISOString().split('T')[0]);
    const [selectedMonth, setSelectedMonth] = useState<number>(new Date().getMonth() + 1);
    const [selectedYear, setSelectedYear] = useState<number>(new Date().getFullYear());

    // Summary calculations
    const totalInvoiced = invoices.reduce((sum, inv) => sum + inv.totalAmount, 0);
    const totalCollected = invoices.reduce((sum, inv) => sum + inv.paidAmount, 0);
    const pendingBalance = totalInvoiced - totalCollected;

    const fetchBillingData = async () => {
        setLoading(true);
        setError(null);
        try {
            let data: BillingInvoiceDTO[];

            switch (reportType) {
                case 'DAILY':
                    data = await billingApi.getDaily(selectedDate);
                    break;
                case 'MONTHLY':
                    data = await billingApi.getMonthly(selectedYear, selectedMonth);
                    break;
                case 'YEARLY':
                    data = await billingApi.getYearly(selectedYear);
                    break;
                default:
                    data = [];
            }

            setInvoices(data);
        } catch (err: any) {
            setError(err.message || 'Error loading billing data');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchBillingData();
    }, [reportType, selectedDate, selectedMonth, selectedYear]);

    // Generate month options
    const months = [
        { value: 1, es: 'Enero', en: 'January' },
        { value: 2, es: 'Febrero', en: 'February' },
        { value: 3, es: 'Marzo', en: 'March' },
        { value: 4, es: 'Abril', en: 'April' },
        { value: 5, es: 'Mayo', en: 'May' },
        { value: 6, es: 'Junio', en: 'June' },
        { value: 7, es: 'Julio', en: 'July' },
        { value: 8, es: 'Agosto', en: 'August' },
        { value: 9, es: 'Septiembre', en: 'September' },
        { value: 10, es: 'Octubre', en: 'October' },
        { value: 11, es: 'Noviembre', en: 'November' },
        { value: 12, es: 'Diciembre', en: 'December' }
    ];

    // Generate year options (last 5 years)
    const currentYear = new Date().getFullYear();
    const years = Array.from({ length: 6 }, (_, i) => currentYear - i);

    return (
        <div className="p-8 max-w-7xl mx-auto animate-fade-in">
            {/* Header */}
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-3xl font-bold flex items-center gap-3">
                    <DollarSign className="text-neon-green" />
                    {lang === 'es' ? 'Facturación' : 'Billing'}
                </h2>
                <Button onClick={fetchBillingData} size="sm" variant="secondary">
                    <RefreshCw size={16} className={loading ? 'animate-spin' : ''} />
                    {lang === 'es' ? 'Actualizar' : 'Refresh'}
                </Button>
            </div>

            {/* Filters */}
            <GlassCard className="mb-6">
                <div className="flex flex-wrap gap-4 items-center">
                    {/* Report Type Selector */}
                    <div className="flex items-center gap-2">
                        <Calendar size={18} className="text-gray-400" />
                        <select
                            value={reportType}
                            onChange={(e) => setReportType(e.target.value as ReportType)}
                            className="bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
                        >
                            <option value="DAILY">{lang === 'es' ? 'Diario' : 'Daily'}</option>
                            <option value="MONTHLY">{lang === 'es' ? 'Mensual' : 'Monthly'}</option>
                            <option value="YEARLY">{lang === 'es' ? 'Anual' : 'Yearly'}</option>
                        </select>
                    </div>

                    {/* Date Picker for Daily */}
                    {reportType === 'DAILY' && (
                        <div className="flex items-center gap-2">
                            <span className="text-gray-400">{lang === 'es' ? 'Fecha:' : 'Date:'}</span>
                            <input
                                type="date"
                                value={selectedDate}
                                onChange={(e) => setSelectedDate(e.target.value)}
                                className="bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
                            />
                        </div>
                    )}

                    {/* Month/Year Picker for Monthly */}
                    {reportType === 'MONTHLY' && (
                        <>
                            <div className="flex items-center gap-2">
                                <span className="text-gray-400">{lang === 'es' ? 'Mes:' : 'Month:'}</span>
                                <select
                                    value={selectedMonth}
                                    onChange={(e) => setSelectedMonth(Number(e.target.value))}
                                    className="bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
                                >
                                    {months.map(m => (
                                        <option key={m.value} value={m.value}>
                                            {lang === 'es' ? m.es : m.en}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="flex items-center gap-2">
                                <span className="text-gray-400">{lang === 'es' ? 'Año:' : 'Year:'}</span>
                                <select
                                    value={selectedYear}
                                    onChange={(e) => setSelectedYear(Number(e.target.value))}
                                    className="bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
                                >
                                    {years.map(y => (
                                        <option key={y} value={y}>{y}</option>
                                    ))}
                                </select>
                            </div>
                        </>
                    )}

                    {/* Year Picker for Yearly */}
                    {reportType === 'YEARLY' && (
                        <div className="flex items-center gap-2">
                            <span className="text-gray-400">{lang === 'es' ? 'Año:' : 'Year:'}</span>
                            <select
                                value={selectedYear}
                                onChange={(e) => setSelectedYear(Number(e.target.value))}
                                className="bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
                            >
                                {years.map(y => (
                                    <option key={y} value={y}>{y}</option>
                                ))}
                            </select>
                        </div>
                    )}
                </div>
            </GlassCard>

            {/* Summary Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                {/* Total Invoiced */}
                <GlassCard className="border-l-4 border-l-blue-500">
                    <div className="flex justify-between items-start">
                        <div>
                            <p className="text-gray-400 text-sm">
                                {lang === 'es' ? 'Total Facturado' : 'Total Invoiced'}
                            </p>
                            <p className="text-2xl font-bold text-white mt-1">
                                {formatCurrency(totalInvoiced)}
                            </p>
                        </div>
                        <div className="w-12 h-12 rounded-xl bg-blue-500/20 flex items-center justify-center">
                            <FileText className="text-blue-500" size={24} />
                        </div>
                    </div>
                </GlassCard>

                {/* Total Collected */}
                <GlassCard className="border-l-4 border-l-green-500">
                    <div className="flex justify-between items-start">
                        <div>
                            <p className="text-gray-400 text-sm">
                                {lang === 'es' ? 'Total Cobrado' : 'Total Collected'}
                            </p>
                            <p className="text-2xl font-bold text-white mt-1">
                                {formatCurrency(totalCollected)}
                            </p>
                        </div>
                        <div className="w-12 h-12 rounded-xl bg-green-500/20 flex items-center justify-center">
                            <TrendingUp className="text-green-500" size={24} />
                        </div>
                    </div>
                </GlassCard>

                {/* Pending Balance */}
                <GlassCard className="border-l-4 border-l-yellow-500">
                    <div className="flex justify-between items-start">
                        <div>
                            <p className="text-gray-400 text-sm">
                                {lang === 'es' ? 'Saldo Pendiente' : 'Pending Balance'}
                            </p>
                            <p className="text-2xl font-bold text-white mt-1">
                                {formatCurrency(pendingBalance)}
                            </p>
                        </div>
                        <div className="w-12 h-12 rounded-xl bg-yellow-500/20 flex items-center justify-center">
                            <Wallet className="text-yellow-500" size={24} />
                        </div>
                    </div>
                </GlassCard>
            </div>

            {/* Error Message */}
            {error && (
                <GlassCard className="mb-6 border-l-4 border-l-red-500">
                    <p className="text-red-400">{error}</p>
                </GlassCard>
            )}

            {/* Loading State */}
            {loading && (
                <div className="flex justify-center py-12">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-neon-blue"></div>
                </div>
            )}

            {/* Invoices Table */}
            {!loading && invoices.length === 0 && (
                <GlassCard className="text-center py-12">
                    <p className="text-gray-400">
                        {lang === 'es' ? 'No hay facturas para este período.' : 'No invoices for this period.'}
                    </p>
                </GlassCard>
            )}

            {!loading && invoices.length > 0 && (
                <GlassCard className="overflow-hidden">
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead>
                                <tr className="border-b border-white/10">
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Nº Factura' : 'Invoice #'}
                                    </th>
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Fecha' : 'Date'}
                                    </th>
                                    <th className="text-right p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Total' : 'Total'}
                                    </th>
                                    <th className="text-right p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Pagado' : 'Paid'}
                                    </th>
                                    <th className="text-right p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Saldo' : 'Balance'}
                                    </th>
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Estado' : 'Status'}
                                    </th>
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Método' : 'Method'}
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {invoices.map((invoice) => (
                                    <tr key={invoice.id} className="border-b border-white/5 hover:bg-white/5 transition-colors">
                                        <td className="p-4 text-white font-medium">{invoice.invoiceNumber}</td>
                                        <td className="p-4 text-gray-300 font-mono text-sm">{invoice.issueDate}</td>
                                        <td className="p-4 text-right text-white">{formatCurrency(invoice.totalAmount)}</td>
                                        <td className="p-4 text-right text-green-400">{formatCurrency(invoice.paidAmount)}</td>
                                        <td className="p-4 text-right text-yellow-400">{formatCurrency(invoice.balance)}</td>
                                        <td className="p-4">
                                            <PaymentStatusBadge status={invoice.paymentStatus} lang={lang} />
                                        </td>
                                        <td className="p-4 text-gray-400">{invoice.paymentMethod || '-'}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    {/* Footer with count */}
                    <div className="p-4 border-t border-white/10 text-gray-400 text-sm">
                        {lang === 'es'
                            ? `Mostrando ${invoices.length} facturas`
                            : `Showing ${invoices.length} invoices`
                        }
                    </div>
                </GlassCard>
            )}
        </div>
    );
};

export default BillingView;
