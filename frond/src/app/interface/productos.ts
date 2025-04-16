import { Category } from "./category";
export interface Producto{

    id: number;

    nombre: string;

    description: string;

    precio: number;

    photoUrl: string;

    descuento: boolean;

    published: boolean;

    category: Category;
    
    disponible: boolean;

}



