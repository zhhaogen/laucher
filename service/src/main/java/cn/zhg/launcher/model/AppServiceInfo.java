/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午2:52:48
 */
package cn.zhg.launcher.model;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import cn.zhg.basic.model.BaseData;

/**
 * 
 *
 */
public class AppServiceInfo extends BaseData
{ 
	/**
	 * 
	 */
	public AppServiceInfo(ServiceInfo target)
	{
		this.target = target;  
		this.name=target.name;
		this.packageName=target.packageName;
		this.exported=target.exported; 
		this.className=new ComponentName(packageName,name);
	}
	/**
	 * 
	 */
	public AppServiceInfo(ServiceInfo target, PackageManager pm)
	{
		this(target);
		this.updateData(pm);
	}
	@Override
	public void updateData(Object context)
	{
		this.updateData((PackageManager)context);
	}
	public void updateData(PackageManager pm)
	{
		this.label=target.loadLabel(pm);
		this.isLoaded=true;
	}
	public ServiceInfo target;
	/**
	 * 类名
	 */
	public ComponentName className;
	/**
	 * 标签
	 */
	public CharSequence label;
	/**
	 * 应用包名
	 */
	public String packageName;
	/**
	 * class简单名称
	 */
	public String name;
	/**
	 * 是否对外开放
	 */
	public boolean exported;
}
