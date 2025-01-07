import { Category } from "./category";
export interface Producto{
   
    id: number;

    name: string;

    description: string;

    price: number;

    photoUrl: string;

    descuento: boolean;

    category: Category;

}



