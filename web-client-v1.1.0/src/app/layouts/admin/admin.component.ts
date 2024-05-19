import { Component } from '@angular/core';
import { AuthServiceService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent {
constructor(private authService:AuthServiceService){}
logout(){
  this.authService.logout();
}
}
