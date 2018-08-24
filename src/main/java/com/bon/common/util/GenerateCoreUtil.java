package com.bon.common.util;


import com.sun.javafx.logging.PulseLogger;

import java.security.PublicKey;
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
public class GenerateCoreUtil {
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
    public static final String KEY = "KEY";//表键类型
    public static final String REMARKS = "REMARKS";//表注释
    public static final String TYPE = "TYPE";//表的类型
    public static final String SIZE = "SIZE";//大小
    public static final String CLASS = "CLASS";//类别

    public static String TABLE_COMMENT = "TABLE_COMMENT";//表注释
    public static String TABLE_PRIMARY_KEY = "";//表主键名称

    /*************************变量****End************************************/

    public static final String NOW_DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    /***************获取数据库的配置连接************/
    public static final String DB_NAME = GeneratePropertiesUtils.getValueByKey("generator.jdbc.url").substring(
            GeneratePropertiesUtils.getValueByKey("generator.jdbc.url").lastIndexOf("/") + 1,
            GeneratePropertiesUtils.getValueByKey("generator.jdbc.url").indexOf("?") == -1 ?
                    GeneratePropertiesUtils.getValueByKey("generator.jdbc.url").length() :
                    GeneratePropertiesUtils.getValueByKey("generator.jdbc.url").indexOf("?"));
    //从配置获取工程的报名路径
    public static final String ROOT_PACKAGE = GeneratePropertiesUtils.getValueByKey("basePackage");
    //获取作者.
    public static final String AUTHOR = GeneratePropertiesUtils.getValueByKey("author");
    //忽略表的后缀.
    public static final List<String> IGNORE_TABLE_PREFIX = new ArrayList<String>();

    /*******定义代码块*******/
    static {

    }

    /**
     * 生成所有文件
     *
     * @param tableName
     * @param modules
     * @throws Exception
     */
    public static void generateAll(String tableName, String modules) throws Exception {
        createEntityClass(tableName, modules);
        createDTOClass(tableName, modules);
        createVOClass(tableName, modules);
        createListDTOClass(tableName, modules);
        createServiceClass(tableName, modules);
        createServiceImplClass(tableName, modules);
        createMapperClass(tableName, modules);
        createMapperXML(tableName, modules);
        createControllerClass(tableName, modules);
    }

    /**
     * 生成所有文件
     *
     * @param tableName
     * @param modules
     * @throws Exception
     */
    public static void generateVUE(String tableName, String modules) throws Exception {
        createVUE(tableName, modules);
    }

