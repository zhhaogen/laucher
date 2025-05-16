/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午2:52:48
 */
package cn.zhg.launcher.model;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import cn.zhg.basic.model.BaseData;

/**
 * 
 *
 */
public class AppActivityInfo extends BaseData
{ 

	/**
	 * 
	 */
	public AppActivityInfo(ActivityInfo target )
	{
		this.target = target;  
		this.name=target.name;
		this.packageName=target.packageName;
		this.exported=target.exported; 
		this.enabled=target.enabled;
		this.className=new ComponentName(packageName,name); 
	}
	/**
	 * 
	 */
	public AppActivityInfo(ActivityInfo target, PackageManager pm)
	{
		this(target);
		this.updateData(pm);	 
	} 

	@Override
	public void updateData(Object context)
	{
		this.updateData((PackageManager)context);
	}
	public void updateData(	PackageManager pm)
	{
		if(isLoaded){
			return;
		}
		this.label=target.loadLabel(pm);
		this.isLoaded=true;
	}
	public ActivityInfo target;
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
	 * 是否启用
	 */
	public boolean enabled;
	/**
	 * 是否对外开放
	 */
	public boolean exported;
}
