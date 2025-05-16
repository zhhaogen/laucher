package cn.zhg.launcher.util;

import android.content.pm.Signature;
import android.view.Menu;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * 因为Junit不支持android类,所以此处分开SomeAndroidUtil
 */
public final class SomeAndroidUtil {
    private SomeAndroidUtil(){}

    /**
     * 强制显示图标
     *
     * @param pop
     */
    public static void showIcon(Menu menu)
    {
        try
        {
            Class<?> clazz = Class
                    .forName("com.android.internal.view.menu.MenuBuilder");
            Method method = clazz.getDeclaredMethod("setOptionalIconsVisible",
                    boolean.class);
            boolean isAccessible = method.isAccessible();
            if (!isAccessible)
            {
                method.setAccessible(true);
            }
            method.invoke(menu, true);
            if (!isAccessible)
            {
                method.setAccessible(false);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取签名证书
     *
     * @param sign
     * @return
     */
    public static X509Certificate getCert(Signature sign)
    {
        try
        {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(
                            new ByteArrayInputStream(sign.toByteArray()));
            return cert;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
