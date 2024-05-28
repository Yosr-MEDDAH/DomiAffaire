import { Component, OnInit, OnDestroy } from '@angular/core';
// import { ConnectionService } from 'ng-connection-service';
@Component({
  selector: 'app-connection-status',
  template: ` <div class="offline-indicator">
    You are offline. Please check your internet connection.
  </div>`,
  styles: [
    `
      .offline-indicator {
        background-color: #f8d7da;
        color: #721c24;
        padding: 10px;
        border: solid 1px #f5c6cb;
        border-radius: 5px;
        margin: 10px;
      }
    `,
  ],
})
export class ConnectionStatusComponent {
  // isOnline: boolean = navigator.onLine;
  // constructor(){console.log(this.isOnline)}
  // title = 'internet-connection-check';
  // status = 'ONLINE';
  // isConnected: any;
  // constructor(private connectionService: ConnectionService) {
  //   this.connectionService.monitor().subscribe((isConnected) => {
  //     this.isConnected = isConnected;
  //     if(this.isConnected){this.status="ONLINE";}
  //     else{this.status="OFFLINE";}
  //     alert(this.status);
  //   });
  // }
}
