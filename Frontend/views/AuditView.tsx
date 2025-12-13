import React, { useState, useEffect } from 'react';
import { Search, Filter, Calendar, User, FileText, RefreshCw } from 'lucide-react';
import { AuditLog, AuditAction, AuditActionDisplay, Role, RoleDisplay, Language } from '../types';
import { auditApi, AuditLogDTO } from '../services/api';
import { GlassCard, Button } from '../components/ui';

interface AuditViewProps {
    lang: Language;
    t: (key: string) => string;
}

// Badge de acción con color dinámico
const ActionBadge: React.FC<{ action: AuditAction; lang: Language }> = ({ action, lang }) => {
    const display = AuditActionDisplay[action];
    return (
        <span
            className="px-2 py-1 rounded-full text-xs font-semibold"
            style={{ backgroundColor: `${display?.color}20`, color: display?.color }}
        >
            {lang === 'es' ? display?.es : display?.en}
        </span>
    );
};

// Badge de rol
const RoleBadge: React.FC<{ role: Role; lang: Language }> = ({ role, lang }) => {
    const display = RoleDisplay[role];
    const colors: Record<Role, string> = {
        [Role.ADMIN]: '#F44336',
        [Role.TECHNICIAN]: '#2196F3',
        [Role.RECEPTIONIST]: '#4CAF50'
    };
    return (
        <span
            className="px-2 py-1 rounded-full text-xs font-semibold"
            style={{ backgroundColor: `${colors[role]}20`, color: colors[role] }}
        >
            {lang === 'es' ? display?.es : display?.en}
        </span>
    );
};

