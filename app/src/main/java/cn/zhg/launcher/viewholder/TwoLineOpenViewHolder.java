package cn.zhg.launcher.viewholder;

import android.view.View;
import android.widget.TextView;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.launcher.R;

public abstract class TwoLineOpenViewHolder<T> extends BasicAdapter.BasicViewHolder<T>
{
	public final TextView titleText;
	public final TextView desText;
	public final View openBtn;
	public TwoLineOpenViewHolder(View itemView)
	{
		super(itemView);
		titleText=this.getViewById(R.id.title_tv);
		desText=this.getViewById(R.id.des_tv);
		openBtn=this.getViewById(R.id.open_btn);
	} 

}
