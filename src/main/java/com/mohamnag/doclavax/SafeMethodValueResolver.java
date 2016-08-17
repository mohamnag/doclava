package com.mohamnag.doclavax;

import com.github.jknack.handlebars.ValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by mohamnag on 17/08/16.
 */
public class SafeMethodValueResolver extends MethodValueResolver {

    /**
     * The default instance.
     */
    public static final ValueResolver INSTANCE = new SafeMethodValueResolver();

    @Override
    public Set<Map.Entry<String, Object>> propertySet(Object context) {
        try {
            return super.propertySet(context);

        } catch (Exception e) {
            return Collections.emptySet();
        }
    }
}
