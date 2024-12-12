package generics.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import model.Argument;
import model.DefaultValue;

abstract class Value {


  @SuppressWarnings("unchecked")
  public <T> T value() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

    String callingMethod = stackTrace[3].getMethodName();

    for (Method method : this.getClass().getDeclaredMethods()) {
      if (method.getName().equals(callingMethod)) {
        if (method.isAnnotationPresent(DefaultValue.class)) {
          DefaultValue defaultValue = method.getAnnotation(DefaultValue.class);
          Argument argument = search(method.getParameterAnnotations(), Argument.class);
          Class<?> type = method.getParameterTypes()[0];
          if(argument != null){
              type = getParameterType(method);
          }

          return (T) parseValue(defaultValue.value(), type);
        }

      }
    }

    return null;
  }

  private Object parseValue(String value, Class<?> type) {
    if (type == String.class) {
      return value;
    } else if (type == int.class || type == Integer.class) {
      return Integer.parseInt(value);
    } else if (type == double.class || type == Double.class) {
      return Double.parseDouble(value);
    } else if (type == long.class || type == Long.class) {
      return Long.parseLong(value);
    } else if (type == boolean.class || type == Boolean.class) {
      return Boolean.parseBoolean(value);
    } else if (type == float.class || type == Float.class) {
      return Float.parseFloat(value);
    } else if (type == short.class || type == Short.class) {
      return Short.parseShort(value);
    } else if (type == byte.class || type == Byte.class) {
      return Byte.parseByte(value);
    } else {
      return value;
    }
  }


  private Class<?> getParameterType(Method method){
    for (Parameter parameter : method.getParameters()) {
      if(parameter.isAnnotationPresent(Argument.class)){
        return parameter.getType();
      }
    }
    return method.getParameterTypes()[0];
  }

  @SuppressWarnings("unchecked")
  private <T> T search(Annotation[][] parameterAnnotations, Class<T> searched) {
    for (int i = 0; i < parameterAnnotations.length; i++) {
      for (Annotation annotation : parameterAnnotations[i]) {
        if (annotation.annotationType().isAssignableFrom(searched)) {
          return (T) annotation;
        }
      }
    }
    return null;
  }

}
