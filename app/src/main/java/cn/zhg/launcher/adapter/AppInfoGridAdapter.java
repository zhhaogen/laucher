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
 *应用列表适配器,以网格形式显示
 */
public class AppInfoGridAdapter extends SortFilterAdapter<AppInfo>
{ 
	public AppInfoGridAdapter(Context context, List<AppInfo> datas)
	{
		super(context,datas);
	}
	@Override
	public View onCreateView(ViewGroup parent)
	{
		return inflater.inflate(R.layout.item_app_simple, parent,
				false);
	}
	@Override
	public  BasicAdapter.BasicViewHolder<AppInfo> onCreateViewHolder(
			View convertView)
	{
		return new AppInfoViewHolder(convertView);
	} 
	private class AppInfoViewHolder extends BasicAdapter.BasicViewHolder<AppInfo>{
		private ImageView iconImage;
		private TextView appText;
		/**
		 * @param convertView
		 */
		public AppInfoViewHolder(View convertView)
		{
			super(convertView);
			iconImage=this.getViewById(R.id.icon_iv);
			appText=this.getViewById(R.id.app_tv);
		}

		/**
		 * @param item
		 */
		public void update(AppInfo item,int positon)
		{ 
			if (item.icon == null)
			{
				iconImage.setImageResource(android.R.drawable.sym_def_app_icon);
			} else
			{
				iconImage.setImageDrawable(item.icon);
			}
			if(item.label==null){
				appText.setText(item.packageName);
			}else{
				appText.setText(item.label);
			}
			
		}
		
	}
}
