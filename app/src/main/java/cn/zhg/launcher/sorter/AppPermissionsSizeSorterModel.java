/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午12:45:16
 */
package cn.zhg.launcher.sorter;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.basic.sorter.*;
/**
 *  
 */
public class AppPermissionsSizeSorterModel  extends ComparableSorterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppPermissionsSizeSorterModel(String name)
	{
		super(name); 
	}

	@Override
	public Comparable<Integer> getDataValue(AppInfo data)
	{
		return data.permissions.size();
	}
 

}
