/**
 * 
 * @author zhhaogen
 * 创建于 2019年7月17日 下午8:15:20
 */
package cn.zhg.laucher.matcher;

/**
 * 字符串过滤器
 */
public abstract class StringFilterModel<T> extends  ValueFilterModel<T>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param name
	 */
	public StringFilterModel(String name)
	{
		super(name,"contains","startsWith","endsWith","equals","matches","empty"); 
	}

	/**
	 * @return
	 */
	public abstract String getDataValue(T data) ;

	@Override
	public final boolean match(String method, T data, String value)
	{
		String dataValue=getDataValue(data);
		if(method.equals("empty"))
		{
			return dataValue==null||dataValue.isEmpty();
		}
		if (dataValue== null  )
		{
			return false;
		}
		switch (method)
		{
		case "startsWith":
			return dataValue.startsWith(value);
		case "endsWith":
			return dataValue.endsWith(value);
		case "equals":
			return value.contentEquals(dataValue);
		case "contains":
			return dataValue.contains(value);
		case "matches":
			return dataValue.matches(value);
		}
		return false;
	}

	 

}
