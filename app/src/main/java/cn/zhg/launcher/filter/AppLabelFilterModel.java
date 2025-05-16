/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午8:50:10
 */
package cn.zhg.launcher.filter;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.launcher.util.SomeUtil;
import cn.zhg.basic.filter.*;
/**
 * 
 *
 */
public class AppLabelFilterModel  extends StringFilterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppLabelFilterModel(String name)
	{
		super(name); 
	}

	@Override
	public String getDataValue(AppInfo data)
	{
		return SomeUtil.toString(data.label);
	}

}
