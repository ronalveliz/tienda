import { Routes } from '@angular/router';
import { ProductFormComponent } from './product-form/product-form.component';
import { ProductoDetailComponent } from './product-detail/product-detail.component';
import { HomeComponent } from './home/home.component';
import { CarritoComponent } from './carrito/carrito.component';
import { UsuariosFormComponent } from './usuarios-form/usuarios-form.component';
import { LoginComponent } from './login/login.component';



export const routes: Routes = [
    {
        path:'',
        component: HomeComponent
    },
    {
        path:'productos/:id/detail',
        component: ProductoDetailComponent,

    },
    {
        path:'productos/create',
        component: ProductFormComponent,

    },
    {
        path:'productos/:id/update',
        component: ProductFormComponent,

    },
    {
        path: 'carrito',
        component: CarritoComponent,
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'register',
        component: UsuariosFormComponent
    }
]
