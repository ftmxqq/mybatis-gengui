package com.ddhigh.mybatis.worker;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.ddhigh.mybatis.entity.DataBase;
import com.ddhigh.mybatis.entity.TableEntity;
import com.ddhigh.mybatis.util.DbUtil;
import com.ddhigh.mybatis.util.StringUtil;

/**
 * 获取数据库列表
 */
public class GetTablesWorker extends SwingWorker<DataBase, String> {
    private static Logger logger = Logger.getLogger(GetTablesWorker.class);

    public interface OnLoadedListener {
        void onSuccess(DataBase database);

        void onError(String message, Throwable ex);
    }

    private String host;
    private String port;
    private String user;
    private String password;
    private String type;
    private String database;
    private OnLoadedListener listener;

    private DbUtil dbUtil;

    private long lastTimestamp;

    public GetTablesWorker(String host, String port, String user, String password, String type, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.type = type;
        this.database = database;
        logger.info("数据表加载线程启动");
        lastTimestamp = System.currentTimeMillis();
    }

    public GetTablesWorker(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
        this.database = dbUtil.getDatabase();
        logger.info("数据表加载线程启动");
        lastTimestamp = System.currentTimeMillis();
    }

    public void setListener(OnLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    protected DataBase doInBackground() throws Exception {
        DataBase dataBase = new DataBase();
        List<TableEntity> list = new ArrayList<>();
        DbUtil.Type dbType;
        if (type == null) {
            dbType = dbUtil.getType();
        } else {
            switch (type) {
            case "MySQL":
                dbType = DbUtil.Type.MySQL;
                break;
            case "Oracle":
                dbType = DbUtil.Type.Oracle;
                break;
            default:
                throw new UnsupportedOperationException("不支持的数据库类型");
            }
        }

        if (dbUtil == null) {
            dbUtil = new DbUtil(host, port, user, password, dbType, database);
        }
        ResultSet resultSet = dbUtil.query("SHOW TABLES", null);
        while (resultSet.next()) {
            String table = resultSet.getString(1);
            TableEntity tableEntity = new TableEntity(table, StringUtil.humpString(table));
            tableEntity.setComment(dbUtil.getTableComment(database, table));
            tableEntity.setColumns(dbUtil.getColumns(table, database));
            list.add(tableEntity);
        }
        dataBase.setTables(list);
        resultSet.close();
        dbUtil.close();
        return dataBase;
    }

    @Override
    protected void done() {
        try {
            listener.onSuccess(get());
        } catch (InterruptedException | ExecutionException e) {
            listener.onError("连接数据库失败", e);
        }
        logger.info("数据表加载线程退出，耗时 " + (System.currentTimeMillis() - lastTimestamp) + "ms");
    }
}
