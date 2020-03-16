/**
 * 
 * @author zhhaogen
 * 创建于 2018年4月30日 上午1:38:46
 */
package cn.zhg.laucher.util;

import java.io.*;
import java.lang.reflect.Method;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import android.content.pm.Signature;
import android.view.Menu;

/**
 * 
 *
 */
public class SomeUtils
{
	private static SimpleDateFormat format1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
	 
	/**
	 * 是否今天
	 * 
	 * @param da
	 * @return
	 */
	public static boolean isToday(Date da)
	{
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
	public static String getLastModified(long lastModified)
	{
		if (lastModified <= 0)
		{
			return "-";
		} else
		{
			Date da = new Date(lastModified);
			if (isToday(da))
			{
				return format2.format(da);
			} else
			{
				return format1.format(da);
			}
		}
	}

	/**
	 * @param l
	 * @return
	 */
	public static int getArrayLength(Object[] array)
	{
		if (array == null)
		{
			return 0;
		}
		return array.length;
	}

	/**
	 * @param size
	 * @return
	 */
	public static String getFileSize(long size)
	{
		if (size <= 0)
		{
			return size + "";
		}
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#.##").format(
				size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
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

	public static String toString(CharSequence s)
	{
		if (s == null)
		{
			return null;
		} 
		return s.toString();
	}
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int compare(Comparable o1, Comparable o2)
	{
		if (o1 == null && o2 == null)
		{
			return 0;
		} else if (o1 == null && o2 != null)
		{
			return -1;
		} else if (o1 != null && o2 == null)
		{
			return 1;
		} else
		{
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
	public static <K, V> V getOrPut(Map<K, V> map, K key, V value)
	{
		if (map.containsKey(key))
		{
			return map.get(key);
		} else
		{
			map.put(key, value);
			return value;
		}
	}

	/**
	 * @param l
	 * @return
	 */
	public static String getDurationTime(long ms)
	{
		 SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
	        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));   
		return formatter.format(ms);
	}

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
		} catch ( Exception e)
		{
			 e.printStackTrace();
		}
	}

	/**
	 * @param delimiter
	 * @param elements
	 * @return
	 */
	public static CharSequence join(String delimiter, List<String> elements)
	{ 
		 if(elements==null){
			 return null;
		 }
		 StringBuilder sb=new StringBuilder(); 
		 for(int i=0,size=elements.size();i<size;i++){
			 if(i!=0){
				 sb.append(delimiter);
			 }
			 sb.append(elements.get(i));
		 }
		return sb.toString();
	} 
}
