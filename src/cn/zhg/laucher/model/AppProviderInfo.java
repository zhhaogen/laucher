/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午5:36:03
 */
package cn.zhg.laucher.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.PatternMatcher;

/**
 * 
 *
 */
public class AppProviderInfo extends BaseData
{ 
	public void updateData(Object context)
	{
		this.updateData((PackageManager)context);
	}
	public void updateData(PackageManager pm)
	{ 
		this.label=target.loadLabel(pm);
		this.dateloaded=true;
	}
	public AppProviderInfo(ProviderInfo target, PackageManager pm)
	{
		this(target);
		this.updateData(pm);
	}
	/**
	 * 
	 */
	public AppProviderInfo(ProviderInfo target )
	{
		this.target = target; 
		this.name = target.name;
		this.packageName = target.packageName;
		this.exported = target.exported;
		this.className = new ComponentName(packageName, name);
		this.authority = target.authority; 
		 this.uriPermissionPatterns=new ArrayList<>();
		 if(target.uriPermissionPatterns!=null)
		 {
			 for(PatternMatcher uri:target.uriPermissionPatterns )
			 {
				 uriPermissionPatterns.add(uri);
			 }
		 } 
	}
	public ProviderInfo target;
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
	/**
	 * content://内容
	 */
	public String authority;
	public  List<PatternMatcher> uriPermissionPatterns;
}
