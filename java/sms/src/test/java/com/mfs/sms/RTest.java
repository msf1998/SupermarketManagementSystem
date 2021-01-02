package com.mfs.sms;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.print.attribute.standard.Finishings;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SpringBootTest
public class RTest {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @org.junit.jupiter.api.Test
    public void t() throws CollectionClassMapObjectUtil.TypeException, IOException {
        /*ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set("he", new Test(""));
        Object he = ops.get("he");
        Test map = new CollectionClassMapObjectUtil().map((Map<?, ?>) he, Test.class);
        System.out.println(map);*/
        System.out.println("hello.php");
    }

    public static void main(String[] args) {
        System.out.println("012321321".matches("[0-9]{1,10}"));
    }
}

@Data
@ToString
class Test {
    private byte a;
    private Byte b;
    private short c;
    private Short d;
    private char e;
    private int f;
    private float g;
    private double h;
    private long i;
    private Long j;
    private List<Test> list = new ArrayList<>();
    private Test[] tests = new Test[10];
    private Test test;

    public Test() {

    }

    public Test(String msg) {
        a = 1;
        b = 2;
        c = 3;
        d = 4;
        e = '5';
        f = 6;
        g = (float) 7.0;
        h = 8.0;
        i = 9;
        j = 10l;
        list.add(new Test(1));
        tests[0] = new Test(2);
        test = new Test();
    }

    public Test(int i) {
        a = 1;
        b = 2;
        c = 3;
        d = 4;
        e = '5';
        f = 6;
        g = (float) 7.0;
        h = 8.0;
        i = 9;
        j = 10l;
    }
}

class CollectionClassMapObjectUtil {
    private static final String BYTE = byte.class.getTypeName();
    private static final String BYTE_OBJECT = Byte.class.getTypeName();

    private static final String SHORT = short.class.getTypeName();
    private static final String SHORT_OBJECT = Short.class.getName();

    private static final String CHAR = char.class.getTypeName();
    private static final String CHARACTER = Character.class.getTypeName();

    private static final String INT = int.class.getTypeName();
    private static final String INTEGER = Integer.class.getTypeName();

    private static final String FLOAT = float.class.getTypeName();
    private static final String FLOAT_OBJECT = Float.class.getTypeName();

    private static final String DOUBLE = double.class.getTypeName();
    private static final String DOUBLE_OBJECT = Double.class.getTypeName();

    private static final String LONG = long.class.getTypeName();
    private static final String LONG_OBJECT = Long.class.getTypeName();

    private static final String STRING = String.class.getTypeName();

    public <T> T map(Map<? extends Object, ? extends Object> object, Class<T> clazz) throws TypeException {
        return mapToObject(object, clazz);
    }

