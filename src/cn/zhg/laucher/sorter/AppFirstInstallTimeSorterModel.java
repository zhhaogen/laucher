/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午1:00:38
 */
package cn.zhg.laucher.sorter;

import cn.zhg.laucher.model.AppInfo;

/**
 * 
 *
 */
public class AppFirstInstallTimeSorterModel extends ComparableSorterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppFirstInstallTimeSorterModel(String name)
	{
		super(name); 
	}

	@Override
	public Comparable<Long> getDataValue(AppInfo data)
	{
		return data.firstInstallTime;
	}

}
