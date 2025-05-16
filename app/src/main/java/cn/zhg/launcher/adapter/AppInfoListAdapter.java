/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月26日 下午4:02:18
 */
package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView; 
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.AppInfo;
import cn.zhg.basic.adapter.BasicAdapter;
/**
 * 应用列表适配器,以列表形式显示
 */
public class AppInfoListAdapter extends SortFilterAdapter<AppInfo>
{ 
	public AppInfoListAdapter(Context context, List<AppInfo> datas)
	{
		super(context,datas);
	} 
	public View onCreateView(ViewGroup parent)
	{
		return this.inflater.inflate(R.layout.item_app_detail, parent, false);
	} 
	public BasicViewHolder<AppInfo> onCreateViewHolder(
			View convertView)
	{
		return new AppInfoViewHolder(convertView);
	} 
	private class AppInfoViewHolder extends BasicAdapter.BasicViewHolder<AppInfo>{
		private ImageView iconImage;
		private TextView appText;
		private TextView versionCodeText; 
		private TextView packageNameText; 
		public AppInfoViewHolder(View convertView)
		{
			super(convertView);
			iconImage=this.getViewById(R.id.icon_iv);
			appText=this.getViewById(R.id.app_tv);
			packageNameText=this.getViewById(R.id.packagename_tv);
			versionCodeText=this.getViewById(R.id.versioncode_tv);
		} 
		public void update(AppInfo item, int position)
		{
			if (item.icon == null)
			{
				iconImage.setImageResource(android.R.drawable.sym_def_app_icon);
			} else
			{
				iconImage.setImageDrawable(item.icon);
			}
			appText.setText(item.label);
			packageNameText.setText(item.packageName);
			versionCodeText.setText(item.versionName+"["+item.versionCode+"]");
		} 
	}
}
