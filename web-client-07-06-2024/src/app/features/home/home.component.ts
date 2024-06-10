import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, Renderer2 } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DarkModeService } from 'src/app/core/services/dark-mode.service';
import { VisitorsService } from 'src/app/core/services/visitors.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  carouselItems = [
    { image: 'assets/dist/images/IMG-20230705-WA0007.jpg' },
    { image: 'assets/dist/images/IMG-20230705-WA0014.jpg' },
    { image: 'assets/dist/images/IMG-20230705-WA0028.jpg' },
    { image: 'assets/dist/images/IMG-20230705-WA0029.jpg' },
    { image: 'assets/dist/images/IMG-20230705-WA0032.jpg' },
  ];
  packs: any;

  constructor(
    private renderer: Renderer2,
    private translate: TranslateService,
    private darkModeService: DarkModeService,
    private visitorService: VisitorsService
  ) {}

  ngOnInit() {
    this.loadScripts();
    this.getPacks();
    this.translate.setDefaultLang('en');
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
  switchLanguage(language: string) {
    this.translate.use(language);
  }

  isDarkModeEnabled() {
    return this.darkModeService.isDarkModeEnabled();
  }
  getPacks() {
    this.visitorService.getPacks().subscribe({
      next: (data: any) => {
        console.log(data);
        this.packs = data.map((pack: any) => {
          return {
            ...pack,
            descriptionList: pack.description
              .split(/[\n.]+/) // Split by new lines or periods followed by space or newline
              .map((item: string) => item.trim())
              .filter((item: string) => item),
          };
        });
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
  
}
