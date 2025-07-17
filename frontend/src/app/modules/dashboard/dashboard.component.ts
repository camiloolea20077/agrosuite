import { Component, OnInit } from '@angular/core';
import { ChartType } from 'chart.js';
import { ChartModule } from 'primeng/chart';
import { CardModule } from 'primeng/card';
import { RouterModule } from '@angular/router';
import { DashboardBirthDto, DashboardData } from 'src/app/shared/dto/dashboard.dto';
import { DashboardService } from 'src/app/shared/services/dashboard.service';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
@Component({
    selector: 'app-dashboard',
    standalone: true,
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [ChartModule,
        RouterModule,
        CardModule,],
})
export class DashboardComponent implements OnInit {
    barChartData: any;
    pieChartData: any;
    birthData: DashboardBirthDto[] = []
    totalCattle: number = 0
    totalBirths: number = 0
    totalEmployees: number = 0
    constructor(private dashboardService: DashboardService) { }
    ngOnInit(): void {
        this.loadDashboardData();
    }

    loadDashboardData(): void {
        this.dashboardService.getBirthsData().subscribe(
            (response: ResponseModel<DashboardData>) => {
                if (response && response.data) {
                    const data = response.data
                    this.birthData = data.births
                    this.totalCattle = data.totalCattle
                    this.totalBirths = data.totalBirths
                    this.totalEmployees = data.totalEmployees
                    this.prepareChartsData(); // Prepara los gráficos
                } else {
                    console.error('No se recibieron datos de nacimientos.');
                }
            },
            (error) => {
                console.error('Error al obtener los datos de nacimientos:', error);
            }
        );
    }


    prepareChartsData(): void {
        const months = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];

        const maleCounts = Array(12).fill(0)
        const femaleCounts = Array(12).fill(0)

        this.birthData.forEach((data) => {
            const monthIndex = data.month - 1
            maleCounts[monthIndex] = data.maleCount
            femaleCounts[monthIndex] = data.femaleCount
        });

        this.barChartData = {
            labels: months,
            datasets: [
                {
                    label: 'Nacimientos Machos',
                    backgroundColor: '#42A5F5',
                    data: maleCounts,
                },
                {
                    label: 'Nacimientos Hembras',
                    backgroundColor: '#66BB6A',
                    data: femaleCounts,
                },
            ],
        };
        const totalMale = maleCounts.reduce((sum, value) => sum + value, 0);
        const totalFemale = femaleCounts.reduce((sum, value) => sum + value, 0);

        this.pieChartData = {
            labels: ['Nacimientos Machos', 'Nacimientos Hembras'],
            datasets: [
                {
                    data: [totalMale, totalFemale],
                    backgroundColor: ['#42A5F5', '#66BB6A'],
                    hoverBackgroundColor: ['#64B5F6', '#81C784'],
                },
            ],
        };
    }
}
