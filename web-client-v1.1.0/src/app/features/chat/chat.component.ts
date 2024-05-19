import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChatMessage } from 'src/app/core/models/chatMessage';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ChatService } from 'src/app/core/services/chat.service';
import { ClientService } from 'src/app/core/services/client.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit {
  // userId: any;
  // messageInput: string='';
  // messageList:any
  constructor(
    private chatService: ChatService,
    private authService: AuthServiceService,
    private clientService: ClientService
  ) {}
  ngOnInit(): void {
    // this.getAllChats();
  }
//   getAllChats() {
//     this.chatService.getAllChats().subscribe({
//       next: (data: any) => {
//         console.log(data);
//       },
//       error: (err: HttpErrorResponse) => {
//         console.log(err);
//       },
//     });
//   }
//   getUserID() {
//     const email = this.authService.getEmail();
//     this.clientService.getClientData(email).subscribe({
//       next: (data: any) => {
//         console.log(data);
//         this.userId = data.id;
//       },
//       error: (err: HttpErrorResponse) => {
//         console.log(err);
//       },
//     });
//   }
//   sendMessage(){
//     const chatMessage = {
//       message: this.messageInput,
//       user:this.userId
//     }as ChatMessage
//     this.chatService.sendMessage("6630e47cf9333f6000286acb",chatMessage)
//     this.messageInput='';
//   }
//   lisenerMessage(){
//     this.chatService.getMessageSubject().subscribe((messages:any)=>{
//       this.messageList = messages.map((item : any)=>({
//         ...item,
//         message_side: item.user === this.userId ? 'sender' : 'receiver'

//       }));
  
//   });

// }
}
