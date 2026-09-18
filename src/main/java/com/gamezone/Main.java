package com.gamezone;

import com.gamezone.model.Person;
import com.gamezone.model.Return;
import com.gamezone.persistence.*;
import com.gamezone.service.*;
import com.gamezone.ui.ConsoleMenu;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.service.PromotionService;

import java.util.List;

public class Main {

        public static void main(String[] args) {

                // Repositories
                ProductRepository productRepository =
                        new ProductRepository("Data/Products.csv");

                PersonRepository personRepository =
                        new PersonRepository("Data/Persons.csv");

                AccessoryRepository accessoryRepository =
                        new AccessoryRepository("Data/Accessories.csv");

                SaleRepository saleRepository =
                        new SaleRepository();

                PromotionRepository promotionRepository =
                        new PromotionRepository("data/promotions.csv");

                PromotionService promotionService =
                        new PromotionService(promotionRepository);

                // Services
                ProductService productService =
                        new ProductService();

                AccessoryService accessoryService =
                        new AccessoryService(accessoryRepository);

                List<Person> people =
                        personRepository.findAll();

                PersonService personService =
                        new PersonService(personRepository, people);

                SaleService saleService =
                        new SaleService(
                                saleRepository,
                                productService,
                                accessoryService,
                                promotionService
                        );

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
                                returnService,
                                accessoryService,
                                promotionService
                        );

                // Start application
                consoleMenu.showMenu();
        }
}