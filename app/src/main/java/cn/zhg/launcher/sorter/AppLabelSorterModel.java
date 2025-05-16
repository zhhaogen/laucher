/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午12:45:16
 */
package cn.zhg.launcher.sorter;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.launcher.util.SomeUtil;
import cn.zhg.basic.sorter.*;
/**
 * 
 *应用名排序
 */
public class AppLabelSorterModel  extends ComparableSorterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppLabelSorterModel(String name)
	{
		super(name); 
	}

	@Override
	public String getDataValue(AppInfo data)
	{
		return SomeUtil.toString(data.label);
	}
 

}
