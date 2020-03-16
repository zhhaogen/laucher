/**
 * 
 * @author zhhaogen
 * 创建于 2018年5月1日 下午1:36:25
 */
package cn.zhg.laucher.adapter;

import java.util.*; 

import android.content.Context;
import android.content.Intent;
import android.view.*;  
import android.widget.*;
import cn.zhg.laucher.R;
import cn.zhg.laucher.model.*;
import cn.zhg.laucher.util.SomeUtils;;

/**
 * 
 *
 */
public class AppInfoDetailAdapter extends BaseExpandableListAdapter
{  
	private AppInfo app;
	protected Context context;
	private LayoutInflater inflater; 
	private LinkedHashMap<String,ExpandableDetail> map;
	private ArrayList<String> keys;

	/**
	 * 
	 */
	public AppInfoDetailAdapter(final Context context, AppInfo item)
	{
		this.app = item;
		this.context = context;
		inflater = LayoutInflater.from(context);
		map = new LinkedHashMap<>();
		//基本信息 
		map.put(context.getString(R.string.lab_base),
				new ExpandableDetail(buildBaseValues(item))
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						TitleDes data = this.get(childPosition);
						holder.titleText.setText(data.title);
						if(data.des==null){
							holder.desText.setVisibility(View.GONE);
						} else{
							holder.desText.setText(data.des);
						}
					}
				});
		map.put(context.getString(R.string.lab_activities),
				new ExpandableDetail(app.activities)
				{ 
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						final AppActivityInfo data = this.get(childPosition);
						holder.titleText.setText(data.name);
						holder.desText.setText(data.label);
						holder.view.setEnabled(data.exported);
						holder.openBtn.setVisibility(View.VISIBLE);
						holder.openBtn
								.setOnClickListener(new View.OnClickListener()
								{
									public void onClick(View v)
									{
										try
										{
											Intent intent = new Intent();
											intent.setComponent(data.className);
											context.startActivity(intent);
										} catch (Exception igr)
										{
											tip(igr);
										}
									}
								});
					}
				});
		map.put(context.getString(R.string.lab_services),
				new ExpandableDetail(app.services)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						final AppServiceInfo data = this.get(childPosition);
						holder.titleText.setText(data.name);
						holder.desText.setText(data.label);
						holder.view.setEnabled(data.exported);
						holder.openBtn.setVisibility(View.VISIBLE);
						holder.openBtn
								.setOnClickListener(new View.OnClickListener()
								{
									public void onClick(View v)
									{
										try
										{
											Intent intent = new Intent();
											intent.setComponent(data.className);
											context.startService(intent);
										} catch (Exception igr)
										{
											tip(igr);
										}
									}
								});
					}
				});
		map.put(context.getString(R.string.lab_providers),
				new ExpandableDetail(app.providers)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						AppProviderInfo data = this.get(childPosition);
						holder.titleText.setText(data.name);
						holder.desText.setText(data.authority);
					}
				});
		map.put(context.getString(R.string.lab_permissions),
				new ExpandableDetail(app.permissions)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						AppPermissionInfo data = this.get(childPosition);
						holder.titleText.setText(data.name);
						holder.desText.setText(data.label);
					}
				});
		map.put(context.getString(R.string.lab_request_permissions),
				new ExpandableDetail(app.requestedPermissions)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						AppPermissionInfo data = this.get(childPosition);
						holder.titleText.setText(data.name);
						holder.desText.setText(data.label);
					}
				});
		map.put(context.getString(R.string.lab_signatures),
				new ExpandableDetail(app.signatures)
				{

					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						AppSignatureInfo data = this.get(childPosition);
						holder.titleText.setText(data.toString());
						holder.desText.setVisibility(View.GONE);
					}
				});
		map.put(context.getString(R.string.lab_flags),
				new ExpandableDetail(app.flagFields)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						String data = this.get(childPosition);
						holder.titleText.setText(data);
						holder.desText.setVisibility(View.GONE);
					}
				});
		map.put(context.getString(R.string.lab_instrumentations),
				new ExpandableDetail(app.instrumentations)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						AppInstrumentationInfo data = this.get(childPosition);
						holder.titleText.setText(data.name);
						holder.desText.setText(data.targetPackage);
					}
				});
		map.put(context.getString(R.string.lab_receivers),
				new ExpandableDetail(app.receivers)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						AppActivityInfo data = this.get(childPosition);
						holder.titleText.setText(data.name);
						holder.desText.setText(data.label);
					}
				});
		map.put(context.getString(R.string.lab_sharedlibraryfiles),
				new ExpandableDetail(app.sharedLibraryFiles)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						String data = this.get(childPosition);
						holder.titleText.setText(data);
						holder.desText.setVisibility(View.GONE);
					}
				});
		map.put(context.getString(R.string.lab_appsources),
				new ExpandableDetail(app.appSources)
				{
					void onUpdateHolder(TwoLineHolder holder, int childPosition)
					{
						AppSourceInfo data = this.get(childPosition);
						holder.titleText.setText(data.name);
						holder.desText.setText(data.sourceDir);
					}
				});
		keys = new ArrayList<>(map.keySet());
	}

	/**
	 * @param item
	 * @return
	 */
	private List<TitleDes> buildBaseValues(AppInfo item)
	{
		List<TitleDes> baseValues = new ArrayList<>();
		baseValues.add(new TitleDes(context.getString(R.string.lab_label),
				SomeUtils.toString(item.label)));
		baseValues.add(new TitleDes(context.getString(R.string.lab_packagename),
				item.packageName));
		baseValues.add(new TitleDes(context.getString(R.string.lab_appclassname), item.appClassName));
		baseValues.add(new TitleDes(context.getString(R.string.lab_appname), item.name));
		baseValues.add(new TitleDes(context.getString(R.string.lab_processname), item.processName)); 
		baseValues.add(new TitleDes(context.getString(R.string.lab_datadir), item.dataDir));
		baseValues.add(new TitleDes(context.getString(R.string.lab_sourcedir), item.sourceDir));
		baseValues.add(new TitleDes(context.getString(R.string.lab_versionname), item.versionName));
		baseValues.add(new TitleDes(context.getString(R.string.lab_versioncode), item.versionCode));
		baseValues.add(new TitleDes(context.getString(R.string.lab_description), item.description));
		baseValues.add(new TitleDes(context.getString(R.string.lab_targetsdkversion), item.targetSdkVersion));
		baseValues.add(new TitleDes(context.getString(R.string.lab_uid), item.uid));
		baseValues.add(new TitleDes(context.getString(R.string.lab_filesize), item.fileSizeStr));
		baseValues.add(
				new TitleDes(context.getString(R.string.lab_firstinstall_time), item.firstInstallTimeStr));
		baseValues.add(new TitleDes(context.getString(R.string.lab_lastupdate_time), item.lastUpdateTimeStr));
		return baseValues;
	}

	private void tip(Exception exception)
	{
		Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
	}
	public int getGroupCount()
	{
		return keys.size();
	}

	public int getChildrenCount(int groupPosition)
	{ 
		return map.get(keys.get(groupPosition)).getSize();
	}

	public String getGroup(int groupPosition)
	{
		return keys.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition)
	{ 
		return map.get(keys.get(groupPosition)).get(childPosition);
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
		((CheckedTextView) convertView).setText(this.getGroup(groupPosition)+" "+this.getChildrenCount(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded); 
		((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.selector_arrow); 
		return convertView;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{ 
		return 	map.get(keys.get(groupPosition)).getChildView(childPosition,convertView,parent); 
	}
	private   class TitleDes
	{ 
		public TitleDes(String title,Object des){
			this.title=title;
			this.des=String.valueOf(des);
		}
		public String title;
		public String des;
	}
	private abstract class ExpandableDetail
	{ 
		private List<?> list;
 
		private ExpandableDetail(List<?> list)
		{
			this.list=list;
		}
		/**
		 * @return
		 */
		public int getSize()
		{ 
			if(list!=null)
			{
				return list.size();
			}
			return 0;
		}

		 

		/**
		 * @param <T>
		 * @param childPosition
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public <T> T get(int childPosition)
		{ 
			if(list!=null)
			{
				return (T) list.get(childPosition);
			}
			return null;
		}
		public View getChildView(int childPosition, View convertView,
				ViewGroup parent)
		{
			TwoLineHolder holder;
			if(convertView==null)
			{
				convertView=inflater.inflate(R.layout.item_twoline, parent,false);
				holder=new TwoLineHolder(convertView);
				convertView.setTag(holder);
			}else
			{
				holder=(TwoLineHolder) convertView.getTag();
			} 
			holder.desText.setVisibility(View.VISIBLE);//des显示
			holder.openBtn.setVisibility(View.GONE);
			holder.view.setEnabled(true); 
			onUpdateHolder(holder,childPosition);
			return convertView;
		}
		/**
		 * 更新数据视图,默认openBtn不显示, des显示
		 * @param holder
		 * @param childPosition
		 */
		  abstract void onUpdateHolder(TwoLineHolder holder, int childPosition) ;
		
	}
	private class TwoLineHolder 
	{
		private View view;
		private TextView titleText;
		private TextView desText;
		private ImageButton openBtn;
		TwoLineHolder(View view)
		{
			this.view=view;
			titleText=(TextView) view.findViewById(R.id.title_tv);
			desText=(TextView) view.findViewById(R.id.des_tv);
			openBtn=(ImageButton) view.findViewById(R.id.open_btn);
		}
	}
}
