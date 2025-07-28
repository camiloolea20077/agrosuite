import { CreateCattleSaleItemDto } from './create-cattle-sale-item.dto';

export interface CreateCattleSaleDto {
  fechaVenta: string; // formato yyyy-MM-dd
  pesoTotal: number;
  precioKilo: number;
  destino: string;
  cattleIds: number[];
  precioTotal: number;
  comprador: string;
  items: CreateCattleSaleItemDto[];
}
