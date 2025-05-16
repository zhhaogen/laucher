package cn.zhg.launcher.service.impl;

import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import java.util.*;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.launcher.service.AppServiceListener;
import cn.zhg.launcher.service.IAppService;
import xiaogen.util.Logger;

/**
 * app服务
 */
public class AppService implements IAppService {
    private final Context context;
    private final PackageManager pm;
    private final String myPackageName;
    private final List<AppServiceListener> listeners;
    /**
     * app安装卸载监听器
     */
    private BroadcastReceiver receiver;
    /**
     * 全部app列表
     */
    private List<AppInfo> datas;
    private byte state = STATE_UNLOAD;

    public AppService(Context context) {
        this.context = context;
        pm = context.getPackageManager();
        this.myPackageName = context.getPackageName();
        listeners = new ArrayList<>();
        registerReceiver();
    }

    public void addListener(AppServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AppServiceListener listener) {
        listeners.remove(listener);
    }

    public byte getState() {
        return state;
    }

    public  void reload() {
        if (state == STATE_UNLOAD || state == STATE_LOADED) {
            //启动一个线程
            new Thread() {
                public void run() {
                    Logger.d("开始后台线程");
                    doLoad();
                }
            }.start();
            return;
        }
        //重启线程执行
        Logger.d("更新后台线程任务");
        state = STATE_RELOADING;
    }

    private synchronized void doLoad() {
        datas = new ArrayList<>();
        List<PackageInfo> packages = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        int size = packages.size();
        Logger.d("开始加载,size="+size);
        onLoadStart(size - 1);
        state = STATE_LOADING_SIMPLE;
        int count = 0;
        for (int i = 0; i < size; i++) {
            //任务信号
            if (state == STATE_STOPING) {
                state = STATE_UNLOAD;
                Logger.d("终止信号");
                onLoadStop();
                return;
            }
            if (state == STATE_RELOADING) {
                Logger.d("重载信号");
                doLoad();
                return;
            }
            PackageInfo info = packages.get(i);
            if (info.packageName.equals(myPackageName)) {
                //不显示本身
                continue;
            }
            AppInfo item = new AppInfo(info);
            onAppUpdate(item, AppServiceListener.TYPE_UPDATE_SIMPLE, count);
            datas.add(item);
            count++;
        }
        Logger.d("读取简单信息完成,count="+count);
        onAppListUpdate(datas, AppServiceListener.TYPE_UPDATE_SIMPLE);
        state = STATE_LOADING_DETAIL;
        for (int i = 0; i < count; i++) {
            AppInfo item = datas.get(i);
            item.updateData(pm);
            //任务信号
            if (state == STATE_STOPING) {
                state = STATE_UNLOAD;
                Logger.d("终止信号");
                onLoadStop();
                return;
            }
            if (state == STATE_RELOADING) {
                Logger.d("重载信号");
                doLoad();
                return;
            }
            onAppUpdate(item, AppServiceListener.TYPE_UPDATE_DETAIL, i);
        }
        Logger.d("读取详细信息完成,count="+count);
        onAppListUpdate(datas, AppServiceListener.TYPE_UPDATE_DETAIL);
        state = STATE_LOADING_XML;
        for (int i = 0; i < count; i++) {
            AppInfo item = datas.get(i);
            item.updateManifestXml();
            //任务信号
            if (state == STATE_STOPING) {
                state = STATE_UNLOAD;
                Logger.d("终止信号");
                onLoadStop();
                return;
            }
            if (state == STATE_RELOADING) {
                Logger.d("重载信号");
                doLoad();
                return;
            }
            onAppUpdate(item, AppServiceListener.TYPE_UPDATE_XML, i);
        }
        Logger.d("读取xml信息完成,count="+count);
        onAppListUpdate(datas, AppServiceListener.TYPE_UPDATE_XML);
        state = STATE_LOADED;
        onLoadStop();
    }

    public List<AppInfo> getSimpleList() {
        List<AppInfo> datas = new ArrayList<>();
        List<PackageInfo> packages = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        int size = packages.size();
        for (int i = 0; i < size; i++) {
            PackageInfo info = packages.get(i);
            if (info.packageName.equals(myPackageName)) {
                //不显示本身
                continue;
            }
            AppInfo item = new AppInfo(info);
            datas.add(item);
        }
        return datas;
    }

    public List<AppInfo> getList() {
        return datas;
    }

    public void uninstall(String packageName) {
        context.startActivity(new Intent(Intent.ACTION_DELETE,
                Uri.parse("package:" + packageName)));
    }

    public void launch(String packageName) {
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            throw new NullPointerException();
        }
        context.startActivity(intent);
    }

    public void openSetting() {
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    public void openSetting(String packageName) {
        context.startActivity(
                new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + packageName)));
    }

    public void stop() {
        if (state == STATE_LOADING_SIMPLE || state == STATE_LOADING_DETAIL || state == STATE_LOADING_XML) {
            state = STATE_STOPING;
        }
        unregisterReceiver();
    }

    private void onLoadStart(int size) {
        if (listeners == null||listeners.isEmpty()){
            return;
        }
        for (AppServiceListener listener : listeners) {
            listener.onLoadStart(size);
        }
    }

    private void onAppUpdate(AppInfo data, byte type, int index) {
        if (listeners == null||listeners.isEmpty()){
            return;
        }
        for (AppServiceListener listener : listeners) {
            listener.onAppUpdate(data, type, index);
        }
    }

    private void onAppListUpdate(List<AppInfo> datas, byte type) {
        if (listeners == null||listeners.isEmpty()){
            return;
        }
        for (AppServiceListener listener : listeners) {
            listener.onAppListUpdate(datas, type);
        }
    }

    private void onLoadStop() {
        Logger.d("加载完成");
        if (listeners == null||listeners.isEmpty()){
            return;
        }
        for (AppServiceListener listener : listeners) {
            listener.onLoadStop();
        }
    }

    private void unregisterReceiver() {
        if (receiver == null) {
            return;
        }
        context.unregisterReceiver(receiver);
        receiver = null;
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        if (receiver != null) {
            return;
        }
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String packageName = intent.getDataString();
                if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                    Logger.d("安装:" + packageName);
                    //读取这个包的信息
                } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                    Logger.d("卸载:" + packageName);
                    //删除这个包的信息
                }
                //需要重新加载
                reload();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        context.registerReceiver(receiver, filter);
    }
}
