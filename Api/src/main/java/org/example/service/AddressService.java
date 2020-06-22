package org.example.service;

import org.example.dao.AddressMapper;
import org.example.entity.AddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/22 下午2:46
 * @email 437547058@qq.com
 * @Version 1.0
 */

@Service
public class AddressService {
    @Autowired
    private AddressMapper addressDao;

    public void save(AddressEntity entity){
        addressDao.save(entity);
    }

    public void update(AddressEntity address){
        addressDao.update(address);
    }

    public void delete(Integer id){
        addressDao.delete(id);
    }

    public AddressEntity queryObject(Integer id){
        return addressDao.queryObject(id);
    }

    public List<AddressEntity> queryList(Map<String,Object> map){
        return addressDao.queryList(map);
    }

    public int queryTotal(Map<String,Object> map){
        return addressDao.queryTotal(map);
    }

    public void deleteBatch(Integer[] ids){
        addressDao.deleteBatch(ids);
    }
}
