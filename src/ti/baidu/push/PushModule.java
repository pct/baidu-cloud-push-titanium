/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.baidu.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Process;
import android.text.TextUtils;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.silentupdate.SilentManager;

@Kroll.module(name = "Push", id = "ti.baidu.push")
public class PushModule extends KrollModule {

	private static PushModule _THIS;

	// Standard Debugging variables
	private static final String TAG = "PushModule";
	// 开发者中心获取的API Key
	private static final String API_KEY = "wMXxstDL8nt7w87MMtdT7woL";

	private KrollFunction successCallback;
	private KrollFunction errorCallback;
	private KrollFunction messageCallback;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;

	public PushModule() {
		super();
		_THIS = this;
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(TAG, "inside onAppCreate");
		// put module init code that needs to run when the application is
		// created

		// 初始化百度推送App
		initFrontiaApplication(app.getApplicationContext());
	}

	// Methods
	@Kroll.method
	public String example() {
		Log.d(TAG, "example called");
		return "hello world";
	}

	// Properties
	@Kroll.getProperty
	public String getExampleProp() {
		Log.d(TAG, "get example property");
		return "hello world";
	}

	@Kroll.setProperty
	public void setExampleProp(String value) {
		Log.d(TAG, "set example property: " + value);
	}

	@Kroll.method(name = "registerBcm")
	public void registerBcm(HashMap options) {
		Log.d(TAG, "registerC2dm called");

		successCallback = (KrollFunction) options.get("success");
		errorCallback = (KrollFunction) options.get("error");
		messageCallback = (KrollFunction) options.get("callback");

		TiApplication app = TiApplication.getInstance();
		// String android_id = Secure.getString(app.getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		PushManager.startWork(app.getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, API_KEY);
	}

	/**
	 * 绑定到Baidu时成功
	 * 
	 * @param data
	 * 
	 * data中的字段(绑定成功后，百度返回的):
	 * errorCode, appid, userId, channelId, requestId
	 */
	public void sendSuccess(HashMap registration) {
		if (successCallback != null) {
			HashMap data = new HashMap();
			data.put("data", registration);
			successCallback.callAsync(getKrollObject(), data);
		}
	}

	/**
	 * 绑定到Baidu时出错
	 * 
	 * @param error
	 */
	public void sendError(String error) {
		if (errorCallback != null) {
			HashMap data = new HashMap();
			data.put("error", error);

			errorCallback.callAsync(getKrollObject(), data);
		}
	}

	/**
	 * 发送推送消息时，将接受到的消息反馈到前台
	 * 
	 * @param messageData
	 */
	public void sendMessage(HashMap messageData) {
		if (messageCallback != null) {
			HashMap data = new HashMap();
			data.put("data", messageData);

			messageCallback.call(getKrollObject(), data);
		}
	}

	@Kroll.method(name = "unregisterBcm")
	public void unregisterBcm() {
		TiApplication app = TiApplication.getInstance();

		Log.d(TAG, "unregister from 'Baidu Push'...");
		PushManager.stopWork(app.getApplicationContext());
	}


	public static void initFrontiaApplication(Context context) {
		if (d(context)) {
			boolean flag = a(context);
			if (!flag)
				b(context);
		} else {
			b(context);
		}
	}

	private static boolean a(Context context) {
		boolean flag = true;
		try {
			ActivityManager activitymanager = (ActivityManager) context
					.getSystemService("activity");
			int i = Process.myPid();
			Iterator iterator = activitymanager.getRunningAppProcesses()
					.iterator();
			do {
				if (!iterator.hasNext())
					break;
				android.app.ActivityManager.RunningAppProcessInfo runningappprocessinfo = (android.app.ActivityManager.RunningAppProcessInfo) iterator
						.next();
				if (runningappprocessinfo.pid == i) {
					String s = c(context);
					if (runningappprocessinfo.processName.equalsIgnoreCase(s)) {
						boolean flag1 = b(context.getApplicationContext());
						if (!flag1)
							Process.killProcess(i);
					}
				}
			} while (true);
		} catch (Exception exception) {
			flag = false;
			exception.printStackTrace();
		}
		return flag;
	}

	private static boolean b(Context context) {
		boolean flag = false;
		try {
			SilentManager
					.setKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYAFbG0oYmKgh6o7BhZIHf1njBpZXqyWBnYz2ip3Wp+s97OeA/pTe8xebuGJHwq4xbsGQrJWepIbUVrdjm6JRmdvuJhar7/hC/UNnUkJgYdYl10OZKlvcFFgK3V7XGBPplXldDnhbgscna3JG8U3025WSxZCP5vy/8cfxsEoVx5QIDAQAB");
			SilentManager.enableRSA(true);
			flag = SilentManager.loadLib(context.getApplicationContext(),
					"frontia_plugin", "plugin-deploy.jar");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return flag;
	}

	private static String c(Context context) {
		PackageManager packagemanager = context.getPackageManager();
		ServiceInfo aserviceinfo[] = null;
		try {
			aserviceinfo = packagemanager.getPackageInfo(
					context.getPackageName(), 4).services;
		} catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) {
			namenotfoundexception.printStackTrace();
		}
		if (aserviceinfo == null)
			return null;
		for (int i = 0; i < aserviceinfo.length; i++)
			if ("com.baidu.android.pushservice.PushService"
					.equals(aserviceinfo[i].name))
				return aserviceinfo[i].processName;

		return null;
	}

	private static boolean d(Context context) {
		boolean flag = false;
		try {
			InputStream inputstream = context.getAssets().open(
					"frontia_plugin/plugin-deploy.key");
			InputStreamReader inputstreamreader = new InputStreamReader(
					inputstream);
			BufferedReader bufferedreader = new BufferedReader(
					inputstreamreader);
			String s = "";
			for (String s1 = ""; (s1 = bufferedreader.readLine()) != null;)
				s = (new StringBuilder()).append(s).append(s1).append("\r\n")
						.toString();

			String s2 = SilentManager
					.decrypt(
							"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYAFbG0oYmKgh6o7BhZIHf1njBpZXqyWBnYz2ip3Wp+s97OeA/pTe8xebuGJHwq4xbsGQrJWepIbUVrdjm6JRmdvuJhar7/hC/UNnUkJgYdYl10OZKlvcFFgK3V7XGBPplXldDnhbgscna3JG8U3025WSxZCP5vy/8cfxsEoVx5QIDAQAB",
							s);
			if (!TextUtils.isEmpty(s2)) {
				JSONObject jsonobject = new JSONObject(s2);
				flag = jsonobject.optString("flag", "null").equals(
						"f5de4bda49c00a19757289cd02a60f5d");
			}
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		} catch (JSONException jsonexception) {
			jsonexception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return flag;
	}

	private static final String a = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYAFbG0oYmKgh6o7BhZIHf1njBpZXqyWBnYz2ip3Wp+s97OeA/pTe8xebuGJHwq4xbsGQrJWepIbUVrdjm6JRmdvuJhar7/hC/UNnUkJgYdYl10OZKlvcFFgK3V7XGBPplXldDnhbgscna3JG8U3025WSxZCP5vy/8cfxsEoVx5QIDAQAB";
	private static final String b = "f5de4bda49c00a19757289cd02a60f5d";
	private static final String c = "com.baidu.android.pushservice.PushService";

	public static PushModule getInstance() {
		return _THIS;
	}
}
