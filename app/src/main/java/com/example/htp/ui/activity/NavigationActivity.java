package com.example.htp.ui.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.htp.AppConfig;
import com.example.htp.AppContext;
import com.example.htp.adapter.NavDrawerListAdapter;
import com.example.htp.bean.NavDrawerItem;
import com.example.htp.AppManager;
import com.example.htp.ui.fragment.NavigationFragment;
import com.example.htp.utils.DeviceInfo;
import com.example.htp.htpandroidframe.R;
import com.example.htp.utils.DoubleClickExitHelper;
import com.example.htp.utils.UpdateManager;
import com.example.htp.widget.ActionBarDrawerToggle;
import com.example.htp.widget.DrawerArrowDrawable;


public class NavigationActivity extends AppCompatActivity {
	public String TAG = "NavigationActivity";
	private DoubleClickExitHelper mDoubleClickExit;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private NavDrawerListAdapter adapter;
	private ListView mDrawerList;
	private int curItem;
	private DrawerArrowDrawable drawerArrow;
	private String title = "我的应用";
	private ArrayList<CloseSoftKeyboardListener> myListeners = new ArrayList<CloseSoftKeyboardListener>();
	private String switcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		mDoubleClickExit = new DoubleClickExitHelper(this);
		setActionBarStyle();
		initView();
		AppManager.getAppManager().addActivity(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.content_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
				closeSoftKeyboard();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// switcher = MobclickAgent.getConfigParams(this, "news_switcher");

		if ("".equals(switcher)) {
			switcher = "no";
		}
		initLiftView();

		if (savedInstanceState == null) {
			if ("no".equals(switcher)) {
				displayView(1);
			} else {
				displayView(0);
			}
		}

		// 版本更新
		//updVersion();
	}

	public void initView()
	{


	}
	private void checkUpdate() {
		if (!AppContext.get(AppConfig.KEY_CHECK_UPDATE, true)) {
			return;
		}
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				new UpdateManager(NavigationActivity.this, false).checkUpdate();
			}
		}, 2000);
	}
	/**
	 * 版本更新
	 */
	private void updVersion() {

		// 获取当前客户端版本号
		int verCode = 1;
		try {
			verCode = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			verCode = 1;
			Log.e(TAG, "版本号获取失败: " + e.getMessage());
		}
	}

		// 获取版本信息
//		VersionApi api = new VersionApi(String.valueOf(verCode), "android") {
//
//			@Override
//			public void success(ClientVersion version) {
//				Log.i(TAG, version.toString());
//				// 弹出提示框
//				downloadNewVersion(version);
//			}
//
//			/*
//			 * (non-Javadoc)
//			 *
//			 * @see com.juguo.api.JuguoRequest#error(java.lang.String)
//			 */
//			@Override
//			public void error(String error) {
//				Log.e(TAG, error);
//			}
//		};
//
//		AppContext.getInstance().addRequest(api, TAG);
//		AppContext.getInstance().getRequestQueue().start();
//	}

	/**
	 * 提示版本更新
	 */
