package cn.zhg.launcher.inter;

/**
 * api 24下支持
 * @see java.util.function.Function
 * @param <T>
 * @param <R>
 */
public interface Function<ARG, RET> {
    RET apply(ARG t);
}
