export type TransferType = 'GANADO' | 'TERNEROS';

export interface CreateCattleTransferItemDto {
  cattleId: number | null;
  birthId: number | null;
  // Nuevos campos agregados
  numero_ganado?: string | null;
  peso?: number | null;
}

export interface CattleTransferItemDto {
  id: number;
  transferId: number;
  cattleId: number | null;
  birthId: number | null;
  // Nuevos campos agregados
  numero_ganado?: string | null;
  peso?: number | null;
}

export interface CreateCattleTransferDto {
  transferType: TransferType;
  originFarmId: number;
  destinationFarmId: number;
  transferDate: string; // 'YYYY-MM-DD'
  observations?: string | null;
  items: CreateCattleTransferItemDto[];
}

export interface CattleTransferDto {
  id: number;
  transferType: TransferType;
  originFarmId: number;
  destinationFarmId: number;
  transferDate: string;
  observations?: string | null;
  createdBy?: number;
  createdAt?: string;
  items: CattleTransferItemDto[]; // Cambié a CattleTransferItemDto para consistencia
}

export interface CattleTransferModel extends CattleTransferDto {}