package connection;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final Logger logger = Logger.getLogger(PropertiesReader.class);

    // Więcej na temat używania Properties tutaj: https://www.baeldung.com/java-properties
    public Properties loadFromFile(String fileName) {

        Properties properties = new Properties();
        InputStream input;

        input = getClass().getClassLoader().getResourceAsStream(fileName);

        try {
            properties.load(input);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return properties;
    }
}
