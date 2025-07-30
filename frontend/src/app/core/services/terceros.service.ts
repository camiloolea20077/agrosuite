import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AutoCompleteModelDto, FilterTerceroDto, IAutoComplete } from 'src/app/shared/dto/autocomplete-terceros.model';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TerceroService {
  private apiUrl = environment.tercerosUrl;

  constructor(private http: HttpClient) {}

  autoCompleteTerceros(
    iAutoComplete: IAutoComplete<FilterTerceroDto>
  ): Observable<ResponseModel<AutoCompleteModelDto[]>> {
    return this.http.post<ResponseModel<AutoCompleteModelDto[]>>(
      `${this.apiUrl}/autocomplete`,
      iAutoComplete
    );
  }
}
