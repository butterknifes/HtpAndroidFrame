package com.example.htp.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.htp.AppContext;
import com.example.htp.UIHelper;
import com.example.htp.htpandroidframe.R;
import com.example.htp.ui.activity.MainActivity;
import com.example.htp.ui.activity.NavigationActivity;
import com.example.htp.ui.activity.QuickOptionDialog;
import com.example.htp.ui.dialog.CommonToast;
import com.example.htp.utils.DialogHelp;
import com.example.htp.widget.togglebutton.ToggleButton;
import com.htp.zxing.activity.activity.CaptureActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TestFragment extends Fragment implements OnClickListener,
        NavigationActivity.CloseSoftKeyboardListener {

    @Bind(R.id.toggle)
    ToggleButton toggle;
    private EditText search_site;
    private ImageButton btn_search_delete;
    private Button icon_search;
    private ProgressDialog mpDialog;
    private Context context;
    private TextView bus_help;
    private InputMethodManager imm;// 软键盘相关
    private ImageView panel_img;
    private TextView panel_name;
    private LinearLayout collect_lin;
    private View share_lin;
    private TextView title;
    private View help_lin;
    private View setting_lin;
    private View accountInfo_lin;
    private ImageView titleIcon;
    private View logout_lin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, null);
        ButterKnife.bind(this, rootView);
        initView(rootView);
        registerListener();

        ((NavigationActivity) context).registerMyTouchListener(this);

        return rootView;
    }

    private void registerListener() {
        // TODO Auto-generated method stub
    }

    private void initView(View rootView) {
        // TODO Auto-generated method stub
        toggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on)
                {
                //  UIHelper.showImagePreview(getActivity(),new String[]{"http://img3.imgtn.bdimg.com/it/u=2414756802,531263205&fm=21&gp=0.jpg","http://img2.imgtn.bdimg.com/it/u=679614889,4219762343&fm=21&gp=0.jpg"});
                   showQuickOption( );
                }
            }
        });
    }
    // 显示快速操作界面
    private void showQuickOption() {
        final QuickOptionDialog dialog = new QuickOptionDialog(
               getActivity());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    private void initListView() {
        bus_help.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

    }

    //退出
    private void logout() {
        // TODO Auto-generated method stub
        //清楚记录
        //AppContext.getInstance().Logout();
        //AppManager.getAppManager().AppExit(getActivity());
        getActivity().finish();
        //UIHelper.showMobileLoginActivity(getActivity());

    }

    private void dismissShow() {
        if (mpDialog != null) {
            mpDialog.dismiss();
        }
    }

    class BusSiteHolder {
        TextView name;
        TextView posotion_text;
        TextView sect_canton;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((NavigationActivity) context).unRegisterMyTouchListener(this);
        ButterKnife.unbind(this);
    }

    @Override
    public void onCloseListener() {
        /* imm.hideSoftInputFromWindow(search_site.getWindowToken(), 0); */

    }

}
