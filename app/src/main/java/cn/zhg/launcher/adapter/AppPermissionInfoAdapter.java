package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.*;
import cn.zhg.launcher.viewholder.TwoLineViewHolder;

public class AppPermissionInfoAdapter extends SortFilterAdapter<AppPermissionInfo>
{

	public AppPermissionInfoAdapter(Context context,
			List<AppPermissionInfo> datas)
	{
		super(context, datas);
	}

	@Override
	public View onCreateView(ViewGroup parent)
	{
		return this.inflater.inflate(R.layout.item_twoline, parent, false);
	}

	@Override
	public BasicAdapter.BasicViewHolder<AppPermissionInfo> onCreateViewHolder(
			View itemView)
	{
		return new AppPermissionInfoViewHolder(itemView);
	}
	private class AppPermissionInfoViewHolder extends TwoLineViewHolder<AppPermissionInfo>{
		public AppPermissionInfoViewHolder(View itemView)
		{
			super(itemView);
		} 
		public void update(AppPermissionInfo item, int position)
		{
			titleText.setText(item.name);
			desText.setText(item.label);
		}		
	}
}
