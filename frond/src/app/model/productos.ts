
// Definimos la interfaz
export interface Producto{
   
    id: number;

    name: string;

    description: string;

    price: number;

    photoUrl: string;

    descuento: boolean;

    category: {
        id: number;
        name: string;
    };

    store: {
        id: number;
        name: string;
        location: string;
    };
}



