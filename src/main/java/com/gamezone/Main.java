package com.gamezone;

import com.gamezone.model.Person;
import com.gamezone.model.Return;
import com.gamezone.persistence.*;
import com.gamezone.service.*;
import com.gamezone.ui.ConsoleMenu;

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

                WarrantyRepository warrantyRepository =
                        new WarrantyRepository();

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

                WarrantyService warrantyService =
                        new WarrantyService(
                                warrantyRepository,
                                saleRepository,
                                productService
                        );

                SaleService saleService =
                        new SaleService(
                                saleRepository,
                                productService,
                                accessoryService,
                                promotionService,
                                warrantyService
                        );

                ReturnRepository returnRepository =
                        new ReturnRepository(accessoryService,saleService,productService);

                List<Return> returns =
                        returnRepository.loadAll();

                ReturnService returnService =
                        new ReturnService(
                                accessoryService,
                                returnRepository,
                                saleService,
                                productService,
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
                                promotionService,
                                warrantyService
                        );

                // Start application
                consoleMenu.showMenu();
        }
}