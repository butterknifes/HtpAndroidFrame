package com.example.htp;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.example.htp.interf.ICallbackResult;
import com.example.htp.service.DownloadService;
import com.example.htp.ui.activity.ImagePreviewActivity;
import com.example.htp.utils.DialogHelp;
import com.example.htp.utils.StringUtils;
import com.example.htp.utils.URLsUtils;
import com.htp.zxing.activity.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 界面帮助类
 * 
 * @author htp
 * @version
 */
public class UIHelper {

	/**
	 * 浏览图片
	 * 
	 * @param position
	 * @param images
	 */
	public static void imageBrower(int position, List<String> images,
			Context context) {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(context, ImagePagerActivity.class);
//		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
//		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
//				(ArrayList<String>) images);
//		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//		context.startActivity(intent);
	}

	/** 全局web样式 */
	// 链接样式文件，代码块高亮的处理
	public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/detail_page.js\"></script>"
			+ "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
			+ "<script type=\"text/javascript\">function showImagePreview(var url){window.location.url= url;}</script>"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/common.css\">";
	public final static String WEB_STYLE = linkCss;

	public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";

	private static final String SHOWIMAGE = "ima-api:action=showImage&data=";

	/**
	 * 发送App异常崩溃报告
	 *
	 * @param context
	 * @param crashReport
	 */
	public static void sendAppCrashReport(final Context context) {

		DialogHelp.getConfirmDialog(context, "程序发生异常", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				// 退出
				System.exit(-1);
			}
		}).show();
	}

	/**
	 * 显示登录界面
	 *
	 * @param context
	 */
	public static void showLoginActivity(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}


	public static void openDownLoadService(Context context, String downurl,
										   String tilte) {
		final ICallbackResult callback = new ICallbackResult() {

			@Override
			public void OnBackResult(Object s) {}
		};
		ServiceConnection conn = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
				binder.addCallback(callback);
				binder.start();

			}
		};
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
		intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
		context.startService(intent);
		context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	/**
	 * url跳转
	 *
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url) {
		if (url == null)
			return;
		if (url.contains("city.oschina.net/")) {
			int id = StringUtils.toInt(url.substring(url.lastIndexOf('/') + 1));
		//	UIHelper.showEventDetail(context, id);
			return;
		}

		if (url.startsWith(SHOWIMAGE)) {
			String realUrl = url.substring(SHOWIMAGE.length());
			try {
				JSONObject json = new JSONObject(realUrl);
				int idx = json.optInt("index");
				String[] urls = json.getString("urls").split(",");
				showImagePreview(context, idx, urls);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return;
		}
		URLsUtils urls = URLsUtils.parseURL(url);
		if (urls != null) {
			showLinkRedirect(context, urls.getObjType(), urls.getObjId(),
					urls.getObjKey());
		} else {
			openBrowser(context, url);
		}
	}

	public static void showLinkRedirect(Context context, int objType,
										int objId, String objKey) {
		switch (objType) {
			case URLsUtils.URL_OBJ_TYPE_NEWS:
			//	showNewsDetail(context, objId, -1);
				break;
			case URLsUtils.URL_OBJ_TYPE_QUESTION:
			//	showPostDetail(context, objId, 0);
				break;
			case URLsUtils.URL_OBJ_TYPE_QUESTION_TAG:
			//	showPostListByTag(context, objKey);
				break;
			case URLsUtils.URL_OBJ_TYPE_SOFTWARE:
			//	showSoftwareDetail(context, objKey);
				break;
			case URLsUtils.URL_OBJ_TYPE_ZONE:
			//	showUserCenter(context, objId, objKey);
				break;
			case URLsUtils.URL_OBJ_TYPE_TWEET:
			//	showTweetDetail(context, null, objId);
				break;
			case URLsUtils.URL_OBJ_TYPE_BLOG:
			//	showBlogDetail(context, objId, 0);
				break;
			case URLsUtils.URL_OBJ_TYPE_OTHER:
				openBrowser(context, objKey);
				break;
			case URLsUtils.URL_OBJ_TYPE_TEAM:
				openSysBrowser(context, objKey);
				break;
			case URLsUtils.URL_OBJ_TYPE_GIT:
				openSysBrowser(context, objKey);
				break;
		}
	}

	/**
	 * 打开内置浏览器
	 *
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url) {

		if (StringUtils.isImgUrl(url)) {
			ImagePreviewActivity.showImagePrivew(context, 0,
					new String[] { url });
			return;
		}

//		if (url.startsWith("http://www.oschina.net/tweet-topic/")) {
//			Bundle bundle = new Bundle();
//			int i = url.lastIndexOf("/");
//			if (i != -1) {
//				bundle.putString("topic",
//						URLDecoder.decode(url.substring(i + 1)));
//			}
//			UIHelper.showSimpleBack(context, SimpleBackPage.TWEET_TOPIC_LIST,
//					bundle);
//			return;
//		}
//		try {
//			// 启用外部浏览器
//			// Uri uri = Uri.parse(url);
//			// Intent it = new Intent(Intent.ACTION_VIEW, uri);
//			// context.startActivity(it);
//			Bundle bundle = new Bundle();
//			bundle.putString(BrowserFragment.BROWSER_KEY, url);
//			showSimpleBack(context, SimpleBackPage.BROWSER, bundle);
//		} catch (Exception e) {
//			e.printStackTrace();
//			AppContext.showToastShort("无法浏览此网页");
//		}
	}

	/**
	 * 打开系统中的浏览器
	 *
	 * @param context
	 * @param url
	 */
	public static void openSysBrowser(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			AppContext.showToastShort("无法浏览此网页");
		}
	}

	@JavascriptInterface
	public static void showImagePreview(Context context, String[] imageUrls) {
		ImagePreviewActivity.showImagePrivew(context, 0, imageUrls);
	}

	@JavascriptInterface
	public static void showImagePreview(Context context, int index,
										String[] imageUrls) {
		ImagePreviewActivity.showImagePrivew(context, index, imageUrls);
	}

