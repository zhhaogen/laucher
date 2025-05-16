/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午3:03:40
 */
package cn.zhg.launcher.filter;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.basic.filter.*;
/**
 * 
 *
 */
public class AppHasLaunchIntentFilterModel extends BooleanFilterModel<AppInfo>
{ 
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppHasLaunchIntentFilterModel(String name)
	{
		super(name); 
	}

	@Override
	public boolean getDataValue(AppInfo data)
	{
		return data.hasLaunchIntent;
	}

}
