package org.example.service;

import org.example.dao.CartMapper;
import org.example.entity.CartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/21 下午4:21
 * @email 437547058@qq.com
 * @Version 1.0
 */
@Service
public class CartService {
    @Autowired
    private CartMapper cartDao;

    public CartEntity queryObject(Integer id){
        return cartDao.queryObject(id);
    }

    public List<CartEntity> queryList(Map<String,Object> map){
        return cartDao.queryList(map);
    }

    private int queryTotal(Map<String,Object> map){
        return cartDao.queryTotal(map);
    }

    public void save(CartEntity cartEntity){
        cartDao.save(cartEntity);

        Map cartParam = new HashMap();
        cartParam.put("userId",cartEntity.getUser_id());

        List<CartEntity> cartInfoList = cartDao.queryList(cartParam);
        Map crashParam = new HashMap();
        List<Integer> goods_ids = new ArrayList();
        List<CartEntity> cartsToUpdateList = new ArrayList();

        for (CartEntity entity : cartInfoList){
            if (entity.getChecked()!=null && entity.getChecked()==1){
                //将选中的购物车中的商品id存放进list
                goods_ids.add(entity.getGoods_id());
            }
            if (!entity.getRetail_price().equals(entity.getRetail_product_price())){
                entity.setRetail_price(entity.getRetail_product_price());
                cartsToUpdateList.add(entity);
            }
        }

        crashParam.put("goods_ids",goods_ids);

        for (CartEntity cartItem : cartInfoList){
            //如果购物车已选中
            if (cartItem.getChecked()!=null && cartItem.getChecked()==1){
                //再次遍历，这次会遍历全部的购物车对象，不管选中还是没选中
                for (CartEntity cartCrash : cartInfoList){
                    if (!cartCrash.getId().equals(cartItem.getId())){
                        cartsToUpdateList.add(cartItem);
                    }
                }
            }
        }

        if (cartsToUpdateList!=null && cartsToUpdateList.size()>0){
            for (CartEntity entity:cartsToUpdateList){
                cartDao.update(entity);
            }
        }
    }

    public void update(CartEntity cart){
        cartDao.update(cart);
    }

    public void delete(Integer id){
        cartDao.delete(id);
    }

    public void deleteBatch(Integer[] ids){
        cartDao.deleteBatch(ids);
    }
    //更新检查
    public void updateCheck(String[] productIds,Integer isChecked,Long userId){
        cartDao.updateCheck(productIds,isChecked,userId);

        Map cartParam = new HashMap();
        cartParam.put("user_id",userId);
        //这里返回的结果是此用户id下的所有购物车对象
        List<CartEntity> cartListInfo = cartDao.queryList(cartParam);

        Map crashParam = new HashMap();
        List<Integer> goods_ids = new ArrayList();
        List<CartEntity> cartListToUpdate = new ArrayList();

        if (cartListInfo!=null && cartListInfo.size()>0){
            for (CartEntity entity:cartListInfo){
                //选中的购物车才能执行下面的步骤，未被选中的购物车不符合
                if (entity.getChecked()!=null && entity.getChecked()==1){
                    //把选中的购物车中的商品id放入list中
                    goods_ids.add(entity.getGoods_id());
                }
                //再判断如果购物车的零售价格与零售产品价格不一致，购物车中商品价格与product表中的零售价格有关
                if (!entity.getRetail_price().equals(entity.getRetail_product_price())){
                    entity.setRetail_price(entity.getRetail_product_price());
                    cartListToUpdate.add(entity);
                }
            }
            if (goods_ids != null && goods_ids.size()>0){
                crashParam.put("goods_ids",goods_ids);

                for (CartEntity cartEntity : cartListInfo){
                    if (cartEntity.getChecked()!=null && cartEntity.getChecked()==1){
                        for (CartEntity cartCrash:cartListInfo){
                            if (cartEntity.getChecked()!=null && cartEntity.getChecked()==1 &&(!cartCrash.getId().equals(cartEntity.getId()))){
                                cartListToUpdate.add(cartCrash);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void deleteByProductId(Integer[] ids){
        cartDao.deleteByProductIds(ids);
    }

    public void deleteByUserAndProductIds(Long userId, String[] productIds) {
        cartDao.deleteByUserAndProductIds(userId, productIds);
    }

    public void deleteByCart(Long user_id, Integer session_id, Integer checked) {
        cartDao.deleteByCart(user_id, session_id, checked);
    }
}
