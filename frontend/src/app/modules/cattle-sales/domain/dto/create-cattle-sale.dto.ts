import { CreateCattleSaleItemDto } from './create-cattle-sale-item.dto';

export interface CreateCattleSaleDto {
  tipoVenta: 'INDIVIDUAL' | 'LOTE'; // Tipo de venta, puede ser 'INDIVIDUAL' o 'LOTE'
  numeroFactura: string;
  fechaVenta: string; // formato yyyy-MM-dd
  horaEmision: string; // formato HH:mm
  moneda: string;      // Ej: 'COP'
  formaPago: string;   // Ej: 'Contado', 'Crédito'
  
  destino: string;
  observaciones: string;

  precioKilo: number;
  pesoTotal: number;
  subtotal: number;
  iva: number;
  descuentos: number;
  total: number; // Total a pagar, puede ser igual a precioTotal

  terceroId: number;
  cattleIds: number[];
  items: CreateCattleSaleItemDto[];
}
