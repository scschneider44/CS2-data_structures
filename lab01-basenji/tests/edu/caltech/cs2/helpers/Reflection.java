package edu.caltech.cs2.helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Reflection {
  public static Method getPrivateMethod(Class clazz, String name) {
    Method method = null;
    try {
      method = clazz.getDeclaredMethod(name);
      method.setAccessible(true);
    } catch (NoSuchMethodException e) {
      return null;
    }
    return method;
  }

  public static <T> T invoke(Method m, Object... args) throws Throwable {
    T result;
    try {
      result = (T) m.invoke(args[0], Arrays.copyOfRange(args, 1, args.length));
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw e.getCause();
    }
    return result;
  }

}
