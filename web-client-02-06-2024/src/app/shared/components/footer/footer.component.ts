import { Component, OnInit, Renderer2 } from '@angular/core';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
})
export class FooterComponent implements OnInit {
  content: string = '';
  constructor(
    private darkModeService: DarkModeService,
    private renderer: Renderer2
  ) {}
  ngOnInit(): void {
    this.loadScripts();
  }
  sendMail() {
    const mailtoLink = `mailto:info@domi-affaire.com?body=${encodeURIComponent(
      this.content
    )}`;
    window.location.href = mailtoLink;
  }
  loadScripts() {
    const scripts = [
      'https://code.jquery.com/jquery-3.5.1.slim.min.js',
      'https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js',
      'https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js',
      '../../../assets/home/vendor/jquery/jquery.min.js',
      '../../../assets/home/vendor/bootstrap/js/bootstrap.bundle.min.js',
      '../../../assets/home/assets/js/owl-carousel.js',
      '../../../assets/home/assets/js/animation.js',
      '../../../assets/home/assets/js/imagesloaded.js',
      '../../../assets/home/assets/js/popup.js',
      '../../../assets/home/assets/js/custom.js',
    ];

    for (let script of scripts) {
      const scriptElement = this.renderer.createElement('script');
      scriptElement.src = script;
      this.renderer.appendChild(document.body, scriptElement);
    }
  }
  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
}
