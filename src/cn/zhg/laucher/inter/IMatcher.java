/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 上午11:33:44
 */
package cn.zhg.laucher.inter;

/**
 * 匹配规则
 */
public interface IMatcher<T>
{
	/**
	 * 是否符合规则
	 * @param data
	 * @return
	 */
	boolean match(T data);
}
