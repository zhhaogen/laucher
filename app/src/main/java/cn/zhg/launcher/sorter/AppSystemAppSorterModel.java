/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午12:45:16
 */
package cn.zhg.launcher.sorter;

import cn.zhg.basic.sorter.*;
import cn.zhg.launcher.model.AppInfo;

/**
 *  
 */
public class AppSystemAppSorterModel  extends ComparableSorterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppSystemAppSorterModel(String name)
	{
		super(name); 
	}

	@Override
	public Comparable<Boolean> getDataValue(AppInfo data)
	{
		return data.isSystemApp;
	}
 

}
