package com.ddhigh.mybatis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ddhigh.mybatis.entity.ColumnEntity;
import com.sun.istack.internal.Nullable;

@SuppressWarnings("unused")
public class DbUtil {
    private Connection connection;
    private String host;
    private String port;
    private String user;
    private String password;
    private Type type;
    private String database;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public static Map<Type, Integer> portMap = new HashMap<>();

    static {
        portMap.put(Type.MySQL, 3306);
        portMap.put(Type.Oracle, 1521);
    }

    public enum Type {
        MySQL, Oracle
    }

    public DbUtil() {
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DbUtil(String host, String port, String user, String password, Type type, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.type = type;
        this.database = database;
    }

    public String buildConnectionString(DbUtil dbUtil) {
        if (dbUtil.type.equals(Type.MySQL)) {
            return "jdbc:mysql://" + host + ":" + port + "/" + database;
        } else if (type.equals(Type.Oracle)) {
            return "jdbc:oracle:thin:@//" + host + ":" + port + "/" + database;
        }
        throw new UnsupportedOperationException("不支持的数据库类型");
    }

    private void connect() throws ClassNotFoundException, SQLException {
        String connectString = buildConnectionString(this);
        if (type.equals(Type.MySQL)) {
            Class.forName("com.mysql.jdbc.Driver");
        } else if (type.equals(Type.Oracle)) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } else {
            throw new UnsupportedOperationException("不支持的数据库类型");
        }
        connection = DriverManager.getConnection(connectString, user, password);
    }

    public ResultSet query(String sql, @Nullable Map<Integer, Object> params) throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (params != null) {
            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
            }
        }
        return preparedStatement.executeQuery();
    }

    public int execute(String sql, @Nullable Map<Integer, Object> params) throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (params != null) {
            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
            }
        }
        return preparedStatement.executeUpdate();
    }

    public List<ColumnEntity> getColumns(String tablename, String schema) throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        List<ColumnEntity> cols = new ArrayList<ColumnEntity>();
        String sql = "select * from " + tablename;
        PreparedStatement pStemt = connection.prepareStatement(sql);
        ResultSetMetaData rsmd = pStemt.getMetaData();
        int len = rsmd.getColumnCount();
        for (int i = 0; i < len; i++) {
            ColumnEntity ce = new ColumnEntity();
            ce.setName(rsmd.getColumnName(i + 1).toLowerCase());
            ce.setType(rsmd.getColumnTypeName(i + 1));
            ce.setComment(getColumnComment(schema, tablename, ce.getName()));
            cols.add(ce);
        }
        return cols;
    }

    public String getColumnComment(String schema, String tablename, String columnName) throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        String comment = "";
        String sql = "select COLUMN_COMMENT from information_schema.columns WHERE TABLE_SCHEMA=? and TABLE_NAME=? and COLUMN_NAME=?";
        PreparedStatement pStemt = null;
        ResultSet rs = null;

        pStemt = connection.prepareStatement(sql);
        pStemt.setString(1, schema);
        pStemt.setString(2, tablename);
        pStemt.setString(3, columnName);
        rs = pStemt.executeQuery();
        while (rs.next()) {
            comment = rs.getString(1);
        }
        pStemt.close();
        rs.close();
        return comment;
    }

    public String getTableComment(String schema, String tablename) throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        String comment = "";
        String sql = "select TABLE_COMMENT from information_schema.tables WHERE TABLE_SCHEMA=? and TABLE_NAME=?";
        PreparedStatement pStemt = null;
        ResultSet rs = null;

        pStemt = connection.prepareStatement(sql);
        pStemt.setString(1, schema);
        pStemt.setString(2, tablename);
        rs = pStemt.executeQuery();
        while (rs.next()) {
            comment = rs.getString(1);
        }
        pStemt.close();
        rs.close();
        return comment;
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
