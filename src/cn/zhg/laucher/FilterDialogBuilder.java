/**
 * 
 * @author zhhaogen
 * 创建于 2020年2月28日 下午8:22:29
 */
package cn.zhg.laucher;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface; 
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*; 
import android.widget.TextView;
import android.widget.AdapterView.*;
import cn.zhg.laucher.matcher.*; 
import cn.zhg.laucher.model.AppInfo;
import cn.zhg.laucher.util.SomeUtils;

/**
 * 
 *
 */
public class FilterDialogBuilder
{

	private List<FilterModel<AppInfo>> filters; 
	private Context context;
	private DialogInterface.OnClickListener negativeButtonClickListener;
	private  DialogInterface.OnClickListener positiveButtonClickListener;

	/**
	 * @param context
	 */
	public FilterDialogBuilder(Context context)
	{ 
		  this.context=context;
	}

	/**
	 * @param filters
	 * @return
	 */
	public FilterDialogBuilder setFilters(List<FilterModel<AppInfo>> filters)
	{
		this.filters=filters;
		return this;
	}

	/**
	 * @param onClickListener
	 * @return
	 */
	public FilterDialogBuilder setNegativeButton(DialogInterface.OnClickListener onClickListener)
	{
		negativeButtonClickListener=onClickListener;
		return this;
	}
	/**
	 * @param onClickListener
	 * @return
	 */
	public FilterDialogBuilder setPositiveButton(DialogInterface.OnClickListener onClickListener)
	{
		positiveButtonClickListener=onClickListener;
		return this;
	}
	/**
	 * @param item
	 * @param convertView
	 */
	private void initValueFilterModel(final ValueFilterModel<AppInfo> item,
			View convertView)
	{
		// init views
		final TextView nameText = (TextView) convertView
				.findViewById(R.id.name_tv);
		final TextView valueText = (TextView) convertView
				.findViewById(R.id.value_tv);
		final Switch enableSwitch = (Switch) convertView
				.findViewById(R.id.enable_sw);
		final Spinner methodSp = (Spinner) convertView
				.findViewById(R.id.method_sp);

		//初始化值
		nameText.setText(item.name);
		valueText.setText(item.value);
		methodSp.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item,
				item.supportMethods));
		int idx = item.supportMethods.indexOf(item.method);
		if (idx != -1)
		{
			methodSp.setSelection(idx);
		}
		
		// 组件情况
		enableSwitch.setChecked(item.enable);
		valueText.setEnabled(item.enable);
		methodSp.setEnabled(item.enable);

		//监听器
		enableSwitch.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				item.enable = !item.enable;
				valueText.setEnabled(item.enable);
				methodSp.setEnabled(item.enable);
			}
		});
		methodSp.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				item.method = (String) parent.getSelectedItem();
			} 
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		}); 
		valueText.addTextChangedListener(new TextWatcher()
		{
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after)
			{
			} 
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
			} 
			public void afterTextChanged(Editable s)
			{
				item.value = s.toString();
			}
		});
	}

	/**
	 * @param item
	 * @param convertView
	 */
	private void initRangeValueFilterModel(
			final RangeValueFilterModel<AppInfo> item, View convertView)
	{
		// init views
		final TextView nameText = (TextView) convertView
				.findViewById(R.id.name_tv);
		final TextView valueText = (TextView) convertView
				.findViewById(R.id.value_tv);
		final Switch enableSwitch = (Switch) convertView
				.findViewById(R.id.enable_sw);
		final Spinner methodSp = (Spinner) convertView
				.findViewById(R.id.method_sp);

		// 初始化值
		nameText.setText(item.name);
		valueText.setText(item.value);
		methodSp.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item,
				item.supportValues));
		int idx = item.supportValues.indexOf(item.value);
		if (idx != -1)
		{
			methodSp.setSelection(idx);
		}

		// 组件情况
		enableSwitch.setChecked(item.enable);
		valueText.setVisibility(View.GONE);
		methodSp.setEnabled(item.enable);

		// 监听器
		enableSwitch.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				item.enable = !item.enable; 
				methodSp.setEnabled(item.enable);
			}
		});
		methodSp.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				item.value = (String) parent.getSelectedItem();
			}

			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	/**
	 * @param item
	 * @param convertView
	 */
	private void initMultiValueFilterModel(
			final MultiValueFilterModel<AppInfo> item, View convertView)
	{
		// init views
		final TextView nameText = (TextView) convertView
				.findViewById(R.id.name_tv);
		final TextView valueText = (EditText) convertView
				.findViewById(R.id.value_tv);
		final Switch enableSwitch = (Switch) convertView
				.findViewById(R.id.enable_sw);
		final Spinner methodSp = (Spinner) convertView
				.findViewById(R.id.method_sp);

		// 初始化值
		nameText.setText(item.name); 
		valueText.setText(SomeUtils.join(",", item.values)); 

		// 组件情况
		enableSwitch.setChecked(item.enable);
		methodSp.setVisibility(View.GONE); 
		valueText.setEnabled(item.enable);
		valueText.setInputType(EditorInfo.TYPE_NULL);
//		valueText.setFocusableInTouchMode(false);
		// 监听器
		enableSwitch.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				item.enable = !item.enable;
				valueText.setEnabled(item.enable);
			}
		}); 
		valueText.setOnClickListener(new View.OnClickListener()
		{ 
			public void onClick(View v)
			{
				showChoiceDialog(item,valueText);
			}
		});
	}

	/**
	 * @param item
	 * @param convertView
	 */
	private void initFilterModel(final FilterModel<AppInfo> item,
			View convertView)
	{
		// init views
		final TextView nameText = (TextView) convertView
				.findViewById(R.id.name_tv);
		final TextView valueText = (TextView) convertView
				.findViewById(R.id.value_tv);
		final Switch enableSwitch = (Switch) convertView
				.findViewById(R.id.enable_sw);
		final Spinner methodSp = (Spinner) convertView
				.findViewById(R.id.method_sp);

		// 初始化值
		nameText.setText(item.name); 

		// 组件情况
		enableSwitch.setChecked(item.enable);
		valueText.setVisibility(View.GONE);
		methodSp.setVisibility(View.GONE);
		
		// 监听器
		enableSwitch.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				item.enable = !item.enable;
				valueText.setEnabled(item.enable);
				methodSp.setEnabled(item.enable);
			}
		});
	}

	/**
	 * @param item
	 * @param valueText 
	 */
	private void showChoiceDialog(final MultiValueFilterModel<AppInfo> item, final TextView valueText)
	{
		final String[] items = item.supportValues.toArray(new String[0]);
		final	boolean[] checkedItems = new boolean[items.length];
		for(int i=0,size=item.values.size();i<size;i++){
			String itemValue = item.values.get(i);
			int idx=item.supportValues.indexOf(itemValue);
			if(idx!=-1){
				checkedItems[idx]=true;
			}
		}
		DialogInterface.OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener()
		{ 
			public void onClick(DialogInterface dialog, int which, boolean isChecked)
			{
				checkedItems[which]=isChecked; 
			}
		}; 
		AlertDialog alert = new AlertDialog.Builder(context)
				.setCancelable(false)
				.setMultiChoiceItems(items, checkedItems, listener)
				.setNeutralButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{ 
					public void onClick(DialogInterface dialog, int which)
					{
						item.values.clear();
						for(int i=0;i<checkedItems.length;i++){
							if(checkedItems[i]){
								item.values.add(items[i]);
							} 
						}
						valueText.setText(SomeUtils.join(",", item.values));
					}
				}).create(); 
		alert.show();
	}

	/**
	 * 
	 */
	@SuppressLint("InflateParams")
	public void show()
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.layout_viewgroup, null,
				false);
		LinearLayout viewGroup = (LinearLayout) view
				.findViewById(R.id.viewgroup_layout);
		for (int position = 0, size = filters
				.size(); position < size; position++)
		{
			final FilterModel<AppInfo> item = filters.get(position);

			View convertView = layoutInflater.inflate(R.layout.item_filter,
					viewGroup, false);
			viewGroup.addView(convertView);

			if (item instanceof ValueFilterModel)
			{
				initValueFilterModel((ValueFilterModel<AppInfo>) item,
						convertView);
			} else if (item instanceof RangeValueFilterModel)
			{
				initRangeValueFilterModel((RangeValueFilterModel<AppInfo>) item,
						convertView);
			} else if (item instanceof MultiValueFilterModel)
			{
				initMultiValueFilterModel((MultiValueFilterModel<AppInfo>) item,
						convertView);
			} else
			{
				initFilterModel(item, convertView);
			}
		}
		AlertDialog alert = new AlertDialog.Builder(context).setView(view)
				.setTitle(R.string.action_filter).setCancelable(false)
				.setNegativeButton(R.string.action_reset,
						this.negativeButtonClickListener)
				.setNeutralButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok,
						this.positiveButtonClickListener)
				.create();
		alert.show();
	}

}
