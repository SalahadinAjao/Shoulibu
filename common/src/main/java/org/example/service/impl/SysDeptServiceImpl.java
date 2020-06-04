package org.example.service.impl;

import com.qiniu.util.StringUtils;
import org.example.dao.SysDeptDao;
import org.example.entity.SysDeptEntity;
import org.example.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/4 上午8:06
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service("SysDeptService")
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptDao deptDao;


    @Override
    public SysDeptEntity queryObject(Long deptId) {
        return deptDao.queryOBject(deptId);
    }

    @Override
    public List<SysDeptEntity> queryList(Map<String, Object> map) {
        return deptDao.queryList(map);
    }

    @Override
    public void save(SysDeptEntity sysDept) {
         deptDao.save(sysDept);
    }

    @Override
    public void update(SysDeptEntity sysDept) {
           deptDao.update(sysDept);
    }

    @Override
    public void delete(Long deptId) {
          deptDao.delete(deptId);
    }

    @Override
    public List<Long> queryDetpIdList(Long parentId) {
        return deptDao.queryDetpIdList(parentId);
    }

    @Override
    public String getSubDeptIdList(Long deptId) {
        ArrayList<Long> DeptIdlist = new ArrayList<>();

        List<Long> detpIdList = queryDetpIdList(deptId);

        //获取子部门ID
        List<Long> subIdList = queryDetpIdList(deptId);

        getDeptTreeList(subIdList,detpIdList);

        detpIdList.add(deptId);

        String join = StringUtils.join(detpIdList, ",");

        return join;

    }

    /**
     *递归函数，递归的调用此方法实现菜单从根到叶的递归调用
     *@date: 2020/6/4 上午9:14
     *@param subIdList 子部门id列表
     *@param dpetIdList 部门id列表
     */
    public void getDeptTreeList(List<Long> subIdList ,List<Long> dpetIdList){
        for (Long deptId : subIdList){
            List<Long> list = queryDetpIdList(deptId);

            if (list.size()>0){
                getDeptTreeList(list,dpetIdList);
            }
            dpetIdList.add(deptId);
        }
    }
}
