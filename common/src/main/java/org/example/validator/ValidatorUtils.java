package org.example.validator;

import org.example.Util.RRException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
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
     *@param:object 待校验的对象
     *@param:groups 待校验的组
     *@return:
     *@Description:校验对象
     */
    public static void validateEntity(Object object,Class<?>... groups){
        Set<ConstraintViolation<Object>> validate = validator.validate(object, groups);
        if (!validate.isEmpty()){
            ConstraintViolation<Object> next = validate.iterator().next();
            throw new RRException(next.getMessage());
        }
    }
}
