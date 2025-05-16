package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.TitleDes;
import cn.zhg.launcher.viewholder.TwoLineViewHolder;

public class TitleDesAdapter extends SortFilterAdapter<TitleDes>
{ 
	public TitleDesAdapter(Context context, List<TitleDes> datas)
	{
		super(context, datas);
	} 
	public View onCreateView(ViewGroup parent)
	{
		return inflater.inflate(R.layout.item_twoline, parent, false);
	}

	@Override
	public  BasicAdapter.BasicViewHolder<TitleDes> onCreateViewHolder(
			View itemView)
	{
		return new TitleDesViewHolder(itemView);
	}
	private class TitleDesViewHolder extends TwoLineViewHolder<TitleDes>{ 
		public TitleDesViewHolder(View itemView)
		{
			super(itemView); 
		}
 
		public void update(TitleDes item, int position)
		{
			titleText.setText(item.title);
			desText.setText(item.des); 
		}
		
	}
}
