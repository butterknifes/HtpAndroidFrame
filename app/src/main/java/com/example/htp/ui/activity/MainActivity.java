package com.example.htp.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.htp.utils.AppUtil;
import com.example.htp.htpandroidframe.Custom;
import com.example.htp.htpandroidframe.PrefConstants;
import com.example.htp.htpandroidframe.ProductTourActivity;
import com.example.htp.htpandroidframe.R;
import com.example.htp.model.User;

import butterknife.Bind;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_product_tour)
    Button btnProductTour;
    @Bind(R.id.btn_product_tour2)
    Button btnProductTour2;
    @Bind(R.id.btn_product_tour3)
    Button btnProductTour3;
    @Bind(R.id.btn_product_tour4)
    Button btnProductTour4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkShowTutorial();
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_main, null);
        Custom binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (view == null) {
            Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
        }
//        setContentView(R.layout.activity_main);
        User user = new User();
        user.setName("123");
        binding.setUser(user);
//        Button button = (Button)findViewById(R.id.btn_product_tour);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,ProductTourActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
//        });
//
//        Button button2 = (Button)findViewById(R.id.btn_product_tour2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,ProductTour2Activity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
//        });
//
//        Button button3 = (Button)findViewById(R.id.btn_product_tour3);
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,ProductTour3Activity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
//        });
    }

    /**
     * 检查新版本号是否大于旧版本号，若是，则跳转到引导页
     */
    private void checkShowTutorial() {
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = AppUtil.getAppVersionCode(this);
        if (currentVersionCode > oldVersionCode) {
            startActivity(new Intent(MainActivity.this, ProductTourActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
