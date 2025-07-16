export interface DashboardBirthDto {
  month: number;
  maleCount: number; // Usamos `number` ya que BigDecimal se convierte en un número
  femaleCount: number;
}
export interface DashboardData {
  births: DashboardBirthDto[];
  totalCattle: number;
  totalBirths: number;
  totalEmployees: number;
}