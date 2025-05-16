package cn.zhg.launcher.service;

import java.util.*;

import cn.zhg.launcher.model.AppInfo;

/**
 * app服务监听器,监听异步任务状态和app列表更新通知
 */
public interface AppServiceListener {
    /**
     * 数据更新类型:简单内容
     */
    byte TYPE_UPDATE_SIMPLE=1;
    /**
     * 数据更新类型:详细内容
     */
    byte TYPE_UPDATE_DETAIL=2;
    /**
     * 数据更新类型:manifest.xml内容
     */
    byte TYPE_UPDATE_XML=3;
    /**
     * 数据已经停止加载
     */
    void onLoadStop();

    /**
     * 数据开始加载
     * @param size 数据大小
     */
    void onLoadStart(int size);
    /**
     * 数据列表更新
     * @param type  数据更新类型
     */
    void onAppListUpdate(List<AppInfo> datas,byte type);
    /**
     * 数据更新
     * @param type  数据更新类型
     * @param index  位置序号
     */
    void onAppUpdate(AppInfo data,byte type,int index);
}
