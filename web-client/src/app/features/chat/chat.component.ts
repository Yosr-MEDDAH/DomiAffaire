import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit,ViewChildren, QueryList, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChatMessage } from 'src/app/core/models/chatMessage';
import { AccountantServiceService } from 'src/app/core/services/accountant-service.service';
import { AuthServiceService } from 'src/app/core/services/auth.service';
import { ChatService } from 'src/app/core/services/chat.service';
import { ClientService } from 'src/app/core/services/client.service';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit , AfterViewInit{
  @ViewChildren('messageItem') messageItems!: QueryList<any>;

  chatId: any;
  messageInput: string = '';
  userId: string = '';
  messageList: any[] = [];
  userImage: any = '';
  chatsList:any
  constructor(
    private chatService: ChatService,
    private authService: AuthServiceService,
    private clientService: ClientService,
    private accountantService: AccountantServiceService,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.chatId = this.route.snapshot.paramMap.get('id');
    this.getUserID();
    this.chatService.joinRoom(this.chatId);
    this.listenForMessages();
    // this.getAllMessages();
    
  }

  ngAfterViewInit(): void {
    this.scrollToBottom();
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      if (this.messageItems && this.messageItems.last && this.messageItems.last.nativeElement) {
        this.messageItems.last.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'end' });
      }
    } catch (err) {
      console.error(err);
    }
  }
  sendMessage() {
     const chatMessage = {
      content: this.messageInput,
      user: this.userId,
      messageId: '',
    } as ChatMessage;
    this.chatService.sendMessage(this.chatId, this.userId, chatMessage);
    this.messageInput = '';
  }

  // listenForMessages() {
  //   this.chatService.getMessageSubject().subscribe((messages: ChatMessage[]) => {
  //     this.messageList = [...this.messageList, ...messages.map((item: ChatMessage) => ({
  //       ...item,
  //       message_side: item.user === this.userId ? 'right' : 'left',
  //     }))];
  //     console.log(this.messageList)
  //   });
  // }
  listenForMessages() {
    const processedMessageIds: Set<string> = new Set();
  
    this.chatService.getMessageSubject().subscribe((messages: ChatMessage[]) => {
      messages.forEach((message: ChatMessage) => {
        // Check if the message has already been processed
        if (!processedMessageIds.has(message.messageId)) {
          this.messageList = [
            ...this.messageList,
            {
              ...message,
              message_side: message.user === this.userId ? 'right' : 'left',
            }
          ];
          // Add the message ID to the set of processed IDs
          processedMessageIds.add(message.messageId);
        }
      });
      console.log(this.messageList);
    });
  }
  
  // listenForMessages() {
  //   this.chatService.getMessageSubject().subscribe((messages: ChatMessage[]) => {
  //     // Iterate through new messages
  //     for (const newMessage of messages) {
  //       // Check if the message already exists in the messageList
  //       const exists = this.messageList.some((message: ChatMessage) => message.user === newMessage.user);
  //       // If the message doesn't exist, append it to the messageList
  //       if (!exists) {
  //         this.messageList.push({
  //           ...newMessage,
  //           message_side: newMessage.user === this.userId ? 'right' : 'left',
  //         });
  //       }
  //     }
  //   });
  // }
  
  
  

  getUserID() {
    const email = this.authService.getEmail();
    if (email) {
      if (this.authService.LoggedInUser()) {
        this.clientService.getClientData(email).subscribe({
          next: (data: any) => {
            this.userId = data.id;
            this.getAllMessages();
            this.getAllChats()
          },
          error: (err) => {
            console.error('Error getting client data', err);
          },
        });
      } else if (this.authService.LoggedInAccountant()) {
        this.accountantService.getAccountantData(email).subscribe({
          next: (data: any) => {
            this.userId = data.id;
            this.getAllMessages();
            this.getAllChats()
          },
          error: (err) => {
            console.error('Error getting accountant data', err);
          },
        });
      }
    }
  }


getAllMessages() {
  const email = this.authService.getEmail();
  if (email) {
    const messageObservable = this.authService.LoggedInUser()
      ? this.chatService.getClientMessages(this.chatId)
      : this.chatService.getAccountantMessages(this.chatId);

    messageObservable?.subscribe({
      next: (data: any) => {
        this.messageList = data.map((item: any) => ({
          ...item,
          message_side: item.sender.id === this.userId ? 'right' : 'left',
        }));
        console.log(this.messageList);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}
getAllChats(){
  const email = this.authService.getEmail();
  if (email) {
    const chats= this.authService.LoggedInUser()
      ? this.chatService.getAllChatsAuthClient()
      : this.chatService.getAllChatsAuthAccountant();
      chats.subscribe({
        next: (data: any) => {
          this.chatsList=data;
          console.log(this.chatsList);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      })
  }
}


}
