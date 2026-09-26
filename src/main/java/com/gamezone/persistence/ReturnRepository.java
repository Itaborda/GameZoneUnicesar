package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Repository responsible for persisting and retrieving returns
 * using a text file.
 *
 * Since a Return references a Sale and a list of Products, only
 * their identifiers are persisted in the file; the full objects are
 * resolved back using the injected SaleService and ProductService
 * when loading.
 */
public class ReturnRepository {
    private static final String PRODUCT_ID_SEPARATOR = ";";
    private  String filePath;
    private final AccessoryService accessoryService;
    private final SaleService saleService;
    private final ProductService productService;
    /**
     * Creates a new ReturnRepository using the default file path.
     *
     * @param saleService      used to resolve the original sale by id when loading
     * @param productService   used to resolve returned products by id when loading
     * @param accessoryService used to resolve returned accessories by id when loading,
     *                         for items not found as a product
     */
    public ReturnRepository(AccessoryService accessoryService, SaleService saleService, ProductService productService) {
        this.accessoryService = accessoryService;
        this.saleService = saleService;
        this.productService = productService;
        this.filePath = "data/return.csv";
    }


    /**
     * Saves all returns to the configured file.
     *
     * Each return is stored on a separate line using comma-separated
     * values. The original sale and the returned products are stored
     * by their identifiers; returned product ids are joined with a
     * semicolon, since the comma is already used as the column
     * separator.
     *
     * @param returns the list of returns to save
     */
    public void saveAll(List<Return> returns){
        File file = new File(filePath); File parent = file.getParentFile();
        if(parent!=null && !parent.exists()){
            parent.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for(Return r: returns ){
                StringBuilder productIds = new StringBuilder();
                List<Product> returnedProducts = r.getReturnedProducts();

                for (int i = 0; i < returnedProducts.size(); i++) {
                    productIds.append(returnedProducts.get(i).getId());
                    if (i < returnedProducts.size() - 1) {
                        productIds.append(PRODUCT_ID_SEPARATOR);
                    }
                }

                String line = r.getReturnId() + ","
                        + r.getReturnDate() + ","
                        + r.getOriginalSale().getSaleId() + ","
                        + productIds + ","
                        + r.getReason() + ","
                        + r.getRefundAmount();

                writer.write(line);
                writer.newLine();
            }
            } catch (IOException e) {
        e.printStackTrace();
        }
    }
    /**
     * Retrieves all returns stored in the configured file.
     *
     * The method reads each line of the file, resolves the original
     * sale using the injected SaleService, resolves each returned
     * item first as a product using ProductService, and falls back
     * to AccessoryService when no matching product is found, then
     * reconstructs each Return object.
     *
     * @return a list containing all returns found in the file, or an
     *         empty list if the file does not exist
     */

public List<Return> loadAll() {
    List<Return> returns = new ArrayList<>();
    File file = new File(filePath);

    if (!file.exists()) {
        return returns;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

        String line;

        while ((line = reader.readLine()) != null) {

            String[] data = line.split(",", -1);

            String returnId = data[0];
            LocalDate returnDate = LocalDate.parse(data[1]);
            Sale originalSale = null;
            for (Sale s : saleService.findAll()) {
                if (s.getSaleId().equals(data[2])) {
                    originalSale = s;
                    break;
                }
            }

            List<Product> returnedProducts = new ArrayList<>();
            if (!data[3].isEmpty()) {
                String[] productIds = data[3].split(ReturnRepository.PRODUCT_ID_SEPARATOR);
                for (String productId : productIds) {
                    Product resolved = productService.findById(productId);
                    if (resolved == null) {
                        resolved = accessoryService.findById(productId);
                    }
                    returnedProducts.add(resolved);
                }
            }

            String reason = data[4];
            double refundAmount = Double.parseDouble(data[5]);

            Return r = new Return(returnId, returnDate, originalSale, returnedProducts, reason, refundAmount);
            returns.add(r);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

    return returns;
}
}
