package cn.zhg.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import cn.zhg.base.BaseActivity;
import cn.zhg.launcher.inter.Constant;

public class SettingActivity extends BaseActivity implements Constant, TextWatcher, OnClickListener
{
	private TextView versionCodeText;
	private TextView versionNameText;
	private TextView packageNameText;
	private TextView numcolumnsText;
	private CompoundButton wallpaperCheck;
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
		wallpaperCheck=this.getViewById(R.id.wallpaper_check);
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
		wallpaperCheck.setChecked(sh.getBoolean(KEY_Wallpaper, false));
		numcolumnsText.addTextChangedListener(this );
		wallpaperCheck.setOnClickListener(this);
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
	@Override
	public void onClick(View v)
	{
		if(v==this.wallpaperCheck){
			sh.edit().putBoolean(KEY_Wallpaper, wallpaperCheck.isChecked()).commit();
			Intent data=new Intent();
			data.putExtra(KEY_Recreate, true);
			this.setResult(RESULT_OK, data);
		}
	}

}
