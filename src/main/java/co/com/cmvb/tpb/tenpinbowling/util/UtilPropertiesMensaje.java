package co.com.cmvb.tpb.tenpinbowling.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilPropertiesMensaje {

    private static final Logger LOG = Logger.getLogger(UtilPropertiesMensaje.class.getName());

    private static final Properties PROPERTIE;

    static {
        PROPERTIE = new Properties();

        try (InputStream propertiesStream = UtilPropertiesMensaje.class.getClassLoader().getResourceAsStream("properties/mensaje.properties")) {
            PROPERTIE.load(propertiesStream);

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public static String getProperty(String key, Object... args) {
        String message = PROPERTIE.getProperty(key);

        if (args != null && args.length > 0) {
            message = String.format(message, args);
        }

        return message;
    }

}