//	public static void showSimpleBackForResult(Fragment fragment,
//											   int requestCode, SimpleBackPage page, Bundle args) {
//		Intent intent = new Intent(fragment.getActivity(),
//				SimpleBackActivity.class);
//		intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
//		intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
//		fragment.startActivityForResult(intent, requestCode);
//	}
//
//	public static void showSimpleBackForResult(Activity context,
//											   int requestCode, SimpleBackPage page, Bundle args) {
//		Intent intent = new Intent(context, SimpleBackActivity.class);
//		intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
//		intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
//		context.startActivityForResult(intent, requestCode);
//	}
//
//	public static void showSimpleBackForResult(Activity context,
//											   int requestCode, SimpleBackPage page) {
//		Intent intent = new Intent(context, SimpleBackActivity.class);
//		intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
//		context.startActivityForResult(intent, requestCode);
//	}
//
//	public static void showSimpleBack(Context context, SimpleBackPage page) {
//		Intent intent = new Intent(context, SimpleBackActivity.class);
//		intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
//		context.startActivity(intent);
//	}
//
//	public static void showSimpleBack(Context context, SimpleBackPage page,
//									  Bundle args) {
//		Intent intent = new Intent(context, SimpleBackActivity.class);
//		intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
//		intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
//		context.startActivity(intent);
//	}


	/**
	 * 显示扫一扫界面
	 *
	 * @param context
	 */
	public static void showScanActivity(Context context) {
		Intent intent = new Intent(context, CaptureActivity.class);
		context.startActivity(intent);
	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	public static void initWebView(WebView webView) {
		WebSettings settings = webView.getSettings();
		settings.setDefaultFontSize(15);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		int sysVersion = Build.VERSION.SDK_INT;
		if (sysVersion >= 11) {
			settings.setDisplayZoomControls(false);
		} else {
			ZoomButtonsController zbc = new ZoomButtonsController(webView);
			zbc.getZoomControls().setVisibility(View.GONE);
		}
		webView.setWebViewClient(UIHelper.getWebViewClient());
	}

	/**
	 * 添加网页的点击图片展示支持
	 */
//	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
//	@JavascriptInterface
//	public static void addWebImageShow(final Context cxt, WebView wv) {
//		wv.getSettings().setJavaScriptEnabled(true);
//		wv.addJavascriptInterface(new OnWebViewImageListener() {
//			@Override
//			@JavascriptInterface
//			public void showImagePreview(String bigImageUrl) {
//				if (bigImageUrl != null && !StringUtils.isEmpty(bigImageUrl)) {
//					UIHelper.showImagePreview(cxt, new String[] { bigImageUrl });
//				}
//			}
//		}, "mWebViewImageListener");
//	}

	/**
	 * 获取webviewClient对象
	 *
	 * @return
	 */
	public static WebViewClient getWebViewClient() {

		return new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}


}
