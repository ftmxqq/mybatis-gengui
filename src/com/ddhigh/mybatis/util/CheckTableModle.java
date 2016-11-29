/**
 * Project Name:entityAuto
 * File Name:CheckTableModel.java
 * Package Name:com.ddhigh.mybatis.util
 * Date:2016年11月28日上午10:56:10
 *
 */

package com.ddhigh.mybatis.util;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * ClassName:CheckTableModel <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月28日 上午10:56:10 <br/>
 * 
 * @author gxp
 * @version
 * @since JDK 1.7
 * @see
 */
public class CheckTableModle extends DefaultTableModel {

    public CheckTableModle(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    // /**
    // * 根据类型返回显示空间
    // * 布尔类型返回显示checkbox
    // */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void selectAllOrNull(boolean value) {
        for (int i = 0; i < getRowCount(); i++) {
            this.setValueAt(value, i, 0);
        }
    }

}
