package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysDeptEntity;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/4 上午8:07
 * @email 437547058@qq.com
 * @Version 1.0
 */


@Mapper
public interface SysDeptDao extends BaseDao<SysDeptEntity> {

    List<Long> queryDetpIdList(Long parentId);
}
