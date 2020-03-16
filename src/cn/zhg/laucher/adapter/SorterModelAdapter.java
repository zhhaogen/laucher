/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月27日 下午11:10:42
 */
package cn.zhg.laucher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import cn.zhg.base.BaseViewHolder;
import cn.zhg.laucher.R;
import cn.zhg.laucher.model.AppInfo;
import cn.zhg.laucher.sorter.SorterModel;

/**
 * 
 *
 */
public class SorterModelAdapter  extends BaseAdapter
{
	private List<SorterModel<AppInfo>> datas;
	private LayoutInflater inflater;
	public SorterModelAdapter(Context context, List<SorterModel<AppInfo>> sorters)
	{
		this.datas = sorters;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount()
	{
		return datas.size();
	}

	@Override
	public SorterModel<AppInfo> getItem(int position)
	{
		return datas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SorterModelViewHolder holder;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.item_sort, parent,
					false);
			holder=new SorterModelViewHolder(convertView);
			convertView.setTag(holder);
		} else
		{
			holder=(SorterModelViewHolder) convertView.getTag();
		}
		final SorterModel<AppInfo> item = datas.get(position);
		holder.update(item,position); 
		return convertView;
	}
	private class SorterModelViewHolder extends BaseViewHolder{
		private Switch reverseCheck;
		private TextView nameText;
		private View moveupBtn;
		private View movedownBtn;
		/**
		 * @param itemView
		 */
		public SorterModelViewHolder(View itemView)
		{
			super(itemView); 
			reverseCheck=this.getViewById(R.id.reverse_sw);
			nameText=this.getViewById(R.id.name_tv);
			movedownBtn=this.getViewById(R.id.movedown_btn);
			moveupBtn=this.getViewById(R.id.moveup_btn);
		}

		/**
		 * @param item
		 */
		public void update(final SorterModel<AppInfo> item, final int position)
		{
			reverseCheck.setChecked(item.isReverse);
			nameText.setText(item.name);
			movedownBtn.setOnClickListener(new View.OnClickListener()
			{ 
				public void onClick(View v)
				{
					if (position <datas.size()-1)
					{
						datas.remove(position);
						datas.add(position + 1, item);
						notifyDataSetChanged();
					}
				}
			});
			moveupBtn.setOnClickListener(new View.OnClickListener()
			{ 
				public void onClick(View v)
				{
					if (position>0)
					{
						datas.remove(position);
						datas.add(position - 1, item);
						notifyDataSetChanged();
					}
				}
			});
			reverseCheck.setOnClickListener(new View.OnClickListener()
			{ 
				public void onClick(View v)
				{
					item.isReverse=!item.isReverse;
					notifyDataSetChanged();
				}
			});
		}
		
	}
}
