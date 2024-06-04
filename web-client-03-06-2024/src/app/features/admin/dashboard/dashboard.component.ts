import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import * as moment from 'moment';
import { Label } from 'ng2-charts';
import { AdminService } from 'src/app/core/services/admin.service';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent  {
  public barChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      xAxes: [{
        type: 'time',
        time: {
            unit: 'day',
            displayFormats: {
              day: 'DD-MM-YYYY', // Format for displaying dates
            },
        }
      }],
    },
  };

  public barChartLabels: Label[] = ['02-06-2024', '03-06-2024', '04-06-2024', '05-06-2024', '06-06-2024', '07-06-2024'];
  public barChartData: ChartDataSets[] = [
    {
      data: [
        { x: new Date('2024-06-02'), y: 10 },
        { x: new Date('2024-06-03'), y: 15 },
        { x: new Date('2024-06-04'), y: 20 },
        { x: new Date('2024-06-05'), y: 25 },
        { x: new Date('2024-06-06'), y: 30 },
        { x: new Date('2024-06-07'), y: 35 },
      ],
      label: 'My Data',
    },
  ];
}
//   public barChartOptions: ChartOptions = {
//     responsive: true,
//     maintainAspectRatio: true,
//     legend: {
//       display: false,
//       // position: 'top',
//       // align: 'end',
//       // labels: {
//       //   fontColor: 'blue',
//       //   fontSize: 14,
//       //   fontFamily: 'Arial',
//       //   fontStyle: 'italic',
//       // },
//     },
//     animation: {
//       duration: 0,
//     },
//     scales: {
//       xAxes: [
//         {
//           stacked: false,
//           ticks: {
//             beginAtZero: false,
//             callback: (value, index, values) => {
//               let startDate = moment(); // Get today's date
//               let date = startDate.clone().add(index, 'days'); // Add index days to today's date
//               if (index === 0 || index === values.length - 1) {
//                 // Display the full date for the first and last ticks
//                 return date.format('MMM DD, YYYY');
//               } else {
//                 // Display only the date for other ticks
//                 return date.format('DD');
//               }
//             },
//           },
//         },
//       ],
//       yAxes: [
//         {
//           stacked: false,
//           ticks: {
//             beginAtZero: false,
//           },
//         },
//       ],
//     },
//     plugins: {
//       zoom: {
//         pan: {
//           enabled: true,
//           mode: 'x',
//         },
//         zoom: {
//           enabled: true,
//           mode: 'x',
//         },
//       },
//     },
//   };
//   public barChartLabels: Label[] = [];
//   public barChartData: ChartDataSets[] = [];

//   public barChartType: ChartType = 'horizontalBar';
//   public barChartLegend = true;
//   public barChartPlugins = [];

//   constructor(private adminService: AdminService) {}

//   ngOnInit() {
//     this.getDefaultDeadlines();
//   }
//   getDefaultDeadlines() {
//     this.adminService.getDeadlinesBetweenTwoWeeks().subscribe({
//       next: (data: any[]) => {
//         const uniqueEmails: Set<string> = new Set(); // To store unique user emails
//         const datasets: ChartDataSets[] = [];
//         this.barChartData = [];
//         data.forEach((user: any, index: number) => {
//           // console.log(index);
//           const email = user.client.email;
//           const limitedDate = moment(
//             this.parseDateArray(user.limitedDate)
//           ).valueOf();
          

//           if (!uniqueEmails.has(email)) {
//             uniqueEmails.add(email);
//             const dataset: ChartDataSets = {
//               label: email,
//               data: [ { x: moment().valueOf(), y: index },
//                 { x: limitedDate, y: datasets.length },
//                 { x: limitedDate, y: index }
//               ],
//               backgroundColor: 'rgba(0, 158, 155, 0.5)', 
//               borderColor: 'rgba(0, 158, 155, 1)',
//               borderWidth: 1,
//             };
//             datasets.push(dataset);
//           }
//         });
//        this.barChartLabels = Array.from(uniqueEmails);
//       this.barChartData = datasets;
//       },
//       error: (err: HttpErrorResponse) => {
//         console.log(err);
//       },
//     });
//   }
//   parseDateArray(dateArray: number[]): Date {
//     const [year, month, day, hour, minute, second, milliseconds] = dateArray;
//     return new Date(
//       year,
//       month - 1,
//       day,
//       hour,
//       minute,
//       second,
//       milliseconds / 1000000
//     );
//   }
// }
