import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import { Label } from 'ng2-charts';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-deadline-chart',
  templateUrl: './deadline-chart.component.html',
  styleUrls: ['./deadline-chart.component.css']
})
export class DeadlineChartComponent implements OnInit {
  public lineChartOptions: ChartOptions = {
    responsive: true,
  };
  public lineChartLabels: Label[] = [];
  public lineChartType: ChartType = 'line';
  public lineChartLegend = true;
  public lineChartPlugins = [];
  public lineChartData: any[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.deadlines();
  }

  deadlines() {
    this.adminService.reportingDeadlines().subscribe({
      next: (data: any) => {
        console.log(data);
        this.lineChartLabels = data.map((item: any) => item.delai);
        this.lineChartData = [
          {
            data: data.map((item: any) => item.count),
            label: "Nombre d'entreprises",
          },
        ];
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}