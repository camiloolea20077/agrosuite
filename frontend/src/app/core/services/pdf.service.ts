import { Injectable } from '@angular/core';
import * as pdfMake from 'pdfmake/build/pdfmake';
import { CreateCattleSaleItemDto } from 'src/app/modules/cattle-sales/domain/dto/create-cattle-sale-item.dto';
import { vfs } from 'pdfmake/build/vfs_fonts';
import { IndexDBService } from './index-db.service';
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
    const documento: any = {
      content: [
        {
        canvas: [
            // Fondo negro del encabezado
            { type: 'rect', x: 0, y: 0, w: 595, h: 70, color: '#000000' },
            
            // Curva amarilla mejor posicionada
            { type: 'ellipse', x: 500, y: 10, r1: 70, r2: 35, color: '#FFD700' }
        ]
        },
        {
          text: nombreFinca.toUpperCase(),
          fontSize: 18,
          bold: true,
          color: 'white',
          absolutePosition: { x: 30, y: 30 },
        },
        {
          text: 'DATOS DEL CLIENTE',
          style: 'sectionHeader',
          margin: [0, 90, 0, 5],
        },
        {
          columns: [
            {
              width: '*',
              text: [
                `Nombre: ${cliente.nombre}\n`,
                `Dirección: ${cliente.direccion}\n`,
                `Teléfono: ${cliente.telefono}\n`,
                `Fecha: ${cliente.fecha}\n`,
              ],
            },
          ],
        },
        {
          style: 'tableStyle',
          table: {
            widths: ['*', 50, 60, 60, 70],
            body: [
              ['Concepto', 'Cantidad', 'Peso kg', 'Precio/kg', 'Total'],
              ...animales.map((a, i) => [
                `Animal #${i + 1}`,
                '1',
                `${a.pesoVenta}`,
                `$${precioKilo.toLocaleString()}`,
                `$${(a.pesoVenta * precioKilo).toLocaleString()}`,
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
          canvas: [
            {
              type: 'ellipse',
              x: -60,
              y: 740,
              r1: 70,
              r2: 40,
              color: '#FFD700',
            },
            { type: 'rect', x: 0, y: 770, w: 595, h: 30, color: '#000000' },
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
