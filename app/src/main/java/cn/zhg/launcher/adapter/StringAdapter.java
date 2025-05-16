package cn.zhg.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.launcher.R;

public class StringAdapter extends SortFilterAdapter<Object>
{

	@SuppressWarnings("unchecked")
	public StringAdapter(Context context, List<?> datas)
	{
		super(context, (List<Object>) datas);
	} 
	public View onCreateView(ViewGroup parent)
	{
		return inflater.inflate(R.layout.item_oneline, parent, false);
	}

	@Override
	public  BasicAdapter.BasicViewHolder<Object> onCreateViewHolder(
			View itemView)
	{
		return new StringViewHolder(itemView);
	}
	private class StringViewHolder extends BasicAdapter.BasicViewHolder<Object>{ 
		private TextView titleText;
		public StringViewHolder(View itemView)
		{
			super(itemView);
			this.titleText=this.getViewById(R.id.title_tv);
		}
 
		public void update(Object item, int position)
		{
			if(item==null){
				titleText.setText("");
			}else{
				titleText.setText(item.toString());
			}
			
		}
		
	}
}
