/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午5:52:14
 */
package cn.zhg.laucher.model;

import java.security.cert.X509Certificate;

import android.content.pm.Signature;
import cn.zhg.laucher.util.SomeUtils;

/**
 * 
 *
 */
public class AppSignatureInfo extends BaseData
{ 
	/**
	 * 
	 */
	public AppSignatureInfo(Signature target )
	{
		this.target=target;
		cert  = SomeUtils.getCert(target);
		if(cert!=null)
		{
			 
		}
		this.dateloaded=true;
	} 
	public Signature target;
	public X509Certificate cert; 
	public String toString()
	{
		return String.valueOf(cert);
	}  
	public void updateData(Object context)
	{
		this.dateloaded=true;
	} 
	
}
