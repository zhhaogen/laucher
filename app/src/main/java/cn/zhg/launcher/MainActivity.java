package cn.zhg.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.*;
import android.os.Bundle;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import android.widget.PopupMenu.*;
import android.widget.SearchView.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import cn.zhg.base.BasicActivity;
import cn.zhg.basic.FilterDialogBuilder;
import cn.zhg.basic.SorterDialogBuilder;
import cn.zhg.basic.adapter.BasicAdapter;
import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.basic.filter.*;
import cn.zhg.basic.inter.EConsumer;
import cn.zhg.basic.sorter.SorterModel;
import cn.zhg.basic.sorter.SortersModel;
import cn.zhg.launcher.adapter.AppInfoGridAdapter;
import cn.zhg.launcher.adapter.AppInfoListAdapter;
import cn.zhg.launcher.filter.*;
import cn.zhg.launcher.inter.Constant;
import cn.zhg.launcher.model.AppInfo;
import cn.zhg.launcher.service.AppServiceListener;
import cn.zhg.launcher.service.IAppService;
import cn.zhg.launcher.service.impl.AAppService;
import cn.zhg.launcher.service.impl.AppService;
import cn.zhg.launcher.sorter.*;
import cn.zhg.launcher.util.SomeAndroidUtil;
import cn.zhg.logger.Logger;


/**
 *
 */
public class MainActivity extends BasicActivity implements Constant, OnQueryTextListener, OnCloseListener, BasicAdapter.BasicItemClickListener<AppInfo>, BasicAdapter.BasicItemLongClickListener<AppInfo>, AppServiceListener
{

    private static final String sortersPath = "sorters.bin";
    private static final String filtersPath = "filters.bin";
    private GridView gridView;

    private Menu menu;
    private SharedPreferences sh;
    private Editor shEditor;
    private ProgressBar loadingBar;
    private SortFilterAdapter<AppInfo> adapter;
    private KeyWordFilter<AppInfo> keyWordFilter;
    private IAppService service;

