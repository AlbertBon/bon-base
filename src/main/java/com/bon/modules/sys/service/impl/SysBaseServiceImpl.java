package com.bon.modules.sys.service.impl;

import com.bon.common.domain.dto.BaseDTO;
import com.bon.common.exception.BusinessException;
import com.bon.common.util.*;
import com.bon.modules.sys.dao.GenerateMapper;
import com.bon.modules.sys.dao.SysBaseExtendMapper;
import com.bon.modules.sys.dao.SysBaseMapper;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.entity.SysBase;
import com.bon.modules.sys.domain.vo.SysBaseVO;
import com.bon.modules.sys.service.SysBaseService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: bon基础项目
 * @description: 系统基础表接口实现类
 * @author: Bon
 * @create: 2018-07-12 11:52
 **/
@Transactional
@Service
public class SysBaseServiceImpl implements SysBaseService {

    private static final MyLog log = MyLog.getLog(SysBaseServiceImpl.class);

    @Autowired
    SysBaseMapper sysBaseMapper;

    @Autowired
    SysBaseExtendMapper sysBaseExtendMapper;

    @Autowired
    GenerateMapper generateMapper;

    @Override
    public List<SysBaseVO> listTables() {
        List<SysBase> list = sysBaseExtendMapper.listTables();
        List<SysBaseVO> voList = new ArrayList<>();
        for (SysBase sysBase : list) {
            SysBaseVO vo = new SysBaseVO();
            BeanUtil.copyPropertys(sysBase, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<SysBaseVO> listByTableName(SysBaseDTO dto) {
        dto.andFind("tableName",dto.getTableName());
        List<SysBase> list = sysBaseMapper.selectByExample(dto.getExample());
        List<SysBaseVO> voList = new ArrayList<>();
        for (SysBase sysBase : list) {
            SysBaseVO vo = new SysBaseVO();
            BeanUtil.copyPropertys(sysBase, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public void saveTable(SysBaseDTO dto) {
        if(StringUtils.isBlank(dto.getTableName())){
            throw new BusinessException("请输入表名");
        }
        if(StringUtils.isBlank(dto.getModules())){
            throw new BusinessException("请输入模块名称");
        }
        List<SysBaseFieldDTO> fieldList=dto.getFieldList();
        for(SysBaseFieldDTO field:fieldList){
            if(StringUtils.isBlank(field.getFieldName())||StringUtils.isBlank(field.getFieldType())){
                throw new BusinessException("请输入字段名和字段类型");
            }
            SysBase sysBase = new SysBase();
            sysBase = BeanUtil.copyPropertys(field,sysBase);
            sysBase.setTableName(dto.getTableName());
            sysBase.setTableRemark(dto.getTableRemark());
            sysBase.setModules(dto.getModules());
            //如果是id字段，则系统定义，无论前端如何修改
            if(StringUtils.isByteTrue(field.getIsId())){
                sysBase.setFieldRemark("ID");
                sysBase.setFieldType("BIGINT");
                sysBase.setFieldLength(20);
                sysBase.setIsNull((byte) 0);
            }
            //判断系统表id是否为空，不为空即修改
            if(null!=field.getBaseId()&&field.getBaseId()>0){
                sysBase.setGmtModified(new Date());
                sysBaseMapper.updateByPrimaryKeySelective(sysBase);
            }else{
                sysBase.setGmtCreate(new Date());
                sysBase.setGmtModified(new Date());
                sysBaseMapper.insert(sysBase);
            }
        }
    }

    @Override
    public void generateTable(File file,List<String> tableList) {
        try {
            log.info("开始执行创建表语句");
            String sql = POIUtil.generateViewSql(file.getAbsolutePath(),tableList);
            generateMapper.generateTable(sql);
            log.info("创建表完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateTableSQL(File file,List<String> tableList) throws Exception {
        String string= POIUtil.generateViewSql(file.getAbsolutePath(),tableList);
        return string;
    }

    @Override
    public void generateTable(SysGenerateTableDTO dto) {
        if(StringUtils.isBlank(dto.getTableName())){
            throw new BusinessException("表名不能为空");
        }
        if(StringUtils.isBlank(dto.getTableRemark())){
            dto.setTableRemark("");
        }
        String sql = generateCreateSql(dto.getTableName(),dto.getTableRemark());
        generateMapper.generateTable(sql);
    }

    @Override
    public void generateClass(List<SysGenerateClassDTO> dtoList) {
        for (SysGenerateClassDTO dto : dtoList) {
            List<String> tableNameList = dto.getTableNameList();
            String modules = dto.getModules();
            try {
                log.info("开始修改generate.xml文件,生成模块--{}--数据",modules);
                //创建Document对象，读取已存在的Xml文件generator.xml
                Document doc = new SAXReader().read(new File(SysBaseService.class.getResource("/generator.xml").getFile()));
                //删除所有table标签
                List<Element> elements = doc.getRootElement().element("context").elements();
                for (Element element : elements) {
                    if (element.getName().equals("table")) {
                        element.detach();
                    }
                }
                //修改生成文件的包路径
                String packagePath = PropertyUtil.getProperty("basePackage")+".modules."+modules;
                Element modelElement = doc.getRootElement().element("context").element("javaModelGenerator");
                modelElement.attribute("targetPackage").setValue(packagePath+".domain.entity");
                Element mapperXmlMap = doc.getRootElement().element("context").element("sqlMapGenerator");
                mapperXmlMap.attribute("targetPackage").setValue("mapper."+modules);
                Element mapperElement = doc.getRootElement().element("context").element("javaClientGenerator");
                mapperElement.attribute("targetPackage").setValue(packagePath+".dao");

                //循环写入修改表信息到xml文件
                for (String tableName : tableNameList) {
                    String domainName = StringUtils.upperCase(StringUtils.underline2Camel(tableName, false));
                    log.error("实体类--{}--生成", domainName);
                    //1.得到属性值标签
                    Element tableElem = doc.getRootElement().element("context").addElement("table");
                    //2.通过增加同名属性的方法，修改属性值----key相同，覆盖；不存在key，则添加
                    tableElem.addAttribute("tableName", tableName).addAttribute("domainObjectName", domainName)
                            .addAttribute("enableCountByExample", "false").addAttribute("enableUpdateByExample", "false")
                            .addAttribute("enableDeleteByExample", "false").addAttribute("enableSelectByExample", "false")
                            .addAttribute("selectByExampleQueryId", "false").addAttribute("enableSelectByPrimaryKey", "true")//只留根据id查询接口
                            .addAttribute("enableUpdateByPrimaryKey", "false").addAttribute("enableInsert", "false")
                            .addAttribute("enableDeleteByPrimaryKey", "false");
                    tableElem.addElement("property").addAttribute("name", "useActualColumnNames").addAttribute("value", "false");
                    if(dto.getIsExtend()==1){
                        GenerateCoreUtil.generateAll(tableName,modules);
                    }
                }

                //指定文件输出的位置
                FileOutputStream out = new FileOutputStream(SysBaseService.class.getResource("/generator.xml").getFile());
                // 指定文本的写出的格式：
                OutputFormat format = OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
                format.setEncoding("UTF-8");
                //1.创建写出对象
                XMLWriter writer = new XMLWriter(out, format);
                //2.写出Document对象
                writer.write(doc);
                //3.关闭流
                writer.close();
                log.info("修改generate.xml文件完成");


                log.info("开始生成实体类 ...");
                List<String> warnings = new ArrayList<String>();
                boolean overwrite = true;
                File configFile = new File(SysBaseService.class.getResource("/generator.xml").getFile());
                ConfigurationParser cp = new ConfigurationParser(warnings);
                Configuration config = cp.parseConfiguration(configFile);
                DefaultShellCallback callback = new DefaultShellCallback(overwrite);
                MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
                myBatisGenerator.generate(null);
                log.info("实体类生成完毕");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteField(SysBaseDeleteDTO dto) {
        sysBaseMapper.deleteByPrimaryKey(dto.getBaseId());
    }

    @Override
    public void deleteTable(SysBaseDeleteDTO dto) {
        dto.andFind(new SysBase(),"tableName",dto.getTableName());
        sysBaseMapper.deleteByExample(dto.getExample());
        //运行销毁数据库语句
        String sql = generateDropTable(dto.getTableName());
        generateMapper.generateTable(sql);
    }

    @Override
    public void dropTable(SysGenerateTableDTO dto) {
        //运行销毁数据库语句
        String sql = generateDropTable(dto.getTableName());
        generateMapper.generateTable(sql);
        //运行删除基础表
        if(null != dto.getIsDeleteBase()&&1 == dto.getIsDeleteBase()){
            dto.andFind(new SysBase(),"tableName",dto.getTableName());
            sysBaseMapper.deleteByPrimaryKey(dto.getExample());
        }
    }

    /**
     * 创建数据库sql语句
     * @param tableName
     * @param tableComment
     * @return
     */
    private String generateCreateSql(String tableName,String tableRemark){
        if(StringUtils.isBlank(tableRemark)){
            tableRemark = "";
        }
        BaseDTO dto =new BaseDTO();
        dto.andFind(new SysBase(),"tableName",tableName);
        List<SysBase> sysBaseList = sysBaseMapper.selectByExample(dto.getExample());
        if(sysBaseList.size()<=0){
            throw new BusinessException("数据表不存在");
        }
        String sql="";
        //语句头
        sql += "CREATE TABLE IF NOT EXISTS `" + tableName + "` ( " ;
        for(int i = 0;i<sysBaseList.size();i++){
            SysBase sysBase = sysBaseList.get(i);
            if(StringUtils.isBlank(sysBase.getFieldName())||StringUtils.isBlank(sysBase.getFieldType())){
                throw new BusinessException("请输入字段名和字段类型");
            }
            /*判断是否是表id*/
            if (StringUtils.isByteTrue(sysBase.getIsId())) {
                sql += "  `" + sysBase.getFieldName() + "`  bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',PRIMARY KEY (`" + sysBase.getFieldName() + "`) ,"+
                        "`gmt_create` datetime DEFAULT NULL COMMENT '创建时间'," +
                        "`gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间' ";
                if(i<sysBaseList.size()-1){
                    sql += ", ";
                }
                continue;
            }
            //字段名
            sql += " `" + sysBase.getFieldName() + "` ";
            //字段类型
            sql += sysBase.getFieldType();
            //字段长度
            if(StringUtils.isNumNotBlank(sysBase.getFieldLength())){
                sql += "(" +sysBase.getFieldLength()+") ";
            }else {
                sql += " ";
            }
            //字段是否为空
            if(StringUtils.isByteTrue(sysBase.getIsNull())){
                sql += " NULL ";
            }else{
                sql += " NOT NULL ";
            }
            //字段是否唯一
            if(StringUtils.isByteTrue(sysBase.getIsUnique())){
                sql += " UNIQUE ";
            }else{
                sql += " ";
            }
            //字段是否为无符号
            if(StringUtils.isByteTrue(sysBase.getIsUnsigned())){
                sql += " UNSIGNED ";
            }else{
                sql += " ";
            }
            //字段默认值
            if(StringUtils.isNotBlank(sysBase.getDefaultValue())){
                sql += " DEFAULT '" + sysBase.getDefaultValue() + "' ";
            }
            //字段备注
            if(StringUtils.isNotBlank(sysBase.getFieldRemark())){
                sql += " COMMENT '" + sysBase.getFieldRemark() + "' ";
            }
            if(i<sysBaseList.size()-1){
                sql += ", ";
            }
        }
        //语句尾
        sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='" + tableRemark + "';";
        return sql;
    }

    /**
     * 销毁数据库sql语句
     * @param tableName
     * @param tableComment
     * @return
     */
    private String generateDropTable(String tableName){
        String sql = "DROP TABLE IF EXISTS `" + tableName + "`;";
        return sql;
    }
}
