package cn.zhg.launcher.util;

import static org.junit.Assert.*;

import android.content.Intent;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.*;

public class SomeUtilTest {
    static interface FlagClass{
        int FLAG_A=1<<0;
        int FLAG_B=1<<1;
        int FLAG_C=1<<2;
        int FLAG_D=1<<3;
        int FLAG_E=1<<4;
    }
    @Test
    public void testSpiltFlags() {
        int flags=FlagClass.FLAG_A |FlagClass.FLAG_C;
        List<String> ret = SomeUtil.spiltFlags(flags, FlagClass.class, "FLAG_");
        System.out.println(ret);
    }
    @Test
    public void testSpiltFlagsRnds() {
        for(int i=0;i<10;i++){
            testSpiltFlagsRnd();
        }
    }
    void testSpiltFlagsRnd(){
        String prefix="FLAG_";
        Class<?> clazz=FlagClass.class;
        Field[] fields = clazz.getFields();
        List<String> flagNames=new ArrayList<>();
        List<Integer> flagValues=new ArrayList<>();
        for(Field field:fields){
            int mod=field.getModifiers();
            if(!Modifier.isStatic(mod)){
                continue;
            }
            String name=field.getName();
            if(!name.startsWith(prefix)){
                continue;
            }
            try {
                flagNames.add(name);
                flagValues.add(field.getInt(null));
            } catch ( Exception e) {
            }
        }
        //正确结果
        Set<String> aFlagNames=new HashSet<>();
        int flags=0;
        Random rnd= ThreadLocalRandom.current();
        for(int i=0,size=flagNames.size();i<size;i++){
            int index=rnd.nextInt(size);
            String flagName=flagNames.get(index);
            int flag= flagValues.get(index);
            aFlagNames.add(flagName);
            if(i==0){
                flags=flag;
                continue;
            }
            flags=flags | flag;
        }
        System.out.println(aFlagNames);
        //实际结果
        List<String> rFlagNames = SomeUtil.spiltFlags(flags, clazz, prefix);
        System.out.println(rFlagNames);
        //
        assertTrue(aFlagNames.size()==rFlagNames.size());
        assertTrue(aFlagNames.containsAll(rFlagNames)&&rFlagNames.containsAll(aFlagNames));
    }
}