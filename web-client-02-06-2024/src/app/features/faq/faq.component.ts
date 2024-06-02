import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/core/services/client.service';

@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html',
  styleUrls: ['./faq.component.css']
})
export class FaqComponent implements OnInit {
  faq:any;
  constructor(private clientService:ClientService){}
  ngOnInit(): void {
    this.getAllfaq();
  }
  getAllfaq() {
    this.clientService.getAllFAQ().subscribe({
      next: (data: any) => {
        console.log(data);
        this.faq = data;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  toggleAccordion(id: any): void {
    const element = document.getElementById('collapse' + id);
    element?.parentElement?.classList.toggle('active');
  }

}
