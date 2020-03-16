/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月26日 下午4:02:18
 */
package cn.zhg.laucher.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.zhg.base.BaseViewHolder;
import cn.zhg.laucher.R;
import cn.zhg.laucher.model.AppInfo;

/**
 * 
 *
 */
public class AppInfoListAdapter extends BaseAppInfoAdapter
{ 
	public AppInfoListAdapter(Context context, List<AppInfo> datas)
	{
		super(context,datas);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{ 
		AppInfoViewHolder holder;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.item_app_detail, parent,
					false);
			holder=new AppInfoViewHolder(convertView);
			convertView.setTag(holder);
		} else
		{
			holder=(AppInfoViewHolder) convertView.getTag();
		}
		final AppInfo item = datas.get(position);
		holder.update(item); 
		if(onAppInfoClickListener!=null){
			convertView.setOnClickListener(new View.OnClickListener()
			{ 
				public void onClick(View v)
				{
					onAppInfoClickListener.onClick(item); 
				}
			});
		}
		if(onAppInfoLongClickListener!=null){
			convertView.setOnLongClickListener(new View.OnLongClickListener()
			{ 
				public boolean onLongClick(View v)
				{  
					return onAppInfoLongClickListener.onLongClick(v,item);
				}
			});
		}
		
		
		return convertView;
	}
	private class AppInfoViewHolder extends BaseViewHolder{
		private ImageView iconImage;
		private TextView appText;
		private TextView versionCodeText; 
		private TextView packageNameText;
		/**
		 * @param convertView
		 */
		public AppInfoViewHolder(View convertView)
		{
			super(convertView);
			iconImage=this.getViewById(R.id.icon_iv);
			appText=this.getViewById(R.id.app_tv);
			packageNameText=this.getViewById(R.id.packagename_tv);
			versionCodeText=this.getViewById(R.id.versioncode_tv);
		}

		/**
		 * @param item
		 */
		public void update(AppInfo item)
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
