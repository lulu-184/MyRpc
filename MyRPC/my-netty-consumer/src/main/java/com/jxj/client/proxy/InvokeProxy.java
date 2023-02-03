package com.jxj.client.proxy;

import com.jxj.client.annotation.RemoteInvoke;
import com.jxj.client.core.TcpClient;
import com.jxj.client.param.ClientRequest;
import com.jxj.client.param.Response;
import com.jxj.user.bean.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 */
@Component
public class InvokeProxy implements BeanPostProcessor {
    public static Enhancer enhancer = new Enhancer();
    @Override
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RemoteInvoke.class)){
                field.setAccessible(true);

                final Map<Method, Class> methodClassMap = new HashMap<>();
                putMethodClass(methodClassMap, field);

                enhancer.setInterfaces(new Class[]{field.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        // 采用netty客户端去调用服务器
                        ClientRequest request = new ClientRequest();
//                        request.setContent(users);
                        request.setContent(args[0]);
//                        User user = new User();
//                        request.setCommand("com.jxj.user.controller.UserController.saveUser");
                        request.setCommand(methodClassMap.get(method).getName() +
                                "." + method.getName());
//                        String command = method.getName();
//                        request.setCommand(command);
//                        System.out.println(request.getCommand());
                        Response response = TcpClient.send(request);
                        return response;
                    }
                });
                try {
                    field.set(bean, enhancer.create());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
        /*
         *@Description 对属性的所有方法和属性接口类型放入到一个 map 中
         * @param methodClassMap
         * @param field
         * @return
         */
        Method[] methods = field.getType().getDeclaredMethods();
//        System.out.println("field:" +field.getName());
//        System.out.println("type:" +field.getType());
        for (Method  m : methods) {
            methodClassMap.put(m, field.getType());
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        return bean;
    }
}
