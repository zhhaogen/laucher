package cn.zhg.launcher.viewholder;

import android.view.View;
import android.widget.*;

import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.launcher.R;

public abstract class TwoLineViewHolder<T> extends BasicAdapter.BasicViewHolder<T>
{
	public final TextView titleText;
	public final TextView desText;
	public TwoLineViewHolder(View itemView)
	{
		super(itemView);
		titleText=this.getViewById(R.id.title_tv);
		desText=this.getViewById(R.id.des_tv);
	} 

}
