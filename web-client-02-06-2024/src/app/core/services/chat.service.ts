import { Injectable } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';
import { ChatMessage } from '../models/chatMessage';
import * as SockJS from 'sockjs-client';
import { JwtHelperService } from '@auth0/angular-jwt';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { AuthServiceService } from './auth.service';
import { ClientService } from './client.service';
import { AccountantServiceService } from './accountant-service.service';
@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private stompClient: any;
  private messageSubject: BehaviorSubject<ChatMessage[]> = new BehaviorSubject<
    ChatMessage[]
  >([]);
  helper = new JwtHelperService();
  messages: any;
  constructor(
    private http: HttpClient,
    private authService: AuthServiceService,
    private clientService: ClientService,
    private accountantService: AccountantServiceService
  ) {
    this.initConnectionSocket();
  }
  private getHeaders() {
    let token = sessionStorage.getItem('token');
    return token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();
  }
  initConnectionSocket() {
    const url = `${environment.socketIo}chat-socket`;
    const socket = new SockJS(url);
    this.stompClient = Stomp.over(socket);
  }

  joinRoom(roomId: any) {
    this.stompClient.connect({}, () => {
      this.stompClient.subscribe(`/topic/${roomId}`, (messages: any) => {
        console.log(messages)
        const messageContent = JSON.parse(messages.body);
        console.log(messageContent)
        const currentMessage = this.messageSubject.getValue();
        console.log(currentMessage)
        currentMessage.push(messageContent);
        console.log(currentMessage)
        this.messageSubject.next(currentMessage);
      });
    });
  }

  getAllChatsAuthClient() {
    return this.http.get(
      `${environment.urlBackend}api/clients/all-chats`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getAllChatsAuthAccountant() {
    return this.http.get(
      `${environment.urlBackend}api/accountants/all-chats`,
      {
        headers: this.getHeaders(),
      }
    );
  }

  sendMessage(roomId: any, userId: any, chatMessage: ChatMessage) {

    this.stompClient.send(
      `/app/chat/${roomId}`,
      { userId: userId },
      JSON.stringify(chatMessage)
    );
  }

  convertDateToArray(dateString: any): number[] {
    const date = new Date(dateString);
    const dateArray: number[] = [
        date.getFullYear(),
        date.getMonth() + 1, // Months are zero-based, so add 1
        date.getDate(),
        date.getHours(),
        date.getMinutes(),
        date.getSeconds(),
        date.getMilliseconds()
    ];
    return dateArray;
}
  getMessageSubject() {
    return this.messageSubject.asObservable();
  }


  getClientMessages(id: any) {
    return this.http.get(
      `${environment.urlBackend}api/clients/chat/messages/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getAccountantMessages(id: any) {
    return this.http.get(
      `${environment.urlBackend}api/accountants/chat/messages/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }
  getChat(id: any) {
    return this.http.get(
      `${environment.urlBackend}api/users/all-chats/${id}`,
      {
        headers: this.getHeaders(),
      }
    );
  }

  // getChatMessages(id: any) {
  //   this.getChatById(id).subscribe({
  //     next: (data: any) => {
  //       console.log(data);
  //       this.messages = data[0].messages;
  //     },
  //     error: (err: HttpErrorResponse) => {
  //       console.log(err);
  //     },
  //   });
  // }
}
