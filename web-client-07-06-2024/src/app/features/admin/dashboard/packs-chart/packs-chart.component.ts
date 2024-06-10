import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import { Color, Label } from 'ng2-charts';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-packs-chart',
  templateUrl: './packs-chart.component.html',
  styleUrls: ['./packs-chart.component.css'],
})
export class PacksChartComponent implements OnInit {
  public pieChartOptions: ChartOptions = {
    responsive: true,
  };
  public pieChartLabels: Label[] = [];
  public pieChartData: number[] = [];
  public pieChartType: ChartType = 'pie';
  public pieChartLegend = true;
  public pieChartPlugins = [];
  public pieChartColors: Color[] = [
    {
      backgroundColor: [
        'rgba(255, 99, 132, 0.6)',
        'rgba(54, 162, 235, 0.6)',
        'rgba(255, 206, 86, 0.6)',
        'rgba(75, 192, 192, 0.6)',
        'rgba(153, 102, 255, 0.6)',
        'rgba(255, 159, 64, 0.6)',
      ],
    },
  ];
  constructor(private adminService: AdminService) {}
  ngOnInit(): void {
    this.packs();
  }
  packs() {
    this.adminService.reportingPackCount().subscribe({
      next: (data: any) => {
        console.log(data);
        this.pieChartLabels = data.map((pack: any) => pack.pack);
        this.pieChartData = data.map((pack: any) => pack.count);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}
