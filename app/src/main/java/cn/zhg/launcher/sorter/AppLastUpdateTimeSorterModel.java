/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午1:00:38
 */
package cn.zhg.launcher.sorter;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.basic.sorter.*;
/**
 * 
 *
 */
public class AppLastUpdateTimeSorterModel extends ComparableSorterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppLastUpdateTimeSorterModel(String name)
	{
		super(name); 
	}

	@Override
	public Comparable<Long> getDataValue(AppInfo data)
	{
		return data.lastUpdateTime;
	}

}
