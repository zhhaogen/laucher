/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月26日 下午4:02:18
 */
package cn.zhg.laucher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import cn.zhg.laucher.inter.*;
import cn.zhg.laucher.model.AppInfo;

/**
 * 
 *
 */
public abstract class BaseAppInfoAdapter extends BaseAdapter
{
	protected List<AppInfo> datas;
	protected LayoutInflater inflater;
	protected OnAppInfoClickListener onAppInfoClickListener;
	protected OnAppInfoLongClickListener onAppInfoLongClickListener;
	public BaseAppInfoAdapter(Context context, List<AppInfo> datas)
	{
		this.datas = datas;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount()
	{
		return datas.size();
	}

	/**
	 * @param onAppInfoClickListener 设置 onAppInfoClickListener
	 */
	public void setOnAppInfoClickListener(
			OnAppInfoClickListener onAppInfoClickListener)
	{
		this.onAppInfoClickListener = onAppInfoClickListener;
	}
	/**
	 * @param onAppInfoLongClickListener 设置 onAppInfoLongClickListener
	 */
	public void setOnAppInfoLongClickListener(
			OnAppInfoLongClickListener onAppInfoLongClickListener)
	{
		this.onAppInfoLongClickListener = onAppInfoLongClickListener;
	}
	@Override
	public AppInfo getItem(int position)
	{
		return datas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
}
