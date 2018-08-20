package com.mall.util;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

import java.lang.ref.WeakReference;

import static org.dozer.loader.api.TypeMappingOptions.mapEmptyString;
import static org.dozer.loader.api.TypeMappingOptions.mapNull;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 17:07
 */
public class BeanCopyUtil {
    public static void copyProperties(final Object sources, final Object destination) {

        WeakReference weakReference = new WeakReference(new DozerBeanMapper());
        DozerBeanMapper mapper = (DozerBeanMapper) weakReference.get();

        mapper.addMapping(new BeanMappingBuilder() {

            @Override
            protected void configure() {
                mapping(sources.getClass(), destination.getClass(), mapNull(false), mapEmptyString(false));

            }
        });

        mapper.map(sources, destination);
        mapper.destroy();
        weakReference.clear();

    }
}
