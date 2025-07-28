export interface CattleSaleModel {
  id: number;
  fechaVenta: string;
  pesoTotal: number;
  precioKilo: number;
  precioTotal: number;
  destino: string;
  farmId: number;
  cattleIds: number[];
  comprador: string;
}
