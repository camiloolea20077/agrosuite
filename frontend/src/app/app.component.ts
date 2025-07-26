import { registerLocaleData } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import localeEs from '@angular/common/locales/es';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit{
  title = 'frontend-erp';
    constructor(private primengConfig: PrimeNGConfig) {
    registerLocaleData(localeEs);
  }
  ngOnInit() {
    this.primengConfig.setTranslation({
      dayNames: ['domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado'],
      dayNamesShort: ['dom', 'lun', 'mar', 'mié', 'jue', 'vie', 'sáb'],
      dayNamesMin: ['D', 'L', 'M', 'M', 'J', 'V', 'S'],
      monthNames: [
        'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
      ],
      monthNamesShort: [
        'ene', 'feb', 'mar', 'abr', 'may', 'jun',
        'jul', 'ago', 'sep', 'oct', 'nov', 'dic'
      ],
      today: 'Hoy',
      clear: 'Limpiar',
      weekHeader: 'Sm',
      firstDayOfWeek: 1,
      dateFormat: 'dd/mm/yy'
    });
  }
}
