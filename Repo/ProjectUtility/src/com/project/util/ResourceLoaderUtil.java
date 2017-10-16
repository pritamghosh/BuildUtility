package com.project.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;


/**
 * <pre>
 * <b>Description : </b>
 * ResourceLoaderUtil.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:11:48 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
public class ResourceLoaderUtil {

    private static final String CONFIG_PROPERTIES = "config.properties";

    private ResourceLoaderUtil() {
        super();
    }

    public static void copyPropertirs() {

        try (InputStream input = new FileInputStream(CONFIG_PROPERTIES)) {
        }
        catch (IOException ex) {
            try (
                InputStream input = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(CONFIG_PROPERTIES);
                FileOutputStream output = new FileOutputStream(CONFIG_PROPERTIES);) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
            catch (IOException ex1) {
                System.out.println("unable to save properties file");
            }

        }
    }

    public static void loadPropertirs(String key) {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(CONFIG_PROPERTIES)) {

            prop.load(input);
            ProjecUtilContext.put(key, prop.get(key));

        }
        catch (IOException ex) {
            copyPropertirs();
        }
    }

    public static void loadPropertirs() {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(CONFIG_PROPERTIES)) {
            prop.load(input);
            Enumeration<Object> keys = prop.keys();
            if (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                ProjecUtilContext.put(key, prop.get(key));
            }

        }
        catch (IOException ex) {
            copyPropertirs();
        }
    }
}
