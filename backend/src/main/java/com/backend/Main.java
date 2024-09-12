package com.backend;

import com.backend.model.*;
import com.backend.repository.*;
import com.backend.repository.StoreRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


import java.util.List;


@SpringBootApplication
public class Main {

	public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Main.class, args);

        ShoppingListRepository repoShopList = context.getBean(ShoppingListRepository.class);
        UserRepository userRepo = context.getBean(UserRepository.class);
        ProductoRepository repoProducto = context.getBean(ProductoRepository.class);
        StoreRepository repoStore = context.getBean(StoreRepository.class);
        OrderssRepository repoOrderss = context.getBean(OrderssRepository.class);

                // borrado de tablas
            repoShopList.deleteAll();
            repoOrderss.deleteAll();
            repoProducto.deleteAll();
            userRepo.deleteAll();
            repoStore.deleteAll();


            // Crear y guardar usuarios
            User u1 = new User(null, "user1", "user1", "user1@gmail.com", "xxxx", "640364015");
            User u2 = new User(null, "user2", "user2", "user2@gmail.com", "xxxx", "640364015");
            User u3 = new User(null, "user3", "user3", "user3@gmail.com", "xxxx", "640364015");
            userRepo.saveAll(List.of(u1, u2, u3));

            // Crear y guardar tiendas
            Store store1 = new Store(null, "Tienda A", "Calle Principal 123");
            Store store2 = new Store(null, "Tienda B", "Avenida Secundaria 456");
            Store store3 = new Store(null, "Tienda C", "Plaza Central 789");
            repoStore.saveAll(List.of(store1, store2, store3));

            // Crear y guardar productos
            Productos p1 = new Productos(null, "Producto 1", "Descripción del producto 1", 50.00, "img", store1);
            Productos p2 = new Productos(null, "Producto 2", "Descripción del producto 2", 75.00, "img", store2);
            Productos p3 = new Productos(null, "Producto 3", "Descripción del producto 3", 100.00, "img", store3);
            repoProducto.saveAll(List.of(p1, p2, p3));

            // Crear y guardar pedidos
            Orderss order1 = new Orderss(null,store1,u1 );
            Orderss order2 = new Orderss(null,store1,u1);
            Orderss order3 = new Orderss(null,store1,u1);
            repoOrderss.saveAll(List.of(order1, order2, order3));

            // Crear y guardar listas de compras
            ShoppingList sl1 = new ShoppingList(null, 2, 100.00, order1, p1);
            ShoppingList sl2 = new ShoppingList(null, 3, 150.00, order2, p2);
            ShoppingList sl3 = new ShoppingList(null, 1, 50.00, order3, p3);
            repoShopList.saveAll(List.of(sl1, sl2, sl3));

    }

}
