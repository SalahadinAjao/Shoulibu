package org.example.service;

import org.example.dao.BaseDao;
import org.example.dao.CouponMapper;
import org.example.entity.CouponEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/21 下午3:35
 * @email 437547058@qq.com
 * @Version 1.0
 */
@Service
public class CouponService{
    @Autowired
    private CouponMapper couponDao;

    public CouponEntity queryObject(Integer couponId) {
        return couponDao.queryObject(couponId);
    }

    public List<CouponEntity> queryList(Map<String, Object> map) {
        return couponDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return couponDao.queryTotal(map);
    }


    public void save(CouponEntity couponEntity) {
        couponDao.save(couponEntity);
    }

    public void update(CouponEntity couponEntity) {
        couponDao.update(couponEntity);
    }

    public void delete(Long userId) {
        couponDao.delete(userId);
    }

    public void deleteBatch(Long[] userIds) {
        couponDao.deleteBatch(userIds);
    }

    //查询用户优惠券列表
    public List<CouponEntity> queryUserCoupons(Map<String,Object> map){
        List<CouponEntity> couponList = couponDao.queryUserCoupons(map);
        //判断优惠券是否过期
        if (couponList != null && couponList.size()>0){
            for (CouponEntity couponEntity : couponList){
                //优惠券状态 1 可用 2 已用 3 过期
                if (couponEntity.getCoupon_status()==1){
                    /**
                     * 优惠券是可用状态，需要检查是否过期
                     * 如果优惠券上面的最后使用期限早于当前时间--->已过期
                     */
                    if (couponEntity.getUse_end_date().before(new Date())){
                        //已过期，将优惠券状态设置为过期
                        couponEntity.setCoupon_status(3);
                        couponDao.updateUserCoupon(couponEntity);
                    }
                    /**
                     * 这里无需考虑优惠券未过期这个情况，因为返回的是一个list列表
                     */
                }
                if (couponEntity.getCoupon_status()==3){
                    //判断优惠券是否未i过期
                    if (couponEntity.getUse_end_date().after(new Date())){
                        couponEntity.setCoupon_status(1);
                        couponDao.updateUserCoupon(couponEntity);
                    }
                }
            }
        }
        return couponList;
    }
    /**
     * 查询用户最多可用优惠券
     */
    public CouponEntity queryUserMaxCoupon(Map<String,Object> map){
       return couponDao.queryMaxUserEnableCoupon(map);
    }

    public List<CouponEntity>  queryUserCouponList(Map<String, Object> map){
      return couponDao.queryUserCoupons(map);
    }
}
