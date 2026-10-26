package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;

import java.util.*;

/**
 * Converts {@link Accessory} objects to and from their comma-separated
 * representation. It only knows the line format (eleven columns whose
 * first one is the type tag); it never reads or writes files, which is
 * the job of {@link AccessoryRepository}.
 * <p>
 * Column layout:
 * {@code type,id,title,price,stockQuantity,compatibleConsoleIds,connectionType,
 * lengthInMeters,connectorType,capacityInGigabytes,memoryType}
 */
public class AccessoryCsvMapper {
    private final Map<Class<?>, TypeConverter> byClass = new HashMap<>();
    private final Map<String, TypeConverter> byType = new HashMap<>();

    private interface TypeConverter {
        String toLine(Accessory accessory);
        Accessory fromFields(String[] data);
    }

    /**
     * Creates a mapper that knows controllers, cables and memories.
     */
    public AccessoryCsvMapper() {
        register("CONTROLLER", Controller.class, new ControllerConverter());
        register("CABLE", Cable.class, new CableConverter());
        register("MEMORY", Memory.class, new MemoryConverter());
    }

    private void register(String type, Class<?> cls, TypeConverter converter) {
        byClass.put(cls, converter);
        byType.put(type, converter);
    }

    /**
     * Converts an accessory into one comma-separated line.
     *
     * @param accessory the accessory to convert
     * @return the line to write in the file, or an empty string if the
     * kind of accessory is not supported
     */
    public String toLine(Accessory accessory) {
        TypeConverter converter = byClass.get(accessory.getClass());
        return converter == null ? "" : converter.toLine(accessory);
    }

    /**
     * Rebuilds an accessory from the fields of one line of the file.
     *
     * @param data the line split by commas; the first field is the type tag
     * @return the accessory described by the fields, or {@code null} if
     * the type tag is not recognized
     */
    public Accessory fromFields(String[] data) {
        TypeConverter converter = byType.get(data[0]);
        return converter == null ? null : converter.fromFields(data);
    }

    // ---- shared helpers -------------------------------------------------

    private static String consolesToText(Accessory accessory) {
        return accessory.getCompatibleConsoleIds() == null
                ? ""
                : String.join(";", accessory.getCompatibleConsoleIds());
    }

    private static List<String> consolesFromText(String[] data) {
        return data[5].trim().isEmpty()
                ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(data[5].split(";")));
    }

    private static String commonColumns(String type, Accessory a) {
        return type + ","
                + a.getId() + ","
                + a.getTitle() + ","
                + a.getPrice() + ","
                + a.getStockQuantity() + ","
                + consolesToText(a) + ",";
    }

    private static double price(String[] data) {
        return Double.parseDouble(data[3].trim());
    }

    private static int stock(String[] data) {
        return Integer.parseInt(data[4].trim());
    }

    // ---- one converter per accessory type --------------------------------

    private static class ControllerConverter implements TypeConverter {
        @Override
        public String toLine(Accessory accessory) {
            Controller c = (Controller) accessory;
            return commonColumns("CONTROLLER", c) + c.getConnectionType() + ", , , ,";
        }

        @Override
        public Accessory fromFields(String[] data) {
            return new Controller(data[1], data[2], price(data), stock(data),
                    consolesFromText(data), data[6].trim());
        }
    }

    private static class CableConverter implements TypeConverter {
        @Override
        public String toLine(Accessory accessory) {
            Cable c = (Cable) accessory;
            return commonColumns("CABLE", c) + " ,"
                    + c.getLengthInMeters() + ","
                    + c.getConnectorType() + ", ,";
        }

        @Override
        public Accessory fromFields(String[] data) {
            return new Cable(data[1], data[2], price(data), stock(data),
                    consolesFromText(data),
                    Double.parseDouble(data[7].trim()), data[8].trim());
        }
    }

    private static class MemoryConverter implements TypeConverter {
        @Override
        public String toLine(Accessory accessory) {
            Memory m = (Memory) accessory;
            return commonColumns("MEMORY", m) + " , , ,"
                    + m.getCapacityInGigabytes() + ","
                    + m.getMemoryType();
        }

        @Override
        public Accessory fromFields(String[] data) {
            return new Memory(data[1], data[2], price(data), stock(data),
                    consolesFromText(data),
                    Integer.parseInt(data[9].trim()), data[10].trim());
        }
    }
}