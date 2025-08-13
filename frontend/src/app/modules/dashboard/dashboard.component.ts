import { Component, OnInit } from '@angular/core';
import { ChartType } from 'chart.js';
import { ChartModule } from 'primeng/chart';
import { CardModule } from 'primeng/card';
import { RouterModule } from '@angular/router';
import { DashboardBirthDto, DashboardData } from 'src/app/shared/dto/dashboard.dto';
import { DashboardService } from 'src/app/shared/services/dashboard.service';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { ButtonModule } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { TooltipModule } from 'primeng/tooltip';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [ChartModule,
        CommonModule,
        ButtonModule,
        ChipModule,
        TooltipModule,
        RouterModule,
        CardModule,],
})
export class DashboardComponent implements OnInit {
     barChartData: any;
    pieChartData: any;
    birthData: DashboardBirthDto[] = [];
    totalCattle: number = 0;
    totalBirths: number = 0;
    totalEmployees: number = 0;

    // Opciones mejoradas para gráfico de líneas
    lineChartOptions = {
        maintainAspectRatio: false,
        aspectRatio: 0.8,
        plugins: {
            legend: {
                display: false, // Usamos nuestra propia leyenda
            },
            tooltip: {
                mode: 'index',
                intersect: false,
                backgroundColor: 'rgba(0, 0, 0, 0.8)',
                titleColor: '#fff',
                bodyColor: '#fff',
                borderColor: 'rgba(102, 126, 234, 0.8)',
                borderWidth: 1,
                cornerRadius: 8,
                displayColors: true,
                callbacks: {
                    title: function(context: any) {
                        return `${context[0].label}`;
                    },
                    label: function(context: any) {
                        return `${context.dataset.label}: ${context.parsed.y} nacimientos`;
                    }
                }
            }
        },
        responsive: true,
        interaction: {
            mode: 'nearest',
            axis: 'x',
            intersect: false
        },
        scales: {
            x: {
                display: true,
                grid: {
                    display: false,
                },
                ticks: {
                    color: '#6c757d',
                    font: {
                        size: 12,
                        weight: '500'
                    }
                }
            },
            y: {
                display: true,
                grid: {
                    color: 'rgba(0, 0, 0, 0.05)',
                    drawBorder: false,
                },
                ticks: {
                    color: '#6c757d',
                    font: {
                        size: 12,
                        weight: '500'
                    },
                    callback: function(value: any) {
                        return value + ' nacimientos';
                    }
                },
                beginAtZero: true
            }
        },
        elements: {
            line: {
                tension: 0.4,
                borderWidth: 3,
                borderCapStyle: 'round'
            },
            point: {
                radius: 6,
                hoverRadius: 8,
                backgroundColor: '#fff',
                borderWidth: 3
            }
        }
    };

    // Opciones mejoradas para gráfico de dona
    doughnutOptions = {
        plugins: {
            legend: {
                display: false, // Usamos nuestra propia leyenda
            },
            tooltip: {
                backgroundColor: 'rgba(0, 0, 0, 0.8)',
                titleColor: '#fff',
                bodyColor: '#fff',
                borderColor: 'rgba(102, 126, 234, 0.8)',
                borderWidth: 1,
                cornerRadius: 8,
                callbacks: {
                    label: function(context: any) {
                        const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
                        const percentage = Math.round((context.parsed * 100) / total);
                        return `${context.label}: ${context.parsed} (${percentage}%)`;
                    }
                }
            }
        },
        cutout: '70%',
        maintainAspectRatio: true,
        responsive: true,
        elements: {
            arc: {
                borderWidth: 0,
                hoverBorderWidth: 2,
                hoverBorderColor: '#fff'
            }
        },
        animation: {
            animateScale: true,
            animateRotate: true
        }
    };

    constructor(private dashboardService: DashboardService) { }

    ngOnInit(): void {
        this.loadDashboardData();
    }

    loadDashboardData(): void {
        this.dashboardService.getBirthsData().subscribe(
            (response: ResponseModel<DashboardData>) => {
                if (response && response.data) {
                    const data = response.data;
                    this.birthData = data.births;
                    this.totalCattle = data.totalCattle;
                    this.totalBirths = data.totalBirths;
                    this.totalEmployees = data.totalEmployees;
                    this.prepareChartsData();
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
        const months = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

        const maleCounts = Array(12).fill(0);
        const femaleCounts = Array(12).fill(0);

        this.birthData.forEach((data) => {
            const monthIndex = data.month - 1;
            maleCounts[monthIndex] = data.maleCount;
            femaleCounts[monthIndex] = data.femaleCount;
        });

        // Configuración mejorada para gráfico de líneas
        this.barChartData = {
            labels: months,
            datasets: [
                {
                    label: 'Nacimientos Machos',
                    data: maleCounts,
                    borderColor: '#42A5F5',
                    backgroundColor: 'rgba(66, 165, 245, 0.1)',
                    fill: true,
                    tension: 0.4,
                    pointBackgroundColor: '#42A5F5',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 6,
                    pointHoverRadius: 8
                },
                {
                    label: 'Nacimientos Hembras',
                    data: femaleCounts,
                    borderColor: '#66BB6A',
                    backgroundColor: 'rgba(102, 187, 106, 0.1)',
                    fill: true,
                    tension: 0.4,
                    pointBackgroundColor: '#66BB6A',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 6,
                    pointHoverRadius: 8
                },
            ],
        };

        const totalMale = maleCounts.reduce((sum, value) => sum + value, 0);
        const totalFemale = femaleCounts.reduce((sum, value) => sum + value, 0);

        // Configuración mejorada para gráfico de dona
        this.pieChartData = {
            labels: ['Nacimientos Machos', 'Nacimientos Hembras'],
            datasets: [
                {
                    data: [totalMale, totalFemale],
                    backgroundColor: [
                        'rgba(66, 165, 245, 0.8)',
                        'rgba(102, 187, 106, 0.8)'
                    ],
                    borderColor: [
                        '#42A5F5',
                        '#66BB6A'
                    ],
                    borderWidth: 0,
                    hoverBackgroundColor: [
                        'rgba(66, 165, 245, 0.9)',
                        'rgba(102, 187, 106, 0.9)'
                    ],
                    hoverBorderWidth: 2,
                    hoverBorderColor: '#fff'
                },
            ],
        };
    }

    // Métodos para estadísticas adicionales
    getBirthRate(): number {
        if (this.totalCattle === 0) return 0;
        return Math.round((this.totalBirths / this.totalCattle) * 100);
    }

    getMonthlyAverage(): string {
        const average = this.totalBirths / 12;
        return average.toFixed(1);
    }

    getMostProductiveMonth(): string {
        const months = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 
                       'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
        
        let maxBirths = 0;
        let mostProductiveMonth = '';

        this.birthData.forEach((data) => {
            const totalBirthsInMonth = data.maleCount + data.femaleCount;
            if (totalBirthsInMonth > maxBirths) {
                maxBirths = totalBirthsInMonth;
                mostProductiveMonth = months[data.month - 1];
            }
        });

        return mostProductiveMonth || 'N/A';
    }

    getGenderRatio(): string {
        const maleCounts = this.birthData.reduce((sum, data) => sum + data.maleCount, 0);
        const femaleCounts = this.birthData.reduce((sum, data) => sum + data.femaleCount, 0);

        if (maleCounts === 0) return '0:1';
        
        const ratio = femaleCounts / maleCounts;
        return `${ratio.toFixed(1)}:1`;
    }

    // Método para refrescar datos
    refreshData(): void {
        this.loadDashboardData();
    }
}
