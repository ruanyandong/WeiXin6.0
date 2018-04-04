package com.example.ai.weixin60;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;

    private ViewPager viewPager;

    private List<Fragment> mTabs=new ArrayList<Fragment>();

    private String[] mTitles=new String[]{
            "First Fragment",
            "Second Fragment",
            "Third Fragment",
            "Fourth Fragment"
    };

    private FragmentPagerAdapter mAdapter;

    private List<ChangeColorIconWithText> mTabIndicators=new ArrayList<>();

    private ChangeColorIconWithText one,two,three,four;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //setOverflowButtonAlways();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initDatas();

        /**
         * ViewPager内部实现了恢复机制，旋转屏幕，ViewPager不会重置，但是底部的Tab会重置，可以在配置文件里设置屏幕为竖直的解决
         * 但是如果应用处于后台，内存不足时，应用会有被杀死的可能性，
         */
        viewPager.setAdapter(mAdapter);

        initEvent();
    }

    private void initEvent() {

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                /**
                 * 从第一页到第二页 position一直为0，最后为1，positionOffset从0.0到1.0最后变为0.0
                 * 从第二也到第一页 position一直为0，positionOffset：1.0-0.0
                 *
                 * 从第二页到第三页 position一直为1，最后变为2，positionOffset从0.0到1.0最后变为0.0
                 *从第三页到第二页 position一直为1，positionOffset：1.0-0.0
                 *
                 */

                Log.d("onPageScrolled", position+"========>"+positionOffset);

                /**
                 * 动态设置Tab变色
                 */
                if(positionOffset>0){
                    ChangeColorIconWithText left=mTabIndicators.get(position);
                    ChangeColorIconWithText right=mTabIndicators.get(position+1);

                    left.setIconAlpha(1-positionOffset);
                    right.setIconAlpha(positionOffset);
                }

            }

            @Override
            public void onPageSelected(int position) {

                /**
                 * 静态设置Tab变色
                int currentPosition=viewPager.getCurrentItem();
                resetOtherTabs();
                switch (currentPosition){

                    case 0:
                        one.setIconAlpha(1.0f);
                        break;
                    case 1:
                        two.setIconAlpha(1.0f);
                        break;
                    case 2:
                        three.setIconAlpha(1.0f);
                        break;
                    case 3:
                        four.setIconAlpha(1.0f);
                        break;

                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**
         * 使OverFlowButton修改图标,api21以上才可以用
         */
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_menu_add));

        viewPager=findViewById(R.id.id_viewpager);

        one=findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);

        two=findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);

        three=findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);

        four=findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);

        /**
         * 默认第一个被选中，为绿色
         */
        one.setIconAlpha(1.0f);
    }

    @Override
    public void onClick(View v) {

        clickTab(v);
        /**
         * 其他button的点击事件
         */
        switch (v.getId()){

        }

    }

    /**
     * 点击Tab按钮
     * @param v
     */
    private void clickTab(View v) {
        resetOtherTabs();

        switch (v.getId()){
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                viewPager.setCurrentItem(0);
                break;

            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                viewPager.setCurrentItem(1);
                break;

            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                viewPager.setCurrentItem(2);
                break;

            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                viewPager.setCurrentItem(3);
                break;

        }
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {

        for(int i=0;i<mTabIndicators.size();i++){
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    private void initDatas() {

        for (String title:mTitles){

            TabFragment tabFragment=new TabFragment();
            /**
             * bundle:捆绑
             */
            Bundle bundle=new Bundle();
            bundle.putString(TabFragment.TITLE,title);

            tabFragment.setArguments(bundle);
            mTabs.add(tabFragment);
        }

        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount(){
                return mTabs.size();
            }
        };

    }

    /**
     * /**
     *  此方法用于初始化菜单，其中menu参数就是即将要显示的Menu实例。
     *  返回true则显示该menu,false 则不显示;
     * (只会在第一次初始化菜单时调用)
     * onCreateOptionsMenu：

     只会调用一次，他只会在Menu显示之前去调用一次，之后就不会在去调用。
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //整个行注释掉，就没有overflowButton
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }


    /**
     * onPrepareOptionsMenu是每次在display Menu之前，都会去调用，
     * 只要按一次Menu按鍵，就会调用一次。所以可以在这里动态的改变menu。

     注意：
     在onPrepareOptionsMenu(Menumenu)函数中，首先需要调用：

     super.onPrepareOptionsMenu(menu);
     menu.clear();

     如果没有clear而直接add的话，那么菜单中菜单项是会“追加”的，这样随着你不停的点menu键，菜单项就不停的增加。
     另外，android系统默认的菜单样式是支持最多3个一行，如果有4项就每行2个有2行...如果想自定义样式，可以使用xml文件定义样式。
     */
    /**
     如果在menu创建之后,想对menu进行动态的修改，
     那么就不能再对onCreateOptionsMenu做修改，要重写onPrepareOptionsMenu(Menumenu)方法。

     注意：在使用onPrepareOptionsMenu的时候，如果你每次都调用了menu.add()方法的话，
     那么菜单中的项目就会越来越多，所以，一般情况下是要调用一下menu.clear()的。
     */
    //动态改变菜单，显示icon
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 设置menu显示icon
     * 这个方法是菜单打开后发生的动作，在OverFlowButton点击后，打开菜单后调用
     * 这个方法没有用，无法显示icon
     */

    /**
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if(featureId== Window.FEATURE_ACTION_BAR&&menu!=null){

            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try {
                    Method m=menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);

                    m.setAccessible(true);

                    m.invoke(menu,true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }catch (InvocationTargetException e){

                }catch (IllegalAccessException e){

                }
            }
        }


        return super.onMenuOpened(featureId, menu);
    }*/

    /**
     * 使能够出现OverflowButton，并且是一直出现
     * java反射
     * 这个方法也没有用，无法显示OverFlowButton
     */
    /**
    private void setOverflowButtonAlways(){

        ViewConfiguration config=ViewConfiguration.get(this);

        try {
            Field menuKey=ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            //设置权限为可以访问
            menuKey.setAccessible(true);
            menuKey.setBoolean(config,false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e){

        }
    }*/

}
