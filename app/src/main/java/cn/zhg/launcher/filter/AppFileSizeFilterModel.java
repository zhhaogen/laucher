/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午8:50:10
 */
package cn.zhg.launcher.filter;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.basic.filter.*;
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
