package test.java.example.dbunit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

public final class DataHelper {


    public static DataSource getDatasource(final String propertiesFile) {
        System.out.println("Creating datasource");
        InputStream is;
        Properties properties = new Properties();
        try {
            is = TestDB.class.getResourceAsStream(propertiesFile);
            properties.load(is);
            is.close();
        } catch (IOException e) {
            System.out.println("Unable to load " + propertiesFile);
        }
        System.out.println("Datasource configuration - ");
        String key;
        Iterator<Object> pit = properties.keySet().iterator();
        while (pit.hasNext()) {
            key = (String) pit.next();
            if (key.contains("pass")) {
                continue;
            }
            System.out.println(String.format("   %-12s: %s", key, properties.getProperty(key)));
        }
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(properties.getProperty("jdbc.username"));
        ds.setPassword(properties.getProperty("jdbc.password"));
        ds.setDriverClassName(properties.getProperty("jdbc.driver"));
        ds.setUrl(properties.getProperty("jdbc.url"));
        return ds;
    }

}
