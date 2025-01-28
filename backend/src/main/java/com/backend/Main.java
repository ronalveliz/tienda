package com.backend;

import com.backend.model.*;
import com.backend.repository.*;
import com.backend.repository.StoreRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;


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
        CategoryRepository repoCatego= context.getBean(CategoryRepository.class);


            // borrado de tablas
            repoShopList.deleteAll();
            repoOrderss.deleteAll();
            repoProducto.deleteAll();
            userRepo.deleteAll();
            repoStore.deleteAll();
            repoCatego.deleteAll();


            // Crear y guardar usuarios
            PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
            User a0= new User(null, "Admin", "Admin", "Admin@gmail.com", passwordEncoder.encode("Admin1234"), "640364015", RolName.ADMIN,"avatar.png");
            User u1 = new User(null, "user1", "user1", "user1@gmail.com", passwordEncoder.encode("Admin1234"), "640364015", RolName.USER,"avatar.png");
            User u2 = new User(null, "user2", "user2", "user2@gmail.com", passwordEncoder.encode("Admin1234"), "640364015", RolName.USER,"avatar.png");
            User u3 = new User(null, "tienda1", "tienda1", "tienda1@gmail.com", passwordEncoder.encode("Admin1234"), "640364015", RolName.TIENDA,"avatar.png");
            userRepo.saveAll(List.of(a0, u1, u2, u3));

            // Crear y guardar tiendas
            Store store1 = new Store(null, "Tienda A", "Calle Principal 123","https://media.tacdn.com/media/attractions-splice-spp-674x446/0d/ac/68/23.jpg", u3);
            Store store2 = new Store(null, "Tienda B", "Avenida Secundaria 456","https://media.tacdn.com/media/attractions-splice-spp-674x446/0d/ac/68/23.jpg", u3);
            Store store3 = new Store(null, "Tienda C", "Plaza Central 789","https://media.tacdn.com/media/attractions-splice-spp-674x446/0d/ac/68/23.jpg", u3);
            repoStore.saveAll(List.of(store1, store2, store3));

            // crear category y guarda
            Category cat1 = new Category(null, "Categoria 1", store1);
            Category cat2 = new Category(null, "Categoria 2", store1);
            Category cat3 = new Category(null, "Categoria 3", store1);
            Category cat4 = new Category(null, "Categoria 4", store1);
            repoCatego.saveAll(List.of(cat1, cat2, cat3, cat4 ));

        // Crear y guardar productos
        Productos p1 = new Productos(null, "Producto 1", "Descripción del producto 1", 50.00, "https://media.istockphoto.com/id/1729271656/es/foto/perezoso-de-dos-dedos-colgado-en-un-%C3%A1rbol-la-fortuna-costa-rica.jpg?s=2048x2048&w=is&k=20&c=gunJbj1aGRH5JCjuQFAzUt7OYQCxcv1yCp0-HwjG-bs=", true,cat1 );
        Productos p2 = new Productos(null, "Producto 2", "Descripción del producto 2", 75.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat1);
        Productos p3 = new Productos(null, "Producto 3", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/09/01/16/16/white-2704666_1280.jpg",true,cat1);
        Productos p4 = new Productos(null, "Producto 4", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2016/11/17/11/50/penguin-1831375_1280.jpg",true,cat1);
        Productos p5 = new Productos(null, "Producto 5", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat1);
        Productos p6 = new Productos(null, "Producto 6", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2016/11/17/11/50/penguin-1831375_1280.jpg",true,cat2);
        Productos p7 = new Productos(null, "Producto 7", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat2);
        Productos p8 = new Productos(null, "Producto 8", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat2);
        Productos p9 = new Productos(null, "Producto 9", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat1);
        Productos p10 = new Productos(null, "Producto 10", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/09/01/16/16/white-2704666_1280.jpg",true,cat3);
        Productos p11 = new Productos(null, "Producto 11", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat3);
        Productos p12 = new Productos(null, "Producto 12", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2016/11/17/11/50/penguin-1831375_1280.jpg",true,cat3);
        Productos p13 = new Productos(null, "Producto 13", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2016/11/17/11/50/penguin-1831375_1280.jpg",true,cat3);
        Productos p14 = new Productos(null, "Producto 14", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/09/01/16/16/white-2704666_1280.jpg",true,cat3);
        Productos p15 = new Productos(null, "Producto 15", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/09/01/16/16/white-2704666_1280.jpg",true,cat3);
        Productos p16 = new Productos(null, "Producto 16", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat4);
        Productos p17 = new Productos(null, "Producto 17", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat4);
        Productos p18 = new Productos(null, "Producto 18", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/09/01/16/16/white-2704666_1280.jpg",true,cat4);
        Productos p19 = new Productos(null, "Producto 19", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/10/18/16/08/wolves-2864647_1280.jpg",true,cat1);
        Productos p20 = new Productos(null, "Producto 20", "Descripción del producto 3", 100.00, "https://cdn.pixabay.com/photo/2017/09/01/16/16/white-2704666_1280.jpg",true,cat1);
        repoProducto.saveAll(List.of(p1, p2, p3, p4, p5, p6, p7, p8,p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20));

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
