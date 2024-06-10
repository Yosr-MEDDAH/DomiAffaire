import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { gantt } from 'dhtmlx-gantt';
@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'app-deadlines',
  templateUrl: './deadlines.component.html',
  styleUrls: ['./deadlines.component.css'],
})
export class DeadlinesComponent implements OnInit {
  @ViewChild('gantt_here', { static: true }) ganttContainer!: ElementRef;

  deadlines: any[] = [];
  constructor(private adminService: AdminService) {}
  ngOnInit(): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const startOfDay = new Date(Date.UTC(today.getFullYear(), today.getMonth(), today.getDate()));
    gantt.config.start_date = startOfDay;
    gantt.config.date_format = '%Y-%m-%d %H:%i';

    gantt.config.columns = [
      { name: 'text', label: 'Client', width: '*'},
      { name: 'duration', label: 'Restant', align: 'center' },
      { name: 'limited_date', label: 'Limite', width: '*' },
      { name: 'progress', label: 'impayés', align: 'center' },
      { name: 'amount', label: 'Montant', align: 'center' },
    ];
    gantt.init(this.ganttContainer.nativeElement);

    if (!(gantt as any).$_initOnce) {
      (gantt as any).$_initOnce = true;
    }
    this.getDeadlines();
  }

  getDeadlines() {
    this.adminService.getDeadlinesBetweenTwoWeeks().subscribe({
      next: (data: any) => {
        this.deadlines = data.map((item: any) => {
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          const startOfDay = new Date(today.getFullYear(), today.getMonth(), today.getDate());
          const limitedDate = this.arrayToDate(item.limitedDate);
          const duration = this.calculateDuration(today, limitedDate);
          let color = '#FF7733'; 
        
        if (duration <= 5) {
          color = '#FF3933'; 
        }
          return {
            text: item.client.firstName,
            start_date: startOfDay.toISOString().slice(0, 16).replace('T', ' '),
            duration: duration,
            limited_date: limitedDate.toISOString().slice(0, 16).replace('T', ' '),
            progress: item.counterOfNotPaidPeriods,
            amount: item.netPayable,
            readonly: true,
            color: color
          };
        });
        gantt.parse({ data: this.deadlines });
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  arrayToDate(dateTimeArray: any[]): Date {
    const [year, month, day, hour, minute, second, milliseconds] =
      dateTimeArray;
    return new Date(
      year,
      month - 1,
      day,
      hour,
      minute,
      second,
      milliseconds / 1000000
    );
  }


  calculateDuration(startDate: Date, endDate: Date): number {
    const oneDay = 24 * 60 * 60 * 1000; 
    const duration = Math.round((endDate.getTime() - startDate.getTime()) / oneDay);
    return duration;
  }
}
