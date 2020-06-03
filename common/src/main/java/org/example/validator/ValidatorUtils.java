package org.example.validator;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: houlintao
 * @Date:2020/5/24 上午11:42
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     *@date: 2020/5/24 上午11:46
     *@param object  待校验的对象
     *@param groups  待校验的组
     *@return:
     *@Description:校验对象
     * 详情：
     * https://blog.csdn.net/dream_broken/article/details/53584169?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.nonecase
     */
    public static Map<String,StringBuilder> validateEntity(Object object, Class<?>... groups){
        //判断groups是否为空
        if(groups == null){
            //如果为空，则设置默认值
            groups = new Class[]{Default.class};
        }

        HashMap<String, StringBuilder> errMap = new HashMap<>(16);

        /**
         * Constraint是约束的意思，violation是违反的名词形式;
         * Constraint+violation的意思就是违反的约束，这里是作一个java类表示的;
         * 如果object违反了一条约束，那么validate方法返回的set就包含一个cv;
         */
        Set<ConstraintViolation<Object>> validateSet = validator.validate(object, groups);

        if(CollectionUtils.isEmpty(validateSet)){
            return null;
        }
        String property;

        for (ConstraintViolation cv : validateSet){
            property = cv.getPropertyPath().toString();
            /**
             * 在这里if条件判断errMap中有没有以property为key的映射value，由于errMap初始的时候是一个
             * 空的map，因此第一个property的时候errMap.get(property)肯定会是null
             * 因此会执行else中的逻辑；
             * 第二次开始的时候errMap就不为null了。
             */
            if (errMap.get(property) != null){
                errMap.get(property).append(",").append(cv.getMessage());
            }else {
                //new一个StringBuilder用于存储message
                StringBuilder builder = new StringBuilder();
                builder.append(cv.getMessage());
                //将builder存入Map，有几个存几个
                errMap.put(property,builder);
            }
        }
        return errMap;
    }
}