    /**
     * 映射向一个自定义对象
     */
    private <T> T mapToObject(Map<? extends Object, ? extends Object> map, Class<T> clazz) throws TypeException {
        T t = null;
        try {
            t = clazz.newInstance();
            for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Object value = entry.getValue();
                Field field = clazz.getDeclaredField(key);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (value == null) {
                    field.set(t, null);
                    continue;
                }
                String valueTypeName = field.getType().getTypeName();
                if (valueTypeName.equals(BYTE) || valueTypeName.equals(BYTE_OBJECT)) {
                    field.set(t, Byte.valueOf(value.toString()));
                } else if (valueTypeName.equals(SHORT) || valueTypeName.equals(SHORT_OBJECT)) {
                    field.set(t, Short.valueOf(value.toString()));
                } else if (valueTypeName.equals(CHAR) || valueTypeName.equals(CHARACTER)) {
                    field.set(t, Character.valueOf(value.toString().charAt(0)));
                } else if (valueTypeName.equals(INT) || valueTypeName.equals(INTEGER)) {
                    field.set(t, Integer.valueOf(value.toString()));
                } else if (valueTypeName.equals(FLOAT) || valueTypeName.equals(FLOAT_OBJECT)) {
                    field.set(t, Float.valueOf(value.toString()));
                } else if (valueTypeName.equals(DOUBLE) || valueTypeName.equals(DOUBLE_OBJECT)) {
                    field.set(t, Double.valueOf(value.toString()));
                } else if (valueTypeName.equals(STRING)) {
                    field.set(t, value.toString());
                } else if (valueTypeName.equals(LONG) || valueTypeName.equals(LONG_OBJECT)) {
                    field.set(t, Long.valueOf(value.toString()));
                } else {
                    if (value instanceof Map) {
                        field.set(t, mapToObject((Map<Object, Object>) value, field.getType()));
                    } else if (value instanceof Collection && !field.getType().isArray()) {
                        String s = field.getGenericType().toString();
                        s = s.substring(s.indexOf('<') + 1, s.indexOf('>'));
                        Class<?> type = Class.forName(s);
                        Method method = clazz.getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
                        Object invoke = method.invoke(t);
                        mapToCollection((Collection<Object>) value, (Collection<Object>) invoke, type);
                    } else if (value instanceof Collection && field.getType().isArray()) {
                        Method method = clazz.getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
                        Object invoke = method.invoke(t);
                        mapToArray((Collection<Object>) value, invoke);
                    } else {
                        throw new TypeException("不支持" + value.getClass().getTypeName() + "与" + field.getType().getTypeName() + "转换");
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 映射向一个Collection集合
     */
    private void mapToCollection(Collection<? extends Object> collection, Collection<? extends Object> target, Class<? extends Object> valueClass) throws TypeException {
        String valueTypeName = valueClass.getTypeName();

        //目标对象
        //如果目标对象是Collection的子类
        if (target instanceof Collection) {
            Collection tem = (Collection) target;
            //依次添加目标Collection的元素
            Iterator<? extends Object> iterator = collection.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next == null) {
                    tem.add(null);
                }
                String value = next.toString();
                if (valueTypeName.equals(BYTE) || valueTypeName.equals(BYTE_OBJECT)) {
                    tem.add(Byte.valueOf(value));
                } else if (valueTypeName.equals(SHORT) || valueTypeName.equals(SHORT_OBJECT)) {
                    tem.add(Short.valueOf(value));
                } else if (valueTypeName.equals(CHAR) || valueTypeName.equals(CHARACTER)) {
                    tem.add(Character.valueOf(value.charAt(0)));
                } else if (valueTypeName.equals(INT) || valueTypeName.equals(INTEGER)) {
                    tem.add(Integer.valueOf(value));
                } else if (valueTypeName.equals(FLOAT) || valueTypeName.equals(FLOAT_OBJECT)) {
                    tem.add(Float.valueOf(value));
                } else if (valueTypeName.equals(DOUBLE) || valueTypeName.equals(DOUBLE_OBJECT)) {
                    tem.add(Double.valueOf(value));
                } else if (valueTypeName.equals(STRING)) {
                    tem.add(value);
                } else if (valueTypeName.equals(LONG) || valueTypeName.equals(LONG_OBJECT)) {
                    tem.add(Long.valueOf(value.toString()));
                } else {
                    if (next instanceof Map) {
                        tem.add(mapToObject((Map<Object, Object>) next, valueClass));
                    } else {
                        throw new TypeException("不支持" + value.getClass().getTypeName() + "与" + valueClass.getTypeName() + "转换");
                    }
                }
            }
        } else {
            throw new TypeException(target.getClass().getTypeName() + "不是Collection的子类");
        }
    }


    /**
     * 映射向一个数组
     */
    private void mapToArray(Collection<? extends Object> collection, Object array) throws TypeException {
        Class<?> clazz = array.getClass();
        if (clazz.isArray()) {
            String valueTypeName = clazz.getComponentType().getTypeName();
            //依次添加目标Collection的元素
            Iterator<? extends Object> iterator = collection.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next == null) {
                    Array.set(array, i++, null);
                    continue;
                }
                String value = next.toString();
                if (valueTypeName.equals(BYTE) || valueTypeName.equals(BYTE_OBJECT)) {
                    Array.set(array, i++, Byte.valueOf(value));
                } else if (valueTypeName.equals(SHORT) || valueTypeName.equals(SHORT_OBJECT)) {
                    Array.set(array, i++, Short.valueOf(value));
                } else if (valueTypeName.equals(CHAR) || valueTypeName.equals(CHARACTER)) {
                    Array.set(array, i++, Character.valueOf(value.toString().charAt(0)));
                } else if (valueTypeName.equals(INT) || valueTypeName.equals(INTEGER)) {
                    Array.set(array, i++, Integer.valueOf(value));
                } else if (valueTypeName.equals(FLOAT) || valueTypeName.equals(FLOAT_OBJECT)) {
                    Array.set(array, i++, Float.valueOf(value));
                } else if (valueTypeName.equals(DOUBLE) || valueTypeName.equals(DOUBLE_OBJECT)) {
                    Array.set(array, i++, Double.valueOf(value));
                } else if (valueTypeName.equals(STRING)) {
                    Array.set(array, i++, value.toString());
                } else if (valueTypeName.equals(LONG) || valueTypeName.equals(LONG_OBJECT)) {
                    Array.set(array, i++, Long.valueOf(value));
                } else {
                    if (next instanceof Map) {
                        Array.set(array, i++, mapToObject((Map<Object, Object>) next, clazz.getComponentType()));
                    } else {
                        throw new TypeException("不支持" + value.getClass().getTypeName() + "与" + clazz.getComponentType().getTypeName() + "转换");
                    }
                }
            }
        } else {
            throw new TypeException(clazz.getTypeName() + "必须是一维数组类型");
        }
    }


    public class TypeException extends Exception {
        public TypeException(String msg) {
            super(msg);
        }

        public TypeException() {
        }
    }
}
