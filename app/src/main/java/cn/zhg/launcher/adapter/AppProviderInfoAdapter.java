package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.*;
import cn.zhg.launcher.viewholder.ThreeLineViewHolder;

public class AppProviderInfoAdapter extends SortFilterAdapter<AppProviderInfo>
{

	public AppProviderInfoAdapter(Context context,
			List<AppProviderInfo> datas)
	{
		super(context, datas);
	}

	@Override
	public View onCreateView(ViewGroup parent)
	{
		return this.inflater.inflate(R.layout.item_threeline, parent, false);
	}

	@Override
	public BasicAdapter.BasicViewHolder<AppProviderInfo> onCreateViewHolder(
			View itemView)
	{
		return new AppProviderInfoViewHolder(itemView);
	}
	private class AppProviderInfoViewHolder extends ThreeLineViewHolder<AppProviderInfo>{
		public AppProviderInfoViewHolder(View itemView)
		{
			super(itemView);
		} 

		public void update(AppProviderInfo item, int position)
		{
			titleText.setText(item.name);
			StringBuilder sb = new StringBuilder();
			if (!TextUtils.isEmpty(item.label))
			{
				sb.append(item.label).append("\n");
			}
			if (!TextUtils.isEmpty(item.target.readPermission))
			{
				sb.append(context.getString(R.string.lab_read_permission))
						.append(item.target.readPermission).append("\n");
			}
			if (!TextUtils.isEmpty(item.target.writePermission))
			{
				sb.append(context.getString(R.string.lab_write_permission))
						.append(item.target.writePermission).append("\n");
			}
			if (!TextUtils.isEmpty(item.target.authority))
			{
				sb.append(context.getString(R.string.lab_query_uri))
						.append("content://" + item.target.authority)
						.append("\n");
			} 
			if (sb.length() == 0)
			{
				desText.setVisibility(View.GONE);
			} else
			{
				desText.setVisibility(View.VISIBLE);
				sb.deleteCharAt(sb.length() - 1);
				desText.setText(sb);
			}
			enabledCheck.setChecked(item.target.enabled);
			exportedCheck.setChecked(item.exported);
		}		
	}
}
