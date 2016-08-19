package com.gehtsoft.configProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dkuzmin on 8/10/2016.
 */
public class ConfigProperties {

    public static Properties getProperties(){
        Properties properties = new Properties();
        String PROPERTIES_FILE = "/config.properties";
        InputStream inputStream = ConfigProperties.class.getResourceAsStream(PROPERTIES_FILE);
        try {
            properties.load(inputStream);
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return properties;
    }
}
