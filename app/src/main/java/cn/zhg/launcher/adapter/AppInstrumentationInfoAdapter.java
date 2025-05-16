package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.AppInstrumentationInfo;
import cn.zhg.launcher.viewholder.TwoLineViewHolder;

public class AppInstrumentationInfoAdapter extends SortFilterAdapter<AppInstrumentationInfo>
{

	public AppInstrumentationInfoAdapter(Context context,
			List<AppInstrumentationInfo> datas)
	{
		super(context, datas);
	}

	@Override
	public View onCreateView(ViewGroup parent)
	{
		return this.inflater.inflate(R.layout.item_twoline, parent, false);
	}

	@Override
	public BasicAdapter.BasicViewHolder<AppInstrumentationInfo> onCreateViewHolder(
			View itemView)
	{
		return new AppInstrumentationInfoViewHolder(itemView);
	}
	private class AppInstrumentationInfoViewHolder extends TwoLineViewHolder<AppInstrumentationInfo>{
		public AppInstrumentationInfoViewHolder(View itemView)
		{
			super(itemView);
		} 
		public void update(AppInstrumentationInfo item, int position)
		{
			titleText.setText(item.name);
			desText.setText(item.label);
		}		
	}
}
