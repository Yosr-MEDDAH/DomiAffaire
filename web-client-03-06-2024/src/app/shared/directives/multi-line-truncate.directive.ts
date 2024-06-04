import { Directive, ElementRef, AfterViewInit } from '@angular/core';

@Directive({
  selector: '[appMultiLineTruncate]'
})
export class MultiLineTruncateDirective implements AfterViewInit {

  constructor(private elementRef: ElementRef) {}

  ngAfterViewInit(): void {
    const element = this.elementRef.nativeElement;
    const lineHeight = parseInt(window.getComputedStyle(element).lineHeight);

    const maxLines = Math.floor(element.clientHeight / lineHeight);

    if (maxLines <= 1) {
      return; // If there's only one line, no need to truncate
    }

    const originalHTML = element.innerHTML;
    let truncatedHTML = originalHTML;

    while (element.clientHeight > element.scrollHeight) {
      const lastSpaceIndex = truncatedHTML.lastIndexOf(' ');
      if (lastSpaceIndex === -1) break;
      
      truncatedHTML = truncatedHTML.slice(0, lastSpaceIndex).trim();
      element.innerHTML = truncatedHTML + '...';
    }
  }
}
