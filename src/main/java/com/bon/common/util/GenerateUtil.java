package com.bon.common.util;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: 后台-基础项目
 * @description: 生成代码工具类
 * @author: Bon
 * @create: 2018-08-17 14:36
 **/
public class GenerateUtil {
    /*************************变量****Begin************************************/
    private static final String myEmail = "502285815@qq.com";//Email
    private static final String Version = "1.0";//版本
    private static final String Description = " ";//描述

    public static final String ENTER = "\n";//换行
    public static final String TAB = "    ";//tab
    public static final String NAME = "NAME";
    public static final String TABLE_CAT = "TABLE_CAT";//表 catalog
    public static final String TABLE_SCHEM = "TABLE_SCHEM";//表 schema
    public static final String TABLE_NAME = "TABLE_NAME";//表名
    public static final String TABLE_TYPE = "TABLE_TYPE";//表类型
    public static final String REMARKS = "REMARKS";//表注释
    public static final String TYPE = "TYPE";//表的类型
    public static final String SIZE = "SIZE";//大小
    public static final String CLASS = "CLASS";//类别

    /*************************变量****End************************************/

    public static final String NOW_DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    /***************获取数据库的配置连接************/
    public static final String DB_NAME = PropertiesHelper.getValueByKey("generator.jdbc.url").substring(
            PropertiesHelper.getValueByKey("generator.jdbc.url").lastIndexOf("/") + 1,
            PropertiesHelper.getValueByKey("generator.jdbc.url").indexOf("?") == -1 ?
                    PropertiesHelper.getValueByKey("generator.jdbc.url").length() :
                    PropertiesHelper.getValueByKey("generator.jdbc.url").indexOf("?"));
    //从配置获取工程的报名路径
    public static final String ROOT_PACKAGE = PropertiesHelper.getValueByKey("basePackage");
    //获取作者.
    public static final String AUTHOR = PropertiesHelper.getValueByKey("author");
    //忽略表的后缀.
    public static final List<String> IGNORE_TABLE_PREFIX = new ArrayList<String>();

    /*******定义代码块*******/
    static {
        String ignoreTablePrefix = PropertiesHelper.getValueByKey("ignoreTablePrefix");
        if (ignoreTablePrefix.length() > 0) {
            String[] ignoreTablePrefixs = ignoreTablePrefix.split("\\s*\\,\\s*");
            for (String elem : ignoreTablePrefixs) {
                IGNORE_TABLE_PREFIX.add(elem);
            }
        }
    }

