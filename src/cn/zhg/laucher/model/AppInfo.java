/**
 * 
 * @author zhhaogen
 * 创建于 2018年4月30日 下午10:42:57
 */
package cn.zhg.laucher.model;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.pm.*; 
import android.graphics.drawable.Drawable;
import cn.zhg.laucher.util.SomeUtils;
import xiaogen.util.Logger;

/**
 * 
 *
 */
@SuppressWarnings("deprecation")
public class AppInfo extends BaseData
{ 
	/**
	 * 图标
	 */
	
	public Drawable icon;
	/**
	 * 图标
	 */
	
	public Drawable logo;
	public String name;
	
	public CharSequence label;
	
	public CharSequence description;
	public String packageName; 
	public int versionCode;
	public String versionName;
	public String signatureStr;
	public String firstInstallTimeStr;
	public long firstInstallTime;
	public String lastUpdateTimeStr;
	public long lastUpdateTime;
	public CharSequence author;
	public int uid;
	public String dataDir;
	public String sourceDir;
	public int targetSdkVersion; 
	public String processName;
	public String fileSizeStr;  
	public long fileSize;
	public boolean isSystemApp;
	public int flags; 
	public List<String> flagFields; 
	/**
	 * activity列表
	 */
	
	public List<AppActivityInfo>  activities; 
	
	public List<AppServiceInfo> services;
	
	public List<AppProviderInfo> providers;
	
	public List<AppPermissionInfo> requestedPermissions;
	
	public List<AppSignatureInfo> signatures;
	
	public List<AppPermissionInfo>  permissions; 
	
	public List<Integer>  gids; 
	
	public List<AppInstrumentationInfo>  instrumentations; 
	
	public List<AppActivityInfo>  receivers; 
	
