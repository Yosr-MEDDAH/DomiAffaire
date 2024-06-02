import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.css']
})
export class CarouselComponent {
  @Input() items: { image: string }[] = [];
  currentIndex: number = 0;
  slideWidth: number = 0;

  ngOnInit() {
    this.slideWidth = window.innerWidth;
  }

  next() {
    if (this.currentIndex < this.items.length - 1) {
      this.currentIndex++;
    }
  }

  prev() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }
}