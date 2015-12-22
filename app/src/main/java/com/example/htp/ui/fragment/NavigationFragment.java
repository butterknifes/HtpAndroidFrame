package com.example.htp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.htp.animation.ZoomOutPageTransformer;
import com.example.htp.htpandroidframe.R;
import com.example.htp.widget.PagerSlidingTabStrip;

public class NavigationFragment extends Fragment {

	private ViewPager contentPager;
	private mPagerAdapter adapter;
	private PagerSlidingTabStrip tabs;
	
	// 悬浮按钮
	// 语音通知
	private View actionSend;
	// 基站发布
	private View actionReceive;
	// 多人通话
	private View actionTalks;
//	private FloatingActionsMenu rightLabels;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.navigation_viewpager, null);
		setPager(rootView);
		setListener();
		return rootView;
	}

	private void setPager(View rootView) {
		contentPager = (ViewPager) rootView.findViewById(R.id.content_pager);
		contentPager.setBackgroundColor(getResources().getColor(R.color.white));
		adapter = new mPagerAdapter(getActivity().getSupportFragmentManager());
		contentPager.setAdapter(adapter);
		contentPager.setOffscreenPageLimit(2);
		contentPager.setPageTransformer(true, new ZoomOutPageTransformer());
		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		tabs.setTextColorResource(R.color.light_gray_text);
		tabs.setDividerColorResource(R.color.common_list_divider);
		// tabs.setUnderlineColorResource(R.color.common_list_divider);
		tabs.setIndicatorColorResource(R.color.red);
		tabs.setSelectedTextColorResource(R.color.red);
		tabs.setViewPager(contentPager);

//		// 悬浮按钮
//		actionSend = rootView.findViewById(R.id.action_send);
//		//actionReceive = rootView.findViewById(R.id.action_receive);
//		//actionTalks = rootView.findViewById(R.id.action_talks);
//		rightLabels = (FloatingActionsMenu) rootView
//				.findViewById(R.id.multiple_actions);
	}

	private void setListener() {

//		actionSend.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), VoiceMsgActivity.class);
//				startActivity(intent);
//				// 收起menu
//				rightLabels.collapse();
//
//			}
//		});

//		actionReceive.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), BaseStationListActivity.class);
//				startActivity(intent);
//				// 收起menu
//				rightLabels.collapse();
//			}
//		});
		
//		actionTalks.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
////				Intent intent = new Intent();
////				intent.setClass(getActivity(), BaseStationListActivity.class);
////				startActivity(intent);
//				// 收起menu
//				rightLabels.collapse();
//			}
//		});
	}

	private class mPagerAdapter extends FragmentStatePagerAdapter {


		private String Title[] = { "标题1",  "标题2", "标题3" };

		public mPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = null;
			if (arg0 == 0) {
				fragment = new TestFragment();
			} else if (arg0 == 1) {
				fragment = new TestFragment();
			} else if (arg0 == 2) {
				fragment = new TestFragment();
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return Title[position];
		}

	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}
}
