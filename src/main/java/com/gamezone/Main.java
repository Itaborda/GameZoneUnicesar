package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.MainMenu;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // 1. Instanciación de los Repositorios
        // 1. Instanciación de los Repositorios con la ruta del archivo
        ProductRepository productRepository = new ProductRepository("products.dat");
        PersonRepository personRepository = new PersonRepository("persons.dat");
        SaleRepository saleRepository = new SaleRepository();
        // 2. Instanciación de Servicios
        ProductService productService = new ProductService(productRepository);

        // PersonService requiere el repositorio y una lista inicial (List<Person>)
        PersonService personService = new PersonService(personRepository, new ArrayList<>());

        // SaleService en tu código solo pide (SaleRepository, ProductService)
        SaleService saleService = new SaleService(saleRepository, productService);

        // 3. Inicialización del Menú
        MainMenu menu = new MainMenu();

        // En tu MainMenu el método para iniciar se llama showMenu()
        menu.showMenu();
    }
}