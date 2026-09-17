package com.gamezone;

import com.gamezone.model.Person;
import com.gamezone.model.Return;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.ConsoleMenu;

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
                        new SaleService(saleRepository, productService);

                ReturnRepository returnRepository =
                        new ReturnRepository(saleService, productService);

                List<Return> returns =
                        returnRepository.loadAll();

                ReturnService returnService =
                        new ReturnService(
                                productService,
                                returnRepository,
                                saleService,
                                returns
                        );

                // Main Menu
                ConsoleMenu consoleMenu =
                        new ConsoleMenu(
                                productService,
                                personService,
                                saleService,
                                returnService
                        );

                // Start application
                consoleMenu.showMenu();
        }
}