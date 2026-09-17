package com.gamezone;

import com.gamezone.model.Person;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.MainMenu;

import java.util.List;

public class Main {

        public static void main(String[] args) {

                // Repositories
                ProductRepository productRepository =
                        new ProductRepository("Data/Products.csv");

                PersonRepository personRepository =
                        new PersonRepository("Data/Persons.csv");

                SaleRepository saleRepository =
                        new SaleRepository();

                // Services
                ProductService productService =
                        new ProductService();

                List<Person> people =
                        personRepository.findAll();

                PersonService personService =
                        new PersonService(personRepository, people);

                SaleService saleService =
                        new SaleService();

                // Main Menu
                MainMenu mainMenu =
                        new MainMenu(
                                productService,
                                personService,
                                saleService
                        );

                // Start application
                mainMenu.showMenu();
        }
}