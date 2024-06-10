import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-capitaux-chart',
  templateUrl: './capitaux-chart.component.html',
  styleUrls: ['./capitaux-chart.component.css'],
})
export class CapitauxChartComponent implements OnInit {
  public barChartLegend = true;
  public barChartPlugins = [];
  public barChartOptions: any = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      yAxes: [
        {
          ticks: {
            min: 0, // définir la valeur minimale ici
          },
        },
      ],
    },
  };
  public barChartLabels: string[] = [];
  public barChartType = 'bar';
  public barChartData: any[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.capitaux();
  }
  capitaux() {
    this.adminService.reportingCapiteaux().subscribe({
      next: (data: any) => {
        console.log('Data from backend:', data);

        console.log(
          'Range values:',
          data.map((item: any) => item.range)
        );

        this.barChartLabels = data.map((item: any) => item.range);
        this.barChartData = [
          {
            data: data.map((item: any) => item.count),
            label: "Nombre d'entreprises",
          },
        ];

        console.log('Mapped labels:', this.barChartLabels);
        console.log('Mapped data:', this.barChartData);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}
