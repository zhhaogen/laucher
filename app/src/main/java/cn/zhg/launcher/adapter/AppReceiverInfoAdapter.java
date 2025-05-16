package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.AppActivityInfo;
import cn.zhg.launcher.viewholder.ThreeLineViewHolder;

public class AppReceiverInfoAdapter extends SortFilterAdapter<AppActivityInfo>
{

	public AppReceiverInfoAdapter(Context context, List<AppActivityInfo> datas)
	{
		super(context, datas);
	}

	@Override
	public View onCreateView(ViewGroup parent)
	{
		return this.inflater.inflate(R.layout.item_threeline, parent, false);
	}

	@Override
	public  BasicAdapter.BasicViewHolder<AppActivityInfo> onCreateViewHolder(
			View itemView)
	{
		return new AppReceiverViewHolder(itemView);
	}
	private class AppReceiverViewHolder extends ThreeLineViewHolder<AppActivityInfo>{
		 
		public AppReceiverViewHolder(View itemView)
		{
			super(itemView); 
		} 
		public void update(final AppActivityInfo item, int position)
		{
			titleText.setText(item.name);
			desText.setText(item.label);  
			exportedCheck.setChecked(item.exported);
			enabledCheck.setChecked(item.enabled);
		}
		
	}
}
