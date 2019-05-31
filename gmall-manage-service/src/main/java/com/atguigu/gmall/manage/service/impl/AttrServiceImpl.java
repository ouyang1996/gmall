package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.manage.mapper.PmsAttrBaseInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsAttrBaseValueMapper;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {
    @Autowired
    PmsAttrBaseInfoMapper pmsAttrBaseInfoMapper;

    @Autowired
    PmsAttrBaseValueMapper pmsAttrBaseValueMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsAttrBaseInfoMapper.select(pmsBaseAttrInfo);
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String id = pmsBaseAttrInfo.getId();
        if (StringUtils.isBlank(id)) {
            //id不存在
            //保存属性信息
            pmsAttrBaseInfoMapper.insertSelective(pmsBaseAttrInfo);

            //保存属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsAttrBaseValueMapper.insertSelective(pmsBaseAttrValue);
            }
        } else {
            //id不为空
            //属性信息修改
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id", pmsBaseAttrInfo.getId());
            pmsAttrBaseInfoMapper.updateByExampleSelective(pmsBaseAttrInfo, example);

            //属性值修改
            //根据id删除所有属性值
            PmsBaseAttrValue pmsBaseAttrValuedel = new PmsBaseAttrValue();
            pmsBaseAttrValuedel.setId(pmsBaseAttrInfo.getId());
            pmsAttrBaseValueMapper.delete(pmsBaseAttrValuedel);

            //删除后，将新的属性值插入
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsAttrBaseValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }
           return  "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValueList = pmsAttrBaseValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValueList;
    }

}
