/**
 * 
 * @author zhhaogen
 * 创建于 2018年4月30日 下午7:38:43
 */
package cn.zhg.laucher.sorter;

import java.util.Comparator;

/**
 * 
 *排序器
 */
public abstract class SorterModel<T> implements Comparator<T> ,java.io.Serializable
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public SorterModel(String name)
	{
		this.name=name;
	}
	/**
	 * 排序器名称
	 */
	public String name;
	/**
	 * 是否反转排序
	 */
	public boolean isReverse;
	  
	public int compare(T o1, T o2)
	{
		int ret=0;
		if(o1==null&&o2==null)
		{
			ret=0;
		}else if(o1==null&&o2!=null)
		{
			ret=-1;
		} else if(o1!=null&&o2==null)
		{
			ret=1;
		}else
		{
			  ret = compareNotNull(o1,o2); 
		}
		return isReverse?ret:-ret;
	}

	/**
	 * 比较不为null值
	 * @param o1
	 * @param o2
	 * @return o1-o2值
	 */
	public abstract int compareNotNull(T o1, T o2) ;
 
	public String toString()
	{
		return (this.isReverse?"-":"+")+name;
	}
	
}
