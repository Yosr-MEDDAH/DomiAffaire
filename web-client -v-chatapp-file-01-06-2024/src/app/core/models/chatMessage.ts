export interface ChatMessage{
    content: string;
    user: any;
    messageId:string;
    sentAt:any;
    fileContent?: Blob;
}