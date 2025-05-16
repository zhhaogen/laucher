/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午5:49:38
 */
package cn.zhg.launcher.model;

import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import cn.zhg.basic.model.BaseData;

/**
 * 
 *
 */
public class AppInstrumentationInfo extends BaseData
{  
	public void updateData(Object context)
	{
		this.updateData((PackageManager)context);
	}

	public void updateData(PackageManager pm)
	{
		try
		{   
			this.label=  target.loadLabel(pm);   
			if (android.os.Build.VERSION.SDK_INT >= 26)
			{
				this.targetProcesses=  target.targetProcesses;
			}  
			this.icon=target.loadIcon(pm);
		} catch ( Exception e)
		{
//			e.printStackTrace();
		}
		this.isLoaded=true;
	}
	public AppInstrumentationInfo(InstrumentationInfo target, PackageManager pm)
	{
		this(target);
		this.updateData(pm);
	}
	public AppInstrumentationInfo( InstrumentationInfo target )
	{
		this.target=target;
		this.name=target.name;
		this.targetPackage=  target.targetPackage; 
	}
	public String targetPackage;
	public String targetProcesses;
	/**
	 *  
	 */
	public InstrumentationInfo target;
	/**
	 *  
	 */
	public String name;
	/**
	 *  
	 */
	public CharSequence label;
  
	/**
	 * 图标
	 */
	public Drawable icon;
}
