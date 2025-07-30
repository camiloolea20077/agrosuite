export interface CreateCattleSaleItemDto {
  tipoOrigen: 'GANADO' | 'TERNERO';
  idOrigen: number;
  pesoVenta: number;
  precioKilo: number;
  precioTotal: number;

  numero_ganado?: string | number; // Opcional, solo si tipoOrigen es 'GANADO'
}