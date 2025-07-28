export interface CattleSaleDto {
  id: number;
  comprador: string;
  destino: string;
  fechaVenta: string;
  observaciones: string;
  precioKilo: number;
  pesoTotal: number;
  precioTotal: number;
  items: CreateCattleSaleItemDto[];
}
export interface CreateCattleSaleItemDto {
  tipoOrigen: 'GANADO' | 'TERNERO';
  idOrigen: number;
  pesoVenta: number;
  precioKilo: number;
  precioTotal: number;
}
