export interface Inventory {
  id?: number;
  codigoInterno: string;
  nombreInsumo: string;
  descripcion?: string;
  marca?: string;
  tipoInsumoId: number;
  unidadMedida: string;
  unidadCompra: string;
  factorConversion: number;
  cantidadActual: number;
  cantidadMinima: number;
  puntoReorden: number;
  cantidadReservada: number;
  ubicacionAlmacen?: string;
  esPeligroso?: boolean;
  requiereCuidadoEspecial?: boolean;
  notas?: string;
  estadoId: number;
  farmId?: number;
  createdBy?: number;
  updatedBy?: number;
}

export interface InventoryTableDto {
  id: number;
  nombre_insumo: string;
  unidad: string;
  stock_actual: number;
  descripcion: string;
}

export interface CreateInventoryDto {
  codigoInterno: string;
  nombreInsumo: string;
  descripcion?: string;
  marca?: string;
  tipoInsumoId: number;
  unidadMedida: string;
  unidadCompra: string;
  factorConversion: number;
  cantidadActual: number;
  cantidadMinima: number;
  puntoReorden: number;
  cantidadReservada: number;
  ubicacionAlmacen?: string;
  esPeligroso?: boolean;
  requiereCuidadoEspecial?: boolean;
  notas?: string;
  estadoId: number;
  farmId?: number;
  createdBy?: number;
}

export interface UpdateStockDto {
  inventoryId: number;
  newQuantity: number;
}

export interface ReserveStockDto {
  inventoryId: number;
  quantity: number;
}

// inventory-movement.interface.ts
export interface InventoryMovement {
  id?: number;
  inventoryId: number;
  farmId?: number;
  tipoMovimientoId: number;
  estadoId: number;
  cantidad: number;
  unidadMedida: string;
  cantidadAnterior?: number;
  cantidadNueva?: number;
  fechaMovimiento?: Date;
  fechaProgramada?: Date;
  employeeId?: number;
  numeroDocumento?: string;
  observaciones?: string;
  notas?: string;
  cantidadDevuelta?: number;
  cantidadUsada?: number;
  fechaDevolucion?: Date;
  estaCerrado?: boolean;
  ubicacionOrigen?: string;
  ubicacionDestino?: string;
  requiereAprobacion?: boolean;
  aprobadoPor?: number;
  fechaAprobacion?: Date;
  esAutomatico?: boolean;
  movimientoPadreId?: number;
  createdBy?: number;
  updatedBy?: number;
}

// catalog.interface.ts
export interface TipoInsumo {
  id?: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  activo: number;
}

export interface EstadoInventario {
  id?: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  activo: number;
}

export interface TipoMovimiento {
  id?: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  esEntrada: number;
  esSalida: number;
  requiereEmpleado: number;
  requiereAprobacion: number;
  activo: number;
}

export interface EstadoMovimiento {
  id?: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  activo: number;
}

export interface Supplier {
  id: number;
  nit: string;
  cargo: string;
  observaciones?: string;
  nombre: string;
  contacto: string;
  telefono: string;
  email: string;
  direccion?: string;
  activo: number;
}
export interface SupplierTableModelDto {
  id: number;
  nombre: string;
  contacto: string;
  telefono: string;
  nit: string;
  email: string;
  activo: number;
}
export interface IFilterTableSupplier {
  id: number;
  nombre: string;
  contacto: string;
  telefono: string;
  nit: string;
  email: string;
  activo: number;
}
export interface InventoryMovementTableModelDto {
  /** Identificador único del movimiento */
  id: number;
  
  /** Fecha y hora del movimiento (ISO string) */
  fecha: string;
  
  /** Nombre del insumo/producto */
  insumo: string;
  
  /** Tipo de movimiento (Entrada, Salida, Ajuste, etc.) */
  tipoMovimiento: string | null;
  
  /** Cantidad del movimiento (puede ser positiva o negativa) */
  cantidad: number;
  
  /** Número de documento asociado al movimiento */
  documento: string;
  
  /** Estado actual del movimiento (Pendiente, Aprobado, Rechazado, etc.) */
  estado: string | null;
  
  /** Observaciones adicionales del movimiento */
  observaciones: string | null;
  
  /** ID del inventario asociado */
  inventoryId: number | null;
  
  /** ID del tipo de movimiento */
  tipoMovimientoId: number | null;
  
  /** ID del estado del movimiento */
  estadoId: number | null;
}
export interface IFilterTableMovimientos {
  id?: number;
  fecha?: string;
  insumo?: string;
  tipoMovimiento?: string;
  cantidad?: number;
  documento?: string;
  estado?: string;
  observaciones?: string;
}
export interface InventoryListDto{
  id:number
  nombre:string
  unidadMedida:string
}