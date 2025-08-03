import { Injectable } from '@angular/core';
import * as pdfMake from 'pdfmake/build/pdfmake';
import { CreateCattleSaleItemDto } from 'src/app/modules/cattle-sales/domain/dto/create-cattle-sale-item.dto';
import { vfs } from 'pdfmake/build/vfs_fonts';
import { IndexDBService } from './index-db.service';
import { footer, headerFactura } from 'src/app/shared/utils/img';
import { HeadersValues } from 'src/app/shared/utils/models/heades-clients';
(pdfMake as any).vfs = vfs;

@Injectable({
  providedIn: 'root',
})
export class CattleSalePdfService {
  constructor(private readonly indexDB: IndexDBService) {}
  async exportarFactura(
    cliente: {
      nombre: string;
      direccion: string;
      telefono: string;
      fecha: string;
      formaPago: string;
    },
    precioKilo: number,
    animales: CreateCattleSaleItemDto[],
    subtotal: number
  ): Promise<void> {
    const authData = await this.indexDB.loadDataAuthDB();
    const nombreFinca = authData?.user?.farm_name ?? 'GANADERÍA SIN NOMBRE';
    const total = subtotal;
    const headerCell = {
          text: '',
          colSpan: 1,
          alignment: 'center',
          fontSize: 10,
          bold: true,
          color: '#FFFFFF',
          fillColor: 'black',
          margin: [0, 5],
          borderColor: [
            '#FFFFFF',
            '#FFFFFF',
            '#FFFFFF',
            '#FFFFFF',
          ],
    }
    const rowCell = {
          text: '',
          colSpan: 1,
          alignment: 'center',
          fontSize: 10,
          margin: [0, 5],
          borderColor: [
            '#FFFFFF',
            '#FFFFFF',
            '#FFFFFF',
            'black',
          ],
    }
    const titles = ['Concepto', 'Cantidad', 'Peso kg', 'Precio/kg', 'Total']
    
    const documento: any = {
      content: [
        {
          stack: [
            {
              width: 600,
              image: headerFactura,
              relativePosition: { x: -40, y: -40 },
            },
            {
              text: nombreFinca.toUpperCase(),
              fontSize: 18,
              bold: true,
              color: 'white',
            }
          ],
        },
        {
          text: 'DATOS DEL CLIENTE',
          style: 'sectionHeader',
          margin: [0, 90, 0, 5],
        },
        createHeaderResume({
          nombre: cliente.nombre,
          direccion: cliente.direccion,
          telefono: cliente.telefono,
          fecha: cliente.fecha,
        }),
        {
          style: 'tableStyle',
          table: {
            widths: ['*', 50, 60, 60, 70],
            body: [
              titles.map(title => ({ ...headerCell, text: title })),
              ...animales.map((a, i) => [
                { ...rowCell, text: `Animal #${i + 1}` },
                {...rowCell, text: '1', },
                {...rowCell, text: `${a.pesoVenta}`, },
                {...rowCell, text: `$${precioKilo.toLocaleString()}`, },
                {...rowCell, text: `$${(a.pesoVenta * precioKilo).toLocaleString()}`, },
              ]),
            ],
          },
        },
        { text: 'Forma de pago: ' + cliente.formaPago, margin: [0, 10, 0, 2] },
        {
          columns: [
            { width: '*', text: '' },
            {
              width: 'auto',
              table: {
                body: [
                  ['Subtotal', `$${subtotal.toLocaleString()}`],
                  ['IVA 0%', '$0'],
                  [
                    { text: 'Total', bold: true },
                    { text: `$${total.toLocaleString()}`, bold: true },
                  ],
                ],
              },
              layout: 'noBorders',
            },
          ],
        },
        {
          margin: [0, 40, 0, 0],
          canvas: [
            { type: 'line', x1: 400, y1: 0, x2: 540, y2: 0, lineWidth: 1 },
          ],
        },
        {
          text: cliente.nombre,
          alignment: 'right',
          margin: [0, 5, 40, 0],
        },
        {
          stack: [
            {
              width: 600,
              image: footer,
              absolutePosition: { x: 0, y: 680 },
            }
          ],
        },
      ],
      styles: {
        sectionHeader: {
          fontSize: 13,
          bold: true,
          margin: [0, 10, 0, 5],
        },
        tableStyle: {
          margin: [0, 10, 0, 10],
        },
      },
    };

    pdfMake.createPdf(documento).open();
  }
}
export const createHeaderResume = (value: HeadersValues): object => {
  return {
    table: {
      fontSize: 8,
      widths: [100, 20, 100, '*'],
      body: [
        [
          {
            text: 'Nombre:',
            // fontSize: 10,
            bold: true,
            margin: [0, 1, 0, 0],
          },
          '',
          {
            text: `${value.nombre}`,
            alignment: 'right',
            margin: [0, 1, 0, 0],
          },
          '',
        ],
        [
          {
            text: 'Dirección:',
            bold: true,
            margin: [0, 1, 0, 0],
          },
          '',
          {
            text: `${value.direccion}`,
            alignment: 'right',
            margin: [0, 1, 0, 0],
          },
          '',
        ],
        [
          {
            text: 'Telefono:',
            bold: true,
            margin: [0, 1, 0, 0],
          },
          '',
          {
            text: `${value.telefono}`,
            alignment: 'right',
            margin: [0, 1, 0, 0],
          },
          '',
        ],
        [
          {
            text: 'Fecha:',
            bold: true,
            margin: [0, 1, 0, 0],
          },
          '',
          {
            text: `${value.fecha}`,
            alignment: 'right',
            margin: [0, 1, 0, 0],
          },
          '',
        ],
      ],
    },
    layout: 'noBorders',
  }
}
