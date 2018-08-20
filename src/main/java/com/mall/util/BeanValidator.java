package com.mall.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mall.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-07-16
 * @Time: 15:27
 */
public class BeanValidator {

    /**
     * 构建工厂
     */

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 验证参数
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = BeanValidator.validatorFactory.getValidator();

        Set<ConstraintViolation<T>> validateResult = validator.validate(t, groups);


        if (validateResult.isEmpty()) {

            return Collections.emptyMap();
        } else {
            Iterator<ConstraintViolation<T>> iterator = validateResult.iterator();

            Map<String, String> errors = Maps.newLinkedHashMap();

            while (iterator.hasNext()) {
                ConstraintViolation<T> validation = iterator.next();
                errors.put(validation.getPropertyPath().toString(), validation.getMessage());

            }
            return errors;
        }

    }

    /**
     * 列表类型的参数
     * @param collection
     * @return
     */
    public static Map<String, String> validateList(Collection<?> collection) {

        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();

        Map errors;

        do {


            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            } else {
                Object next = iterator.next();
                errors = validate(next, new Class[0]);

            }
        } while (errors.isEmpty());

        return errors;
    }

    /**
     * 对象类型的验证
     * @param first
     * @param objects
     * @return
     */
    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects == null && objects.length > 1) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }

    /**
     * 检查参数是否符合类型
     * @param param
     * @throws ParamException
     */
    public static void check(Object param) throws ParamException {
        Map<String, String> map = BeanValidator.validateObject(param);

        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }


}
