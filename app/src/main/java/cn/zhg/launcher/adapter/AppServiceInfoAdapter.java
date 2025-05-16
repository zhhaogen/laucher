package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.AppServiceInfo;
import cn.zhg.launcher.viewholder.ThreeLineOpenViewHolder;

public class AppServiceInfoAdapter extends SortFilterAdapter<AppServiceInfo>
{

	public AppServiceInfoAdapter(Context context, List<AppServiceInfo> datas)
	{
		super(context, datas);
	}

	@Override
	public View onCreateView(ViewGroup parent)
	{
		return this.inflater.inflate(R.layout.item_threeline_open, parent, false);
	}

	@Override
	public  BasicAdapter.BasicViewHolder<AppServiceInfo> onCreateViewHolder(
			View itemView)
	{
		return new AppServiceInfoViewHolder(itemView);
	}
	private class AppServiceInfoViewHolder extends ThreeLineOpenViewHolder<AppServiceInfo>{
	 
		public AppServiceInfoViewHolder(View itemView)
		{
			super(itemView); 
		} 
		public void update(final AppServiceInfo item, int position)
		{
			titleText.setText(item.name);
			desText.setText(item.label);
			enabledCheck.setChecked(item.target.enabled);
			exportedCheck.setChecked(item.exported);
			openBtn.setOnClickListener(new View.OnClickListener()
			{ 
				public void onClick(View v)
				{
					try
					{
						Intent intent = new Intent(); 
						intent.setComponent(item.className);
						context.startService(intent);
					} catch (Exception igr)
					{
						Toast.makeText(context, igr.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		
	}
}
