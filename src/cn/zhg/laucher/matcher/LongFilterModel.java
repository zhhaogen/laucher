/**
 * 
 * @author zhhaogen
 * 创建于 2019年7月17日 下午8:15:20
 */
package cn.zhg.laucher.matcher;

/**
 * 数值过滤器
 */
public abstract class LongFilterModel<T> extends  ValueFilterModel<T>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public LongFilterModel(String name)
	{
		super(name,">","<"); 
	}

	/**
	 * @return
	 */
	public abstract long getDataValue(T data) ;

	@Override
	public final boolean match(String method, T data, String _value)
	{
		long dataValue = getDataValue(data);
		long value = 0;
		try
		{
			value = Long.parseLong(_value);
		} catch (Exception igr)
		{
		}
		switch (method)
		{
		case ">":
			return value > dataValue;
		case "<":
			return value <dataValue;
		}
		return false;
	}

	 

}
