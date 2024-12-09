package org.example.Model.Utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLSerializator {

    /**
     * Serializa un objeto a un archivo XML.
     *
     * @param obj      El objeto a serializar.
     * @param filename El nombre del archivo donde se guardará el objeto.
     * @throws JAXBException Si ocurre un error durante la serialización.
     */
    public static <T> void serializeObjectToXML(T obj, String filename) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Esto da formato bonito al XML

        // Serializa el objeto a un archivo
        marshaller.marshal(obj, new File(filename));
    }

    public static <T> T deserializeObjectFromXML(String filename, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Deserializar el objeto desde el archivo XML
        return (T) unmarshaller.unmarshal(new File(filename));
    }
}

