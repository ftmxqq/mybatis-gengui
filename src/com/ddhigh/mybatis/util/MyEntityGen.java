/**
 * Project Name:entityAuto
 * File Name:MyEntityGen.java
 * Package Name:com.ddhigh.mybatis.util
 * Date:2016年11月28日上午9:25:08
 *
 */

package com.ddhigh.mybatis.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ddhigh.mybatis.entity.ColumnEntity;
import com.ddhigh.mybatis.entity.ColumnP;
import com.ddhigh.mybatis.entity.TableEntity;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * ClassName:MyEntityGen <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月28日 上午9:25:08 <br/>
 *
 * @author gxp
 * @version
 * @since JDK 1.7
 * @see
 */
public class MyEntityGen {
    private TableEntity te;
    private String srcPath;
    private String modePkg;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时MM分ss秒");

    public MyEntityGen(TableEntity te, String srcPath, String modePkg) {
        this.te = te;
        this.srcPath = srcPath;
        this.modePkg = modePkg;
    }

    public void GenEntity() {
        Map<String, Object> fmMap = new HashMap<String, Object>();
        boolean f_util = false;
        boolean f_sql = false;
        fmMap.put("packagename", modePkg);
        fmMap.put("className", te.getEntityName());
        fmMap.put("author", "gxp");
        String date = sdf.format(new Date());
        fmMap.put("date", date);
        fmMap.put("tbcomment", te.getComment());
        List<ColumnP> list = new ArrayList<ColumnP>();
        for (ColumnEntity cl : te.getColumns()) {
            if (cl.getType().equalsIgnoreCase("datetime")) {
                f_util = true;
            }
            if (cl.getType().equalsIgnoreCase("image") || cl.getType().equalsIgnoreCase("text")) {
                f_sql = true;
            }
            ColumnP cp = new ColumnP();
            cp.setName(cl.getName());
            cp.setCname(StringUtil.ucwords(cl.getName()));
            cp.setCtype(sqlType2JavaType(cl.getType()));
            cp.setComment(cl.getComment());
            list.add(cp);
        }
        fmMap.put("colList", list);
        fmMap.put("f_util", f_util);
        fmMap.put("f_sql", f_sql);
        Configuration cfg = new Configuration();
        try {
            File directory = new File("");
            String ftlpath = directory.getAbsolutePath();
            cfg.setDirectoryForTemplateLoading(new File(ftlpath));
            Template template = cfg.getTemplate("entity.ftl", "utf-8");
            String outputPath = srcPath + "/" + this.modePkg.replace(".", "/") + "/" + te.getEntityName() + ".java";
            FileWriter fw = new FileWriter(outputPath);
            PrintWriter pw = new PrintWriter(fw);
            template.process(fmMap, pw);
            pw.flush();
            pw.close();
        } catch (TemplateException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：获得列的数据类型
     *
     * @param sqlType
     * @return
     */
    private String sqlType2JavaType(String sqlType) {

        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric") || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }

        return null;
    }
}
