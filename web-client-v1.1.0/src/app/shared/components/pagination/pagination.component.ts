import { Component } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent {
  totalItems!: number; // Total number of items
  itemsPerPage: number = 10; // Number of items per page
  currentPage: number = 1; // Current page
  pages: number[] = []; // Array to store page numbers

  constructor() { }

  ngOnInit(): void {
    // Calculate total number of pages
    this.calculatePages();
  }

  calculatePages() {
    this.pages = [];
    const totalPages = Math.ceil(this.totalItems / this.itemsPerPage);
    for (let i = 1; i <= totalPages; i++) {
      this.pages.push(i);
    }
  }

  nextPage() {
    if (this.currentPage < this.pages.length) {
      this.currentPage++;
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  goToPage(page: number) {
    this.currentPage = page;
  }
}