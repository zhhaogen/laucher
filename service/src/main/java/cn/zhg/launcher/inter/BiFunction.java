package cn.zhg.launcher.inter;
/**
 * api 24下支持
 * @see java.util.function.BiFunction
 * @param <T>
 * @param <R>
 */
public interface BiFunction<ARG1, ARG2, RET> {
    RET apply(ARG1 t, ARG2 u);
}
