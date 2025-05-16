package cn.zhg.launcher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.AppActivityInfo;
import cn.zhg.launcher.viewholder.ThreeLineOpenViewHolder;

public class AppActivityInfoAdapter extends SortFilterAdapter<AppActivityInfo>
{

	public AppActivityInfoAdapter(Context context, List<AppActivityInfo> datas)
	{
		super(context, datas);
	}

	@Override
	public View onCreateView(ViewGroup parent)
	{
		return this.inflater.inflate(R.layout.item_threeline_open, parent, false);
	}

	@Override
	public  BasicAdapter.BasicViewHolder<AppActivityInfo> onCreateViewHolder(
			View itemView)
	{
		return new AppActivityInfoViewHolder(itemView);
	}
	private class AppActivityInfoViewHolder extends ThreeLineOpenViewHolder<AppActivityInfo>{
 
		public AppActivityInfoViewHolder(View itemView)
		{
			super(itemView); 
		} 
		public void update(final AppActivityInfo item, int position)
		{
			titleText.setText(item.name);
			desText.setText(item.label);
			enabledCheck.setChecked(item.enabled);
			exportedCheck.setChecked(item.exported);
			openBtn.setOnClickListener(new View.OnClickListener()
			{ 
				public void onClick(View v)
				{
					try
					{
						Intent intent = new Intent(); 
						intent.setComponent(item.className);
						context.startActivity(intent);
					} catch (Exception igr)
					{
						Toast.makeText(context, igr.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		
	}
}
