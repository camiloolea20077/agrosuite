export interface AutoCompleteModelDto {
  id: number;
  numeroIdentificacion: string;
  tipoIdentificacion: string;
  nombreRazonSocial: string;
  telefono: string;
  direccion: string;
}
export interface IAutoComplete<T> {
  search?: string;
  params?: T;
}
export interface FilterTerceroDto {
  // Por ahora sin campos, pero puedes agregar más filtros si deseas
}
