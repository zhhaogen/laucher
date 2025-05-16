package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.AppSourceInfo;
import cn.zhg.launcher.util.SomeUtil;
import cn.zhg.launcher.viewholder.TwoLineViewHolder;

public class AppSourceInfoAdapter extends SortFilterAdapter<AppSourceInfo>
{

	public AppSourceInfoAdapter(Context context, List<AppSourceInfo> datas)
	{
		super(context, datas);
	}

	@Override
	public View onCreateView(ViewGroup parent)
	{
		return this.inflater.inflate(R.layout.item_twoline, parent, false);
	}

	@Override
	public  BasicAdapter.BasicViewHolder<AppSourceInfo> onCreateViewHolder(
			View itemView)
	{
		return new AppSourceInfoViewHolder(itemView); 
	}
	private class AppSourceInfoViewHolder extends TwoLineViewHolder<AppSourceInfo>{
		 
		public AppSourceInfoViewHolder(View itemView)
		{
			super(itemView); 
		} 
		public void update(final AppSourceInfo item, int position)
		{
			titleText.setText(item.name);
			desText.setText(SomeUtil.join("\n", item.sourceDir,item.publicSourceDir));  
		}
		
	}
}
