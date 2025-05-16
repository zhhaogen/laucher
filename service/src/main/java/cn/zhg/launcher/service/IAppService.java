package cn.zhg.launcher.service;

import java.util.*;

import cn.zhg.launcher.model.*;

/**
 * app服务接口
 */
public interface IAppService {
    /**
     * 状态:未加载
     */
    byte STATE_UNLOAD=0;
    /**
     * 状态:加载简单
     */
    byte STATE_LOADING_SIMPLE=1;
    /**
     * 状态:加载详细
     */
    byte STATE_LOADING_DETAIL=2;
    /**
     * 状态:加载manifest.xml内容
     */
    byte STATE_LOADING_XML=3;
    /**
     * 状态:已经加载完成
     */
    byte STATE_LOADED=4;
    /**
     * 状态:正在停止
     */
    byte STATE_STOPING=5;
    /**
     * 状态:正在重启数据
     */
    byte STATE_RELOADING=6;
    /**
     * 数据加载状态
     */
    byte getState();

    /**
     * 停止正在加载的任务，重新加载数据
     */
    void reload();
    /**
     * 获取简要的app列表,同步的
     */
    List<AppInfo> getSimpleList();
    /**
     * 获取当前app列表
     */
    List<AppInfo> getList();
    /**
     * 卸载某个app
     * @param packageName 完整包名
     */
    void uninstall(String packageName);

    /**
     * 卸载某个app
     */
    default void uninstall(AppInfo item) {
        uninstall(item.packageName);
    }
    /**
     * 打开某个app
     * @param packageName 完整包名
     */
    void launch(String packageName);

    /**
     * 打开某个app
     */
    default void launch(AppInfo item) {
        launch(item.packageName);
    }

    /**
     * 打开系统设置
     */
    void openSetting();
    /**
     * 打开某个app的系统设置
     */
    void openSetting(String packageName);
    /**
     * 打开某个app的系统设置
     */
    default void openSetting(AppInfo item) {
        openSetting(item.packageName);
    }
    /**
     * 停止异步任务
     */
    void stop();
    /**
     * 添加监听器
     */
    void addListener(AppServiceListener listener);
    /**
     * 移除监听器
     */
    void removeListener(AppServiceListener listener);
}
