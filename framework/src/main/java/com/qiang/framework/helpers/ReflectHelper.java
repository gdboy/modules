package com.qiang.framework.helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Lin on 2017/3/1.
 */

public class ReflectHelper {

    public static Object invokeStaticMethod(String className, String methodName)
    {
        return invokeStaticMethod(className, methodName, null, Class.class);
    }

    public static Object invokeStaticMethod(String className, String methodName, Object[] objects, Class... parameterTypes)
    {
        return invokeMethod(className, methodName, null, null, Class.class);
    }

    public static Object invokeMethod(String className, String methodName, Object receiver)
    {
        return invokeMethod(className, methodName, receiver, null, Class.class);
    }

    public static Object invokeMethod(String className, String methodName, Object receiver, Object[] objects, Class... parameterTypes)
    {
        Class[] types = null;

        if(objects != null)
        {
            types = new Class[objects.length];

            if(parameterTypes != null)
            {
                for(int i=0;i<parameterTypes.length;i++)
                    types[i] = parameterTypes[i];
            }

            for(int i=0;i<types.length;i++)
            {
                if(types[i] == null)
                    types[i] = objects[i].getClass();
            }
        }

        try {
            Class ownerClass = Class.forName(className);

            Method method = ownerClass.getMethod(methodName, types);
            return method.invoke(receiver, objects);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