    /***
     * 生成实体类的代码
     * @param table
     * @throws Exception
     */
    public void createEntityClass(String table) throws Exception {
        String tableConstantName = getTableConstantName(table);

        String className = getClassName(tableConstantName);
        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".entity;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
        sb.append(" * @Version:").append(Version).append(ENTER);
        sb.append(" * @Description:").append(className).append(ENTER);
        sb.append(" * @Email:").append(myEmail).append("\n*/");
        sb.append(ENTER);
        sb.append("@Relation(" + className + ".TABLE)");
        sb.append(ENTER);
        sb.append("public class " + className + " extends Entity {");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append(TAB);
        sb.append("/** 表名常量 */");
        sb.append(ENTER);
        sb.append(TAB);
        sb.append("public static final String TABLE = Table." + tableConstantName + ";");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append(TAB);
        sb.append("/**");
        sb.append(ENTER);
        sb.append(TAB);
        sb.append(" * 列名常量");
        sb.append(ENTER);
        sb.append(TAB);
        sb.append(" */");
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            String colName = col.get(NAME).toString().toUpperCase();
            sb.append(TAB);//生成字段变量
            sb.append("public static final String COL_" + colName + " = \"" + colName + "\";//" + col.get(REMARKS));
            sb.append(ENTER);
        }
        sb.append(ENTER);
        sb.append(TAB);
        sb.append("/**");
        sb.append(ENTER);
        sb.append(TAB);
        sb.append(" * 列属性");
        sb.append(ENTER);
        sb.append(TAB);
        sb.append(" */");
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            sb.append(TAB);
            sb.append("/**  */");
            sb.append(ENTER);
            if (col.get(NAME).toString().equalsIgnoreCase("ID")) {
                sb.append(TAB);
                sb.append("@Id");
                sb.append(ENTER);
            }
            sb.append(TAB);
            sb.append("@Column(COL_" + col.get(NAME).toString().toUpperCase() + ")");
            sb.append(ENTER);
            sb.append(TAB);
            sb.append("private ");
            if (col.get(NAME).toString().equalsIgnoreCase("ID") || col.get(NAME).toString().toUpperCase().endsWith("_ID")) {
                sb.append("Long");
            } else if (Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) ||
                    Class.forName(col.get(CLASS).toString()) == Timestamp.class) {
                sb.append("Date");
            } else if (getClassName(col.get(NAME).toString()).equals(Class.forName(col.get(CLASS).toString()).getSimpleName())) {
                sb.append(col.get(CLASS));
            } else {
                sb.append(Class.forName(col.get(CLASS).toString()).getSimpleName());
            }
            //sb.append(" " + getFieldName(col.get(NAME).toString()) + ";");
            sb.append(" " + col.get(NAME).toString() + ";");
            sb.append(ENTER);
        }
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            sb.append(TAB);
            sb.append("public ");
            if (col.get(NAME).toString().equalsIgnoreCase("ID") || col.get(NAME).toString().toUpperCase().endsWith("_ID")) {
                sb.append("Long");
            } else if (Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) ||
                    Class.forName(col.get(CLASS).toString()) == Timestamp.class) {
                sb.append("Date");
            } else if (getClassName(col.get(NAME).toString()).equals(Class.forName(col.get(CLASS).toString()).getSimpleName())) {
                sb.append(col.get(CLASS));
            } else {
                sb.append(Class.forName(col.get(CLASS).toString()).getSimpleName());
            }
            sb.append(" ").append("get").append(col.get(NAME).toString().replaceFirst("\\b(\\w)|\\s(\\w)", col.get(NAME).toString().substring(0, 1).toUpperCase()));
            sb.append("() {");
            sb.append(ENTER);
            sb.append(TAB);
            sb.append(TAB);
            sb.append("return ").append(col.get(NAME).toString()).append(";");
            sb.append(ENTER);
            sb.append(TAB);
            sb.append("}");
            sb.append(ENTER);
            sb.append(TAB);
            sb.append("public void ").append("set").append(col.get(NAME).toString().replaceFirst("\\b(\\w)|\\s(\\w)", col.get(NAME).toString().substring(0, 1).toUpperCase()));
            sb.append("(");
            if (col.get(NAME).toString().equalsIgnoreCase("ID") || col.get(NAME).toString().toUpperCase().endsWith("_ID")) {
                sb.append("Long");
            } else if (Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) ||
                    Class.forName(col.get(CLASS).toString()) == Timestamp.class) {
                sb.append("Date");
            } else if (getClassName(col.get(NAME).toString()).equals(Class.forName(col.get(CLASS).toString()).getSimpleName())) {
                sb.append(col.get(CLASS));
            } else {
                sb.append(Class.forName(col.get(CLASS).toString()).getSimpleName());
            }
            sb.append(" ").append(col.get(NAME).toString());
            sb.append(") {");
            sb.append(ENTER);
            sb.append(TAB);
            sb.append(TAB);
            sb.append("this.").append(col.get(NAME).toString()).append(" = ").append(col.get(NAME).toString()).append(";");
            sb.append(ENTER);
            sb.append(TAB);
            sb.append("}");
            sb.append(ENTER);
        }
        sb.append("}");
        sb.append(ENTER);
        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/entity/" + className + ".java", sb.toString());
    }

    /***
     * 生成视图类的代码
     * @param table
     * @throws Exception
     */
    public void createVOClass(String table,String modules) throws Exception {
        String tableConstantName = getTableConstantName(table);

        String className = getClassName(tableConstantName);
        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".vo;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("import java.util.*;");
        sb.append("import java.io.Serializable;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
        sb.append(" * @Version:").append(Version).append(ENTER);
        sb.append(" * @Description:").append(className).append("视图类").append(ENTER);
        sb.append(" * @Email:").append(myEmail).append("\n*/");
        sb.append(ENTER);
        sb.append("public class " + className + "VO implements Serializable{");
        sb.append(ENTER);
        sb.append(TAB);
        sb.append("private static final long serialVersionUID = 1L;");
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            sb.append(ENTER);
            sb.append(TAB);
            sb.append("private ");
            if (Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) || Class.forName(col.get(CLASS).toString()) == Timestamp.class) {
                sb.append("Date");
            } else if (col.get(TYPE).toString().equalsIgnoreCase("TINYINT")) {
                sb.append("Byte");
            } else if (getClassName(col.get(NAME).toString()).equals(Class.forName(col.get(CLASS).toString()).getSimpleName())) {
                sb.append(col.get(CLASS));
            } else {
                sb.append(Class.forName(col.get(CLASS).toString()).getSimpleName());
            }
            sb.append(" " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ";");
            sb.append(ENTER);
        }
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            String type = "";
            if (Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) || Class.forName(col.get(CLASS).toString()) == Timestamp.class) {
                type = "Date";
            } else if (col.get(TYPE).toString().equalsIgnoreCase("TINYINT")) {
                type = "Byte";
            } else if (getClassName(col.get(NAME).toString()).equals(Class.forName(col.get(CLASS).toString()).getSimpleName())) {
                type = col.get(CLASS).toString();
            } else {
                type = Class.forName(col.get(CLASS).toString()).getSimpleName();
            }
            sb.append(TAB);
            sb.append("public " + type + " get" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "() {\n" +
                    "        return " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ";\n" +
                    "    }\n" +
                    "\n" +
                    "    public void set" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "(" + type + " " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ") {\n" +
                    "        this." + StringUtils.underline2Camel(col.get(NAME).toString(), true) + " = " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ";\n" +
                    "    }").append(ENTER).append(TAB);
            sb.append(ENTER);
        }
        sb.append("}");
        sb.append(ENTER);
        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/entity/" + className + "VO.java", sb.toString());
    }

    /***
     * 生成视图类的代码
     * @param table
     * @throws Exception
     */
    public void createListDTOClass(String table,String modules) throws Exception {
        String tableConstantName = getTableConstantName(table);

        String className = getClassName(tableConstantName);
        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".dto;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("import com.bon.common.domain.dto.PageDTO;\n" +
                "import com.bon.modules.sys.domain.entity.User;\n");
        sb.append("import java.io.Serializable;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
        sb.append(" * @Version:").append(Version).append(ENTER);
        sb.append(" * @Description:").append(className).append("列表参数类").append(ENTER);
        sb.append(" * @Email:").append(myEmail).append("\n*/");
        sb.append(ENTER);
        sb.append("public class " + className + "DTO extends PageDTO<User> implements Serializable{");
        sb.append(ENTER);
        sb.append(TAB);
        sb.append("private static final long serialVersionUID = 1L;");
        sb.append(ENTER);

        sb.append("}");
        sb.append(ENTER);
        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/entity/" + className + "DTO.java", sb.toString());
    }

