package cn.zhg.laucher;

import java.io.*; 
import java.lang.reflect.*; 
import java.util.*;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.*; 
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.view.*; 
import android.widget.*;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import cn.zhg.base.BasicActivity;
import cn.zhg.laucher.adapter.*; 
import cn.zhg.laucher.inter.*; 
import cn.zhg.laucher.matcher.*;
import cn.zhg.laucher.model.*;
import cn.zhg.laucher.sorter.*; 
import cn.zhg.laucher.util.SomeUtils;
import xiaogen.util.Logger;

public class MainActivity extends BasicActivity implements Constant, OnQueryTextListener, OnAppInfoClickListener, OnAppInfoLongClickListener, OnCloseListener
{
	private static final int setProgressSize = 30;
	private static final int setProgressValue = 31;
	private static final int onLoadAppsed=32;
	private static final int onSetAdaptered=33;
	private static final String sortersPath="sorters.bin";
	private static final String filtersPath="filters.bin";
	private GridView gridView;
	private SearchView searchView;
	/**
	 * 是否已经加载完毕
	 */
	private boolean loading;
	private Menu menu;
	private PackageManager pm;
	private SharedPreferences sh;
	private Editor shEditor;
	private ProgressBar loadingBar;
	/**
	 * 原始数据
	 */
	private List<AppInfo> orginalDatas;
	/**
	 * 刷选数据
	 */
	private	List<AppInfo> datas; 
	private List<FilterModel<AppInfo>> filters;
	private List<SorterModel<AppInfo>> sorters;
	/**
	 * 正在进行更新适配器
	 */
	private boolean setAdaptering;
	/**
	 * 是否反转排序
	 */
	private boolean isSortReverse;
	/**
	 * 是否列表视图
	 */
	private boolean isListView;
	private String seachKeyWord;
	private BroadcastReceiver receiver;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gridView = this.findViewById(R.id.content_gv);
		loadingBar = this.findViewById(R.id.loading_pb);
		sh = this.getSharedPreferences(getPackageName(), MODE_PRIVATE);
		shEditor = sh.edit();
		pm=this.getPackageManager();
		orginalDatas=new LinkedList<>(); 
		datas=new LinkedList<>(); 
		Logger.d();
	}

	protected void OnPermissionsAccess()
	{
		init();
	}

	/**
	 * 初始化
	 */
	void init()
	{
		if (!isDefaultHome())
		{
			// this.startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
		}
		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(this);
		// 读取配置
		loadMenu();
		//初始化过滤器和排序
		loadSettings();
		registerReceiver();
		loadApps();
	}

	/**
	 * 
	 */
	void registerReceiver()
	{
		receiver = new BroadcastReceiver()
		{
			public void onReceive(Context context, Intent intent)
			{
				String action = intent.getAction();
				if (Intent.ACTION_PACKAGE_ADDED.equals(action))
				{
					String packageName = intent.getDataString();
					Logger.d("安装:"+packageName);
					if (loading)
					{ 
						tip(R.string.tip_checkloaded2);
					} else{
						loadApps();
					}
				} else if (Intent.ACTION_PACKAGE_REMOVED.equals(action))
				{
					String packageName = intent.getDataString();
					Logger.d("卸载:"+packageName);
					if (loading)
					{ 
						tip(R.string.tip_checkloaded2);
					} else{
						loadApps();
					}
				} 
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
		filter.addDataScheme("package"); 
		this.registerReceiver(receiver, filter);
	}

	/**
	 * 载入过滤器与排序器
	 */
	void loadSettings()
	{
		//
		isSortReverse=findMenuItem(R.id.action_sort_reverse).isChecked();
		isListView=findMenuItem(R.id.action_views_list).isChecked();
		//排序器
		if(this.findMenuItem(R.id.action_sort_str).isChecked()){//按字母排序
			sorters = strSorter();
		}else if(this.findMenuItem(R.id.action_sort_install).isChecked()){ 
			sorters = installSorter();
		}else if(this.findMenuItem(R.id.action_sort_install).isChecked()){ 
			sorters = installSorter();
		}else if(this.findMenuItem(R.id.action_sort_advanced).isChecked()){ 
			   sorters =readSorters();
		}
		//过滤器
		if(this.findMenuItem(R.id.action_filter_user).isChecked()){ 
			filters = userLaunchFilter() ;
		}else if(this.findMenuItem(R.id.action_filter_launch).isChecked()){ 
			filters = userLaunchFilter() ;
		}else if(this.findMenuItem(R.id.action_filter_advanced).isChecked()){ 
			  filters =readFilters();
		} 
		
	} 
 

	/**
	 * 读取加载应用
	 */
	void loadApps()
	{
		if (loading)
		{
			Logger.d("正在加载...");
			return;
		}
		if(this.findMenuItem(R.id.action_views_list).isChecked()){
			gridView.setNumColumns(1);
		}else{
			gridView.setNumColumns(sh.getInt(KEY_NumColumns, 5));
		}
		loading = true;
		loadingBar.setVisibility(View.VISIBLE);
		this.loadingBar.setIndeterminate(true);
		new Thread()
		{
			public void run()
			{
				onLoadApps();
			}
		}.start();
	} 
	/**
	 * 开始加载
	 */
	@SuppressWarnings("deprecation")
	void onLoadApps()
	{
		orginalDatas.clear();
		List<PackageInfo> packages = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		int size = packages.size();
		this.sendMessage(setProgressSize, size-1);
		String myPackageName=this.getPackageName();
		for (int i = 0; i < size; i++)
		{ 
			PackageInfo info = packages.get(i);
			if(info.packageName.equals(myPackageName)){
				//不显示本身
				continue;
			}
			AppInfo item = new AppInfo(info);
			item.updateData(pm);
			orginalDatas.add(item);
			sendMessage(setProgressValue, i);
		} 
		sendEmptyMessage(onLoadAppsed);
	}
	/**
	 * 加载完成
	 */
	void onLoadAppsed()
	{
		loadingBar.setVisibility(View.GONE);
		loading=false;
		this.setAdapter();
	}
	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
		case setProgressSize:
			setProgressSize((Integer) msg.obj);
			break;
		case setProgressValue:
			setProgressValue((Integer) msg.obj);
			break;
		case onLoadAppsed:
			onLoadAppsed();
			break;
		case onSetAdaptered:
			onSetAdaptered();
			break;
		}
		return super.handleMessage(msg);
	}

	/**
	 * 设置进度最大值
	 * 
	 * @param size
	 */
	void setProgressSize(int size)
	{
		loadingBar.setIndeterminate(false);
		loadingBar.setMax(size);
	}

	/**
	 * 设置进度值
	 * 
	 * @param progress
	 */
	void setProgressValue(int progress)
	{
		loadingBar.setProgress(progress);
	}
 
	protected void OnPermissionsDenied(List<PermissionInfo> permissions)
	{
		tip(R.string.tip_onpermissionsdenied);
		finish();
	}

	/**
	 * 是否为默认桌面
	 * 
	 * @return
	 */
	boolean isDefaultHome()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		ResolveInfo info = this.getPackageManager().resolveActivity(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (info == null)
		{
			Logger.d("没有桌面程序");
			return false;
		}
		if ("android".equals(info.activityInfo.packageName))
		{
			Logger.d("没有设置默认桌面程序");
			return false;
		}
//		Logger.d("当前桌面程序:" + info.activityInfo.packageName);
		return this.getPackageName().equals(info.activityInfo.packageName);
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu = menu;
		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView(); 
		this.checkPermission();
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
		case R.id.action_refresh:
		{
			if (this.checkLoaded())
			{ 
				this.loadApps();
			} 
			break;
		}
		case R.id.action_setting:
		{
			this.startActivity(new Intent(this, SettingActivity.class));
			break;
		}
		case R.id.action_opensetting:
		{
			this.startActivity(new Intent(Settings.ACTION_SETTINGS));
			break;
		}
		case R.id.action_sethome:
		{
			this.startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
			break;
		}
		case R.id.action_sort_str:
		{
			if (item.isChecked())
			{
				break;
			}
			item.setChecked(true);
			this.setMenuChecked(false, R.id.action_sort_install,
					R.id.action_sort_lasttime, R.id.action_sort_advanced);
			setMenuEnabled(true, R.id.action_sort_reverse);
			sorters = strSorter();
			this.setAdapter();
			this.saveMenu();
			break;
		}
		case R.id.action_sort_install:
		{
			if (item.isChecked())
			{
				break;
			}
			item.setChecked(true);
			this.setMenuChecked(false, R.id.action_sort_str,
					R.id.action_sort_lasttime, R.id.action_sort_advanced);
			setMenuEnabled(true, R.id.action_sort_reverse);
			sorters = installSorter();
			this.setAdapter();
			this.saveMenu();
			break;
		}
		case R.id.action_sort_lasttime:
		{
			if (item.isChecked())
			{
				break;
			}
			item.setChecked(true);
			this.setMenuChecked(false, R.id.action_sort_str,
					R.id.action_sort_install, R.id.action_sort_advanced);
			setMenuEnabled(true, R.id.action_sort_reverse);
			sorters = lasttimeSorter();
			this.setAdapter();
			this.saveMenu();
			break;
		}
		case R.id.action_sort_advanced:
		{
			if(!item.isChecked()){
				sorters=null;
				Logger.d("重置高级排序");
			} 
			item.setChecked(true);
			this.setMenuChecked(false, R.id.action_sort_str,
					R.id.action_sort_install, R.id.action_sort_lasttime,
					R.id.action_sort_reverse);
			this.setMenuEnabled(false, R.id.action_sort_reverse);
			showSorterDialog();  
			saveMenu();
			break;
		}
		case R.id.action_sort_reverse:
		{
			isSortReverse = !item.isChecked();
			item.setChecked(isSortReverse);
			reverseSorter(sorters);
			this.setAdapter();
			this.saveMenu();
			break;
		}
		case R.id.action_sort_reset:
		{
			this.setMenuChecked(false, R.id.action_sort_str,
					R.id.action_sort_install, R.id.action_sort_lasttime,
					R.id.action_sort_advanced, R.id.action_sort_reverse);
			setMenuEnabled(true, R.id.action_sort_reverse);
			sorters = null;
			isSortReverse = false;
			this.setAdapter();
			this.saveMenu();
			this.saveSorters();
			break;
		}
		case R.id.action_filter_user:
		{
			boolean isFilterUser = !item.isChecked();
			item.setChecked(isFilterUser);
			Logger.d("仅显示用户应用:" + isFilterUser);
			setMenuChecked(false, R.id.action_filter_advanced);
			filters = userLaunchFilter();
			this.setAdapter();
			this.saveMenu();
			break;
		}
		case R.id.action_filter_launch:
		{
			boolean isFilterLaunch = !item.isChecked();
			item.setChecked(isFilterLaunch);
			Logger.d("仅显示可启动应用:" + isFilterLaunch);
			setMenuChecked(false, R.id.action_filter_advanced);
			filters = userLaunchFilter();
			this.setAdapter();
			this.saveMenu();
			break;
		}
		case R.id.action_filter_advanced:
		{
			if(!item.isChecked()){
				filters=null;
				Logger.d("重置高级过滤");
			} 
			item.setChecked(true);
			setMenuChecked(false, R.id.action_filter_user,
					R.id.action_filter_launch);
			showFilterDialog(); 
			this.saveMenu();
			break;
		}
		case R.id.action_filter_reset:
		{
			setMenuChecked(false, R.id.action_filter_user,
					R.id.action_filter_launch, R.id.action_filter_advanced);
			filters = null;
			this.setAdapter();
			this.saveFilters();
			this.saveMenu();
			break;
		}
		case R.id.action_views_grid:
		{
			if (item.isChecked())
			{
				break;
			}
			isListView=false;
			item.setChecked(true);
			setMenuChecked(false, R.id.action_views_list);
			this.gridView.setNumColumns(sh.getInt(KEY_NumColumns, 5));
			this.setAdapter();
			this.saveMenu();
			break;
		}
		case R.id.action_views_list:
		{
			if (item.isChecked())
			{
				break;
			}
			isListView=true;
			item.setChecked(true);
			setMenuChecked(false, R.id.action_views_grid);
			this.gridView.setNumColumns(1);
			this.setAdapter();
			this.saveMenu();
			break;
		}
		}
		return true;
	}

	/**
	 * 显示排序对话框
	 */
	void showSorterDialog()
	{ 
		if(sorters==null||sorters.isEmpty()){
			buildSorters();
		}
		SorterModelAdapter adapter = new SorterModelAdapter(this,sorters);
		AlertDialog alert = new AlertDialog.Builder(this)  
				.setAdapter(adapter, null).setTitle(R.string.action_sort)
				.setCancelable(false) 
				.setNegativeButton(R.string.action_reset, new DialogInterface.OnClickListener()
				{ 
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						buildSorters();
						setAdapter();
						saveSorters(); 
					}
				}) 
				.setNeutralButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{ 
					public void onClick(DialogInterface dialog, int which)
					{
//						Logger.d("自定义排序:"+sorters);
						setAdapter();
						saveSorters();
						dialog.dismiss();
					}
				})
				.create(); 
		alert.show(); 
	}

	/**
	 * 创建排屑器
	 */
	void buildSorters()
	{
		sorters = new ArrayList<>();
		sorters.add(
				new AppLabelSorterModel(this.getString(R.string.lab_label)));
		sorters.add(new AppPackageNameSorterModel(
				this.getString(R.string.lab_packagename)));
		sorters.add(new AppFirstInstallTimeSorterModel(
				this.getString(R.string.lab_firstinstall_time)));
		sorters.add(new AppLastUpdateTimeSorterModel(
				this.getString(R.string.lab_lastupdate_time)));
		sorters.add(new AppComponentsSizeSorterModel(
				this.getString(R.string.lab_components_size)));
		sorters.add(new AppPermissionsSizeSorterModel(
				this.getString(R.string.lab_permissions_size)));
		sorters.add(new AppReceiversSizeSorterModel(
				this.getString(R.string.lab_receivers_size)));
		sorters.add(new AppSystemAppSorterModel(
				this.getString(R.string.lab_systemapp)));
	}

	/**
	 * 显示过滤器对话框
	 */
	@SuppressLint("InflateParams")
	void showFilterDialog()
	{
		if (filters == null || filters.isEmpty())
		{
			buildFilters();
		}
		new FilterDialogBuilder(this)  
		.setFilters(filters)
		.setNegativeButton(
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{
						dialog.dismiss();
						buildFilters();
						setAdapter();
						saveFilters();
					}
				})
		.setPositiveButton(
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								Logger.d("自定义过滤器:" + filters);
								setAdapter();
								saveFilters();
								dialog.dismiss();
							}
						})
		.show();  
	}

	/**
	 * 
	 */
	  void buildFilters()
	{
		  filters = new ArrayList<>();
		  filters.add(new AppLabelFilterModel(getString(R.string.lab_label)));
		  filters.add(new AppPackageNameFilterModel(getString(R.string.lab_packagename)));
		  filters.add(new AppFileSizeFilterModel(getString(R.string.lab_filesize)));
		  filters.add(new AppHasLaunchIntentFilterModel(getString(R.string.lab_haslaunch)));
		  filters.add(new AppFlagsFilterModel(getString(R.string.lab_flags)));
		  filters.add(new AppRequestPermissionsFilterModel(getString(R.string.lab_request_permissions)));
	}

	/**
	 * @return
	 */
	List<FilterModel<AppInfo>> userLaunchFilter()
	{
		List<FilterModel<AppInfo>> filters = new ArrayList<>();
		boolean isFilterUser =this.findMenuItem(R.id.action_filter_user).isChecked();
		boolean isFilterLaunch =findMenuItem(R.id.action_filter_launch).isChecked();
		if(isFilterUser){
			AppSystemAppFilterModel filter = new AppSystemAppFilterModel(getString(R.string.action_filter_user));
			filter.enable=true;
			filter.value="false";
			filters.add(filter); 
		}
		if(isFilterLaunch){
			AppHasLaunchIntentFilterModel filter = new AppHasLaunchIntentFilterModel(getString(R.string.action_filter_launch));
			filter.enable=true;
			filter.value="true";
			filters.add(filter);  
		}
		return filters;
	}

	/**
	 * 反转排序
	 * 
	 * @return
	 */
	void reverseSorter(List<SorterModel<AppInfo>> sorters)
	{ 
		if(sorters==null){
			return;
		}
		for (SorterModel<AppInfo> sorter : sorters)
		{
			sorter.isReverse = !sorter.isReverse;
		} 
	}

	/**
	 * @return
	 */
	List<SorterModel<AppInfo>> lasttimeSorter()
	{
		SorterModel<AppInfo> sort = new AppLastUpdateTimeSorterModel(
				this.getString(R.string.action_sort_lasttime));
		sort.isReverse = isSortReverse;
		List<SorterModel<AppInfo>> list = new ArrayList<>();
		list.add(sort);
		return list;
	}

	/**
	 * @return
	 */
	  List<SorterModel<AppInfo>> installSorter()
	{
		  SorterModel<AppInfo> sort = new AppFirstInstallTimeSorterModel (
					this.getString(R.string.action_sort_install));
			sort.isReverse =isSortReverse;
			List<SorterModel<AppInfo>> list = new ArrayList<>();
			list.add(sort);
			return list;
	}

	/**
	 * 按字母排序
	 * 
	 * @return
	 */
	List<SorterModel<AppInfo>> strSorter()
	{
		SorterModel<AppInfo> sort = new AppLabelSorterModel(
				this.getString(R.string.action_sort_str));
		sort.isReverse = isSortReverse;
		Logger.d("排序是否反序:" + sort.isReverse);
		List<SorterModel<AppInfo>> list = new ArrayList<>();
		list.add(sort);
		return list;
	}

	/**
	 * 更新适配器
	 */
	void setAdapter()
	{
		if(this.setAdaptering){
			tip(R.string.tip_setadapter);
			return;
		}
		setAdaptering=true;
		loadingBar.setVisibility(View.VISIBLE);
		this.loadingBar.setIndeterminate(true);
		new Thread()
		{
			public void run()
			{
				onSetAdapter();
			}
		}.start(); 
	} 

	/**
	 * 
	 */
	void onSetAdapter()
	{
		// 根据过滤器、排序器更新适配器
		datas.clear();
//		Logger.d("过滤器:" + filters);
		for (AppInfo item : orginalDatas)
		{
			if (isMatch(item))
			{
				datas.add(item);
			}
		}
		
		if (this.sorters == null || sorters.isEmpty())
		{
			// 反转默认排序
			if (this.isSortReverse)
			{
				Collections.reverse(datas);
			}
		} else
		{
			Logger.d("排序:" + sorters);
			Collections.sort(datas, new Comparator<AppInfo>()
			{
				public int compare(AppInfo o1, AppInfo o2)
				{
					int ret = 0;
					Iterator<SorterModel<AppInfo>> itor = sorters.iterator();
					while (itor.hasNext())
					{ 
						ret = itor.next().compare(o1, o2);
						if (ret != 0)
						{
							break;
						}
					}
					return ret;
				}
			});
		}
		this.sendEmptyMessage(onSetAdaptered);

	}
	void onSetAdaptered()
		{
			final BaseAppInfoAdapter adapter;
			if (this.isListView)
			{
	//			Logger.d("使用listView");
				adapter = new AppInfoListAdapter(this, this.datas);
			} else
			{
				adapter = new AppInfoGridAdapter(this, this.datas);
			} 
			adapter.setOnAppInfoClickListener(this);
			adapter.setOnAppInfoLongClickListener(this);
			this.gridView.setAdapter(adapter);
			setAdaptering = false;
			loadingBar.setVisibility(View.GONE);
		}

	/**
	 * 过滤字符串
	 * @param item
	 * @return
	 */
	boolean isMatchStr(AppInfo item)
	{
		if(this.seachKeyWord==null||seachKeyWord.isEmpty()){
			return true;
		}
		return item.label.toString().contains(seachKeyWord)||item.packageName.contains(seachKeyWord);
	}
	/**
	 * 并且规则 
	 * @param item
	 * @return
	 */
	  boolean isMatch( AppInfo item)
	{
		  if (filters == null || filters.isEmpty()){
			// 没有任何过滤规则
			  return this.isMatchStr(item);
		  }
		  //并且规则 
		for (FilterModel<AppInfo> filter : filters)
		{ 
			if (filter.enable&&!filter.match(item))
			{
				return false;
			}
		}
		return  isMatchStr(item);
	}

	/**
	 * 检查应用是否已经加载完成
	 * 
	 * @return
	 */
	boolean checkLoaded()
	{
		if (loading)
		{
			tip(R.string.tip_checkloaded);
		}
		return !loading;
	}

	/**
	 * 查找菜单选项
	 * @param id
	 * @return
	 */
	MenuItem findMenuItem(int id)
	{
		if (menu != null)
		{
			for (int i = 0, size = menu.size(); i < size; i++)
			{
				MenuItem item = menu.getItem(i); 
				MenuItem findItem = findMenuItem(item,id);
				if(findItem!=null){
					return findItem;
				}
			}
		}  
		return null;
	}

	/**
	 * 查找菜单选项
	 * 
	 * @param item
	 * @param id
	 * @return
	 */
	MenuItem findMenuItem(MenuItem item, int id)
	{
		if (item.getItemId() == id)
		{
			return item;
		} else if (item.hasSubMenu())
		{
			SubMenu menu = item.getSubMenu();
			for (int i = 0, size = menu.size(); i < size; i++)
			{
				MenuItem findItem = findMenuItem(menu.getItem(i),id);
				if(findItem!=null){
					return findItem;
				} 
			}
		}
		return null;
	}

	/**
	 * 加载菜单设置
	 */
	void loadMenu()
	{
		if (menu != null)
		{
			for (int i = 0, size = menu.size(); i < size; i++)
			{
				MenuItem item = menu.getItem(i);
				loadMenuItem(item);
			} 
		} else
		{
			Logger.d("菜单尚未创建");
		}
	}

	/**
	 * 加载菜单项设置
	 * 
	 * @param item
	 */
	void loadMenuItem(MenuItem item)
	{
		if (item.hasSubMenu())
		{
			SubMenu menu = item.getSubMenu();
			for (int i = 0, size = menu.size(); i < size; i++)
			{
				loadMenuItem(menu.getItem(i));
			}
		} else if (item.isCheckable())
		{
			int id = item.getItemId();
			String key = getIdName(id);
			if(sh.contains(key)){
				boolean value = sh.getBoolean(key, false);
				// Logger.d(key + " = " + value);
				item.setChecked(value);
			} 
		}
	}

	/**
	 * 保存菜单状态
	 */
	void saveMenu()
	{
		if (menu != null)
		{
			for (int i = 0, size = menu.size(); i < size; i++)
			{
				MenuItem item = menu.getItem(i);
				saveMenuItem(item);
			}
			shEditor.commit();
		}
	}

	/**
	 * 保存菜单选项状态
	 * 
	 * @param item
	 */
	void saveMenuItem(MenuItem item)
	{
		if (item.hasSubMenu())
		{
			SubMenu menu = item.getSubMenu();
			for (int i = 0, size = menu.size(); i < size; i++)
			{
				saveMenuItem(menu.getItem(i));
			}
		} else if (item.isCheckable())
		{
			int id = item.getItemId();
			String key = getIdName(id);
			boolean value = item.isChecked();
			// Logger.d(key + " = " + value);
			shEditor.putBoolean(key, value);
		}
	}

	/**
	 * @return
	 */
	List<SorterModel<AppInfo>> readSorters()
	{
		File sortersFile = new File(this.getFilesDir(), sortersPath);
		if (sortersFile.exists())
		{
			try (ObjectInputStream is = new ObjectInputStream(
					new FileInputStream(sortersFile)))
			{
				@SuppressWarnings("unchecked")
				List<SorterModel<AppInfo>> sorters = (List<SorterModel<AppInfo>>) is
						.readObject();
				Logger.d("读取排序:" + sorters);
				return sorters;
			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		return null;
	}
	/**
	 * 保存排序设置
	 */
	void saveSorters()
	{
		File sortersFile = new File(this.getFilesDir(), sortersPath);
		if (sorters == null)
		{ 
			if (sortersFile.exists())
			{
				sortersFile.delete();
			}
		}else{
			Logger.d("保存排序:"+sorters);
			try(ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(sortersFile))){
				os.writeObject(sorters);
			}  catch ( Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	/**
	 * 读取过滤设置
	 * @return
	 */
	  List<FilterModel<AppInfo>> readFilters()
	{
		  File filtersFile = new File(this.getFilesDir(), filtersPath);
			if (filtersFile.exists())
			{
				try (ObjectInputStream is = new ObjectInputStream(
						new FileInputStream(filtersFile)))
				{
					@SuppressWarnings("unchecked")
					List<FilterModel<AppInfo>> filters = (List<FilterModel<AppInfo>>) is
							.readObject();
					Logger.d("读取过滤:" + filters);
					return filters;
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}
			return null;
	}
	/**
	 * 保存过滤设置
	 */
	void saveFilters()
	{
		File filtersFile = new File(this.getFilesDir(), filtersPath);
		if (filters == null)
		{ 
			if (filtersFile.exists())
			{
				filtersFile.delete();
			}
		}else{
			Logger.d("保存过滤:"+filters);
			try(ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(filtersFile))){
				os.writeObject(filters);
			}  catch ( Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置菜单可用状态
	 * 
	 * @param b
	 * @param resId
	 */
	void setMenuEnabled(boolean b, int... resIds)
	{
		if (resIds != null && resIds.length > 0)
		{
			for (int resId : resIds)
			{
				this.menu.findItem(resId).setEnabled(b);
			}
		}
	}

	/**
	 * 设置菜单选中状态
	 * 
	 * @param b
	 * @param resId
	 */
	void setMenuChecked(boolean b, int... resIds)
	{
		if (resIds != null && resIds.length > 0)
		{
			for (int resId : resIds)
			{
				this.menu.findItem(resId).setChecked(b);
			}
		}
	}

	/**
	 * 显示应用详情
	 * @param item
	 */
	void showDetailDialog(AppInfo item)
	{
		Logger.d("应用属性:" + item.label);  
		View view = View.inflate(this, R.layout.layout_app_detail, null);
		ExpandableListView	expandListView=(ExpandableListView)view.findViewById(R.id.expand_lv) ;
		ExpandableListAdapter adapter = new AppInfoDetailAdapter(this,item);
		expandListView.setAdapter(adapter); 
		AlertDialog alert = new AlertDialog.Builder(this) 
				.setTitle(R.string.action_detail).setIcon(item.icon).setView(view).setNegativeButton(android.R.string.ok, null)
				.create(); 
		alert.show();
		
	}

	/**
	 * 返回id字段名
	 * 
	 * @param id
	 * @return
	 */
	String getIdName(int id)
	{
		try
		{
			for (Field f : R.id.class.getFields())
			{
				int mod = f.getModifiers();
				if (Modifier.isStatic(mod) && Modifier.isPublic(mod))
				{
					String name = f.getName();
					int value = f.getInt(null);
					if (id == value)
					{
						return name;
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean onQueryTextSubmit(String query)
	{
		if (checkLoaded())
		{
			//过滤应用名或报名
			this.seachKeyWord=query;
			this.setAdapter(); 
		}
		Logger.d("搜索:"+query);
		return true;
	}

	public boolean onQueryTextChange(String newText)
	{
		return false;
	}

	@Override
	public boolean onLongClick(View view,final AppInfo item)
	{ 
		Logger.d("应用菜单:" + item.label); 
		PopupMenu pop = new PopupMenu(this, view);
		pop.inflate(R.menu.app); 
		OnMenuItemClickListener menuListener=new OnMenuItemClickListener(){ 
			public boolean onMenuItemClick(MenuItem menuItem)
			{
				switch(menuItem.getItemId()){
				case R.id.action_uninstall:{
					startActivity(new Intent(Intent.ACTION_DELETE,
							Uri.parse("package:" + item.packageName)));
					break;
				}
				case R.id.action_opensetting:{
					startActivity(
							new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
									Uri.parse("package:" + item.packageName)));
					break;
				}
				case R.id.action_detail:{
					showDetailDialog(item);
					break;
				}	
				}
				return true;
			}};
		pop.setOnMenuItemClickListener(menuListener);
		SomeUtils.showIcon(pop.getMenu());
		pop.show();
		return true;
	}

	@Override
	public void onClick(AppInfo item)
	{
		Logger.d("打开应用:" + item.label);
		try
		{ 
			Intent intent = pm.getLaunchIntentForPackage(item.packageName);
			if (intent == null)
			{
				tip(R.string.tip_nolaunch);
				return;
			}
			startActivity(intent);
		} catch (Exception ex)
		{
			tip(getString(R.string.tip_exception_openapp, ex.getMessage()));
		}
	}

	@Override
	public boolean onClose()
	{
		this.seachKeyWord=null;
		this.setAdapter();
		return false;
	}

	@Override
	protected void onDestroy()
	{
		if(this.receiver!=null){
			this.unregisterReceiver(receiver);
		}
		super.onDestroy();
	}
	
}
