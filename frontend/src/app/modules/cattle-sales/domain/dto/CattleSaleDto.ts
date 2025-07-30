import { CreateCattleSaleItemDto } from './create-cattle-sale-item.dto';

export interface CattleSaleDto {
  id: number;
  numeroFactura: string;
  fechaVenta: string; // formato yyyy-MM-dd
  horaEmision: string; // formato HH:mm
  moneda: string;
  formaPago: string;
  terceroId: number;
  tipoIdentificacion: string;
  numeroIdentificacion: string;
  nombreRazonSocial: string;
  direccion: string;
  telefono: string;

  destino: string;
  observaciones: string;

  precioKilo: number;
  pesoTotal: number;
  subtotal: number;
  iva: number;
  descuentos: number;
  precioTotal: number;

  items: CreateCattleSaleItemDto[];
}
