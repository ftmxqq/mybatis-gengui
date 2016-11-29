/**
 * Project Name:entityAuto
 * File Name:ColumnEntity.java
 * Package Name:com.ddhigh.mybatis.entity
 * Date:2016年11月25日下午4:55:42
 *
 */

package com.ddhigh.mybatis.entity;

/**
 * ClassName:ColumnEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月25日 下午4:55:42 <br/>
 *
 * @author gxp
 * @version
 * @since JDK 1.7
 * @see
 */
public class ColumnEntity {
    private String name;
    private String comment;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
