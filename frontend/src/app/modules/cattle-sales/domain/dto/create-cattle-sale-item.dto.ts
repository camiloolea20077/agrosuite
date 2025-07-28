export interface CreateCattleSaleItemDto {
  tipoOrigen: 'GANADO' | 'TERNERO';
  idOrigen: number;
  pesoVenta: number;
  precioKilo: number;
  precioTotal: number;
}