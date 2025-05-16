package cn.zhg.launcher.viewholder;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.launcher.R;


public abstract class ThreeLineOpenViewHolder<T> extends BasicAdapter.BasicViewHolder<T>
{
	public final TextView titleText;
	public final TextView desText;
	public CompoundButton enabledCheck;
	public CompoundButton exportedCheck;
	public final View openBtn; 
	public ThreeLineOpenViewHolder(View itemView)
	{
		super(itemView);
		titleText=this.getViewById(R.id.title_tv);
		desText=this.getViewById(R.id.des_tv);
		enabledCheck=this.getViewById(R.id.enabled_check);
		exportedCheck=this.getViewById(R.id.exported_check);
		openBtn=this.getViewById(R.id.open_btn);
	} 

}
