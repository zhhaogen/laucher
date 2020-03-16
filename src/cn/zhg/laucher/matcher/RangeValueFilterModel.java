/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午8:04:33
 */
package cn.zhg.laucher.matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 范围值过滤器
 */
public abstract class RangeValueFilterModel<T>  extends FilterModel<T> 
{ 
	/** */
	private static final long serialVersionUID = 1L;
	/**
	 * 支持的值
	 */
	public final List<String> supportValues; 
	/**
	 * 输入值
	 */
	public String value;
	/**
	 * @param name
	 */
	public RangeValueFilterModel(String name,String ...supports)
	{
		super(name); 
		supportValues=new ArrayList<>();
		if(supports!=null)
		{
			for(String support:supports)
			{
				supportValues.add(support);
			}
		}
	}
	public RangeValueFilterModel(String name,Collection<String> supportValues)
	{
		super(name); 
		this.supportValues=new ArrayList<>(supportValues); 
	}  

}
