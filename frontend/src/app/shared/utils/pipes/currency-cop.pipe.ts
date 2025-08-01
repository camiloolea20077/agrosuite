import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'currencyCop'
})
export class CurrencyCopPipe implements PipeTransform {
  transform(value: number): string {
    if (value == null) return '';
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value);
  }
}
