/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午7:53:44
 */
package cn.zhg.laucher.matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * 支持输入不同值
 */
public abstract class ValueFilterModel<T> extends FilterModel<T> 
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public ValueFilterModel(String name)
	{
		super(name); 
		supportMethods=new ArrayList<>();
	}
	
	public ValueFilterModel(String name,Collection<String> supportMethods)
	{
		super(name); 
		this.supportMethods=new ArrayList<>(supportMethods); 
	} 
	public ValueFilterModel(String name,String ...supports)
	{
		super(name); 
		supportMethods=new ArrayList<>();
		if(supports!=null)
		{
			for(String support:supports)
			{
				supportMethods.add(support);
			}
		}
	}  

	/**
	 * 支持方法
	 */
	public final List<String> supportMethods;
	/**
	 * 方法
	 */
	public String method;
	/**
	 * 输入值
	 */
	public String value;
	public abstract boolean match(String method,T data,String value);
	/**
	 * @param data
	 * @return
	 */
	public final boolean match(T data)
	{
		return this.match(method, data,value);
	} 
	public final boolean match(T data,String value)
	{
		return this.match(method, data,value);
	}
	public final String toString()
	{
		if(enable)
		{
			return name+"."+method+"("+value+")";
		}else
		{
			return name+"."+supportMethods;
		} 
	}
}
