/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午2:52:48
 */
package cn.zhg.launcher.model;

/**
 * 
 *
 */
public class AppSourceInfo
{ 
	 public String name;
	 public String sourceDir;
	 public String publicSourceDir;
	/**
	 * 
	 */
	public AppSourceInfo(String name, String sourceDir, String publicSourceDir)
	{
		super();
		this.name = name;
		this.sourceDir = sourceDir;
		this.publicSourceDir = publicSourceDir;
	}
	 
}