    protected void onCreate(Bundle savedInstanceState)
    {
        sh = this.getSharedPreferences(getPackageName(), MODE_PRIVATE);
        shEditor = sh.edit();
        if (sh.getBoolean(KEY_Wallpaper, false))
        {
            Logger.d("显示壁纸");
            this.setTheme(R.style.AppWallpaperTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = this.findViewById(R.id.content_gv);
        loadingBar = this.findViewById(R.id.loading_pb);
        // 添加一个关键词过滤器
        keyWordFilter = new KeyWordFilter<AppInfo>("keyword")
        {
            private static final long serialVersionUID = 1L;

            public String[] getSearchTexts(AppInfo data)
            {
                return new String[]{data.label.toString(), data.packageName};
            }
        };
        service = AAppService.getInstance(this);
        service.addListener(this);
        Logger.d();
        this.checkPermission();
    }

    protected void onPermissionsAccess()
    {
        init();
    }

    protected void onPermissionsDenied(List<PermissionInfo> permissions)
    {
        tip(R.string.tip_onpermissionsdenied);
        finish();
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        Logger.d();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //无法触发??
        Logger.d("菜单加载..");
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        this.loadMenu();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(final MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == R.id.action_refresh)
        {
            if (this.checkLoaded())
            {
                service.reload();
            }
            return true;
        }
        if (itemId == R.id.action_setting)
        {
            this.startActivityForResult(new Intent(this, SettingActivity.class), 21);
            return true;
        }
        if (itemId == R.id.action_setting_system)
        {
            service.openSetting();
            return true;
        }
        if (itemId == R.id.action_sethome)
        {
            this.startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
            return true;
        }
        if (itemId == R.id.action_sort_str)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            if (item.isChecked())
            {
                return true;
            }
            item.setChecked(true);
            this.setMenuChecked(false, R.id.action_sort_install,
                    R.id.action_sort_lasttime, R.id.action_sort_advanced);
            setMenuEnabled(true, R.id.action_sort_reverse);
            // 更新排序器
            adapter.setSorter(getSortersFromMemu());
            adapter.notifyDataSetChanged();
            // 保存菜单
            this.saveMenu();
            return true;
        }
        if (itemId == R.id.action_sort_install)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            if (item.isChecked())
            {
                return true;
            }
            item.setChecked(true);
            this.setMenuChecked(false, R.id.action_sort_str,
                    R.id.action_sort_lasttime, R.id.action_sort_advanced);
            setMenuEnabled(true, R.id.action_sort_reverse);
            // 更新排序器
            adapter.setSorter(getSortersFromMemu());
            adapter.notifyDataSetChanged();
            // 保存菜单
            this.saveMenu();
            return true;
        }
        if (itemId == R.id.action_sort_lasttime)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            if (item.isChecked())
            {
                return true;
            }
            item.setChecked(true);
            this.setMenuChecked(false, R.id.action_sort_str,
                    R.id.action_sort_install, R.id.action_sort_advanced);
            setMenuEnabled(true, R.id.action_sort_reverse);
            // 更新排序器
            adapter.setSorter(getSortersFromMemu());
            adapter.notifyDataSetChanged();
            // 保存菜单
            this.saveMenu();
            return true;
        }
        if (itemId == R.id.action_sort_advanced)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            List<SorterModel<AppInfo>> sorters = this.readSorters();
            if (sorters == null || sorters.isEmpty())
            {
                sorters = this.createSorters();
            }
            new SorterDialogBuilder<AppInfo>(this).setSorters(sorters)
                    .setCancelListener(null)
                    .setOkListener(new EConsumer<List<SorterModel<AppInfo>>>()
                    {
                        public void accept(List<SorterModel<AppInfo>> results)
                        {
                            item.setChecked(true);
                            setMenuChecked(false, R.id.action_sort_str,
                                    R.id.action_sort_install, R.id.action_sort_lasttime,
                                    R.id.action_sort_reverse);
                            setMenuEnabled(false, R.id.action_sort_reverse);
                            saveMenu();

                            adapter.setSorter(new SortersModel<>(results));
                            adapter.notifyDataSetChanged();
                            saveSorters(results);
                        }
                    }).show();
            return true;
        }
        if (itemId == R.id.action_sort_reverse)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            item.setChecked(!item.isChecked());
            // 更新排序器
            adapter.setSorter(getSortersFromMemu());
            adapter.notifyDataSetChanged();
            // 保存菜单
            this.saveMenu();
            return true;
        }
        if (itemId == R.id.action_sort_reset)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            this.setMenuChecked(false, R.id.action_sort_str,
                    R.id.action_sort_install, R.id.action_sort_lasttime,
                    R.id.action_sort_advanced, R.id.action_sort_reverse);
            setMenuEnabled(true, R.id.action_sort_reverse);
            // 更新排序器
            adapter.setSorter(getSortersFromMemu());
            adapter.notifyDataSetChanged();
            // 保存菜单
            this.saveMenu();
            // 删除自定义排序
            this.saveSorters(null);
            return true;
        }
        if (itemId == R.id.action_filter_user)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            boolean isFilterUser = !item.isChecked();
            item.setChecked(isFilterUser);
            setMenuChecked(false, R.id.action_filter_advanced);
            // 更新过滤器
            adapter.setFilter(getFiltersFromMemu());
            adapter.notifyDataSetChanged();
            // 保存菜单
            this.saveMenu();
            return true;
        }
        if (itemId == R.id.action_filter_launch)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            boolean isFilterLaunch = !item.isChecked();
            item.setChecked(isFilterLaunch);
            setMenuChecked(false, R.id.action_filter_advanced);
            // 更新过滤器
            adapter.setFilter(getFiltersFromMemu());
            adapter.notifyDataSetChanged();
            // 保存菜单
            this.saveMenu();
            return true;
        }
        if (itemId == R.id.action_filter_advanced)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            List<FilterModel<AppInfo>> filters = this.readFilters();
            if (filters == null || filters.isEmpty())
            {
                filters = this.createFilters();
            }
            new FilterDialogBuilder<AppInfo>(this).setFilters(filters)
                    .setCancelListener(null)
                    .setOkListener(new EConsumer<List<FilterModel<AppInfo>>>()
                    {
                        public void accept(List<FilterModel<AppInfo>> results)
                        {
                            item.setChecked(true);
                            setMenuChecked(false, R.id.action_filter_user,
                                    R.id.action_filter_launch);
                            saveMenu();

                            adapter.setFilter(new AndFiltersModel<>(results));
                            adapter.notifyDataSetChanged();
                            saveFilters(results);
                        }
                    })
                    .show();
            return true;
        }
        if (itemId == R.id.action_filter_reset)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            setMenuChecked(false, R.id.action_filter_user,
                    R.id.action_filter_launch, R.id.action_filter_advanced);
            // 更新过滤器
            adapter.setFilter(getFiltersFromMemu());
            adapter.notifyDataSetChanged();
            // 保存菜单
            this.saveMenu();
            // 删除自定义排序
            this.saveFilters(null);
            return true;
        }
        if (itemId == R.id.action_views_grid)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            if (item.isChecked())
            {
                return true;
            }
            item.setChecked(true);
            setMenuChecked(false, R.id.action_views_list);
            this.gridView.setNumColumns(sh.getInt(KEY_NumColumns, 5));
            // 重新设置adapter
            setAdapter(adapter.getOrignalDatas());
            this.saveMenu();
            return true;
        }
        if (itemId == R.id.action_views_list)
        {
            if (!this.checkLoaded())
            {
                return true;
            }
            if (item.isChecked())
            {
                return true;
            }
            item.setChecked(true);
            setMenuChecked(false, R.id.action_views_grid);
            this.gridView.setNumColumns(1);
            // 重新设置adapter
            setAdapter(adapter.getOrignalDatas());
            this.saveMenu();
            return true;
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query)
    {
        if (checkLoaded())
        {
            //过滤应用名或报名
            this.keyWordFilter.value = query;
            this.adapter.sortFilter();
            adapter.notifyDataSetChanged();
        }
        Logger.d("搜索:" + query);
        return true;
    }

    public boolean onQueryTextChange(String newText)
    {
        return false;
    }

    /**
     * 长按图标
     */
    public boolean onItemLongClick(BasicAdapter.BasicViewHolder<AppInfo> holder, final AppInfo item, int position)
    {
        Logger.d("应用菜单:" + item.label);
        PopupMenu pop = new PopupMenu(this, holder.itemView);
        pop.inflate(R.menu.app);
        OnMenuItemClickListener menuListener = new OnMenuItemClickListener()
        {
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_uninstall)
                {
                    try
                    {
                        service.uninstall(item.packageName);
                    } catch (Throwable ex)
                    {
                        tip(ex.getMessage());
                    }
                    return true;
                }
                if (itemId == R.id.action_setting_system)
                {
                    try
                    {
                        service.openSetting(item.packageName);
                    } catch (Throwable ex)
                    {
                        tip(ex.getMessage());
                    }
                    return true;
                }
                if (itemId == R.id.action_detail)
                {
                    new AppDetailDialogBuilder(MainActivity.this).setApp(item).show();
                    return true;
                }
                return true;
            }
        };
        pop.setOnMenuItemClickListener(menuListener);
        SomeAndroidUtil.showIcon(pop.getMenu());
        pop.show();
        return true;
    }

    /**
     * 点击图标
     */
    public void onItemClick(BasicAdapter.BasicViewHolder<AppInfo> holder, final AppInfo item, int position)
    {
        Logger.d("打开应用:" + item.label);
        try
        {
            service.launch(item.packageName);
        } catch (Exception ex)
        {
            tip(ex.getMessage());
        }
    }

    /**
     * 关闭搜索框
     */
    public boolean onClose()
    {
        if (!checkLoaded())
        {
            return false;
        }
        this.keyWordFilter.value = null;
        this.adapter.sortFilter();
        adapter.notifyDataSetChanged();
        return false;
    }

    public void onLoadStop()
    {
        this.hideProcess();
        loadingBar.setVisibility(View.GONE);
    }

    public void onLoadStart(int size)
    {
        loadingBar.setVisibility(View.VISIBLE);
        loadingBar.setIndeterminate(false);
        loadingBar.setMax(size);
    }

    public void onAppUpdate(AppInfo data, byte type, int index)
    {
        if (type == TYPE_UPDATE_SIMPLE)
        {
            //简单信息,进度条
            loadingBar.setSecondaryProgress(index);
            return;
        }
        //详细信息
        if (type == TYPE_UPDATE_DETAIL)
        {
            loadingBar.setProgress(index);
            adapter.notifyOrignalDataSetChanged(data);
            return;
        }
        //xml内容
    }

    public void onAppListUpdate(List<AppInfo> datas, byte type)
    {
        setAdapter(datas);
        if (type == TYPE_UPDATE_DETAIL)
        {
            //可以隐藏进度条了
            loadingBar.setVisibility(View.GONE);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getBooleanExtra(KEY_Recreate, false))
        {
            Logger.d("需要重启");
            this.recreate();
        }
    }

    protected void onDestroy()
    {
        service.stop();
        super.onDestroy();
    }

    /**
     * 初始化
     */
    private void init()
    {
        Logger.d();
        if (!isDefaultHome())
        {
            // this.startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
        }
        if ( isMenuItemChecked(R.id.action_views_list))
        {
            gridView.setNumColumns(1);
        } else
        {
            gridView.setNumColumns(sh.getInt(KEY_NumColumns, 5));
        }
        service.reload();
    }

    /**
     * 应用加载完成
     */
    private void setAdapter(List<AppInfo> datas)
    {
        Logger.d("应用总数量:" + datas.size());
        boolean isListView =isMenuItemChecked(R.id.action_views_list);
        if (isListView)
        {
            // Logger.d("使用listView");
            adapter = new AppInfoListAdapter(this, datas);
        } else
        {
            adapter = new AppInfoGridAdapter(this, datas);
        }
        adapter.setItemClickListener(this);
        adapter.setItemLongClickListener(this);
        FilterModel<AppInfo> filter = getFiltersFromMemu();
        Logger.d("设置过滤器:" + filter);
        adapter.setFilter(filter);
        SorterModel<AppInfo> sorter = getSortersFromMemu();
//		Logger.d("设置排序器:"+sorter);
        adapter.setSorter(sorter);
        this.gridView.setAdapter(adapter);
    }

    /**
     * 根据菜单设置读取排序器
     */
    private SorterModel<AppInfo> getSortersFromMemu()
    {
        // 按字母排序
        if (isMenuItemChecked(R.id.action_sort_str))
        {
            boolean isSortReverse = isMenuItemChecked(R.id.action_sort_reverse);
            SorterModel<AppInfo> sort = new AppLabelSorterModel(
                    this.getString(R.string.action_sort_str));
            sort.isReverse = isSortReverse;
            return sort;
        }
        // 安装时间
        if (isMenuItemChecked(R.id.action_sort_install))
        {
            boolean isSortReverse = isMenuItemChecked(R.id.action_sort_reverse);
            SorterModel<AppInfo> sort = new AppFirstInstallTimeSorterModel(
                    this.getString(R.string.action_sort_install));
            sort.isReverse = isSortReverse;
            return sort;
        }
        // 最近使用
        if (this.isMenuItemChecked(R.id.action_sort_lasttime))
        {
            boolean isSortReverse = isMenuItemChecked(R.id.action_sort_reverse);
            SorterModel<AppInfo> sort = new AppLastUpdateTimeSorterModel(
                    this.getString(R.string.action_sort_lasttime));
            sort.isReverse = isSortReverse;
            return sort;
        }
        // 自定义排序
        if (this.isMenuItemChecked(R.id.action_sort_advanced))
        {
            return new SortersModel<AppInfo>(readSorters());
        }
        return null;
    }

    /**
     * 根据菜单设置读取过滤器
     */
    private FilterModel<AppInfo> getFiltersFromMemu()
    {
        AndFiltersModel<AppInfo> filters = new AndFiltersModel<>();
        filters.add(keyWordFilter);
        // 自定义
        if (this.isMenuItemChecked(R.id.action_filter_advanced))
        {
            List<FilterModel<AppInfo>> list = readFilters();
            if (list != null)
            {
                filters.addAll(list);
            }
            return filters;
        }
        // 仅用户应用
        if (this.isMenuItemChecked(R.id.action_filter_user))
        {
            AppSystemAppFilterModel filter = new AppSystemAppFilterModel(
                    getString(R.string.action_filter_user));
            filter.value = "false";
            filters.add(filter);
        }
        // 仅可启动
        if (isMenuItemChecked(R.id.action_filter_launch))
        {
            AppHasLaunchIntentFilterModel filter = new AppHasLaunchIntentFilterModel(
                    getString(R.string.action_filter_launch));
            filter.value = "true";
            filters.add(filter);
        }
        return filters;
    }

    /**
     * 是否为默认桌面
     *
     * @return
     */
    private boolean isDefaultHome()
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

    private List<SorterModel<AppInfo>> createSorters()
    {
        List<SorterModel<AppInfo>> sorters = new ArrayList<>();
        sorters.add(new AppLabelSorterModel(
                this.getString(R.string.lab_label)));
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
        sorters.add(new AppTargetSdkVersionSorterModel(
                this.getString(R.string.lab_targetsdkversion)));
        sorters.add(new AppMinSdkVersionSorterModel(
                this.getString(R.string.lab_minsdkversion)));
        return sorters;
    }

    /**
     * 自定义过滤器列表
     */
    private List<FilterModel<AppInfo>> createFilters()
    {
        List<FilterModel<AppInfo>> filters = new ArrayList<>();
        FilterModel<AppInfo> item = new AppLabelFilterModel(getString(R.string.lab_label));
        item.enable = false;
        filters.add(item);
        item = new AppPackageNameFilterModel(
                getString(R.string.lab_packagename));
        item.enable = false;
        filters.add(item);
        item = new AppFileSizeFilterModel(
                getString(R.string.lab_filesize));
        item.enable = false;
        filters.add(item);
        item = new AppHasLaunchIntentFilterModel(
                getString(R.string.lab_haslaunch));
        item.enable = false;
        filters.add(item);
        item = new AppFlagsFilterModel(getString(R.string.lab_flags));
        item.enable = false;
        filters.add(item);
        item = new AppRequestPermissionsFilterModel(
                getString(R.string.lab_request_permissions));
        item.enable = false;
        filters.add(item);
        item = new AppTargetSdkVersionFilterModel(
                getString(R.string.lab_targetsdkversion));
        item.enable = false;
        filters.add(item);
        item = new AppMinSdkVersionFilterModel(
                getString(R.string.lab_minsdkversion));
        item.enable = false;
        filters.add(item);
        return filters;
    }

    /**
     * 检查数据是否已经加载,可以进行排序或过滤
     */
    private boolean checkLoaded()
    {
        byte state = service.getState();
        if (state == AppService.STATE_LOADED || state == AppService.STATE_LOADING_DETAIL || state == AppService.STATE_LOADING_XML)
        {
            return true;
        }
        tip(R.string.tip_checkloaded);
        return false;
    }

    /**
     * 查找菜单选项
     *
     * @param id
     * @return
     */
    private MenuItem findMenuItem(int id)
    {
        if (menu == null)
        {
            return null;
        }
        for (int i = 0, size = menu.size(); i < size; i++)
        {
            MenuItem item = menu.getItem(i);
            MenuItem findItem = findMenuItem(item, id);
            if (findItem != null)
            {
                return findItem;
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
    private MenuItem findMenuItem(MenuItem item, int id)
    {
        if (item.getItemId() == id)
        {
            return item;
        }
        if (item.hasSubMenu())
        {
            SubMenu menu = item.getSubMenu();
            for (int i = 0, size = menu.size(); i < size; i++)
            {
                MenuItem findItem = findMenuItem(menu.getItem(i), id);
                if (findItem != null)
                {
                    return findItem;
                }
            }
        }
        return null;
    }

    /**
     * 加载菜单设置
     */
    private void loadMenu()
    {
        if (menu == null)
        {
            Logger.d("菜单尚未创建");
        }
        for (int i = 0, size = menu.size(); i < size; i++)
        {
            MenuItem item = menu.getItem(i);
            loadMenuItem(item);
        }
    }
    /**
     * 加载菜单项设置
     *
     * @param item
     */
    private void loadMenuItem(MenuItem item)
    {
        if (item.hasSubMenu())
        {
            SubMenu menu = item.getSubMenu();
            for (int i = 0, size = menu.size(); i < size; i++)
            {
                loadMenuItem(menu.getItem(i));
            }
            return;
        }
        if (!item.isCheckable())
        {
            return;
        }
        int id = item.getItemId();
        String key = getIdName(id);
        if (sh.contains(key))
        {
            boolean value = sh.getBoolean(key, false);
            // Logger.d(key + " = " + value);
            item.setChecked(value);
        }
    }

    /**
     * 菜单是否被选中
     */
    private boolean isMenuItemChecked(int id){
        if(menu!=null){
            return menu.findItem(id).isChecked();
        }
        return sh.getBoolean(getIdName(id),false);
    }
    /**
     * 保存菜单状态
     */
    private void saveMenu()
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
    private void saveMenuItem(MenuItem item)
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
     * 读取自定义排序
     */
    private List<SorterModel<AppInfo>> readSorters()
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
     * 保存自定义排序设置
     */
    private void saveSorters(List<SorterModel<AppInfo>> sorters)
    {
        File sortersFile = new File(this.getFilesDir(), sortersPath);
        if (sorters == null)
        {
            if (sortersFile.exists())
            {
                sortersFile.delete();
            }
            return;
        }
        Logger.d("保存排序:" + sorters);
        try (ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(sortersFile)))
        {
            os.writeObject(sorters);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 读取过滤设置
     *
     * @return
     */
    private List<FilterModel<AppInfo>> readFilters()
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
    private void saveFilters(List<FilterModel<AppInfo>> filters)
    {
        File filtersFile = new File(this.getFilesDir(), filtersPath);
        if (filters == null)
        {
            if (filtersFile.exists())
            {
                filtersFile.delete();
            }
            return;
        }
        Logger.d("保存过滤:" + filters);
        try (ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(filtersFile)))
        {
            os.writeObject(filters);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置菜单可用状态
     */
    private void setMenuEnabled(boolean b, int... resIds)
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
     * @param resIds
     */
    private void setMenuChecked(boolean b, int... resIds)
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
     * 返回id字段名
     *
     * @param id
     * @return
     */
    private String getIdName(int id)
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

}
