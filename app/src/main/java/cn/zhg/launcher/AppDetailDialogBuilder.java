package cn.zhg.launcher;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import cn.zhg.basic.adapter.SortFilterAdapter;
import cn.zhg.basic.filter.*;
import cn.zhg.launcher.adapter.*;
import cn.zhg.launcher.model.*;
import cn.zhg.launcher.service.AppServiceListener;
import cn.zhg.launcher.service.IAppService;
import cn.zhg.launcher.service.impl.AAppService;
import cn.zhg.launcher.util.SomeUtil;
import cn.zhg.logger.Logger;


/**
 * 应用详情对话框
 */
@SuppressLint("InflateParams")
public class AppDetailDialogBuilder {
    private Context context;
    private AppInfo item;
    private RadioGroup tabhost;
    private LayoutInflater inflater;
    private ViewGroup tabContainer;
    private List<Runnable> updaters;
    public AppDetailDialogBuilder(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public AppDetailDialogBuilder setApp(AppInfo item) {
        this.item = item;
        return this;
    }

    public AlertDialog create() {
        Logger.d("应用属性:" + item.label);
        updaters=new ArrayList<>();
        View view = inflater.inflate(R.layout.layout_app_detail, null);
        tabhost = view.findViewById(R.id.tab_btns);
        tabContainer = view.findViewById(R.id.tab_group);
        final IAppService service = AAppService.getInstance(context);
        AppServiceListener listener = new AppServiceListener() {
            public void onLoadStop() {
            }

            public void onLoadStart(int size) {
            }

            public void onAppListUpdate(List<AppInfo> datas, byte type) {
            }

            public void onAppUpdate(AppInfo data, byte type, int index) {
                if (!item.packageName.equals(data.packageName)) {
                    return;
                }
                updateViews();
            }
        };
        service.addListener(listener);
        initViews();
        AlertDialog alert = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(item.label).setIcon(item.icon)
                .setView(view).setNegativeButton(android.R.string.ok, null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        service.removeListener(listener);
                    }
                })
                .create();
        return alert;
    }

    public AppDetailDialogBuilder show() {
        this.create().show();
        return this;
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        // 基本属性
        addTab(R.string.lab_base,
                () -> new TitleDesAdapter(context, buildBaseValues()),
                new KeyWordFilter<TitleDes>() {
                    private static final long serialVersionUID = 1L;

                    public String[] getSearchTexts(TitleDes data) {
                        return new String[]{data.des, data.title};
                    }
                }).setChecked(true);
        //
        addTab(R.string.lab_activities,
                () -> new AppActivityInfoAdapter(context, item.activities),
                new KeyWordFilter<AppActivityInfo>() {
                    private static final long serialVersionUID = 1L;

                    public CharSequence[] getSearchTexts(AppActivityInfo data) {
                        return new CharSequence[]{data.label, data.name};
                    }
                }, new BooleanFilterModel<AppActivityInfo>("enabled") {
                    private static final long serialVersionUID = 1L;

                    public boolean getDataValue(AppActivityInfo data) {
                        return data.enabled;
                    }
                }, new BooleanFilterModel<AppActivityInfo>("exported") {
                    private static final long serialVersionUID = 1L;

                    public boolean getDataValue(AppActivityInfo data) {
                        return data.exported;
                    }
                });
        //
        addTab(R.string.lab_services,
                () -> new AppServiceInfoAdapter(context, item.services),
                new KeyWordFilter<AppServiceInfo>() {
                    private static final long serialVersionUID = 1L;

                    public CharSequence[] getSearchTexts(AppServiceInfo data) {
                        return new CharSequence[]{data.label, data.name};
                    }
                }, new BooleanFilterModel<AppServiceInfo>("enabled") {
                    private static final long serialVersionUID = 1L;

                    public boolean getDataValue(AppServiceInfo data) {
                        return data.target.enabled;
                    }
                }, new BooleanFilterModel<AppServiceInfo>("exported") {
                    private static final long serialVersionUID = 1L;

                    public boolean getDataValue(AppServiceInfo data) {
                        return data.exported;
                    }
                });
        //
        addTab(R.string.lab_providers,
                () -> new AppProviderInfoAdapter(context, item.providers),
                new KeyWordFilter<AppProviderInfo>() {
                    private static final long serialVersionUID = 1L;

                    public CharSequence[] getSearchTexts(AppProviderInfo data) {
                        return new CharSequence[]{data.label, data.name, data.authority};
                    }
                }, new BooleanFilterModel<AppProviderInfo>("enabled") {
                    private static final long serialVersionUID = 1L;

                    public boolean getDataValue(AppProviderInfo data) {
                        return data.target.enabled;
                    }
                }, new BooleanFilterModel<AppProviderInfo>("exported") {
                    private static final long serialVersionUID = 1L;

                    public boolean getDataValue(AppProviderInfo data) {
                        return data.exported;
                    }
                });
        //
        addTab(R.string.lab_permissions,
                () -> new AppPermissionInfoAdapter(context, item.permissions),
                new KeyWordFilter<AppPermissionInfo>() {
                    private static final long serialVersionUID = 1L;

                    public CharSequence[] getSearchTexts(AppPermissionInfo data) {
                        return new CharSequence[]{data.label, data.name,
                                data.description};
                    }
                });
        //
        addTab(R.string.lab_request_permissions,
                () -> new AppPermissionInfoAdapter(context,
                        item.requestedPermissions),
                new KeyWordFilter<AppPermissionInfo>() {
                    private static final long serialVersionUID = 1L;

                    public CharSequence[] getSearchTexts(AppPermissionInfo data) {
                        return new CharSequence[]{data.label, data.name,
                                data.description};
                    }
                });

        ExpandableListView expandListView = (ExpandableListView) inflater.inflate(R.layout.layout_expand_list, tabhost, false);
        addTab(context.getString(R.string.lab_signatures), expandListView, () -> {
            expandListView.setAdapter(new AppSignatureInfoAdapter(context, item.signatures));
        });
        //
        addTab(R.string.lab_flags, () -> new StringAdapter(context, item.flagFields),
                new KeyWordFilter<Object>() {
                    private static final long serialVersionUID = 1L;

                    public String[] getSearchTexts(Object data) {
                        return new String[]{
                                data == null ? null : data.toString()};
                    }
                });
        //
        addTab(R.string.lab_instrumentations, () -> new AppInstrumentationInfoAdapter(
                context, item.instrumentations), new KeyWordFilter<AppInstrumentationInfo>() {
            private static final long serialVersionUID = 1L;

            public CharSequence[] getSearchTexts(AppInstrumentationInfo data) {
                return new CharSequence[]{data.label, data.name};
            }
        });
        //
        addTab(R.string.lab_receivers,
                () -> new AppReceiverInfoAdapter(context, item.receivers),
                new KeyWordFilter<AppActivityInfo>() {
                    private static final long serialVersionUID = 1L;

                    public CharSequence[] getSearchTexts(AppActivityInfo data) {
                        return new CharSequence[]{data.label, data.name};
                    }
                }, new BooleanFilterModel<AppActivityInfo>("enabled") {
                    private static final long serialVersionUID = 1L;

                    public boolean getDataValue(AppActivityInfo data) {
                        return data.enabled;
                    }
                }, new BooleanFilterModel<AppActivityInfo>("exported") {
                    private static final long serialVersionUID = 1L;

                    public boolean getDataValue(AppActivityInfo data) {
                        return data.exported;
                    }
                });
        //
        addTab(R.string.lab_sharedlibraryfiles,
                () -> new StringAdapter(context, item.sharedLibraryFiles),
                new KeyWordFilter<Object>() {
                    private static final long serialVersionUID = 1L;

                    public String[] getSearchTexts(Object data) {
                        return new String[]{
                                data == null ? null : data.toString()};
                    }
                });
        //
        addTab(R.string.lab_appsources,
                () -> new AppSourceInfoAdapter(context, item.appSources),
                new KeyWordFilter<AppSourceInfo>() {
                    private static final long serialVersionUID = 1L;

                    public CharSequence[] getSearchTexts(AppSourceInfo data) {
                        return new String[]{data.name, data.sourceDir, data.publicSourceDir};
                    }
                });
        View scrollTextView = this.inflater.inflate(R.layout.layout_scroll_textview, tabContainer, false);
        TextView textView = (TextView) scrollTextView.findViewById(R.id.des_tv);
        addTab("AndroidManifest.xml", scrollTextView, () -> {
            textView.setText(item.manifestXml);
        });

    }

    /**
     * 更新内容
     */
    private void updateViews() {
        Logger.d("更新应用详情");
        for (Runnable updater:updaters){
            updater.run();
        }
    }

    /**
     * 添加标签,列表内容
     */
    private RadioButton addTab(String title, final View view, Runnable updater) {
        RadioButton btn = (RadioButton) inflater
                .inflate(R.layout.item_tab_buttons, tabhost, false);
        btn.setText(title);
        tabhost.addView(btn);
        tabContainer.addView(view);
        view.setVisibility(View.GONE);
        updater.run();
        updaters.add(updater);
        btn.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            view.setVisibility(View.VISIBLE);
                        } else {
                            view.setVisibility(View.GONE);
                        }
                    }
                });
        return btn;
    }

    /**
     * 添加标签,列表内容
     */
    private <T> RadioButton addTab(int titleId,
                                 final SortFilterAdapterGetter<T> adapter) {
        return this.addTab(titleId, adapter, null, null, null);
    }

    /**
     * 添加标签,列表内容
     */
    private <T> RadioButton addTab(int titleId,
                                 final SortFilterAdapterGetter<T> adapter, KeyWordFilter<T> keyWordFilter) {
        return this.addTab(titleId, adapter, keyWordFilter, null, null);
    }

    /**
     * 添加标签,列表内容
     *
     * @param titleId        标题
     * @param adapterGetter  列表适配器
     * @param keyWordFilter  关键词过滤器
     * @param enableFilter   启用过滤器
     * @param exportedFilter 公开过滤器
     * @return
     */
    private <T> RadioButton addTab(int titleId,
                                 final SortFilterAdapterGetter<T> adapterGetter, final KeyWordFilter<T> keyWordFilter,
                                 final BooleanFilterModel<T> enableFilter, final BooleanFilterModel<T> exportedFilter) {
        final View child = this.inflater.inflate(R.layout.layout_tab_listview,
                tabhost, false);
        ListView listView = child.findViewById(R.id.listView);
        View filterViewGroup = child.findViewById(R.id.filter_group);
        SearchView searchView = child.findViewById(R.id.searchView);
        CompoundButton enabledCheck = child.findViewById(R.id.enabled_check);
        CompoundButton exportedCheck = child.findViewById(R.id.exported_check);
        TextView listSizeText=child.findViewById(R.id.listSizeText);
        AndFiltersModel<T> filters = new AndFiltersModel<>();
        if (keyWordFilter != null) {
            searchView.setVisibility(View.VISIBLE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextSubmit(String query) {
//					Logger.d("搜索:"+query);
                    SortFilterAdapter<T> adapter = (SortFilterAdapter<T>) listView.getAdapter();
                    keyWordFilter.value = query;
                    adapter.sortFilter();
                    adapter.notifyDataSetChanged();
                    return true;
                }

                public boolean onQueryTextChange(String newText) {
                    if (newText == null || newText.isEmpty()) {
//						Logger.d("清空搜索");
                        SortFilterAdapter<T> adapter = (SortFilterAdapter<T>) listView.getAdapter();
                        keyWordFilter.value = null;
                        adapter.sortFilter();
                        adapter.notifyDataSetChanged();
                    }
                    return false;
                }
            });
            filters.add(keyWordFilter);
        }
        if (enableFilter != null) {
            enableFilter.enable = false;
            enableFilter.value = "true";
            enabledCheck.setVisibility(View.VISIBLE);
            enabledCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //是否启用过滤器
                    if (isChecked) {
                        enableFilter.enable = true;
                    } else {
                        enableFilter.enable = false;
                    }
                    SortFilterAdapter<T> adapter = (SortFilterAdapter<T>) listView.getAdapter();
                    adapter.sortFilter();
                    adapter.notifyDataSetChanged();
                }
            });
            filters.add(enableFilter);
        }
        if (exportedFilter != null) {
            exportedFilter.enable = false;
            exportedFilter.value = "true";
            exportedCheck.setVisibility(View.VISIBLE);
            exportedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        exportedFilter.enable = true;
                    } else {
                        exportedFilter.enable = false;
                    }
                    SortFilterAdapter<T> adapter = (SortFilterAdapter<T>) listView.getAdapter();
                    adapter.sortFilter();
                    adapter.notifyDataSetChanged();
                }
            });
            filters.add(exportedFilter);
        }
        if (!filters.getFilters().isEmpty()) {
            //无可用过滤器
            filterViewGroup.setVisibility(View.VISIBLE);
        }
        return addTab(context.getString(titleId), child, () -> {
            SortFilterAdapter<T> adapter = adapterGetter.get();
            adapter.setFilter(filters);
            listView.setAdapter(adapter);
            int count = adapter.getCount();
            if (count == 0) {
                filterViewGroup.setVisibility(View.GONE);
                listSizeText.setVisibility(View.GONE);
            } else {
                filterViewGroup.setVisibility(View.VISIBLE);
                listSizeText.setVisibility(View.VISIBLE);
                listSizeText.setText(context.getString(R.string.lab_list_size,count));
            }

        });
    }

    /**
     * 基础信息
     */
    private List<TitleDes> buildBaseValues() {
        List<TitleDes> baseValues = new ArrayList<>();
        baseValues.add(new TitleDes(context.getString(R.string.lab_label),
                SomeUtil.toString(item.label)));
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
        baseValues.add(new TitleDes(context.getString(R.string.lab_minsdkversion), item.minSdkVersion));
        if(item.compileSdkVersion!=0){
            baseValues.add(new TitleDes(context.getString(R.string.lab_compilesdkversion), item.compileSdkVersion));
        }
        baseValues.add(new TitleDes(context.getString(R.string.lab_uid), item.uid));
        baseValues.add(new TitleDes(context.getString(R.string.lab_filesize), item.fileSizeStr));
        baseValues.add(
                new TitleDes(context.getString(R.string.lab_firstinstall_time), item.firstInstallTimeStr));
        baseValues.add(new TitleDes(context.getString(R.string.lab_lastupdate_time), item.lastUpdateTimeStr));
        return baseValues;
    }


    private interface SortFilterAdapterGetter<T> {
        SortFilterAdapter<T> get();
    }

}
