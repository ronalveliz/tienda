import { Routes } from '@angular/router';
import { ProductFormComponent } from './product-form/product-form.component';
import { ProductoDetailComponent } from './product-detail/product-detail.component';
import { HomeComponent } from './home/home.component';
import { ProductosComponent } from './product-list/product-list.component';
import { CarritoComponent } from './carrito/carrito.component';
import { LoginComponent } from './login/login.component';
import { UsuariosFormComponent } from './usuarios-form/usuarios-form.component';
import { AccountFormComponent } from './account-form/account-form.component';
import { userLoggedIntGuard } from './authentication/user-logged-int.guard';


export const routes: Routes = [
    {
        path:'',
        component: HomeComponent
    },
    {
        path:'productos',
        component: ProductosComponent,
        canActivate: [userLoggedIntGuard]
    },
    {
        path:'productos/:id/detail',
        component: ProductoDetailComponent,
        canActivate: [userLoggedIntGuard]
    },
    {
        path:'productos/create',
        component: ProductFormComponent,
        canActivate: [userLoggedIntGuard]
    },
    {
        path:'productos/:id/update',
        component: ProductFormComponent,
        canActivate: [userLoggedIntGuard]
    },
    {
        path: 'carrito',
        component: CarritoComponent,
        canActivate: [userLoggedIntGuard]
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'register',
       component: UsuariosFormComponent
    },
    {
        path: 'account',
        component: AccountFormComponent,
        canActivate: [userLoggedIntGuard]

    }

];
