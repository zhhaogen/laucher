/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午12:46:20
 */
package cn.zhg.laucher.sorter;

import cn.zhg.laucher.util.SomeUtils;

/**
 * 
 *
 */
public  abstract  class ComparableSorterModel<T> extends SorterModel<T>
{ 
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public ComparableSorterModel(String name)
	{
		super(name); 
	}
	@SuppressWarnings("rawtypes")
	public abstract Comparable getDataValue(T data) ;
	@Override
	public int compareNotNull(T o1, T o2)
	{
		  return SomeUtils.compare(getDataValue(o1),
				  getDataValue(o2));
	}

	 

}
