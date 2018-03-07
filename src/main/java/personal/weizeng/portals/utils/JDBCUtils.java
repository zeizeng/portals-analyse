package personal.weizeng.portals.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * JDBC 工具类
 * 通过读取配置文件配置数据库信息
 *
 * Created by Administrator on 2018/3/5.
 */
public class JDBCUtils {

    private static final Logger logger = LoggerFactory.getLogger(JDBCUtils.class);
    private static final String DRIVER_CLASS;
    private static final String DB_URL;
    private static final String DB_USERNAME;
    private static final String DB_PASSWORD;

    static {
        Properties property = new Properties();
        try {
            property.load(JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        DRIVER_CLASS = property.getProperty("jdbc.driver.class");
        DB_URL = property.getProperty("jdbc.url");
        DB_USERNAME = property.getProperty("jdbc.username");
        DB_PASSWORD = property.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return connection;
    }

    public static void release(Statement stmt, Connection coon) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            stmt = null;
        }
        if (coon != null) {
            try {
                coon.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            coon = null;
        }
    }

    public static void release(ResultSet rs, Statement stmt, Connection coon) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            rs = null;
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            stmt = null;
        }
        if (coon != null) {
            try {
                coon.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            coon = null;
        }
    }
}
