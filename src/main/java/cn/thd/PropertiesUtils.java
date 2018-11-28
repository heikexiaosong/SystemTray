package cn.thd;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

public class PropertiesUtils {

    private static String FILE_NAME = "config.properties";

    public static String getValue(String key, String defaultValue) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(FILE_NAME);
            String _value = config.getString(key);
            if ( _value==null || StringUtils.isBlank(_value)){
                return defaultValue;
            }
            return _value;
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static void setValue(String key, String value) throws ConfigurationException {
        PropertiesConfiguration config  = new PropertiesConfiguration(FILE_NAME);
        config.setAutoSave(true);
        config.setProperty(key, value);
    }

}
