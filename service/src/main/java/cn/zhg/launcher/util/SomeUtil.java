/**
 * @author zhhaogen
 * 创建于 2018年4月30日 上午1:38:46
 */
package cn.zhg.launcher.util;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.zhg.launcher.inter.*;

/**
 *
 */
public final class SomeUtil {
    private static ThreadLocal<SimpleDateFormat> format1 = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static ThreadLocal<SimpleDateFormat> format2 = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };

    private SomeUtil() {
    }
    public static String formatYMDHms(long date) {
        if (date <=0) {
            return date+"";
        }
        return format1.get().format(new Date(date));
    }
    public static String formatYMDHms(Date date) {
        if (date == null) {
            return null;
        }
        return format1.get().format(date);
    }

    /**
     * 是否今天
     *
     * @param da
     * @return
     */
    public static boolean isToday(Date da) {
        Calendar now = Calendar.getInstance();
        final int year = now.get(Calendar.YEAR);
        final int month = now.get(Calendar.MONTH);
        final int day = now.get(Calendar.DAY_OF_MONTH);
        now.setTime(da);
        return (now.get(Calendar.YEAR) == year)
                && (now.get(Calendar.MONTH) == month)
                && (now.get(Calendar.DAY_OF_MONTH) == day);
    }

    /**
     * 获取上次修改的时间
     *
     * @param f
     * @return
     */
    public static String getLastModified(long lastModified) {
        if (lastModified <= 0) {
            return "-";
        } else {
            Date da = new Date(lastModified);
            if (isToday(da)) {
                return format2.get().format(da);
            } else {
                return format1.get().format(da);
            }
        }
    }

    /**
     * @param l
     * @return
     */
    public static int getArrayLength(Object[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    public static int getArrayLength(List array) {
        if (array == null) {
            return 0;
        }
        return array.size();
    }

    /**
     * @param size
     * @return
     */
    public static String getFileSize(long size) {
        if (size <= 0) {
            return size + "";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#.##").format(
                size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    public static String toString(CharSequence s) {
        if (s == null) {
            return null;
        }
        return s.toString();
    }

    /**
     * @param o1
     * @param o2
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static int compare(Comparable o1, Comparable o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null && o2 != null) {
            return -1;
        } else if (o1 != null && o2 == null) {
            return 1;
        } else {
            return o1.compareTo(o2);
        }

    }

    /**
     * 查找或添加
     *
     * @param map
     * @param key
     * @param value
     * @return
     */
    public static <K, V> V getOrPut(Map<K, V> map, K key, V value) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            map.put(key, value);
            return value;
        }
    }

    /**
     * @param l
     * @return
     */
    public static String getDurationTime(long ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(ms);
    }


    /**
     * @param delimiter
     * @param elements
     * @return
     */
    public static CharSequence join(String delimiter, List<String> elements) {
        if (elements == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = elements.size(); i < size; i++) {
            if (i != 0) {
                sb.append(delimiter);
            }
            sb.append(elements.get(i));
        }
        return sb.toString();
    }

    public static CharSequence join(String delimiter, CharSequence... elements) {
        if (elements == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null || elements[i].length() == 0) {
                continue;
            }
            if (count != 0) {
                sb.append(delimiter);
            }
            sb.append(elements[i]);
            count++;
        }
        return sb.toString();
    }

    /**
     * hash算法
     *
     * @param data
     *            二进制数据
     * @param algorithm
     *            算法名称
     * @return
     */
    public static String digest(String algorithm, byte[] input) {
        if (input == null) {
            return null;
        }
        try {
            MessageDigest dig = MessageDigest.getInstance(algorithm);
            return new BigInteger(1, dig.digest(input)).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制流,但不会关闭流
     */
    public static boolean copy(InputStream is, OutputStream os, int buffSize)
            throws IOException {
        if (buffSize <= 0) {
            buffSize = 1024 * 1024 * 10;
        }
        byte[] buffer = new byte[buffSize];
        int leng = 0;
        while ((leng = is.read(buffer)) != -1) {
            os.write(buffer, 0, leng);
        }
        return true;
    }

    /**
     * 转换为<code>|</code>拼接的flags 为列表名称
     * @param flags <code>|</code>拼接的flags
     * @param clazz  Flag所在的类
     * @param prefix  字段前缀
     */
    public static List<String> spiltFlags(int flags, Class<?> clazz, String prefix) {
        Field[] fs = clazz.getFields();
        List<String> result = new ArrayList<>();
        for (Field f : fs) {
            int mod = f.getModifiers();
            if (!Modifier.isStatic(mod)) {
                continue;
            }
            String name = f.getName();
            if (prefix != null && !name.startsWith(prefix)) {
                continue;
            }
            try {
                int i = f.getInt(null);
                if ((flags & i) == i) {
                    result.add(name);
                }
            } catch (Exception igr) {
            }
        }
        return result;
    }
    public static <T>T getArrayItem(T[] array, int position) {
        if(array==null){
            return null;
        }
        return array[position];
    }
    /**
     * 因为Collections.addAll不支持null
     */
    public static  void addAll(Collection<Integer> list,int ...items){
        if(items==null){
            return;
        }
        for(int item:items){
            list.add(item);
        }
    }
    /**
     * 因为Collections.addAll不支持null
     */
    public static <T>void addAll(Collection<T> list,T ...items){
       if(items==null){
           return;
       }
       for(T item:items){
           list.add(item);
       }
    }
    /**
     *
     */
    public static <T,K>void addAllMap(Collection<T> list, K items[], Function<K,T> fun){
        if(items==null){
            return;
        }
        for(K item:items){
            list.add(fun.apply(item));
        }
    }
    /**
     *
     */
    public static <T,K>void addAllMap(Collection<T> list, K items[], BiFunction<K,Integer,T> fun){
        if(items==null){
            return;
        }
        for(int i=0;i<items.length;i++){
            list.add(fun.apply(items[i],i));
        }
    }
}
