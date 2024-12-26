import { Role } from "./role";


export interface User { 
    id:number;
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    phone: string;
    role: Role;
    PhotoUrl: string;
}