/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午5:49:38
 */
package cn.zhg.launcher.model;

import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import cn.zhg.basic.model.BaseData;

/**
 * 
 *
 */
public class AppPermissionInfo extends BaseData
{  
	public void updateData(Object context)
	{
		this.updateData((PackageManager)context);
	}

	public void updateData(PackageManager pm)
	{
		try
		{
			if (target == null )
			{
				if(name!=null)
				{ 
					PermissionInfo info = pm.getPermissionInfo(name,
							PackageManager.GET_META_DATA);
					this.target = info;
				}else
				{
					return;
				}
			} 
			this.label = target.loadLabel(pm);
			this.description = target.loadDescription(pm);
			this.icon = target.loadIcon(pm);
		} catch (Exception e)
		{
			// e.printStackTrace();
		}
		this.isLoaded = true;
	}
	/**
	 * 
	 */
	public AppPermissionInfo(String target )
	{
		this.name = target; 
	}
	public AppPermissionInfo(PermissionInfo target )
	{
		this.target=target; 
		this.name=target.name; 
	}
	public AppPermissionInfo(String target, PackageManager pm)
	{
		this(target);
		this.updateData(pm);
	}
	public AppPermissionInfo(PermissionInfo target, PackageManager pm)
	{
		this(target);
		this.updateData(pm);
	}
	/**
	 * 权限信息
	 */
	public PermissionInfo target;
	/**
	 * 权限内容
	 */
	public String name;
	/**
	 * 权限名称
	 */
	public CharSequence label;
 
	/**
	 * 权限详细描述
	 */
	public CharSequence description;
	/**
	 * 图标
	 */
	public Drawable icon;
}
