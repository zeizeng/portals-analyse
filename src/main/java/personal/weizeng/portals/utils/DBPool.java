package personal.weizeng.portals.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Administrator on 2018/3/11.
 */
public class DBPool {

    private static DBPool instance;

    private ComboPooledDataSource dataSource;

    static {
        instance = new DBPool();
    }

    private DBPool() {
        dataSource = new ComboPooledDataSource();
        Properties properties = new Properties();
        InputStream inputStream = DBPool.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            properties.load(inputStream);
            dataSource.setDriverClass(properties.getProperty("jdbc.driver.class"));
            dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
            dataSource.setUser(properties.getProperty("jdbc.username"));
            dataSource.setPassword(properties.getProperty("jdbc.password"));
            dataSource.setMaxPoolSize(12);
            dataSource.setMaxPoolSize(3);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static DBPool getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