//    /***
//     * 生成dao接口interface类的代码
//     * @param table
//     * @throws Exception
//     */
//    public void createDaoClass(String table) throws Exception {
//        String className = getClassName(getTableConstantName(table));
//
//        String objectName = StringUtils.uncapitalize(className);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("package " + ROOT_PACKAGE + ".dao;").append(ENTER);
//        sb.append("import java.io.Serializable;").append(ENTER);
//        sb.append("import java.util.List;").append(ENTER);
//        sb.append("import com.flong.commons.persistence.bean.SimplePage;").append(ENTER);
//        sb.append("import com.flong.commons.persistence.dao.EntityDao;").append(ENTER);
//        sb.append("import com.flong.modules.pojo." + className + ";").append(ENTER);
//
//        sb.append(ENTER);
//        sb.append(ENTER);
//        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
//        sb.append(" * @Version:").append(Version).append(ENTER);
//        sb.append(" * @Description:").append(className).append(ENTER);
//        sb.append(" * @Email:").append(myEmail).append("\n*/");
//        sb.append(ENTER);
//
//        sb.append("public interface " + className + "Dao extends EntityDao<" + className + "> {").append(ENTER);
//
//
//        sb.append("/**查询*/").append(ENTER);
//        sb.append(" public List<" + className + "> list(SimplePage simplePage," + className + " " + objectName + ");").append(ENTER);
//
//        sb.append("/**保存数据*/").append(ENTER);
//        sb.append(" public void saveData(" + className + " " + objectName + ");").append(ENTER);
//
//        sb.append("/**更新数据*/").append(ENTER);
//
//        sb.append(" public void updateData(" + className + " " + objectName + ");").append(ENTER);
//
//        sb.append("/**删除数据*/").append(ENTER);
//
//        sb.append(" public void deleteData(Long pk);").append(ENTER);
//
//        sb.append(ENTER);
//        sb.append(ENTER);
//        sb.append("}");
//        sb.append(ENTER);
//        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/dao/" + className + "Dao.java", sb.toString());
//    }
//
//    /***
//     * 生成dao的实现类的代码
//     * @param table
//     * @throws Exception
//     */
//    public void createDaoImplClass(String table) throws Exception {
//        String className = getClassName(getTableConstantName(table));
//        String objectName = StringUtils.uncapitalize(className);
//        String tableName = StringUtils.lowerCase(getTableConstantName(table));//获取表名
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("package " + ROOT_PACKAGE + ".dao.impl;").append(ENTER);
//        sb.append("import java.io.Serializable;").append(ENTER);
//        sb.append("import org.apache.commons.lang3.StringUtils;").append(ENTER);
//        sb.append("import org.springframework.stereotype.Repository;").append(ENTER);
//        sb.append("import com.flong.commons.persistence.bean.SimplePage;").append(ENTER);
//        sb.append("import com.flong.commons.persistence.dao.impl.EntityDaoSupport;").append(ENTER);
//        sb.append("import com.flong.modules.dao." + className + "Dao;").append(ENTER);
//        sb.append("import com.flong.modules.pojo." + className + ";").append(ENTER);
//
//
//        sb.append(ENTER);
//        sb.append(ENTER);
//        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
//        sb.append(" * @Version:").append(Version).append(ENTER);
//        sb.append(" * @Description:").append(className).append(ENTER);
//        sb.append(" * @Email:").append(myEmail).append("\n*/");
//        sb.append(ENTER);
//        sb.append("@Repository");//这个是spring的注解
//        sb.append(ENTER);
//        sb.append("public class " + className + "DaoImpl extends EntityDaoSupport<" + className + "> implements " + className + "Dao {");
//
//
//        sb.append("/**查询*/").append(ENTER);
//        sb.append(" public List<" + className + "> list(SimplePage simplePage," + className + " " + objectName + "){").append(ENTER);
//
//        sb.append(ENTER);
//
//        String mergeField = "";//合并字段.
//
//        //--遍历获取列,并拼接字符串,SQL的查询列,查询不建议用*去查询表的所有列.
//        for (Map<String, Object> col : getCols(table)) {
//
//            //
//            if (col.get(NAME).toString() != null) {
//                mergeField += col.get(NAME).toString() + ",";//合并字段并用,隔开字段名
//            }
//        }
//        //去掉最后一个,号然后拼接成一个完成的select查询字段
//        if (mergeField != null) {
//            mergeField = mergeField.substring(0, mergeField.length() - 1);
//        }
//
//        sb.append("    String sql = ").append("\" select " + mergeField + " from ").append(tableName).append(" where 1=1 \" ").append(ENTER);//这个TABLE是实体类的变量
//        //daoQuery这个是底层封装的一个接口,自个可以更加自己需求封装.
//        sb.append("    List<" + className + "> list= daoQuery.query(sql," + className + ".class,simplePage);").append(ENTER);
//        sb.append(" return list;").append(ENTER);
//
//        sb.append("}").append(ENTER);//查询的结束{
//
//
//        sb.append("/**保存数据*/").append(ENTER);
//        sb.append(" public void saveData(" + className + " " + objectName + "){").append(ENTER);
//        sb.append("   try {").append(ENTER);
//        sb.append("	     saveOrUpdate(" + className + ");").append(ENTER);
//        sb.append("   } catch (DaoAccessException e) {").append(ENTER);
//        sb.append("      e.printStackTrace();").append(ENTER);
//        sb.append("  }").append(ENTER);
//
//        sb.append("}");
//
//
//        sb.append("/**更新数据*/").append(ENTER);
//
//        sb.append(" public void updateData(" + className + " " + objectName + "){").append(ENTER);
//
//        sb.append("   try {").append(ENTER);
//        sb.append("	     saveOrUpdate(" + className + ");").append(ENTER);
//        sb.append("   } catch (DaoAccessException e) {").append(ENTER);
//        sb.append("      e.printStackTrace();").append(ENTER);
//        sb.append("  }").append(ENTER);
//
//        sb.append("}");
//
//
//        sb.append("/**删除数据*/").append(ENTER);
//        sb.append(" public void deleteData(Long pk){").append(ENTER);
//
//        sb.append("   try {").append(ENTER);
//        sb.append("	     delete(pk);").append(ENTER);
//        sb.append("   } catch (DaoAccessException e) {").append(ENTER);
//        sb.append("      e.printStackTrace();").append(ENTER);
//        sb.append("  }").append(ENTER);
//
//
//        sb.append("}");
//
//        sb.append(ENTER);
//        sb.append(ENTER);
//        sb.append("}");
//        sb.append(ENTER);
//        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/dao/impl/" + className + "DaoImpl.java", sb.toString());
//    }


    /***
     * 创建Service的接口
     * createServiceClass
     * @param table
     */
    public void createServiceClass(String table, String modules) {

        String className = getClassName(getTableConstantName(table));
        String objectName = StringUtils.uncapitalize(className);

        StringBuilder sb = new StringBuilder();

        sb.append("package " + ROOT_PACKAGE + ".service;");
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("import java.util.List;").append(ENTER);
        sb.append("import com.bon.common.domain.vo.PageVO;").append(ENTER);
        sb.append("import com.bon.modules." + modules + ".domain.dto.*;").append(ENTER);
        sb.append("import com.bon.modules." + modules + ".domain.entity." + className + ";").append(ENTER);
        sb.append("import com.bon.modules." + modules + ".domain.vo.*;").append(ENTER);

        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
        sb.append(" * @Version:").append(Version).append(ENTER);
        sb.append(" * @Description:").append(className).append(ENTER);
        sb.append(" * @Email:").append(myEmail).append("\n*/");
        sb.append(ENTER);
        sb.append("public interface " + className + "Service {");
        sb.append(ENTER).append(TAB);

        sb.append("/**查询单个*/").append(ENTER).append(TAB);
        sb.append(" public void get" + className + "(Long id);").append(ENTER).append(TAB);

        sb.append("/**查询列表*/").append(ENTER).append(TAB);
        sb.append(" public PageVO list" + className + "(" + className + "ListDTO dto);").append(ENTER).append(TAB);

        sb.append("/**保存数据*/").append(ENTER).append(TAB);
        sb.append(" public void save" + className + "(" + className + "DTO dto);").append(ENTER).append(TAB);

        sb.append("/**更新数据*/").append(ENTER).append(TAB);
        sb.append(" public void update" + className + "(" + className + "DTO dto);").append(ENTER).append(TAB);

        sb.append("/**删除数据*/").append(ENTER).append(TAB);
        sb.append(" public void delete" + className + "(Long id);").append(ENTER).append(TAB);

        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("}");
        sb.append(ENTER);
        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/service/" + className + "Service.java", sb.toString());

    }

    /***
     * 创建Service层的实现类
     * createServiceImplClass
     * @param table
     */
    public void createServiceImplClass(String table, String modules) {

        String className = getClassName(getTableConstantName(table));

        String objectName = StringUtils.uncapitalize(className);

        StringBuilder sb = new StringBuilder();

        sb.append("package " + ROOT_PACKAGE + ".service.impl;");
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("import java.io.Serializable;").append(ENTER);
        sb.append("import java.util.List;").append(ENTER);
        sb.append("import com.bon.modules." + modules + ".domain.dto.*;").append(ENTER);
        sb.append("import com.bon.modules." + modules + ".domain.vo.*;").append(ENTER);
        sb.append("import com.bon.modules." + modules + ".domain.entity.*;").append(ENTER);
        sb.append("import com.bon.modules." + modules + ".dao.*;").append(ENTER);
        sb.append("import com.bon.modules." + modules + ".service.*;").append(ENTER);
        sb.append("import org.springframework.transaction.annotation.Transactional;").append(ENTER);
        sb.append("import org.springframework.beans.factory.annotation.Autowired;").append(ENTER);
        sb.append("import org.springframework.stereotype.Service;").append(ENTER);

        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
        sb.append(" * @Version:").append(Version).append(ENTER);
        sb.append(" * @Description:").append(className).append(ENTER);
        sb.append(" * @Email:").append(myEmail).append("\n*/");
        sb.append(ENTER);
        sb.append("@Service");
        sb.append("@Transactional");
        sb.append("public class " + className + "ServiceImpl implements " + className + "Service {").append(ENTER);
        sb.append(ENTER).append(TAB);

        sb.append("@Autowired").append(ENTER).append(TAB);
        sb.append("private " + className + "Dao " + objectName + "Dao;").append(ENTER);
        sb.append(ENTER).append(TAB);

        sb.append("/**查询单个*/").append(ENTER).append(TAB);
        sb.append("@Override\n" +
                "    public " + className + "VO get" + className + "(Long id) {\n" +
                "        " + className + " " + objectName + " = " + objectName + "Mapper.selectByPrimaryKey(id);\n" +
                "        " + className + "VO vo = new " + className + "VO();\n" +
                "        BeanUtil.copyPropertys(" + objectName + ", vo);\n" +
                "        return vo;\n" +
                "    }").append(ENTER).append(TAB);

        sb.append("/**查询列表*/").append(ENTER).append(TAB);
        sb.append("  @Override\n" +
                "    public PageVO list" + className + "(" + className + "ListDTO dto){\n" +
                "        PageHelper.startPage(dto);\n" +
                "        List<" + className + "> list = " + objectName + "Mapper.selectByExample(dto.createExample());\n" +
                "        PageVO pageVO = new PageVO(list);\n" +
                "        List<RoleVO> voList = new ArrayList<>();for (" + className + " " + objectName + " : list) {\n" +
                "            " + className + "VO vo = new " + className + "VO();\n" +
                "            BeanUtil.copyPropertys(" + objectName + ", vo);\n" +
                "            voList.add(vo);\n" +
                "        }\n" +
                "        pageVO.setList(voList);\n" +
                "        return pageVO;\n" +
                "    }").append(ENTER).append(TAB);

        sb.append("/**保存数据*/").append(ENTER).append(TAB);
        sb.append("@Override\n" +
                "    public void save" + className + "(" + className + "DTO dto) {\n" +
                "        " + className + " " + objectName + " = new " + className + "();\n" +
                "        " + objectName + ".set" + className + "Id(null);\n" +
                "        " + objectName + ".setGmtCreate(new Date());\n" +
                "        " + objectName + ".setGmtModified(new Date());\n" +
                "        BeanUtil.copyPropertys(dto, " + objectName + ");\n" +
                "        " + objectName + "Mapper.insertSelective(" + objectName + ");\n" +
                "    }").append(ENTER).append(TAB);

        sb.append("/**更新数据*/").append(ENTER).append(TAB);
        sb.append("@Override\n" +
                "    public void update" + className + "(" + className + "DTO dto) {\n" +
                "        " + className + " " + objectName + " = " + objectName + "Mapper.selectByPrimaryKey(dto.get" + className + "Id());\n" +
                "        if (" + objectName + " == null) {\n" +
                "            throw new BusinessException(\"获取信息失败\");\n" +
                "        }\n" +
                "        " + objectName + ".setGmtModified(new Date());\n" +
                "        BeanUtil.copyPropertys(dto, " + objectName + ");\n" +
                "        " + objectName + "Mapper.updateByPrimaryKeySelective(" + objectName + ");\n" +
                "    }").append(ENTER).append(TAB);

        sb.append("/**删除数据*/").append(ENTER).append(TAB);
        sb.append("@Override\n" +
                "    public void delete" + className + "(Long id) {\n" +
                "        " + objectName + "Mapper.deleteByPrimaryKey(id);\n" +
                "    }").append(ENTER).append(TAB);


        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("}");
        sb.append(ENTER);
        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/service/impl/" + className + "ServiceImpl.java", sb.toString());

    }


    /***
     * 创建控制层类Controller
     * @param table
     */
    public void createControllerClass(String table) {
        //类名
        String className = getClassName(getTableConstantName(table));
        //通过 org.apache.commons.lang3.StringUtils的uncapitalize方法把类名第一个字母转换成小写
        String objectName = StringUtils.uncapitalize(className);

        //通过 org.apache.commons.lang3.StringUtils的lowerCase方法把类名整个单词转化成小写然后为springmvc的路径返回jsp请求.
        String BASE_PATH = "modules/" + StringUtils.lowerCase(className) + "/";//modules+模块名

        StringBuilder sb = new StringBuilder();
        /*******处理这个导入需要的类*********/
        sb.append("import java.util.List;").append(ENTER);
        sb.append("import javax.servlet.http.HttpServletRequest;").append(ENTER);
        sb.append("import javax.servlet.http.HttpServletResponse;").append(ENTER);
        sb.append("import org.springframework.beans.factory.annotation.Autowired;").append(ENTER);
        sb.append("import org.springframework.stereotype.Controller;").append(ENTER);
        sb.append("import org.springframework.web.bind.annotation.RequestMapping;").append(ENTER);
        sb.append("import com.flong.commons.persistence.bean.SimplePage;").append(ENTER);
        sb.append("import com.flong.commons.web.BaseController;").append(ENTER);
        sb.append("import com.flong.modules.pojo." + className + ";").append(ENTER);
        sb.append("import com.flong.modules.service." + className + "Service;").append(ENTER);

        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
        sb.append(" * @Version:").append(Version).append(ENTER);
        sb.append(" * @Description:").append(className).append(ENTER);
        sb.append(" * @Email:").append(myEmail).append("\n*/");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("@Controller").append(ENTER);
        sb.append("@RequestMapping(\"" + StringUtils.lowerCase(className) + "\")");
        sb.append(ENTER);
        sb.append("public class " + className + "Controller extends BaseController {");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append(" @Autowired " + className + "Service " + className + "Service");//注入Service层的接口Name
        sb.append(ENTER);

        //创建一个默认的查询..
        sb.append(ENTER);
        sb.append("   @RequestMapping(value=\"list\")").append(ENTER);
        sb.append("   public String list(" + className + " " + objectName + ",SimplePage simplePage ,HttpServletRequest request ,HttpServletResponse response){");
        sb.append(ENTER);
        sb.append("         List<" + className + "> list = " + className + "Service.list(simplePage, " + objectName + ");");
        sb.append(ENTER);
        sb.append("	     request.setAttribute(\"" + objectName + "\", object);");
        sb.append(ENTER);
        sb.append("		 request.setAttribute(\"page\", simplePage);");
        sb.append(ENTER);
        sb.append("		 request.setAttribute(\"list\", list);");
        sb.append(ENTER);
        sb.append("		 return \"" + BASE_PATH + "list\";");
        sb.append(ENTER);
        sb.append("   }");

        sb.append(ENTER);
        sb.append("}");
        sb.append(ENTER);
        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/controller/" + className + "Controller.java", sb.toString());


    }


    /***
     * 创建JSP页面.
     * 以bootstrap3.x为主.
     * @param table
     */
    public void createJspView(String table) throws Exception {

        String tableConstantName = getTableConstantName(table);

        String className = getClassName(tableConstantName);//获取类名
        //通过 org.apache.commons.lang3.StringUtils的uncapitalize方法把类名第一个字母转换成小写
        String objectName = StringUtils.uncapitalize(className);

        StringBuilder sb = new StringBuilder();

        sb.append(" <%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\"  pageEncoding=\"UTF-8\"%>").append(ENTER);
        //这个就标注一下，这个taglib.jsp文件是JSTL的EL表达式，Spring 标签，自定义标签，等的文件。
        sb.append("  <%@ include file=\"/WEB-INF/views/include/taglib.jsp\"%>").append(ENTER);
        sb.append(" <!DOCTYPE htm>").append(ENTER);
        sb.append(" <html>").append(ENTER);
        sb.append(" <head>").append(ENTER);
        //添加一个插件公共的文件，这个我就不一一备注
        sb.append("  <%@ include file=\"/WEB-INF/views/include/meta.jsp\"%>").append(ENTER);
        sb.append("  <%@ include file=\"/WEB-INF/views/include/include.jsp\"%>").append(ENTER);
        sb.append(" <title></title>").append(ENTER);
        /**=======================添加style===Begin====================**/
        sb.append(" <style>").append(ENTER);
        sb.append(" 	.breadcrumb{").append(ENTER);
        sb.append(" 		background-color: #fff;").append(ENTER);
        sb.append(" 	}").append(ENTER);
        sb.append(" 	.form-search{").append(ENTER);
        sb.append(" 		background-color: #fff;").append(ENTER);
        sb.append(" }").append(ENTER);
        sb.append(" .form-search1{").append(ENTER);
        sb.append(" 	    padding: 8px 15px;").append(ENTER);
        sb.append(" 		background-color: #f5f5f5;").append(ENTER);
        sb.append(" 	}").append(ENTER);
        sb.append(" </style>").append(ENTER);
        sb.append(" </head>").append(ENTER);
        /**=======================添加style===End====================**/

        sb.append("<body>").append(ENTER);
        sb.append("<ul class=\"nav nav-tabs\">").append(ENTER);
        sb.append("<li class=\"active\"><a href=\"${basePath}" + StringUtils.lowerCase(className) + "/list\">" + className + "列表</a></li>").append(ENTER);
        sb.append("</ul>").append(ENTER);
        sb.append(" <form:form id=\"searchForm\" modelAttribute=\"" + className + "\" action=\"${basePath}" + StringUtils.lowerCase(className) + "/list\" method=\"post\" class=\"breadcrumb form-search form-inline\">").append(ENTER);
        sb.append("  <div style=\"margin-bottom: 20px;\" class=\"form-search1\">").append(ENTER);

        //这里可以判断数据库的字段的类型做变量弄处理条件查询.

        for (Map<String, Object> col : getCols(table)) {

            //判断如果是数据库表的字段是DateTime类型的就设值My97DatePicker插件上,方便大家使用.
            if (Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) || Class.forName(col.get(CLASS).toString()) == Timestamp.class) {
                sb.append("<input id=\"" + col.get(NAME).toString() + "\" name=\"" + col.get(NAME).toString() + "\" type=\"text\" readonly=\"readonly\" maxlength=\"20\" class=\"Wdate\"").append(ENTER);
                //在这里用了$是为了查询的时候保留值.
                sb.append(" value=\"<fmt:formatDate value=\"${" + className + "." + col.get(NAME).toString() + "}\" pattern=\"yyyy-MM-dd HH:mm:ss\"/>\"").append(ENTER);
                sb.append(" onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});\"/>").append(ENTER);
            } else if (getClassName(col.get(NAME).toString()).equals(Class.forName(col.get(CLASS).toString()).getSimpleName())) {
                //form:input是spring架构的input标签path必须要等于实体类要有的属性.否则会报错.placeholder是html5有的占位符的属性,
                //htmlEscape也是spring的有属性.在这个jar下面,因为我这个工程是用maven搭建的，所有拷贝的时候,拷贝的时候它带上路径.方便大家伙找jar,而且我在这的spring是用3.x
                //C:\Users\liangjilong\.m2\repository\org\springframework\org.springframework.web.servlet\3.1.1.RELEASE\org.springframework.web.servlet-3.1.1.RELEASE.jar
                //org.springframework.web.servlet-3.1.1.RELEASE.jar这个文件下面有一个spring-from.tld文件，可以找到path,htmlEscape等属性.
                sb.append("   <label>" + col.get(NAME).toString() + " ：</label><form:input path=\"" + col.get(NAME).toString() + "\" htmlEscape=\"false\" maxlength=\"50\" class=\"input-medium form-control\" placeholder=\"" + col.get(NAME).toString() + "\"/>").append(ENTER);
            } else {
                sb.append("   <label>" + col.get(NAME).toString() + " ：</label><form:input path=\"" + col.get(NAME).toString() + "\" htmlEscape=\"false\" maxlength=\"50\" class=\"input-medium form-control\" placeholder=\"" + col.get(NAME).toString() + "\"/>").append(ENTER);
            }
            sb.append(ENTER);
        }
        //btn btn-info这个样式用过bootstrap的人都知道这个是.
        sb.append("     <input id=\"btnSubmit\" class=\"btn btn-info\" type=\"submit\" value=\"查询\"/>").append(ENTER);
        sb.append("  </div>").append(ENTER);

        sb.append("<table id=\"contentTable\" class=\"table table-striped table-bordered table-hover\">").append(ENTER);
        sb.append("<thead>").append(ENTER);//thead标签End
        sb.append("<tr>").append(ENTER);//tr标签End
        /*******遍历列表的th的列*****/
        for (Map<String, Object> col : getTableRemarks(table)) {
            for (String k : col.keySet()) {
                String colName = col.get(k).toString();
                sb.append("<th>").append(colName).append("</th>");
                sb.append(ENTER);
            }
        }
        sb.append("<th>操作</th> ");
        sb.append(ENTER);

        sb.append("</tr>").append(ENTER);
        sb.append("</thead>").append(ENTER);

        sb.append("<tbody>").append(ENTER);

        /*******遍历列表的td的列*****/
        sb.append("   <c:forEach items=\"${list}\" var=\"" + objectName + "\" varStatus=\"row\">").append(ENTER);
        sb.append("		<tr>").append(ENTER);
        sb.append("		<td>${row.index+1 }</td>").append(ENTER);

        for (Map<String, Object> col : getCols(table)) {
            sb.append("         <td>");
            if (Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) || Class.forName(col.get(CLASS).toString()) == Timestamp.class) {
                //如果是Date类型就转换用EL表达式格式化fmt:formatDate
                sb.append("<fmt:formatDate value=\"${" + objectName + "." + col.get(NAME).toString() + "}\"  type=\"date\" dateStyle=\"long\"/>");
            } else if (getClassName(col.get(NAME).toString()).equals(Class.forName(col.get(CLASS).toString()).getSimpleName())) {
                sb.append(" ${" + objectName + "." + col.get(NAME).toString() + "}");
            } else {
                sb.append(" ${" + objectName + "." + col.get(NAME).toString() + "}");
            }
            sb.append("</td>");
            sb.append(ENTER);
        }

        sb.append("    </tr>").append(ENTER);
        sb.append("   </c:forEach>").append(ENTER);
        sb.append("</tbody>").append(ENTER);//tbody标签结束.

        sb.append("</table>").append(ENTER);
        //这个是pagination.jsp是分页文件.
        sb.append("<%@ include file=\"/WEB-INF/views/include/pagination.jsp\"%>").append(ENTER);
        sb.append("</form:form>").append(ENTER);//form:form标签结束.
        sb.append("</body>").append(ENTER);//body标签结束.
        sb.append("</html>").append(ENTER);//html标签结束.
        sb.append(ENTER);
        sb.append(ENTER);
        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/jsp/" + className + ".jsp", sb.toString());

    }


    /***
     * 创建表的类定义常量
     * @param tables
     */
    public void createTableClass(List<String> tables) {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".domain;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n * @Created：" + NOW_DATE + "\n * @Author " + AUTHOR + "\n");
        sb.append(" * @Version:").append(Version).append(ENTER);
        sb.append(" * @Email:").append(myEmail).append("\n*/");
        sb.append(ENTER);
        sb.append("public interface Table {");
        sb.append(ENTER);
        for (String table : tables) {
            sb.append(TAB);
            sb.append("String " + getTableConstantName(table) + " = \"" + table.toUpperCase() + "\";");
            sb.append(ENTER);
        }
        sb.append(ENTER);
        sb.append("}");
        sb.append(ENTER);
        FileUtils.save("output-code/" + ROOT_PACKAGE.replaceAll("\\.", "/") + "/domain/Table.java", sb.toString());
    }

    /***
     * 获取数据库表名
     * @return
     * @throws Exception
     */
    public List<String> getTables() throws Exception {
        List<Object> params = new ArrayList<Object>();
        System.out.println("===========" + DB_NAME);
        //params.add(DB_NAME);
        String dbname = DB_NAME;
        params.add(dbname);

        ResultSet rs = DBHelperUtils.query("select table_name from information_schema.tables where table_schema = ? order by table_name", params);
        List<String> tables = new ArrayList<String>();
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        return tables;
    }

    /***
     * 列名 类型 => 说明
     * TABLE_CAT String => 表 catalog
     * TABLE_SCHEM String => 表 schema
     * TABLE_NAME String => 表名
     * TABLE_TYPE String => 表类型
     * REMARKS String => 表注释
     * 获取表的列
     * @param table
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> getCols(String table) throws Exception {
        List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = DBHelperUtils.query("select * from " + table + " where 1 = 2", null).getMetaData();

        for (int i = 1; i <= md.getColumnCount(); i++) {
            Map<String, Object> col = new HashMap<String, Object>();
            cols.add(col);
            col.put(NAME, md.getColumnName(i));
            col.put(CLASS, md.getColumnClassName(i));
            col.put(SIZE, md.getColumnDisplaySize(i));
            col.put(REMARKS, md.getColumnName(i));
		/*	System.out.println("1"+ md.getCatalogName(i));
			System.out.println("2"+ md.getColumnClassName(i));
			System.out.println("3"+ md.getColumnDisplaySize(i));
			System.out.println("4"+ md.getColumnType(i));
			System.out.println("5"+ md.getSchemaName(i));
			System.out.println("6"+ md.getPrecision(i));
			System.out.println("7"+ md.getScale(i));*/

            String _type = null;
            String type = md.getColumnTypeName(i);
            if (type.equals("INT")) {
                _type = "INTEGER";
            } else if (type.equals("DATETIME")) {
                _type = "TIMESTAMP";
            } else {
                _type = type;
            }
            col.put(TYPE, _type);
        }
        return cols;
    }


    /**
     * 获取所有表
     *
     * @param conn
     * @throws SQLException
     */
    public static List<Map<String, Object>> getAllTable() throws SQLException {
        /**
         * 定义一个Lis
         */
        List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();
        Connection conn = DBHelperUtils.getInstance().getConnection();
        DatabaseMetaData metaData = conn.getMetaData();

        //这个是获取所有表.
        ResultSet rs = metaData.getTables(null, "%", "%", new String[]{"TABLE"});

        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");////这个是获取表名

            if (tableName != null) {
                Map<String, Object> col = new HashMap<String, Object>();
                // rs =getConnection.getMetaData().getColumns(null, getXMLConfig.getSchema(),tableName.toUpperCase(), "%");
                //其他数据库不需要这个方法的，直接传null，这个是oracle和db2这么用

                ResultSet rs1 = metaData.getColumns(null, "%", tableName, "%");

                while (rs1.next()) {
                    String COLUMN_NAME = rs1.getString("COLUMN_NAME");
                    String REMARKS = rs1.getString("REMARKS");
                    //先判断备注是否为空,不为空就取表的字段的注释说明，否则的话就去字段列名
                    if (REMARKS == null || REMARKS == "") {
                        col.put(COLUMN_NAME, COLUMN_NAME);
                    } else {
                        col.put(REMARKS, REMARKS);
                    }
                    cols.add(col);
                }
            }
        }
        return cols;
    }

    /***
     * 获取列的备注
     * @param table
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> getTableRemarks(String table) throws SQLException {

        List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();

        Connection conn = DBHelperUtils.getInstance().getConnection();
        DatabaseMetaData metaData = conn.getMetaData();

        ResultSet rs = metaData.getTables(null, "%", "%", new String[]{"TABLE"});

        while (rs.next()) {

            String tableName = rs.getString("TABLE_NAME");
            //传进来的表名和查询出来的表名作对比，并且是忽略大小写
            if (tableName != null) {
                if (table.equalsIgnoreCase(tableName)) {
                    Map<String, Object> col = new HashMap<String, Object>();
                    //Map<String, Object> col = new HashTable<String, Object>();
                    ResultSet rs1 = metaData.getColumns(null, "%", tableName, "%");
                    while (rs1.next()) {
                        String COLUMN_NAME = rs1.getString("COLUMN_NAME");
                        String REMARKS = rs1.getString("REMARKS");

                        //先判断备注是否为空,不为空就取表的字段的注释说明，否则的话就去字段列名

                        if (REMARKS == null || REMARKS == "") {
                            col.put(COLUMN_NAME, COLUMN_NAME);
                        } else {
                            col.put(REMARKS, REMARKS);
                        }
                        //去掉重复的数据
                        col = removeRepeatData();
                        cols.add(col);
                    }
                    break;
                }
            }
        }
        return cols;
    }

    /***
     * 去掉重复的数据
     * @return
     */
    private static Map<String, Object> removeRepeatData() {
        Map<String, Object> col = new HashMap<String, Object>();
        Set<String> keySet = col.keySet();
        for (String str : keySet) {
            col.put(str, str);
        }
        return col;
    }


    /***
     * 获取表的常量名，一般是在数据库建表的时候，写的注释..
     * @param table
     * @return
     */
    private String getTableConstantName(String table) {
        String tableConstantName = table.toUpperCase();
        for (String item : IGNORE_TABLE_PREFIX) {
            tableConstantName = tableConstantName.replaceAll("^" + item.toUpperCase(), "");
        }
        return tableConstantName;
    }

    /***
     * 获取类的名
     * @param name
     * @return
     */
    private String getClassName(String name) {
        String[] names = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String n : names) {
            if (n.length() == 0) {
                sb.append("_");
            } else {
                sb.append(n.substring(0, 1).toUpperCase());
                if (n.length() > 1) {
                    sb.append(n.substring(1).toLowerCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取字段名
     *
     * @param name
     * @return
     */
    private String getFieldName(String name) {
        String _name = getClassName(name);
        return _name.substring(0, 1).toLowerCase() + _name.substring(1);
    }


    /**
     * 转换成泛型Map
     *
     * @param limit
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<Map> toListMap(int limit, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = 0;
        List list = new ArrayList();
        while (rs.next()) {
            Map row = new HashMap();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                row.put(rsmd.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
            count++;
            if (count >= limit) {
                break;
            }
        }
        return list;
    }


    /***
     * 获取查询list
     * @param conn
     * @param sql
     * @param limit
     * @return
     * @throws SQLException
     */
    public static List<Map> queryForList(Connection conn, String sql, int limit) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql.trim());
        ps.setMaxRows(limit);
        ps.setFetchDirection(1000);
        ResultSet rs = ps.executeQuery();
        return toListMap(limit, rs);
    }

    /***
     * 获取查询list
     * @param conn
     * @param sql
     * @param limit
     * @return
     * @throws SQLException
     */
    public static List<Map> queryForList(String sql, int limit) throws SQLException {
        Connection conn = DBHelperUtils.getConnection();
        return queryForList(conn, sql, limit);
    }


    public static void main(String[] args) throws Exception {
        String sql = "select * from SYS_MENU ";
        //List<Map> queryForList = queryForList(sql, 1000);
		/*for(Map m:queryForList){
			System.out.println("======"+m);
		}*/

        String tableName = "SYS_MENU";//表名

        List<Map<String, Object>> tableRemarks = getTableRemarks(tableName);
        int i = 0;
        for (Map<String, Object> col : getTableRemarks(tableName)) {
            Set<String> keySet = col.keySet();
            for (Object str : keySet) {
                System.out.println(str);
            }
        }

        //new CodeGenerator().createJspView("sup_email");
    }
}
