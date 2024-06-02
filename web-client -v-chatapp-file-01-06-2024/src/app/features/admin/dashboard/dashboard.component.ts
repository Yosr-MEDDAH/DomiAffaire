import { Component, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import * as moment from 'moment';
import { Label } from 'ng2-charts';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent  implements OnInit{
  public barChartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    legend: {
      display: false,
    },
    animation: {
      duration: 0,
    },
      scales: {
          xAxes: [{
             stacked: false,
                ticks: {
                  beginAtZero:false,
                  callback: value => {
                    let date = moment(value);
                    return date.format('HH:mm:ss');
                  }
              }
          }],
          yAxes: [{
            stacked: true,
              ticks: {
                  beginAtZero:false
              }
          }]
      },
      plugins: {
          zoom: {
              pan: {
                  enabled: true,
                  mode: 'x'
              },
              zoom: {
                  enabled: true,
                  mode: 'x',
              }
          }
      }
};
public barChartLabels: Label[] = ["Group 1", "Group 2", "Group 3", "Group 4"];
public barChartType: ChartType = 'horizontalBar';
public barChartLegend = true;
public barChartPlugins = [];

public barChartData: ChartDataSets[] = [
        {
          label: 'Series A',
          data: [
            [ 1598943916000, 1598944216000 ],
            [ 1598947816000, 1598948176000 ],
            undefined,
            [ 1598946916000, 1598948476000 ],
          ],
          borderWidth: 1,
          borderColor: [
            "rgba(0,158,155,1)",
            "rgba(213,94,0,1)",
            "rgba(0,158,155,1)"
          ],
          backgroundColor: [
            "rgba(0,158,155,0.5)",
            "rgba(213,94,0,0.5)",
            "rgba(0,158,155,0.5)"
          ],
        },
        {
          label: 'Series B',
          data: [
            [ 1598944216000, 1598944516000 ],
            [ 1598948176000, 1598948656000 ],
            [ 1598945596000, 1598946496000 ],
            [ 1598947816000, 1598949616000 ],   
          ],
          borderWidth: 1,
          borderColor: [
            "rgba(230,159,0,1)",
            "rgba(180,180,180,1)",
            "rgba(213,94,0,1)",
            "rgba(213,94,0,1)"
          ],
          backgroundColor: [
            "rgba(230,159,0,0.5)",
            "rgba(180,180,180,0.5)",
            "rgba(213,94,0,0.5)",
            "rgba(213,94,0,0.5)"
          ],
        },
        {
          label: 'Series B',
          data: [
            [ 1598944516000, 1598945116000 ],
            [ 1598948656000, 1598949376000 ],
            [ 1598946496000, 1598948116000 ],
            [ 1598949616000, 1598950276000 ],   
          ],
          borderWidth: 1,
          borderColor: [
            "rgba(0,158,155,1)",
            "rgba(213,94,0,1)",
            "rgba(230,159,0,1)",
            "rgba(0,158,155,1)"
          ],
          backgroundColor: [
            "rgba(0,158,155,0.5)",
            "rgba(213,94,0,0.5)",
            "rgba(230,159,0,0.5)",
            "rgba(0,158,155,0.5)"
          ],
        }
      ];

constructor() { }

ngOnInit() {
}
}
