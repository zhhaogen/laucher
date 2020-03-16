/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午12:45:16
 */
package cn.zhg.laucher.sorter;

import cn.zhg.laucher.model.AppInfo;

/**
 *  
 */
public class AppComponentsSizeSorterModel  extends ComparableSorterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppComponentsSizeSorterModel(String name)
	{
		super(name); 
	}

	@Override
	public Comparable<Integer> getDataValue(AppInfo data)
	{
		return data.activities.size() + data.services.size() + data.providers.size();
	}
 

}
