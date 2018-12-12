package com.zlf.dao;

import java.util.List;

import com.zlf.bo.JiJinBo;

public interface JiJinBoMapper {
    //新增
    int insertSelective(JiJinBo record);
    //根据条件分页查询
    List<JiJinBo> selectByConditionsAndPage(JiJinBo jiJinBo);
    //根据条件查询所有，为了得到total
    List<JiJinBo> countSizeByConditions(JiJinBo jiJinBo);
    //修改
    int updateByPrimaryKeySelective(JiJinBo record);
    //获取下拉的值
    List<JiJinBo> getJiJinNameList();
    //新增去重复
    List<JiJinBo> judgeAddRepeat(JiJinBo jiJinBo);
}