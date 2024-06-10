import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  OnInit,
  ViewChildren,
  QueryList,
  AfterViewInit,
} from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
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
export class ChatComponent implements OnInit, AfterViewInit {
  @ViewChildren('messageItem') messageItems!: QueryList<any>;
  senderImage: any;
  receiverImage: any;
  chatId: any;
  messageInput: string = '';
  userId: string = '';
  messageList: any[] = [];
  userImage: any = '';
  chatsList: any;
  reloadOnNavigation = false;
  selectedFile: File | undefined;
  constructor(
    private chatService: ChatService,
    private authService: AuthServiceService,
    private clientService: ClientService,
    private accountantService: AccountantServiceService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        if (this.reloadOnNavigation) {
          this.reloadOnNavigation = false;
          window.location.reload();
        }
      });
  }
  ngOnInit(): void {
    this.chatId = this.route.snapshot.paramMap.get('id');
    this.getUserID();
    this.chatService.joinRoom(this.chatId);
    this.getChat(this.chatId);
  }

  ngAfterViewInit(): void {
    this.scrollToBottom();
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      if (
        this.messageItems &&
        this.messageItems.last &&
        this.messageItems.last.nativeElement
      ) {
        this.messageItems.last.nativeElement.scrollIntoView({
          behavior: 'smooth',
          block: 'end',
        });
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
      sentAt: [],
    } as ChatMessage;
    this.chatService.sendMessage(this.chatId, this.userId, chatMessage);
    this.messageInput = '';
  }
  onFileSelected(event: any) {
    const files = event.target.files;
    if (files.length > 0) {
      this.selectedFile = files[0];
    }
  }
  getImage(userId: string): string {
    return userId === this.userId
      ? `data:image/png;base64,${this.senderImage}`
      : `data:image/png;base64,${this.receiverImage}`;
  }
  listenForMessages() {
    const processedMessageIds: Set<string> = new Set();

    this.chatService
      .getMessageSubject()
      .subscribe((messages: ChatMessage[]) => {
        messages.forEach((message: ChatMessage) => {
          if (!processedMessageIds.has(message.messageId)) {
            const isSender = message.user === this.userId;
            this.messageList = [
              ...this.messageList,
              {
                ...message,
                sentAt: this.parseSentDate(message.sentAt),
                message_side: isSender ? 'right' : 'left',
                image: isSender ? this.senderImage : this.receiverImage,
              },
            ];
            processedMessageIds.add(message.messageId);
          }
        });
      });
  }

  getUserID() {
    const email = this.authService.getEmail();
    if (email) {
      if (this.authService.LoggedInUser()) {
        this.clientService.getClientData(email).subscribe({
          next: (data: any) => {
            this.userId = data.id;
            this.getAllMessages();
            this.getAllChats();
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
            this.getAllChats();
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
            sentAt: this.parseSentDate(item.sentAt),
            message_side: item.sender.id === this.userId ? 'right' : 'left',
            image:
              item.sender.id === this.userId
                ? this.senderImage
                : this.receiverImage,
          }));
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      });
    }
  }
  parseSentDate(dateTimeArray: any[]): Date {
    const [year, month, day, hour, minute, second, milliseconds] =
      dateTimeArray;
    return new Date(
      year,
      month - 1,
      day,
      hour,
      minute,
      second,
      milliseconds / 1000000
    );
  }
  getAllChats() {
    const email = this.authService.getEmail();
    if (email) {
      const chats = this.authService.LoggedInUser()
        ? this.chatService.getAllChatsAuthClient()
        : this.chatService.getAllChatsAuthAccountant();
      chats.subscribe({
        next: (data: any) => {
          this.chatsList = data;
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      });
    }
  }

  getChat(id: any) {
    this.chatService.getChat(id).subscribe({
      next: (data: any) => {
        if (data.client.id === this.userId) {
          this.receiverImage = data.accountant.image;
          this.senderImage = data.client.image;
        } else {
          this.senderImage = data.accountant.image;
          this.receiverImage = data.client.image;
        }
        this.chatId = id;
        this.chatService.joinRoom(this.chatId);
        this.getAllMessages();
        this.listenForMessages();
        this.reloadOnNavigation = true;
        this.router.navigate(['/chat', id]);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}
