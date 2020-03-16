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
 * 多个数值选择过滤器
 */
public abstract class MultiValueFilterModel<T>  extends FilterModel<T> 
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
	public final List<String> values;
	/**
	 * @param name
	 */
	public MultiValueFilterModel(String name,String ...supports)
	{
		super(name); 
		values=new ArrayList<>();
		supportValues=new ArrayList<>();
		if(supports!=null)
		{
			for(String support:supports)
			{
				supportValues.add(support);
			}
		}
	}
	public MultiValueFilterModel(String name,Collection<String> supportValues)
	{
		super(name); 
		values=new ArrayList<>();
		this.supportValues=new ArrayList<>(supportValues); 
	}  
	/**
	 * @return
	 */
	public abstract List<String> getDataValues(T data) ;

	public final boolean match(T data)
	{
		List<String> dataValues = getDataValues(data);
		if(values.isEmpty()){//没有选中任何内容
			return false;
		}
		for(String valueItem:values){
			if(dataValues.contains(valueItem)){//或关系
				return true;
			}
		}
		return false; 
	}  
}
