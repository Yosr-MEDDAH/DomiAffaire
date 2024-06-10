export class Client {
  id: string;
  email: string;
  pwd: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  birthDate: Date;
  image:any;
  role: string;

  constructor(
    id: string = '',
    email: string = '',
    pwd: string = '',
    firstName: string = '',
    lastName: string = '',
    phoneNumber: string = '',
    birthDate: Date = new Date(),
    image: any='',
    role: string = ''
  ) {
    this.id = id;
    this.email = email;
    this.pwd = pwd;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.birthDate = birthDate;
    this.image=image;
    this.role = role;
  }
}
