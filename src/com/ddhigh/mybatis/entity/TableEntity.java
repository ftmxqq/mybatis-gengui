package com.ddhigh.mybatis.entity;

import java.util.List;

public class TableEntity {
    private String tableName;
    private String entityName;
    private String comment;
    private List<ColumnEntity> columns;

    public TableEntity(String tableName, String entityName) {
        this.tableName = tableName;
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnEntity> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnEntity> columns) {
        this.columns = columns;
    }

}
