package org.example.service;

import org.example.Entity.ProductEntity;
import org.example.dao.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/5 下午5:39
 */

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public ProductEntity queryObject(Integer id){
        return productMapper.queryObject(id);
    }

    public List<ProductEntity> queryList(Map<String,Object> map){
        return productMapper.queryList(map);
    }
    public int queryTotal(Map<String, Object> map) {
        return productMapper.queryTotal(map);
    }

    public void save(ProductEntity productEntity){
        productMapper.save(productEntity);
    }

    public void update(ProductEntity productEntity){
        productMapper.update(productEntity);
    }

    public void delete(Integer id){
        productMapper.delete(id);
    }

    public void deleteBatch(Integer[] ids) {
        productMapper.deleteBatch(ids);
    }

}
