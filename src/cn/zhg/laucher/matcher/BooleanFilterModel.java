/**
 * 
 * @author zhhaogen
 * 创建于 2019年7月17日 下午8:15:20
 */
package cn.zhg.laucher.matcher;

/**
 * 布尔过滤器
 */
public abstract class BooleanFilterModel<T> extends  RangeValueFilterModel<T>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public BooleanFilterModel(String name)
	{
		super(name,"true","false"); 
	}

	/**
	 * @return
	 */
	public abstract boolean getDataValue(T data) ;

	public final boolean match(T data)
	{
		boolean dataValue=getDataValue(data); 
		if("true".equals(value)){
			return dataValue;
		}else{
			return !dataValue;
		} 
	}  

}
