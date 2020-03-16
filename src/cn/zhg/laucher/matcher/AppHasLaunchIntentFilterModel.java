/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午3:03:40
 */
package cn.zhg.laucher.matcher;

import cn.zhg.laucher.model.AppInfo;

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