    /***
     * 获取数据库表备注
     * @return
     * @throws Exception
     */
    public static String getTableComment(String table) {
        //获取表注释信息
        List<Object> params = new ArrayList<Object>();
        params.add(DB_NAME);
        params.add(table);
        ResultSet rsTable = GenerateDBUtils.query("select table_comment from information_schema.tables where table_schema = ? and table_name = ?", params);
        try {

            while (rsTable.next()) {
                TABLE_COMMENT = rsTable.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TABLE_COMMENT;
    }

    public static void getPrimaryField(String table) throws SQLException {
        List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = GenerateDBUtils.query("select * from " + table + " where 1 = 2", null).getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            ResultSet rs = GenerateDBUtils.query("show full columns from " + table + " where field = '" + md.getColumnName(i) + "'", null);
            while (rs.next()) {
                if (null != rs.getString("Key") && rs.getString("Key").equals("PRI")) {
                    TABLE_PRIMARY_KEY = md.getColumnName(i);
                }
            }
        }
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
    private static List<Map<String, Object>> getCols(String table) throws Exception {

        //获取表字段信息
        List<Map<String, Object>> cols = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = GenerateDBUtils.query("select * from " + table + " where 1 = 2", null).getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            Map<String, Object> col = new HashMap<String, Object>();
            cols.add(col);
            col.put(NAME, md.getColumnName(i));
            col.put(CLASS, md.getColumnClassName(i));
            col.put(SIZE, md.getColumnDisplaySize(i));
            ResultSet rs = GenerateDBUtils.query("show full columns from " + table + " where field = '" + md.getColumnName(i) + "'", null);
            while (rs.next()) {
                col.put(KEY, rs.getString("Key"));
                col.put(REMARKS, rs.getString("Comment"));
            }
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
     * 获取字段类型
     *
     * @param col
     * @return
     * @throws ClassNotFoundException
     */
    public static String getType(Map col) throws ClassNotFoundException {
        String type = "";
        if (Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) || Class.forName(col.get(CLASS).toString()) == Timestamp.class) {
            type = "Date";
        } else if (col.get(TYPE).toString().equalsIgnoreCase("TINYINT")) {
            type = "Byte";
        } else if (StringUtils.underline2Camel(col.get(NAME).toString(), false).equals(Class.forName(col.get(CLASS).toString()).getSimpleName())) {
            type = col.get(CLASS).toString();
        } else {
            type = Class.forName(col.get(CLASS).toString()).getSimpleName();
        }
        return type;
    }

    /**
     * 生成get和set方法
     *
     * @param col
     * @return
     * @throws ClassNotFoundException
     */
    public static String generateGetAndSet(Map col) throws ClassNotFoundException {
        String type = getType(col);
        StringBuilder sb = new StringBuilder();
        String string =
                "    public " + type + " get" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "() {\n" +
                        "        return " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ";\n" +
                        "    }\n" +
                        "\n" +
                        "    public void set" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "(" + type + " " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ") {\n" +
                        "        this." + StringUtils.underline2Camel(col.get(NAME).toString(), true) + " = " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ";\n" +
                        "    }";
        return string;
    }

    /***
     * 生成实体类的代码
     * @param table
     * @throws Exception
     */
    public static void createEntityClass(String table, String modules) throws Exception {
        String className = StringUtils.underline2Camel(table, false);
        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".modules." + modules + ".domain.entity;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("import java.util.*;\n" +
                "import java.io.Serializable;\n" +
                "import org.springframework.format.annotation.DateTimeFormat;\n" +
                "import com.fasterxml.jackson.annotation.JsonFormat;\n" +
                "import io.swagger.annotations.ApiModel;\n" +
                "import io.swagger.annotations.ApiModelProperty;\n" +
                "import javax.persistence.Id;\n" +
                "import javax.persistence.GeneratedValue;\n");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n" +
                " * @Created：" + NOW_DATE + "\n" +
                " * @Author " + AUTHOR + "\n" +
                " * @Version: " + Version + "\n" +
                " * @Description: " + className + "参数类\n" +
                " * @Email: " + myEmail + "\n" +
                "*/");
        sb.append(ENTER);
        sb.append("@ApiModel(value =\"" + className + "\",description = \"" + getTableComment(table) + "\")\n" +
                "public class " + className + " implements Serializable{");
        sb.append(ENTER).append(TAB);

        sb.append("private static final long serialVersionUID = 1L;");
        sb.append(ENTER);

        for (Map<String, Object> col : getCols(table)) {
            sb.append(ENTER).append(TAB);
            String type = getType(col);
            if (null != col.get(KEY) && col.get(KEY).toString().equals("PRI")) {
                sb.append("@Id\n" +
                        "    @GeneratedValue(generator = \"JDBC\")").append(ENTER).append(TAB);
            }
            sb.append("@ApiModelProperty(value = \"" + col.get(REMARKS) + "\")").append(ENTER).append(TAB);
            if (type.equals("Date")) {
                sb.append("@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")").append(ENTER).append(TAB);
            }
            sb.append("private " + type + " " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ";");
            sb.append(ENTER);
        }
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            sb.append(generateGetAndSet(col)).append(ENTER);
            sb.append(ENTER);
        }

        sb.append("}");
        sb.append(ENTER);
        String filePath = ROOT_PACKAGE + ".modules." + modules + ".domain.entity";
        GenerateFileUtils.save("src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + className + ".java", sb.toString());
    }

    /***
     * 生成视图类的代码
     * @param table
     * @throws Exception
     */
    public static void createVOClass(String table, String modules) throws Exception {
        String className = StringUtils.underline2Camel(table, false);
        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".modules." + modules + ".domain.vo;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("import java.util.*;\n" +
                "import java.io.Serializable;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n" +
                " * @Created：" + NOW_DATE + "\n" +
                " * @Author " + AUTHOR + "\n" +
                " * @Version: " + Version + "\n" +
                " * @Description: " + className + "视图类\n" +
                " * @Email: " + myEmail + "\n" +
                "*/");
        sb.append(ENTER);
        sb.append("public class " + className + "VO implements Serializable{");
        sb.append(ENTER).append(TAB);

