import { Category } from "./category";
import { Store } from "./store";

// Definimos la interfaz
export interface Producto{
   
    id: number;

    name: string;

    description: string;

    price: number;

    photoUrl: string;

    descuento: boolean;

    category: Category;

    store: Store;
}



