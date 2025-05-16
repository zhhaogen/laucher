package cn.zhg.launcher.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import cn.zhg.launcher.R;
import cn.zhg.launcher.model.AppSignatureInfo;
import cn.zhg.launcher.model.TitleDes;
import cn.zhg.launcher.viewholder.TwoLineViewHolder;

public class AppSignatureInfoAdapter extends BaseExpandableListAdapter 
{
	protected Context context;
	private LayoutInflater inflater;
	private List<AppSignatureInfo> datas; 
	private TitleDes[][] childDatas;
	public AppSignatureInfoAdapter(Context context,
			List<AppSignatureInfo> datas)
	{
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
		if(datas==null){
			return;
		}
		childDatas=new TitleDes[datas.size()][];
		for(int i=0;i<childDatas.length;i++){ 
			childDatas[i]=toTitleDesArray(datas.get(i));
		}
	}  
	public int getGroupCount()
	{
		if(datas==null){
			return 0;
		}
		return datas.size();
	} 
	public int getChildrenCount(int groupPosition)
	{
		if(childDatas==null){
			return 0;
		}
		return childDatas[groupPosition].length;
	} 
	public AppSignatureInfo getGroup(int groupPosition)
	{
		if(datas==null){
			return null;
		}
		return datas.get(groupPosition);
	}
 
	public TitleDes getChild(int groupPosition, int childPosition)
	{ 
		if(childDatas==null){
			return null;
		}
		return childDatas[groupPosition][childPosition];
	} 
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	} 
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	} 
	public boolean hasStableIds()
	{
		return true;
	} 
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView=	inflater.inflate(	android.R.layout.simple_spinner_dropdown_item, parent, false); 
		} 
		((CheckedTextView) convertView).setText(context.getString(R.string.lab_signatures)+"-"+(groupPosition+1));
		((CheckedTextView) convertView).setChecked(isExpanded); 
		((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.selector_arrow);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		TitleDesViewHolder holder;
		if(convertView==null){
			convertView=this.inflater.inflate(R.layout.item_twoline, parent, false);
			holder=new TitleDesViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder=(TitleDesViewHolder) convertView.getTag();
		} 
		holder.update(this.getChild(groupPosition, childPosition), childPosition);
		return convertView;
	} 
	private TitleDes[] toTitleDesArray(AppSignatureInfo info)
	{
		List<TitleDes> list=new ArrayList<>();
		list.add(new TitleDes(context.getString(R.string.lab_signature_version),"V"+info.version));
		list.add(new TitleDes(context.getString(R.string.lab_signature_subject),info.subject));
		list.add(new TitleDes(context.getString(R.string.lab_signature_issuer),info.issuer));
		list.add(new TitleDes(context.getString(R.string.lab_signature_begindate),info.beginDate));
		list.add(new TitleDes(context.getString(R.string.lab_signature_enddate),info.endDate));
		list.add(new TitleDes(context.getString(R.string.lab_signature_sha1),info.sha1));
		list.add(new TitleDes(context.getString(R.string.lab_signature_sha256),info.sha256));
		list.add(new TitleDes(context.getString(R.string.lab_signature_serialnumber),info.serialNumber));
		list.add(new TitleDes(context.getString(R.string.lab_signature_signalgorithm),info.signAlgorithm));
		list.add(new TitleDes(context.getString(R.string.lab_signature_pubkey),info.pubKeyInfo));
		return list.toArray(new TitleDes[0]);
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