	public List<String>	sharedLibraryFiles;
	public List<AppSourceInfo>	appSources;
	private PackageInfo target;
	public String appClassName; 
	public boolean hasLaunchIntent;
	public List<String> requestedPermissionLabels;
	/**
	 * 进行数据更新
	 * @param pm
	 */ 
	public void updateData(PackageManager pm)
	{
		if(dateloaded){
			return;
		}
		//清空内容
		activities.clear();
		services.clear();
		providers.clear();
		permissions.clear();
		signatures.clear();
		requestedPermissions.clear();
		requestedPermissionLabels.clear();
		gids.clear();
		instrumentations.clear();
		receivers.clear();
		sharedLibraryFiles.clear();
		try
		{ 
			this.hasLaunchIntent= pm.getLaunchIntentForPackage(packageName)!=null; 
		} catch (Exception ex)
		{
			hasLaunchIntent=false;
		}
		PackageInfo pp = null;
		try
		{
			pp = pm.getPackageInfo(packageName ,
					PackageManager.GET_ACTIVITIES | PackageManager.GET_RECEIVERS
							| PackageManager.GET_SERVICES
							| PackageManager.GET_PROVIDERS
							| PackageManager.GET_INSTRUMENTATION
							| PackageManager.GET_SIGNATURES
							| PackageManager.GET_META_DATA
							| PackageManager.GET_GIDS
							| PackageManager.GET_DISABLED_COMPONENTS
							| PackageManager.GET_SHARED_LIBRARY_FILES
							| PackageManager.GET_URI_PERMISSION_PATTERNS
							| PackageManager.GET_PERMISSIONS
							| PackageManager.GET_CONFIGURATIONS);
		} catch (Throwable e)
		{
			Logger.d(packageName + "读取详细内容错误:" + e.getMessage());
			// 由于PackageInfo太大导致异常,减少flags内容 
			tryUpdateData(pm);
			return;
		} 
		ApplicationInfo app = pp.applicationInfo;
		icon = app.loadIcon(pm);
		label = app.loadLabel(pm);
		description = app.loadDescription(pm);
		logo = app.loadLogo(pm);

		if (pp.activities != null)
		{
			for (ActivityInfo target : pp.activities)
			{
				activities.add(new AppActivityInfo(target, pm));
			}
		}
		if (pp.services != null)
		{
			for (ServiceInfo target : pp.services)
			{
				services.add(new AppServiceInfo(target, pm));
			}
		}
		if (pp.providers != null)
		{
			for (ProviderInfo target : pp.providers)
			{
				providers.add(new AppProviderInfo(target, pm));
			}
		}
		if (pp.requestedPermissions != null)
		{
			for (String target : pp.requestedPermissions)
			{
				requestedPermissions.add(new AppPermissionInfo(target, pm));
				requestedPermissionLabels.add(target);
			}
		}
		if (pp.permissions != null)
		{
			for (PermissionInfo target : pp.permissions)
			{
				permissions.add(new AppPermissionInfo(target, pm));
			}
		}
		if (pp.signatures != null)
		{
			for (Signature target : pp.signatures)
			{
				signatures.add(new AppSignatureInfo(target));
			}
		}
		if (pp.gids != null)
		{
			for (int target : pp.gids)
			{
				gids.add(target);
			}
		}
		if (pp.instrumentation != null)
		{
			for (InstrumentationInfo target : pp.instrumentation)
			{
				instrumentations.add(new AppInstrumentationInfo(target, pm));
			}
		}
		if (pp.receivers != null)
		{
			for (ActivityInfo target : pp.receivers)
			{
				receivers.add(new AppActivityInfo(target, pm));
			}
		}
		if (app.sharedLibraryFiles != null)
		{
			for (String target : app.sharedLibraryFiles)
			{
				sharedLibraryFiles.add(target);
			}
		}
		this.dateloaded = true;
	}
	/** 
	 * @see cn.zhg.manager.model.BaseData#updateData(java.lang.Object)
	 */
	public void updateData(Object context)
	{
		this.updateData((PackageManager)context);
	}
	/**
	 * 尝试更新数据
	 * @param pm
	 */
	private void tryUpdateData(PackageManager pm)
	{ 
		ApplicationInfo app = target.applicationInfo;
		icon = app.loadIcon(pm);
		label = app.loadLabel(pm);
		description = app.loadDescription(pm);
		logo = app.loadLogo(pm);
		
		PackageInfo pp = tryGetPackageInfo(pm,PackageManager.GET_ACTIVITIES); 
		if (pp!=null&&pp.activities != null)
		{
			for (ActivityInfo target : pp.activities)
			{
				activities.add(new AppActivityInfo(target, pm));
			}
		}
		
		pp = tryGetPackageInfo(pm,PackageManager.GET_SERVICES);
		if (pp!=null&&pp.services != null)
		{
			for (ServiceInfo target : pp.services)
			{
				services.add(new AppServiceInfo(target, pm));
			}
		}
		pp = tryGetPackageInfo(pm,PackageManager.GET_PROVIDERS);
		if (pp!=null&&pp.providers != null)
		{
			for (ProviderInfo target : pp.providers)
			{
				providers.add(new AppProviderInfo(target, pm));
			}
		}
		 
		pp = tryGetPackageInfo(pm,PackageManager.GET_PERMISSIONS);
		if (pp!=null&&pp.requestedPermissions != null)
		{
			for (String target : pp.requestedPermissions)
			{
				requestedPermissions.add(new AppPermissionInfo(target, pm));
				requestedPermissionLabels.add(target);
			}
		} 
		if (pp!=null&&pp.permissions != null)
		{
			for (PermissionInfo target : pp.permissions)
			{
				permissions.add(new AppPermissionInfo(target, pm));
			}
		}
		pp = tryGetPackageInfo(pm,PackageManager.GET_SIGNATURES);
		if (pp!=null&&pp.signatures != null)
		{
			for (Signature target : pp.signatures)
			{
				signatures.add(new AppSignatureInfo(target));
			}
		}
		
		pp = tryGetPackageInfo(pm,PackageManager.GET_GIDS);
		if (pp!=null&&pp.gids != null)
		{
			for (int target : pp.gids)
			{
				gids.add(target);
			}
		}
		
		pp = tryGetPackageInfo(pm,PackageManager.GET_INSTRUMENTATION);
		if (pp!=null&&pp.instrumentation != null)
		{
			for (InstrumentationInfo target : pp.instrumentation)
			{
				instrumentations.add(new AppInstrumentationInfo(target, pm));
			}
		}
		pp = tryGetPackageInfo(pm,PackageManager.GET_RECEIVERS);
		if (pp!=null&&pp.receivers != null)
		{
			for (ActivityInfo target : pp.receivers)
			{
				receivers.add(new AppActivityInfo(target, pm));
			}
		}
		pp = tryGetPackageInfo(pm,PackageManager.GET_SHARED_LIBRARY_FILES);
		if (pp!=null&&app.sharedLibraryFiles != null)
		{
			for (String target : app.sharedLibraryFiles)
			{
				sharedLibraryFiles.add(target);
			}
		} 
		this.dateloaded = true;
	}
	/**
	 * @param flags
	 * @return
	 */
	private PackageInfo tryGetPackageInfo(PackageManager pm,int flags)
	{
		try
		{
			PackageInfo pp = pm.getPackageInfo(packageName , flags);
			return pp;
		} catch (Throwable e)
		{
			Logger.d(packageName + "读取"+flags+"详细内容错误:" + e.getMessage());  
		}
		return null;
	}
	public AppInfo( final PackageInfo pp)
	{    
		updateBaseData(pp);
		
		ApplicationInfo app = pp.applicationInfo;
		activities = new ArrayList<>();
		services = new ArrayList<>();
		providers = new ArrayList<>();
		requestedPermissions = new ArrayList<>();
		requestedPermissionLabels= new ArrayList<>();
		permissions = new ArrayList<>();
		signatures = new ArrayList<>();
		gids = new ArrayList<>();
		instrumentations = new ArrayList<>();
		receivers = new ArrayList<>();
		sharedLibraryFiles = new ArrayList<>();
		
		if (pp.activities != null)
		{
			for (ActivityInfo target : pp.activities)
			{
				activities.add(new AppActivityInfo(target));
			}
		}
		if (pp.services != null)
		{
			for (ServiceInfo target : pp.services)
			{
				services.add(new AppServiceInfo(target));
			}
		}
		if (pp.providers != null)
		{
			for (ProviderInfo target : pp.providers)
			{
				providers.add(new AppProviderInfo(target));
			}
		}
		if (pp.requestedPermissions != null)
		{
			for (String target : pp.requestedPermissions)
			{
				requestedPermissions.add(new AppPermissionInfo(target));
				requestedPermissionLabels.add(target);
			}
		}
		if (pp.permissions != null)
		{
			for (PermissionInfo target : pp.permissions)
			{
				permissions.add(new AppPermissionInfo(target));
			}
		}
		if (pp.signatures != null)
		{
			for (Signature target : pp.signatures)
			{
				signatures.add(new AppSignatureInfo(target));
			}
		}
		if (pp.gids != null)
		{
			for (int target : pp.gids)
			{
				gids.add(target);
			}
		}
		if (pp.instrumentation != null)
		{
			for (InstrumentationInfo target : pp.instrumentation)
			{
				instrumentations.add(new AppInstrumentationInfo(target));
			}
		}
		if (pp.receivers != null)
		{ 
			for (ActivityInfo target : pp.receivers)
			{
				receivers.add(new AppActivityInfo(target));
			}
		}
	
		if (app.sharedLibraryFiles != null)
		{
			for (String target : app.sharedLibraryFiles)
			{
				sharedLibraryFiles.add(target);
			}
		} 
	}

