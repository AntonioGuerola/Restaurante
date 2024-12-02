package org.example.Model.Utils;

import java.io.*;

/**
 * A utility class for object serialization and deserialization.
 */
public class Serializator {
    /**
     * Serializes an object to a file.
     *
     * @param obj      The object to serialize.
     * @param filename The name of the file to save the serialized object.
     * @param <T>      The type of the object.
     * @return True if serialization is successful, otherwise false.
     */
    public static <T> boolean serializeObject(T obj, String filename) {
        boolean result = false;
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename)
        )) {
            oos.writeObject(obj);
            result = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * Deserializes an object from a file.
     *
     * @param filename The name of the file containing the serialized object.
     * @param <T>      The type of the object.
     * @return The deserialized object.
     */
    public static <T> T deserializeObject(String filename) {
        T result = null;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename)
        )) {
            result = (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
