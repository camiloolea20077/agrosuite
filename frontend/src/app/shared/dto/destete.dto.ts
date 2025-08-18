export interface DesteteTableDto {
  id: number;
  numero_cria: string;
  fecha_nacimiento: string; // formato: YYYY-MM-DD
  fecha_proxima_destete: string; // formato: YYYY-MM-DD
  dias_restantes: number;
}