import {
  HttpClient,
  HttpClientModule,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from 'src/environments/environment';

import { Stomp } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';
import { ChatMessage } from '../models/chatMessage';
import * as SockJS from 'sockjs-client';
@Injectable({
  providedIn: 'root',
})
export class ChatService {
  // headerAll: any;
  // helper = new JwtHelperService();
  // private stompClient: any;
  // private messageSubject: BehaviorSubject<ChatMessage[]> = new BehaviorSubject<
  //   ChatMessage[]
  // >([]);

  // constructor(private http: HttpClient) {
  //   let token = sessionStorage.getItem('token');
  //   if (token !== null) {
  //     this.headerAll = new HttpHeaders({ Authorization: `Bearer ${token}` });
  //   }
  //   this.initConnectionSocket();
  // }
  // initConnectionSocket() {
  //   const url = '//localhost:8080/chat-socket';
  //   const socket = new SockJS(url);
  //   this.stompClient = Stomp.over(socket);
  // }
  // joinRoom(chatId: string) {
  //   this.getChatMessages(chatId); 
  //   this.stompClient.connect({}, () => {
  //     this.stompClient.subscribe(`/topic/${chatId}`, (messages: any) => {
  //       messages=this.messages;
  //       const messageContent = JSON.parse(messages.body);
  //       const currentMessage = this.messageSubject.getValue();
  //       currentMessage.push(messageContent);

  //       this.messageSubject.next(currentMessage);
  //     });
  //   });
  // }

  // sendMessage(chatId: string, chatMessage: ChatMessage) {
  //   this.stompClient.send(
  //     `/app/chat/${chatId}`,
  //     {},
  //     JSON.stringify(chatMessage)
  //   );
  // }

  // getMessageSubject() {
  //   return this.messageSubject.asObservable();
  // }
  // getAllChats() {
  //   return this.http.get(`${environment.urlBackend}api/clients/all-chats`, {
  //     headers: this.headerAll,
  //   });
  // }
  // getChatById(id: any) {
  //   return this.http.get(
  //     `${environment.urlBackend}api/clients/all-chats/${id}`,
  //     {
  //       headers: this.headerAll,
  //     }
  //   );
  // }
  // messages:any
  // getChatMessages(id:any){
  //   this.getChatById(id).subscribe({
  //     next:(data:any)=>{console.log(data); this.messages=data[0].messages;},
  //     error:(err:HttpErrorResponse)=>{console.log(err)}
  //   })
  // }
}