	/**
	 * 获取基础数据
	 */
	private void updateBaseData(final PackageInfo pp)
	{
		this.target=pp;
		ApplicationInfo app = pp.applicationInfo;
		versionCode = pp.versionCode;
		versionName = pp.versionName; 
		name = app.name;
		appClassName=app.className;
		uid = app.uid;  
		packageName = pp.packageName;
		flags = app.flags;
		isSystemApp = ((app.flags
				& ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
				|| ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
		flagFields = spiltFlags(flags);
		dataDir = app.dataDir;
		targetSdkVersion = app.targetSdkVersion;
		processName = app.processName; 
		this.firstInstallTime = pp.firstInstallTime;
		this.lastUpdateTime = pp.lastUpdateTime;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINESE);
		firstInstallTimeStr = format.format(new Date(pp.firstInstallTime));
		lastUpdateTimeStr = format.format(new Date(pp.lastUpdateTime));
		sourceDir = app.sourceDir;
		if (sourceDir != null)
		{
			fileSize = new File(sourceDir).length();
			fileSizeStr = SomeUtils.getFileSize(fileSize);
		}
		appSources = new ArrayList<>();
		if (app.splitSourceDirs != null)
		{
			if (android.os.Build.VERSION.SDK_INT >= 26)
			{
				for (int i = 0; i < app.splitSourceDirs.length; i++)
				{
					appSources.add(new AppSourceInfo(app.splitNames[i],
							app.splitSourceDirs[i],
							app.splitPublicSourceDirs[i]));
				}
			} else
			{
				for (int i = 0; i < app.splitSourceDirs.length; i++)
				{
					appSources.add(new AppSourceInfo(pp.splitNames[i],
							app.splitSourceDirs[i],
							app.splitPublicSourceDirs[i]));
				}
			}
		} 
	}
	/**
	 * 转换flags
	 * @param i
	 * @return
	 */
	private static List<String> spiltFlags(int i)
	{
		Field[] fs = ApplicationInfo.class.getFields();
		List<String> result=new ArrayList<>();
		for(Field f:fs)
		{
			if(f.getName().startsWith("FLAG_"))
			{
				try
				{
					int l=f.getInt(null);
					if((l&i)!=0)
					{
						result.add(f.getName());
					}
				} catch ( Exception e)
				{ 
				}
			}
		}
		return result;
	}
}
