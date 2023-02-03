package com.jxj.netty.medium;

import com.alibaba.fastjson.JSONObject;
import com.jxj.netty.handler.param.ServerRequest;
import com.jxj.netty.util.Response;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 */
public class Media {

    public static Map<String, BeanMethod> beanMap;
    static {
        beanMap = new HashMap<String, BeanMethod>();
    }

    private static Media m = null;
    private Media() {

    }

    public static Media newInstance() {
        if (m == null) {
            return new Media();
        }
        return null;

    }

    // 反射处理业务
    public Response process(ServerRequest request) {
        Response result = null;
        try {
            String command = request.getCommand();
            BeanMethod beanMethod = beanMap.get(command);
            if (beanMethod == null) {
                return null;
            }

            Object bean = beanMethod.getBean();
            Method method = beanMethod.getMethod();
            Class<?> paramType = method.getParameterTypes()[0];
            Object content = request.getContent();
            Object args = JSONObject.parseObject(JSONObject.toJSONString(content), paramType);
            result = (Response) method.invoke(bean, args);
            result.setId(request.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
