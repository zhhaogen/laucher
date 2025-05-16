/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午9:01:00
 */
package cn.zhg.launcher.filter;

import java.lang.reflect.Field;
import java.util.List;

import android.content.pm.ApplicationInfo;
import cn.zhg.launcher.model.AppInfo;
import cn.zhg.basic.filter.*;
/**
 * 
 *
 */
public class AppFlagsFilterModel extends MultiValueFilterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppFlagsFilterModel(String name)
	{
		super(name); 
		Field[] fs = ApplicationInfo.class.getFields(); 
		for(Field f:fs)
		{
			String fieldName=f.getName();
			if(fieldName.startsWith("FLAG_"))
			{
				this.supportValues.add(fieldName);
			}
		}
	}

	@Override
	public List<String> getDataValues(AppInfo data)
	{
		return data.flagFields;
	}

}
