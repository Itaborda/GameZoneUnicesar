package com.gamezone.persistence;

import com.gamezone.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class WarrantyRepository {
    private String filePath;


    public WarrantyRepository() {
        this.filePath = "data/warranties.csv";

    }

    public void saveAll(List<String[]> rows) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("type,id,productId,saleId,startDate,endDate");
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> loadAll() {
        List<String[]> rows = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return rows;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                rows.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

}
