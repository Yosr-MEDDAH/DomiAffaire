import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import { Color, Label } from 'ng2-charts';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-reservation-chart',
  templateUrl: './reservation-chart.component.html',
  styleUrls: ['./reservation-chart.component.css']
})
export class ReservationChartComponent implements OnInit {
  public polarAreaChartOptions: ChartOptions = {
    responsive: true,
  };
  public polarAreaChartLabels: Label[] = [];
  public polarAreaChartData: number[] = [];
  public polarAreaChartType: ChartType = 'polarArea';
  public polarAreaChartLegend = true;
  public polarAreaChartColors: Color[] = [
    {
      backgroundColor: [
        'rgba(255,99,132,0.2)',
        'rgba(54,162,235,0.2)',
        'rgba(255,206,86,0.2)',
        'rgba(75,192,192,0.2)',
        'rgba(153,102,255,0.2)',
        'rgba(255,159,64,0.2)',
        'rgba(255,99,132,0.2)',
      ],
      borderColor: [
        'rgba(255,99,132,1)',
        'rgba(54,162,235,1)',
        'rgba(255,206,86,1)',
        'rgba(75,192,192,1)',
        'rgba(153,102,255,1)',
        'rgba(255,159,64,1)',
        'rgba(255,99,132,1)',
      ],
      borderWidth: 1,
    },
  ];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.reservationsEquipments();
  }

  reservationsEquipments() {
    this.adminService.reportingReservationEquipments().subscribe({
      next: (data: any) => {
        console.log(data);
        this.polarAreaChartLabels = data.map((item: any) => item.equipement);
        this.polarAreaChartData = data.map((item: any) => item.count);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}