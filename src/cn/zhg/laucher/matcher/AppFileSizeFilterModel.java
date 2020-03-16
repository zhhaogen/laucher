/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午8:50:10
 */
package cn.zhg.laucher.matcher;

import cn.zhg.laucher.model.AppInfo;

/**
 * 
 *
 */
public class AppFileSizeFilterModel  extends LongFilterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppFileSizeFilterModel(String name)
	{
		super(name); 
	}

	@Override
	public long getDataValue(AppInfo data)
	{
		return data.fileSize;
	}

}
