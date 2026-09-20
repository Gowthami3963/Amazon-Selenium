package org.example.utilities;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigReader {

    private static Properties properties;
    static {
        try {

            FileInputStream fileInputStream=new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/config/config.properties");
            properties =new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
        }
        catch (IOException e){
            System.out.println("Error loading config.properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getBrowser(){
        return properties.getProperty("browser","chrome");
    }

    public static String getURL(){
        return properties.getProperty("url");
    }

    public static String getUsername() {
        return properties.getProperty("username");
    }
    public static String getPassword() {
        return properties.getProperty("password");
    }
    public static long getImplicitWait() {
        return Long.parseLong(properties.getProperty("implicitwait", "10"));
    }
    public static long getExplicitWait() {
        return Long.parseLong(properties.getProperty("explicitwait", "20"));
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