export const AuditView: React.FC<AuditViewProps> = ({ lang, t }) => {
    const [logs, setLogs] = useState<AuditLog[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Filters
    const [filterEntityType, setFilterEntityType] = useState<string>('');
    const [filterRole, setFilterRole] = useState<string>('');
    const [searchTerm, setSearchTerm] = useState<string>('');

    const fetchLogs = async () => {
        setLoading(true);
        setError(null);
        try {
            let data: AuditLogDTO[];

            if (filterEntityType) {
                data = await auditApi.getByEntityType(filterEntityType);
            } else if (filterRole) {
                data = await auditApi.getByRole(filterRole);
            } else {
                data = await auditApi.getAll();
            }

            const transformedLogs: AuditLog[] = data.map((log) => ({
                id: log.id,
                userId: log.userId,
                username: log.username,
                userRole: log.userRole as Role,
                userRoleDisplayName: log.userRoleDisplayName,
                action: log.action as AuditAction,
                actionDisplayName: log.actionDisplayName,
                entityType: log.entityType,
                entityId: log.entityId,
                timestamp: log.timestamp,
                details: log.details,
                ipAddress: log.ipAddress
            }));

            setLogs(transformedLogs);
        } catch (err: any) {
            setError(err.message || 'Error loading audit logs');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchLogs();
    }, [filterEntityType, filterRole]);

    // Filter logs by search term
    const filteredLogs = logs.filter(log => {
        if (!searchTerm) return true;
        const term = searchTerm.toLowerCase();
        return (
            log.username?.toLowerCase().includes(term) ||
            log.entityType?.toLowerCase().includes(term) ||
            log.details?.toLowerCase().includes(term) ||
            log.entityId?.toLowerCase().includes(term)
        );
    });

    // Get unique entity types for filter dropdown
    const entityTypes = [...new Set(logs.map(log => log.entityType).filter(Boolean))];

    return (
        <div className="p-8 max-w-7xl mx-auto animate-fade-in">
            {/* Header */}
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-3xl font-bold flex items-center gap-3">
                    <FileText className="text-neon-purple" />
                    {lang === 'es' ? 'Auditoría del Sistema' : 'System Audit'}
                </h2>
                <Button onClick={fetchLogs} size="sm" variant="secondary">
                    <RefreshCw size={16} className={loading ? 'animate-spin' : ''} />
                    {lang === 'es' ? 'Actualizar' : 'Refresh'}
                </Button>
            </div>

            {/* Filters */}
            <GlassCard className="mb-6">
                <div className="flex flex-wrap gap-4 items-center">
                    {/* Search */}
                    <div className="relative flex-1 min-w-[200px]">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500" size={18} />
                        <input
                            type="text"
                            placeholder={lang === 'es' ? 'Buscar en logs...' : 'Search logs...'}
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="w-full pl-10 bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
                        />
                    </div>

                    {/* Entity Type Filter */}
                    <div className="flex items-center gap-2">
                        <Filter size={18} className="text-gray-400" />
                        <select
                            value={filterEntityType}
                            onChange={(e) => { setFilterEntityType(e.target.value); setFilterRole(''); }}
                            className="bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
                        >
                            <option value="">{lang === 'es' ? 'Todas las entidades' : 'All entities'}</option>
                            {entityTypes.map(type => (
                                <option key={type} value={type}>{type}</option>
                            ))}
                        </select>
                    </div>

                    {/* Role Filter */}
                    <div className="flex items-center gap-2">
                        <User size={18} className="text-gray-400" />
                        <select
                            value={filterRole}
                            onChange={(e) => { setFilterRole(e.target.value); setFilterEntityType(''); }}
                            className="bg-white/5 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-neon-blue"
                        >
                            <option value="">{lang === 'es' ? 'Todos los roles' : 'All roles'}</option>
                            <option value="ADMIN">{lang === 'es' ? 'Administrador' : 'Admin'}</option>
                            <option value="TECHNICIAN">{lang === 'es' ? 'Técnico' : 'Technician'}</option>
                            <option value="RECEPTIONIST">{lang === 'es' ? 'Usuario' : 'User'}</option>
                        </select>
                    </div>
                </div>
            </GlassCard>

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

            {/* Logs Table */}
            {!loading && filteredLogs.length === 0 && (
                <GlassCard className="text-center py-12">
                    <p className="text-gray-400">
                        {lang === 'es' ? 'No hay registros de auditoría.' : 'No audit logs found.'}
                    </p>
                </GlassCard>
            )}

            {!loading && filteredLogs.length > 0 && (
                <GlassCard className="overflow-hidden">
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead>
                                <tr className="border-b border-white/10">
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        <Calendar size={14} className="inline mr-2" />
                                        {lang === 'es' ? 'Fecha/Hora' : 'Date/Time'}
                                    </th>
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Usuario' : 'User'}
                                    </th>
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Rol' : 'Role'}
                                    </th>
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Acción' : 'Action'}
                                    </th>
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Entidad' : 'Entity'}
                                    </th>
                                    <th className="text-left p-4 text-gray-400 font-medium">
                                        {lang === 'es' ? 'Detalles' : 'Details'}
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredLogs.map((log) => (
                                    <tr key={log.id} className="border-b border-white/5 hover:bg-white/5 transition-colors">
                                        <td className="p-4 text-sm text-gray-300 font-mono">{log.timestamp}</td>
                                        <td className="p-4">
                                            <span className="text-white font-medium">{log.username}</span>
                                        </td>
                                        <td className="p-4">
                                            <RoleBadge role={log.userRole} lang={lang} />
                                        </td>
                                        <td className="p-4">
                                            <ActionBadge action={log.action} lang={lang} />
                                        </td>
                                        <td className="p-4">
                                            <span className="text-gray-300">{log.entityType}</span>
                                            {log.entityId && (
                                                <span className="text-gray-500 text-xs ml-2">#{log.entityId.substring(0, 8)}...</span>
                                            )}
                                        </td>
                                        <td className="p-4 text-sm text-gray-400 max-w-xs truncate">{log.details}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    {/* Footer with count */}
                    <div className="p-4 border-t border-white/10 text-gray-400 text-sm">
                        {lang === 'es'
                            ? `Mostrando ${filteredLogs.length} de ${logs.length} registros`
                            : `Showing ${filteredLogs.length} of ${logs.length} records`
                        }
                    </div>
                </GlassCard>
            )}
        </div>
    );
};

export default AuditView;
