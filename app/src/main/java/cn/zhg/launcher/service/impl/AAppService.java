package cn.zhg.launcher.service.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.*;

import cn.zhg.launcher.model.AppInfo;
import cn.zhg.launcher.service.AppServiceListener;
import cn.zhg.launcher.service.IAppService;

/**
 * 回调返回在ui线程中
 */
public class AAppService implements IAppService {
    private static final byte onLoadStart = 20;
    private static final byte onLoadStop = 21;
    private static final byte onAppListUpdate = 22;
    private static final byte onAppUpdate = 23;
    /**
     * 代理对象
     */
    private final AppService service;
    private static AAppService ins;
    private final List<AppServiceListener> listeners;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case onLoadStart: {
                    int size = (int) msg.obj;
                    for (AppServiceListener listener : listeners) {
                        listener.onLoadStart(size);
                    }
                    break;
                }
                case onLoadStop: {
                    for (AppServiceListener listener : listeners) {
                        listener.onLoadStop();
                    }
                    break;
                }
                case onAppListUpdate: {
                    Object[] arr = (Object[]) msg.obj;
                    //复制一份
                    List<AppInfo> datas= new ArrayList<>((List<AppInfo>) arr[0]);
                    byte type = (byte) arr[1];
                    for (AppServiceListener listener : listeners) {
                        listener.onAppListUpdate(datas, type);
                    }
                    break;
                }
                case onAppUpdate: {
                    Object[] arr = (Object[]) msg.obj;
                    AppInfo data = (AppInfo) arr[0];
                    byte type = (byte) arr[1];
                    int index = (int) arr[2];
                    for (AppServiceListener listener : listeners) {
                        listener.onAppUpdate(data, type, index);
                    }
                    break;
                }
            }
        }
    };
    private  final AppServiceListener innerListener=new AppServiceListener() {
        public void onLoadStop() {
            Message msg = Message.obtain();
            msg.what = onLoadStop;
            handler.sendMessage(msg);
        }
        public void onLoadStart(int size) {
            Message msg = Message.obtain();
            msg.what = onLoadStart;
            msg.obj = size;
            handler.sendMessage(msg);
        }
        public void onAppListUpdate(List<AppInfo> datas, byte type) {
            Message msg = Message.obtain();
            msg.what = onAppListUpdate;
            msg.obj = new Object[]{datas,type};
            handler.sendMessage(msg);
        }

        public void onAppUpdate(AppInfo data, byte type, int index) {
            Message msg = Message.obtain();
            msg.what = onAppUpdate;
            msg.obj = new Object[]{data, type, index};
            handler.sendMessage(msg);
        }
    };

    private AAppService(Context context) {
        service= new AppService(context);
        listeners = new ArrayList<>();
        service.addListener(innerListener);
    }

    /**
     * 返回单例
     */
    public static AAppService getInstance(Context context) {
        if (ins != null) {
            return ins;
        }
        ins = new AAppService(context);
        return ins;
    }

    public void addListener(AppServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AppServiceListener listener) {
        listeners.remove(listener);
    }

    public byte getState() {
        return service.getState();
    }

    public void reload() {
        service.reload();
    }

    public List<AppInfo> getSimpleList() {
        return service.getSimpleList();
    }

    public List<AppInfo> getList() {
        return service.getList();
    }

    public void uninstall(String packageName) {
        service.uninstall(packageName);
    }

    public void launch(String packageName) {
        service.launch(packageName);
    }

    public void openSetting() {
        service.openSetting();
    }

    public void openSetting(String packageName) {
        service.openSetting(packageName);
    }

    public void stop() {
        service.stop();
    }
}
