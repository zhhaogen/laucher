/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午9:14:34
 */
package cn.zhg.launcher.filter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.basic.filter.*;
/**
 * 
 *
 */
public class AppRequestPermissionsFilterModel extends MultiValueFilterModel<AppInfo>
{ 
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppRequestPermissionsFilterModel(String name)
	{
		super(name);
		for (Field f : android.Manifest.permission.class.getFields())
		{
			int mod = f.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isPublic(mod))
			{
				this.supportValues.add(f.getName());
			}
		}
	}

	@Override
	public List<String> getDataValues(AppInfo data)
	{
		List<String> list=new ArrayList<>(data.requestedPermissions.size());
		return list;
	}

}
