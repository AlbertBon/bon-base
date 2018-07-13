package com.bon.service.impl;

import com.bon.common.dto.BaseDTO;
import com.bon.common.exception.BusinessException;
import com.bon.dao.GenerateMapper;
import com.bon.dao.SysBaseMapper;
import com.bon.domain.dto.SysBaseDTO;
import com.bon.domain.dto.SysBaseFieldDTO;
import com.bon.domain.dto.SysCreateTableDTO;
import com.bon.domain.entity.SysBase;
import com.bon.domain.vo.SysBaseTablesVO;
import com.bon.domain.vo.SysBaseVO;
import com.bon.service.SysBaseService;
import com.bon.util.BeanUtil;
import com.bon.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.javassist.runtime.DotClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: bon基础项目
 * @description: 系统基础表接口实现类
 * @author: Bon
 * @create: 2018-07-12 11:52
 **/
@Service
public class SysBaseServiceImpl implements SysBaseService {

    @Autowired
    SysBaseMapper sysBaseMapper;

    @Autowired
    GenerateMapper generateMapper;

    @Override
    public List<SysBaseVO> listTables() {
        List<SysBase> list = sysBaseMapper.listTables();
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
        List<SysBaseFieldDTO> fieldList=dto.getFieldList();
        for(SysBaseFieldDTO field:fieldList){
            if(StringUtils.isBlank(field.getFieldName())||StringUtils.isBlank(field.getFieldType())){
                throw new BusinessException("请输入字段名和字段类型");
            }
            SysBase sysBase = new SysBase();
            sysBase = BeanUtil.copyPropertys(field,sysBase);
            sysBase.setTableName(dto.getTableName());
            sysBase.setTableRemark(dto.getTableRemark());
            //判断系统表id是否为空，不为空即修改
            if(null!=field.getSysBaseId()&&field.getSysBaseId()>0){
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
    public void createTable(SysCreateTableDTO dto) {
        if(StringUtils.isBlank(dto.getTableName())){
            throw new BusinessException("表名不能为空");
        }
        if(StringUtils.isBlank(dto.getTableRemark())){
            dto.setTableRemark("");
        }
        String sql = generateCreateSql(dto.getTableName(),dto.getTableRemark());
        generateMapper.createTable(sql);
    }

    private String generateCreateSql(String tableName,String tableComment){
        BaseDTO dto =new BaseDTO();
        dto.andFind(new SysBase(),"tableName",tableName);
        List<SysBase> sysBaseList = sysBaseMapper.selectByExample(dto.getExample());
        if(sysBaseList.size()<=0){
            throw new BusinessException("数据表不存在");
        }
        String sql="";
        sql += "CREATE TABLE IF NOT EXISTS `" + tableName + "` ( `" +
                "gmt_create` datetime DEFAULT NULL COMMENT '创建时间'," +
                "`gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间', ";
        for(int i = 0;i<sysBaseList.size();i++){
            SysBase sysBase = sysBaseList.get(i);
            if(StringUtils.isBlank(sysBase.getFieldName())||StringUtils.isBlank(sysBase.getFieldType())){
                throw new BusinessException("请输入字段名和字段类型");
            }
            /*判断是否是表id*/
            if (StringUtils.isByteTrue(sysBase.getIsId())) {
                sql += "  `" + sysBase.getFieldName() + "`  bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',PRIMARY KEY (`" + sysBase.getFieldName() + "`),";
                continue;
            }
            //字段名
            sql += sysBase.getFieldName() + " ";
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
                sql += sysBase.getDefaultValue() + " ";
            }
            //字段备注
            if(StringUtils.isNotBlank(sysBase.getFieldRemark())){
                sql += " COMMENT '" + sysBase.getFieldRemark() + "' ";
            }
            /*如果是最后一行*/
            if (i == sysBaseList.size()-1) {
                sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='" + tableComment + "';";
            }

        }
        return sql;
    }
}
