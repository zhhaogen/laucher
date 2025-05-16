/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午5:52:14
 */
package cn.zhg.launcher.model;

import java.security.Principal;
import java.security.PublicKey;

import javax.security.cert.X509Certificate;

import android.content.pm.Signature;
import cn.zhg.basic.model.BaseData;
import cn.zhg.launcher.util.SomeUtil;

/**
 * 
 *
 */
public class AppSignatureInfo extends BaseData
{
	/**
	 * 
	 */
	public AppSignatureInfo(Signature target)
	{
		this.target = target;
		this.updateData(null);
	}

	public Signature target; 
	/**
	 * 颁发对象,使用者
	 **/
	public String subject;
	/**
	 * 颁发者
	 **/
	public	String issuer;
	/**
	 * 颁发日期
	 **/
	public String beginDate;
	/**
	 * 截止日期
	 **/
	public String endDate;
	/**
	 * SHA-1 指纹
	 */
	public String sha1;
	/**
	 * SHA-256 指纹
	 */
	public String sha256;
	/**
	 * 版本
	 */
	public int version;
	/**
	 * 序列号
	 */
	public String serialNumber;
	/**
	 * 签名算法
	 */
	public String signAlgorithm;
	/**
	 * 公钥算法
	 */
	public String pubKeyInfo;

	public String toString()
	{
		String info = "";
		info += "版本 :V" + version;
		if(subject!=null){
			info += "\n颁发对象 :"+ subject; 
		}
		if(issuer!=null){
			info += "\n颁发者  :"+ issuer;  
		}
		if(beginDate!=null){
			info += "\n开始时间 :" + beginDate;
		}
		if(endDate!=null){
			info += "\n截止时间 :" + endDate; 
		} 
		if(sha1!=null){
			info += "\n指纹sha1 :" + sha1;
		}
		if(sha256!=null){
			info += "\n指纹sha256 :" + sha256;
		} 
		if(endDate!=null){
			info += "\n序列号 :" + serialNumber;
		} 
		if(signAlgorithm!=null){
			info += "\n签名算法 :" + signAlgorithm; 
		}
		if(pubKeyInfo!=null){
			info += "\n公钥 :" + pubKeyInfo;
		} 
		return info;
	}

	public void updateData(Object context)
	{
		try
		{
			X509Certificate cer = X509Certificate
					.getInstance(target.toByteArray());
			version =cer .getVersion();
			serialNumber = cer.getSerialNumber().toString(16);
			subject = toString(cer.getSubjectDN());
			issuer = toString(cer.getIssuerDN());
			beginDate=SomeUtil.formatYMDHms(cer.getNotBefore());
			endDate=SomeUtil.formatYMDHms(cer.getNotAfter()); 
			signAlgorithm=cer.getSigAlgName();
			try {
				byte[] encoded = cer.getEncoded();
				sha1=SomeUtil.digest("sha1", encoded);
				sha256=SomeUtil.digest("sha-256", encoded);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			PublicKey pubKey = cer.getPublicKey();
			if(pubKey!=null){
				pubKeyInfo=pubKey.toString();
			} 
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		this.isLoaded = true;
	}

	private String toString(Principal principal)
	{
		if(principal==null){
			return null;
		}
		return principal.getName();
	}

}
