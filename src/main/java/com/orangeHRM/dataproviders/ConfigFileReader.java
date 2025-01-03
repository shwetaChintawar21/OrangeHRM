package com.orangeHRM.dataproviders;

import com.orangeHRM.enums.DriverType;
import com.orangeHRM.enums.EnvironmentType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigFileReader {

    private final Properties properties;

    public ConfigFileReader() {
        BufferedReader bufferedReader;
        FileReader fileReader;
        String propertyFilePath = "src/main/resources/configuration.properties";

        try {
            fileReader = new FileReader(propertyFilePath);
            bufferedReader = new BufferedReader(fileReader);
            properties = new Properties();

            try {
                properties.load(bufferedReader);
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("configuration.properties not found at " + propertyFilePath);
        }
    }

    public String getUrl() {
        String url = properties.getProperty("url");

        //Simply If...Else
        if (url != null) return url;
        else
            throw new RuntimeException("url not specified in the config file.");
    }


    public Map<String, String> getCredentials(String userNameConfigKey, String passwordConfigKey) {
        Map<String, String> credentials = new HashMap<>();

        String userName = properties.getProperty(userNameConfigKey);
        String password = properties.getProperty(passwordConfigKey);

        if (userName != null && password != null) {
            credentials.put(userNameConfigKey, userName);
            credentials.put(passwordConfigKey, password);
            return credentials;
        } else {
            throw new RuntimeException("username and password not specified in the config file.");
        }
    }

    public int getTime() {
        String timeout = properties.getProperty("timeout");

        //Common If...Else
        if (timeout != null) {
            return Integer.parseInt(timeout);
        } else {
            throw new RuntimeException("timeout not specified in the config file.");
        }
    }

    public DriverType getBrowser() {
        String browserName = properties.getProperty("browser");

        switch (browserName) {
            case "chrome":
                return DriverType.CHROME;
            case "firefox":
                return DriverType.FIREFOX;
            case "edge":
                return DriverType.EDGE;
            case "safari":
                return DriverType.SAFARI;
            default:
                throw new RuntimeException("Browser name key value in configuration file is not matched: " + browserName);
        }
    }

    public EnvironmentType getEnvironment() {
        String environmentName = properties.getProperty("environment");

        switch (environmentName) {
            case "local":
                return EnvironmentType.LOCAL;
            case "remote":
                return EnvironmentType.REMOTE;
            default:
                throw new RuntimeException("Environment type key value in configuration file is not matched: " + environmentName);
        }
    }
}
