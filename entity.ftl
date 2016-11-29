package ${packagename};

import java.io.Serializable;
<#if f_util>
import java.util.Date;
</#if>
<#if f_sql>
import java.sql.*;
</#if>


/**
 * ClassName: ${className} <br/>
 * Function: ${tbcomment}实体类 <br/>
 * Date: ${date} <br/>
 *
 * @author ${author}
 * @version 1.0
 * @since JDK 1.7
 */
public class ${className} implements Serializable {

    /** 序列化 */
    private static final long serialVersionUID = 1L;
    <#list colList as col>
    
    /** ${col.comment} **/
    private ${col.ctype} ${col.name};
    </#list>
    
    <#list colList as col>
    
    /**
     * 获取${col.comment} @return ${col.comment}
     */
    public ${col.ctype} get${col.cname}() {
        return ${col.name};
    }

    /**
     * 设置${col.comment} @param ${col.name} ${col.comment}
     */
    public void set${col.cname}(${col.ctype} ${col.name}) {
        this.${col.name} = ${col.name};
    }
    </#list>
}