package personal.weizeng.portals.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/3/11.
 */
public class DBUtils {

    public static Connection getConnection() throws SQLException {
        DBPool dbPool = DBPool.getInstance();
        return dbPool.getConnection();
    }
}