        sb.append("private static final long serialVersionUID = 1L;");
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            sb.append(ENTER).append(TAB);
            String type = getType(col);
            sb.append("private " + type + " " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ";");
            sb.append(ENTER);
        }
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            sb.append(generateGetAndSet(col)).append(ENTER);
            sb.append(ENTER);
        }
        sb.append("}");
        sb.append(ENTER);
        String filePath = ROOT_PACKAGE + ".modules." + modules + ".domain.vo";
        GenerateFileUtils.save("src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + className + "VO.java", sb.toString());
    }


    /***
     * 生成实体参数类的代码
     * @param table
     * @throws Exception
     */
    public static void createDTOClass(String table, String modules) throws Exception {
        String className = StringUtils.underline2Camel(table, false);
        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".modules." + modules + ".domain.dto;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("import java.util.*;\n" +
                "import java.io.Serializable;\n" +
                "import io.swagger.annotations.ApiModel;\n" +
                "import io.swagger.annotations.ApiModelProperty;\n" +
                "import com.bon.common.domain.dto.BaseDTO;\n" +
                "import com.bon.modules." + modules + ".domain.entity." + className + ";");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n" +
                " * @Created：" + NOW_DATE + "\n" +
                " * @Author " + AUTHOR + "\n" +
                " * @Version: " + Version + "\n" +
                " * @Description: " + className + "参数类\n" +
                " * @Email: " + myEmail + "\n" +
                "*/");
        sb.append(ENTER);
        sb.append("@ApiModel(value =\"" + className + "DTO\")\n" +
                "public class " + className + "DTO extends BaseDTO<" + className + "> implements Serializable{");
        sb.append(ENTER).append(TAB);

        sb.append("private static final long serialVersionUID = 1L;");
        sb.append(ENTER);

        for (Map<String, Object> col : getCols(table)) {
            sb.append(ENTER).append(TAB);
            String type = getType(col);
            sb.append("@ApiModelProperty(value = \"" + col.get(REMARKS) + "\")").append(ENTER).append(TAB);
            sb.append("private " + type + " " + StringUtils.underline2Camel(col.get(NAME).toString(), true) + ";");
            sb.append(ENTER);
        }
        sb.append(ENTER);
        for (Map<String, Object> col : getCols(table)) {
            sb.append(generateGetAndSet(col)).append(ENTER);
            sb.append(ENTER);
        }

        sb.append("}");
        sb.append(ENTER);
        String filePath = ROOT_PACKAGE + ".modules." + modules + ".domain.dto";
        GenerateFileUtils.save("src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + className + "DTO.java", sb.toString());
    }

    /***
     * 生成列表参数类的代码
     * @param table
     * @throws Exception
     */
    public static void createListDTOClass(String table, String modules) throws Exception {
        String className = StringUtils.underline2Camel(table, false);
        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".modules." + modules + ".domain.dto;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("import com.bon.common.domain.dto.PageDTO;\n" +
                "import com.bon.modules." + modules + ".domain.entity." + className + ";\n" +
                "import java.io.Serializable;");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n" +
                " * @Created：" + NOW_DATE + "\n" +
                " * @Author " + AUTHOR + "\n" +
                " * @Version: " + Version + "\n" +
                " * @Description: " + className + "列表参数类\n" +
                " * @Email: " + myEmail + "\n" +
                "*/");
        sb.append(ENTER);

        sb.append("public class " + className + "ListDTO extends PageDTO<" + className + "> implements Serializable{");
        sb.append(ENTER).append(TAB);

        sb.append("private static final long serialVersionUID = 1L;");
        sb.append(ENTER);

        sb.append("}");
        sb.append(ENTER);
        String filePath = ROOT_PACKAGE + ".modules." + modules + ".domain.dto";
        GenerateFileUtils.save("src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + className + "ListDTO.java", sb.toString());
    }


    /***
     * 创建Service的接口
     * createServiceClass
     * @param table
     */
    public static void createServiceClass(String table, String modules) {

        String className = StringUtils.underline2Camel(table, false);
        String objectName = StringUtils.uncapitalize(className);

        StringBuilder sb = new StringBuilder();

        sb.append("package " + ROOT_PACKAGE + ".modules." + modules + ".service;");
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("import java.util.List;\n" +
                "import com.bon.common.domain.vo.PageVO;\n" +
                "import com.bon.modules." + modules + ".domain.dto.*;\n" +
                "import com.bon.modules." + modules + ".domain.entity." + className + ";\n" +
                "import com.bon.modules." + modules + ".domain.vo.*;\n").append(ENTER);
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("/**\n" +
                " * @Created：" + NOW_DATE + "\n" +
                " * @Author " + AUTHOR + "\n" +
                " * @Version: " + Version + "\n" +
                " * @Description: " + className + "服务接口类\n" +
                " * @Email: " + myEmail + "\n" +
                "*/");
        sb.append(ENTER);
        sb.append("public interface " + className + "Service {");
        sb.append(ENTER).append(TAB);

        sb.append("/**查询单个*/").append(ENTER).append(TAB);
        sb.append(" public " + className + "VO get" + className + "(Long id);").append(ENTER).append(TAB);

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
        String filePath = ROOT_PACKAGE + ".modules." + modules + ".service";
        GenerateFileUtils.save("src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + className + "Service.java", sb.toString());

    }

    /***
     * 创建Service层的实现类
     * createServiceImplClass
     * @param table
     */
    public static void createServiceImplClass(String table, String modules) {

        String className = StringUtils.underline2Camel(table, false);

        String objectName = StringUtils.uncapitalize(className);

        StringBuilder sb = new StringBuilder();

        sb.append("package " + ROOT_PACKAGE + ".modules." + modules + ".service.impl;");
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("import java.io.Serializable;\n" +
                "import java.util.*;\n" +
                "import com.bon.common.domain.vo.PageVO;\n" +
                "import com.bon.common.exception.BusinessException;\n" +
                "import com.bon.common.util.BeanUtil;\n" +
                "import com.github.pagehelper.PageHelper;\n" +
                "import com.bon.modules." + modules + ".domain.dto.*;\n" +
                "import com.bon.modules." + modules + ".domain.vo.*;\n" +
                "import com.bon.modules." + modules + ".domain.entity.*;\n" +
                "import com.bon.modules." + modules + ".dao.*;\n" +
                "import com.bon.modules." + modules + ".service.*;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Service;").append(ENTER);
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("/**\n" +
                " * @Created：" + NOW_DATE + "\n" +
                " * @Author " + AUTHOR + "\n" +
                " * @Version: " + Version + "\n" +
                " * @Description: " + className + "服务实现类\n" +
                " * @Email: " + myEmail + "\n" +
                "*/");
        sb.append(ENTER);

        sb.append("@Service\n" +
                "@Transactional\n" +
                "public class " + className + "ServiceImpl implements " + className + "Service {").append(ENTER);
        sb.append(ENTER).append(TAB);

        sb.append("@Autowired").append(ENTER).append(TAB);
        sb.append("private " + className + "Mapper " + objectName + "Mapper;").append(ENTER);
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
                "        List<" + className + "VO> voList = new ArrayList<>();for (" + className + " " + objectName + " : list) {\n" +
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
                "        BeanUtil.copyPropertys(dto, " + objectName + ");\n" +
                "        " + objectName + ".set" + className + "Id(null);\n" +
                "        " + objectName + ".setGmtCreate(new Date());\n" +
                "        " + objectName + ".setGmtModified(new Date());\n" +
                "        " + objectName + "Mapper.insertSelective(" + objectName + ");\n" +
                "    }").append(ENTER).append(TAB);

        sb.append("/**更新数据*/").append(ENTER).append(TAB);
        sb.append("@Override\n" +
                "    public void update" + className + "(" + className + "DTO dto) {\n" +
                "        " + className + " " + objectName + " = " + objectName + "Mapper.selectByPrimaryKey(dto.get" + className + "Id());\n" +
                "        if (" + objectName + " == null) {\n" +
                "            throw new BusinessException(\"获取信息失败\");\n" +
                "        }\n" +
                "        BeanUtil.copyPropertys(dto, " + objectName + ");\n" +
                "        " + objectName + ".setGmtModified(new Date());\n" +
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
        String filePath = ROOT_PACKAGE + ".modules." + modules + ".service.impl";
        GenerateFileUtils.save("src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + className + "ServiceImpl.java", sb.toString());

    }

    /***
     * 创建Dao的接口
     * createServiceClass
     * @param table
     */
    public static void createMapperClass(String table, String modules) {

        String className = StringUtils.underline2Camel(table, false);
        String objectName = StringUtils.uncapitalize(className);
        StringBuilder sb = new StringBuilder();

        sb.append("package " + ROOT_PACKAGE + ".modules." + modules + ".dao;");
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("import tk.mybatis.mapper.common.Mapper;\n" +
                "import com.bon.modules." + modules + ".domain.entity." + className + ";").append(ENTER);
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("/**\n" +
                " * @Created：" + NOW_DATE + "\n" +
                " * @Author " + AUTHOR + "\n" +
                " * @Version: " + Version + "\n" +
                " * @Description: " + className + "服务接口类\n" +
                " * @Email: " + myEmail + "\n" +
                "*/");
        sb.append(ENTER);
        sb.append("public interface " + className + "Mapper extends Mapper<" + className + "> {");

        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("}");
        sb.append(ENTER);
        String filePath = ROOT_PACKAGE + ".modules." + modules + ".dao";
        GenerateFileUtils.save("src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + className + "Mapper.java", sb.toString());

    }

    /***
     * 创建mapper xml文件
     * createServiceClass
     * @param table
     */
    public static void createMapperXML(String table, String modules) {

        String className = StringUtils.underline2Camel(table, false);
        String objectName = StringUtils.uncapitalize(className);
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "<mapper namespace=\"com.bon.modules." + modules + ".dao." + className + "\">\n" +
                "</mapper>");
        sb.append(ENTER);
        GenerateFileUtils.save("src/main/resources/mapper/" + modules + "/" + className + "Mapper.xml", sb.toString());

    }


    /***
     * 创建控制层类Controller
     * @param table
     */
    public static void createControllerClass(String table, String modules) {
        //类名(从表名中获取，转化为驼峰并第一个字母为答谢)
        String className = StringUtils.underline2Camel(table, false);
        //通过 org.apache.commons.lang3.StringUtils的uncapitalize方法把类名第一个字母转换成小写
        String objectName = StringUtils.uncapitalize(className);

        StringBuilder sb = new StringBuilder();
        sb.append("package " + ROOT_PACKAGE + ".modules." + modules + ".controller;\n" +
                "import com.bon.common.domain.vo.PageVO;\n" +
                "import com.bon.common.domain.vo.ResultBody;\n" +
                "import com.bon.modules." + modules + ".domain.dto.*;\n" +
                "import com.bon.modules." + modules + ".domain.vo.*;\n" +
                "import com.bon.modules." + modules + ".service." + className + "Service;\n" +
                "import io.swagger.annotations.Api;\n" +
                "import io.swagger.annotations.ApiOperation;\n" +
                "import io.swagger.annotations.ApiResponse;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.apache.shiro.authz.annotation.RequiresPermissions;\n" +
                "import org.springframework.http.MediaType;\n" +
                "import org.springframework.web.bind.annotation.*;").append(ENTER);
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("/**\n" +
                " * @Created：" + NOW_DATE + "\n" +
                " * @Author " + AUTHOR + "\n" +
                " * @Version: " + Version + "\n" +
                " * @Description: " + className + "控制层\n" +
                " * @Email: " + myEmail + "\n" +
                "*/");
        sb.append(ENTER);

        sb.append("@Api(value = \"" + className + "\",description = \"" + getTableComment(table) + "\")\n" +
                "@RestController\n" +
                "@RequestMapping(\"/" + objectName + "\")\n" +
                "public class " + className + "Controller {");
        sb.append(ENTER);
        sb.append(ENTER);

        sb.append("    @Autowired\n" +
                "    private " + className + "Service " + objectName + "Service; ");//注入Service层的接口Name
        sb.append(ENTER);
        sb.append(ENTER);


        sb.append("    @ApiOperation(value = \"根据条件获取" + className + "列表\")\n" +
                "    @RequiresPermissions({\"url:" + objectName + ":list" + className + "\"})\n" +
                "    @PostMapping(value = \"/list\",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)\n" +
                "    public ResultBody list" + className + "(@RequestBody " + className + "ListDTO listDTO){\n" +
                "        PageVO pageVO = " + objectName + "Service.list" + className + "(listDTO);\n" +
                "        return new ResultBody(pageVO);\n" +
                "    }").append(ENTER);
        sb.append(ENTER);

        //创建单个查询
        sb.append("    @ApiOperation(value = \"获取" + className + "\")\n" +
                "    @RequiresPermissions({\"url:" + objectName + ":get" + className + "\"})\n" +
                "    @GetMapping(value = \"/get\")\n" +
                "    public ResultBody get" + className + "(@RequestParam Long key){\n" +
                "        " + className + "VO vo= " + objectName + "Service.get" + className + "(key);\n" +
                "        return new ResultBody(vo);\n" +
                "    }").append(ENTER);
        sb.append(ENTER);

        sb.append("    @ApiOperation(value = \"新增" + className + "\")\n" +
                "    @RequiresPermissions({\"url:" + objectName + ":save" + className + "\"})\n" +
                "    @PostMapping(value = \"/save\",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)\n" +
                "    public ResultBody save" + className + "(@RequestBody " + className + "DTO dto){\n" +
                "        " + objectName + "Service.save" + className + "(dto);\n" +
                "        return new ResultBody();\n" +
                "    }").append(ENTER);
        sb.append(ENTER);

        sb.append("    @ApiOperation(value = \"修改" + className + "\")\n" +
                "    @RequiresPermissions({\"url:" + objectName + ":update" + className + "\"})\n" +
                "    @PostMapping(value = \"/update\",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)\n" +
                "    public ResultBody update" + className + "(@RequestBody " + className + "DTO dto){\n" +
                "        " + objectName + "Service.update" + className + "(dto);\n" +
                "        return new ResultBody();\n" +
                "    }").append(ENTER);
        sb.append(ENTER);

        sb.append("    @ApiOperation(value = \"删除" + className + "\")\n" +
                "    @RequiresPermissions({\"url:" + objectName + ":delete" + className + "\"})\n" +
                "    @GetMapping(value = \"/delete\")\n" +
                "    public ResultBody delete" + className + "(@RequestParam Long key){\n" +
                "        " + objectName + "Service.delete" + className + "(key);\n" +
                "        return new ResultBody();\n" +
                "    }").append(ENTER);
        sb.append(ENTER);

        sb.append("}");
        sb.append(ENTER);
        String filePath = ROOT_PACKAGE + ".modules." + modules + ".controller";
        GenerateFileUtils.save("src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + className + "Controller.java", sb.toString());

    }

    /***
     * 创建VUE视图文件
     * @param table
     */
    public static void createVUE(String table, String modules) throws Exception {
        //类名(从表名中获取，转化为驼峰并第一个字母为大写)
        String className = StringUtils.underline2Camel(table, false);
        //通过 org.apache.commons.lang3.StringUtils的uncapitalize方法把类名第一个字母转换成小写
        String objectName = StringUtils.uncapitalize(className);
        List<Map<String, Object>> cols = getCols(table);

        StringBuilder sb = new StringBuilder();
        //条件过滤部分
        sb.append("<template>\n" +
                "  <div class=\"app-container calendar-list-container\">\n" );
                sb.append("<!--新增修改弹出框部分-->").append(ENTER);
                sb.append("    <div class=\"filter-container\">\n" +
                "      <el-input style=\"width: 200px;\" v-model=\"listParams.keyMap.equal_" + objectName + "Id\" class=\"filter-item\" placeholder=\"序号\">\n" +
                "      </el-input>\n" +
                "      <el-date-picker style=\"width: 200px;\" v-model=\"listParams.keyMap.greater_gmtCreate\" type=\"datetime\"\n" +
                "                      class=\"filter-item\" placeholder=\"起始时间\" value-format=\"yyyy-MM-dd HH:mm:ss\">\n" +
                "      </el-date-picker>  -\n" +
                "      <el-date-picker style=\"width: 200px;\" v-model=\"listParams.keyMap.less_gmtCreate\" type=\"datetime\"\n" +
                "                      class=\"filter-item\" placeholder=\"结束时间\" value-format=\"yyyy-MM-dd HH:mm:ss\">\n" +
                "      </el-date-picker>\n" +
                "      <el-button class=\"filter-item\" @click=\"getList\" style=\"margin-left: 10px;\" type=\"primary\" icon=\"el-icon-search\">\n" +
                "        搜索\n" +
                "      </el-button>\n" +
                "      <el-button @click=\"handleCreate\" class=\"filter-item\" style=\"margin-left: 10px;\" type=\"primary\" icon=\"el-icon-edit\">\n" +
                "        新增\n" +
                "      </el-button>\n" +
                "    </div>\n");
        sb.append(ENTER);
        sb.append(ENTER);

        //列表显示部分
        sb.append("<!--新增修改弹出框部分-->").append(ENTER);
        sb.append("    <el-table :data=\"pageInfo.list\" v-loading=\"listLoading\" element-loading-text=\"给我一点时间\" border fit\n" +
                "              highlight-current-row\n" +
                "              style=\"width: 100%\">\n" +
                "      <el-table-column type=\"index\" align=\"center\" label=\"序号\" width=\"65\">\n" +
                "      </el-table-column>\n");
        for (Map<String, Object> col : cols) {
            if((col.get(KEY)!=null&&col.get(KEY).toString().equals("PRI")) || col.get(NAME).toString().equals("gmt_modified")){
                sb.append("");
            }else {
                String type = getType(col);
                sb.append("      <el-table-column width=\"160px\" align=\"center\" label=\"" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "\">\n" +
                        "        <template slot-scope=\"scope\">\n");
                if (type.equals("Date")) {
                    sb.append("          <span>{{scope.row." + StringUtils.underline2Camel(col.get(NAME).toString(), true) + " | parseTime('{y}-{m}-{d} {h}:{i}:{s}')}}</span>\n");
                } else {
                    sb.append("          <span>{{scope.row." + StringUtils.underline2Camel(col.get(NAME).toString(), true) + "}}</span>\n");
                }
                sb.append("        </template>\n" +
                        "      </el-table-column>\n");
            }
        }
        sb.append("      <el-table-column align=\"center\" label=\"操作\" width=\"230\" class-name=\"small-padding fixed-width\">\n" +
                "        <template slot-scope=\"scope\">\n" +
                "          <el-button type=\"primary\" size=\"mini\" @click=\"handleUpdate(scope.row." + objectName + "Id)\">修改</el-button>\n" +
                "          <el-button type=\"danger\" size=\"mini\" @click=\"handleDel(scope.row." + objectName + "Id)\">删除</el-button>\n" +
                "        </template>\n" +
                "      </el-table-column>\n" +
                "    </el-table>\n");
        sb.append(ENTER);
        sb.append(ENTER);

        //分页部分
        sb.append("<!--分页部分-->").append(ENTER);
        sb.append("    <pagination\n" +
                "      :pageSizes=\"pageSizes\"\n" +
                "      :pageSize=\"listParams.pageSize\"\n" +
                "      :pageCount=\"pageInfo.total\"\n" +
                "      @handleSizeChange=\"handleSizeChange\"\n" +
                "      @handleCurrentChange=\"handleCurrentChange\"></pagination>\n");
        sb.append(ENTER);
        sb.append(ENTER);

        //新增修改弹出框部分
        sb.append("<!--新增修改弹出框部分-->").append(ENTER);
        sb.append("    <el-dialog :title=\"dialogTitle\" :visible.sync=\"dialogFormVisible\">\n" +
                "      <el-form ref=\"" + objectName + "Form\" :rules=\"rules\" :model=\"" + objectName + "Params\" label-position=\"left\" label-width=\"100px\"\n" +
                "               style='width: 400px; margin-left:50px;'>\n" +
                "        <el-form-item v-show=\"false\" prop=\"" + objectName + "Id\">\n" +
                "          <el-input v-model=\"" + objectName + "Params." + objectName + "Id\"></el-input>\n" +
                "        </el-form-item>\n");
        for (Map<String, Object> col : cols) {
            if(col.get(KEY)!=null&&col.get(KEY).toString().equals("PRI")){
                sb.append("        <el-form-item v-show=\"false\" label=\"" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "\" prop=\"" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "\">\n" +
                        "          <el-input v-model=\"" + objectName + "Params." + StringUtils.underline2Camel(col.get(NAME).toString(), true) + "\"></el-input>\n" +
                        "        </el-form-item>\n");
            }else if(!col.get(NAME).toString().equals("gmt_create") && !col.get(NAME).toString().equals("gmt_modified") ){
                sb.append("        <el-form-item label=\"" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "\" prop=\"" + StringUtils.underline2Camel(col.get(NAME).toString(), false) + "\">\n" +
                        "          <el-input v-model=\"" + objectName + "Params." + StringUtils.underline2Camel(col.get(NAME).toString(), true) + "\"></el-input>\n" +
                        "        </el-form-item>\n");
            }

        }
        sb.append("      </el-form>\n" +
                "      <div slot=\"footer\" class=\"dialog-footer\">\n" +
                "        <el-button @click=\"dialogFormVisible = false\">取消</el-button>\n" +
                "        <el-button v-if=\"dialogTitle=='新增'\" type=\"primary\" @click=\"create" + className + "\">新增</el-button>\n" +
                "        <el-button v-else-if=\"dialogTitle=='修改'\" type=\"primary\" @click=\"update" + className + "\">修改</el-button>\n" +
                "      </div>\n" +
                "    </el-dialog>\n");
        sb.append(ENTER);
        sb.append(ENTER);
        sb.append("  </div>\n" +
                "</template>\n");
        sb.append(ENTER);
        sb.append(ENTER);

        //js部分
        sb.append("<!--js部分-->").append(ENTER);
        sb.append("<script>\n" +
                "  import Pagination from '@/components/Pagination'\n" +
                "\n" +
                "  export default {\n" +
                "    components: {\n" +
                "      Pagination\n" +
                "    },\n" +
                "    data() {\n" +
                "      return {\n" +
                "        //列表部分\n" +
                "        listLoading: true,\n" +
                "        listParams: {\n" +
                "          pageSize: 5,\n" +
                "          pageNum: 1,\n" +
                "          keyMap:{},\n" +
                "          orderBy:''\n" +
                "        },\n" +
                "        pageSizes: [5, 10, 30, 50],\n" +
                "        pageInfo: {\n" +
                "          list: []\n" +
                "        },\n" +
                "        //增删改部分\n" +
                "        dialogFormVisible: false,\n" +
                "        dialogTitle: '',\n" +
                "        " + objectName + "Params: {\n" +
                "        },\n" +
                "        rules:{\n" +
                "        }\n" +
                "      };\n" +
                "    },\n" +
                "    created() {\n" +
                "      this.getList();\n" +
                "    },\n" +
                "    methods: {\n" +
                "      //分页调用方法\n" +
                "      handleSizeChange(size) {\n" +
                "        this.listParams.pageSize = size;\n" +
                "        this.getList();\n" +
                "      },\n" +
                "      handleCurrentChange(currentPage) {\n" +
                "        this.listParams.pageNum = currentPage;\n" +
                "        this.getList();\n" +
                "      },\n" +
                "      //获取列表方法\n" +
                "      getList() {\n" +
                "        let _this = this;\n" +
                "        this.listLoading = true;\n" +
                "        this.postRequest('/" + objectName + "/list', this.listParams).then(res => {\n" +
                "          _this.listLoading = false;\n" +
                "          if (res.data.code == '00') {\n" +
                "            this.pageInfo = res.data.data;\n" +
                "          }\n" +
                "        })\n" +
                "      },\n" +
                "      //点击新增按钮触发方法\n" +
                "      handleCreate() {\n" +
                "        if (this.$refs['" + objectName + "Form']!==undefined) {\n" +
                "          this.$refs['" + objectName + "Form'].resetFields();\n" +
                "        }\n" +
                "        this.dialogFormVisible = true;\n" +
                "        this.dialogTitle = '新增';\n" +
                "      },\n" +
                "      //新增提交方法\n" +
                "      create" + className + "() {\n" +
                "        this.$refs['" + objectName + "Form'].validate((valid=> {\n" +
                "          if (valid) {\n" +
                "            this.postRequest('/" + objectName + "/save', this." + objectName + "Params).then(res => {\n" +
                "              if (res.data.code == '00') {\n" +
                "                this.dialogFormVisible = false;\n" +
                "                this.$message.success('新增成功');\n" +
                "                this.getList();\n" +
                "              }\n" +
                "            })\n" +
                "          }\n" +
                "        }))\n" +
                "      },\n" +
                "      //点击修改按钮触发方法\n" +
                "      handleUpdate(" + objectName + "Id) {\n" +
                "        if (this.$refs['" + objectName + "Form']!==undefined) {\n" +
                "          this.$refs['" + objectName + "Form'].resetFields();\n" +
                "        }\n" +
                "        this.dialogFormVisible = true;\n" +
                "        this.dialogTitle = '修改';\n" +
                "        this.getRequest('/" + objectName + "/get?key='+" + objectName + "Id).then(res => {\n" +
                "          if (res.data.code == '00') {\n" +
                "            this." + objectName + "Params = res.data.data;\n" +
                "          }\n" +
                "        })\n" +
                "      },\n" +
                "      //修改提交方法\n" +
                "      update" + className + "(){\n" +
                "        this.$refs['" + objectName + "Form'].validate((valid=>{\n" +
                "          if(valid){\n" +
                "            this.postRequest('/" + objectName + "/update', this." + objectName + "Params).then(res => {\n" +
                "              if (res.data.code == '00') {\n" +
                "                this.dialogFormVisible = false;\n" +
                "                this.$message.success('修改成功');\n" +
                "                this.getList();\n" +
                "              }\n" +
                "            })\n" +
                "          }\n" +
                "        }))\n" +
                "      },\n" +
                "      //点击删除触发方法\n" +
                "      handleDel(" + objectName + "Id) {\n" +
                "        this.$confirm('是否删除该用户', '提示', {\n" +
                "          confirmButtonText: '确定',\n" +
                "          cancelButtonText: '取消',\n" +
                "          type: 'warning'\n" +
                "        }).then(() => {\n" +
                "          this.getRequest('/" + objectName + "/delete?key='+" + objectName + "Id).then(res => {\n" +
                "            if (res.data.code == '00') {\n" +
                "              this.$message.success('删除成功');\n" +
                "              this.getList();\n" +
                "            }\n" +
                "          })\n" +
                "        }).catch(() => {\n" +
                "          this.$message.info('已取消删除');\n" +
                "        });\n" +
                "      },\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "</script>\n").append(ENTER);
        sb.append(ENTER);
        GenerateFileUtils.save("src/main/resources/vue/" + modules + "/" + className + ".vue", sb.toString());

    }


}
