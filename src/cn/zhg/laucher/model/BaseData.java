/**
 * 
 * @author zhhaogen
 * 创建于 2018年9月17日 下午4:21:44
 */
package cn.zhg.laucher.model;

/**
 * 基础数据模型
 */
public abstract class BaseData
{
	/**
	 * 标志,数据是否完全加载
	 */
	public boolean dateloaded;
	/**
	 * 更新数据
	 * @param context
	 */
	public abstract void updateData(Object context);
}
