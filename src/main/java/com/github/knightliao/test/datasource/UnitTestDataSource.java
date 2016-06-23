/**
 *
 */
package com.github.knightliao.test.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

/**
 * @author zhugongrui
 * @date 2016年6月12日
 */
public class UnitTestDataSource extends AbstractRoutingDataSource {
    private static final Logger LOGGER = Logger.getLogger(UnitTestDataSource.class);

    private static final ThreadLocal<String> SELECTED_KEY = new ThreadLocal<String>();

    /**
     * 选择数据库
     *
     * @param database
     */
    public static void selectDataSource(SqlConfig.Database database) {
        if (database == null) {
            SELECTED_KEY.set(SqlConfig.Database.MYSQL.getName());
            return;
        }
        SELECTED_KEY.set(database.getName());
    }

    /**
     * 获得数据源的key
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String key = SELECTED_KEY.get();
        if (StringUtils.isEmpty(key)) {
            key = SqlConfig.Database.MYSQL.getName();
        }
        LOGGER.info(String.format("DataSouce = [%s] is selected.", key));
        return key;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDataSource(null, null);
    }

    private void clearKey() {
        SELECTED_KEY.remove();
    }

    /**
     * @param username
     * @param password
     *
     * @return
     *
     * @throws SQLException
     */
    private Connection getConnectionFromDataSource(String username, String password) throws SQLException {

        DataSource ds;
        ds = determineTargetDataSource();
        Connection con = null;
        try {
            if (username == null && password == null) {
                con = ds.getConnection();
            } else {
                con = ds.getConnection(username, password);
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }
        return con;

    }

    /**
     * get SQL Connection
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDataSource(username, password);
    }
}
