package cn.zhg.laucher;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import cn.zhg.base.BaseActivity;
import cn.zhg.laucher.inter.Constant;

public class SettingActivity extends BaseActivity implements Constant, TextWatcher
{
	private TextView versionCodeText;
	private TextView versionNameText;
	private TextView packageNameText;
	private TextView numcolumnsText;
	private SharedPreferences sh;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		versionCodeText=this.getViewById(R.id.versioncode_tv);
		versionNameText=this.getViewById(R.id.versionname_tv);
		packageNameText=this.getViewById(R.id.packagename_tv);
		numcolumnsText=this.getViewById(R.id.numcolumns_tv);
		String packageName=this.getPackageName();
		packageNameText.setText(packageName);
		try
		{
			PackageInfo info = this.getPackageManager().getPackageInfo(packageName, 0);
			versionCodeText.setText(String.valueOf(info.versionCode));
			versionNameText.setText(info.versionName);
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		sh = this.getSharedPreferences(getPackageName(), MODE_PRIVATE);
		numcolumnsText.setText(String.valueOf(sh.getInt(KEY_NumColumns, 5)));
		numcolumnsText.addTextChangedListener(this );
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
	}
	@Override
	public void afterTextChanged(Editable s)
	{
		String nums=s.toString();
		if(nums.isEmpty()){ 
			return;
		}
		try{
			int num=Integer.parseInt(nums);
			if(num<1||num>10){
				tip(R.string.tip_numcolumns_error);
				return ;
			}
			sh.edit().putInt(KEY_NumColumns, num).commit();
		}catch(Exception ex){
			
		}
	}

}
