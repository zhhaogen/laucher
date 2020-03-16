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
public class AppPackageNameFilterModel  extends StringFilterModel<AppInfo>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public AppPackageNameFilterModel(String name)
	{
		super(name); 
	}

	@Override
	public String getDataValue(AppInfo data)
	{
		return data.packageName;
	}

}