//	private void downloadNewVersion(final ClientVersion versionInfo) {
//		Log.i(TAG, "downloadNewVersion 开始下载新版本");
//		try {
//			Log.i(TAG, "url:" + Uri.parse(versionInfo.getUpdUrl()));
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.i(TAG, "url无效");
//		}
//
//		Dialog dialog = new AlertDialog.Builder(this)
//				.setTitle("版本更新")
//				.setMessage(versionInfo.getUpdDesp())
//				// 设置内容
//				.setPositiveButton("更新", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// 下载最新版本
//						try {
//							startActivity(new Intent(Intent.ACTION_VIEW, Uri
//									.parse(versionInfo.getUpdUrl())));
//							Log.i("htp", versionInfo.getUpdUrl());
//						} catch (Exception e) {
//							// TODO: handle exception
//							AppContext.showToast("未知错误，更新失败");
//						}
//
//					}
//				})
//				.setNegativeButton("暂不更新",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//							}
//						}).create();
//		// 显示对话框
//		dialog.show();
//	}

	private void initLiftView() {
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		adapter = new NavDrawerListAdapter(getApplicationContext(), getData(),
				switcher);
		// View headView = LayoutInflater.from(this).inflate(R.layout.head_view,
		// null);
		// mDrawerList.addHeaderView(headView);
		mDrawerList.setAdapter(adapter);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == curItem)
				return;
			displayView(position);
		}
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		if (!DeviceInfo.isNetworkAvailable(this)) {
			Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
		}
		switch (position) {
		case 0:
			curItem = 0;
			title = "我的主页";
			fragment = new NavigationFragment();
			break;
		case 1:
			curItem = 1;
			title = "我的空间";
			// fragment = new NewsFragment();
		//	UIHelper.showVoiceMsgListActivity(ContentActivity.this);
			break;
		case 2:
			curItem = 2;
			title = "我的收藏";
		//	UIHelper.showVoiceMsgListActivity(ContentActivity.this);
			// fragment = new NewsFragment();
			break;
		case 3:
			curItem = 3;
			title = "更多";
			// fragment = new BeautyMainFragment();
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.commitAllowingStateLoss();
			this.getSupportActionBar().setTitle(title);
		}
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean isDrawerOper = mDrawerLayout.isDrawerOpen(mDrawerList);
		if (isDrawerOper) {
			this.getSupportActionBar().setTitle("我的应用");
		} else {
			this.getSupportActionBar().setTitle(title);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {

				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about: {

			break;
		}
		// 版本更新
		case R.id.action_updata: {
			// 检查更新
			updVersion();
			break;
		}
		default:
			break;
		}

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private ArrayList<NavDrawerItem> getData() {
		ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
		TypedArray navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);
		String[] navMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items);

		for (int i = 0; i < navMenuIcons.length(); i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}
		// Recycle the typed array
		navMenuIcons.recycle();

		return navDrawerItems;
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void setActionBarStyle() {

		getSupportActionBar().setBackgroundDrawable(
				this.getBaseContext().getResources()
						.getDrawable(R.drawable.actionbar_back));
		getSupportActionBar().setIcon(R.color.transparent);
		// getActionBar().setIcon(R.drawable.ic_action);
		// getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
//		TextView textView = (TextView)findViewById(titleId);
//		// textView.setTypeface(Typeface.createFromAsset(getAssets(),
//		// "font/Wendy.ttf"));
//		textView.setTextColor(this.getBaseContext().getResources()
//				.getColor(R.color.white));
//		textView.setTextSize(17);
		// textView.setPadding(15, 0, 0, 0);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	/**
	 * 连续按两次返回键就退出
	 */
	private int keyBackClickCount = 0;

	@Override
	protected void onResume() {
		super.onResume();
		keyBackClickCount = 0;
	}

	public void onPause() {
		super.onPause();
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			switch (keyBackClickCount++) {
//			case 0:
//				Toast.makeText(this,
//						getResources().getString(R.string.press_again_exit),
//						Toast.LENGTH_SHORT).show();
//				Timer timer = new Timer();
//				timer.schedule(new TimerTask() {
//					@Override
//					public void run() {
//						keyBackClickCount = 0;
//					}
//				}, 3000);
//				break;
//			case 1:
//				// this.finish();
//				setResult(1);
//				this.finish();
//			//	AppManager.getAppManager().AppExit(this);
//				break;
//			default:
//				break;
//			}
//			return true;
//		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
//			// if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
//			// mDrawerLayout.closeDrawer(mDrawerList);
//			// } else {
//			// mDrawerLayout.openDrawer(mDrawerList);
//			// }
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	/**
	 * 监听返回--是否退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 是否退出应用
			if (AppContext.get(AppConfig.KEY_DOUBLE_CLICK_EXIT, true)) {
				return mDoubleClickExit.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/*------------  侧边栏打开后 关闭所有的软键盘  ------------*/

	public interface CloseSoftKeyboardListener {
		public void onCloseListener();
	}

	public void registerMyTouchListener(CloseSoftKeyboardListener listener) {
		myListeners.add(listener);
	}

	public void unRegisterMyTouchListener(CloseSoftKeyboardListener listener) {
		myListeners.remove(listener);
	}

	public void closeSoftKeyboard() {
		for (CloseSoftKeyboardListener listener : myListeners) {
			listener.onCloseListener();
		}
	}
}
