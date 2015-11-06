package io.github.sunxu3074.bannerwithindicator;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final int PHOTO_CHANGE_TIME = 3000;
    private static final int ZOOM_RUN = 0x001;
    private static final int AUTO_RUN = 0x002;

    private ViewPager mViewPager;

    private AutoScrollViewPager mAutoScrollViewPager;

    private int[] imgIds = new int[]{R.drawable.guide_image1, R.drawable.guide_image2, R.drawable
            .guide_image3};
    private int[] imgDotaIds = new int[]{R.drawable.dota2_1, R.drawable.dota2_2, R.drawable
            .dota2_3};

    private ArrayList<ImageView> mImages = new ArrayList<>();

    private LinearLayout mLinearLayout;

    /**
     * 记录当前执行viewPager
     */
    private int flag ;
    /**
     * 记录当前指示器
     */
    private ImageView currentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        //默认执行入场动画
        runZoomOutPageTransformer();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_main);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_home_banner);
        mAutoScrollViewPager = (AutoScrollViewPager) findViewById(R.id.vp_auto_activitiy_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_zoomout) {
            runZoomOutPageTransformer();
            return false;
        } else if (id == R.id.action_autoscroll) {
            runAutoScrollViewPager();
        }

        return super.onOptionsItemSelected(item);
    }

    private void runAutoScrollViewPager() {
        flag = AUTO_RUN;
        mViewPager.setVisibility(View.GONE);
        mAutoScrollViewPager.setVisibility(View.VISIBLE);

        // 初始化指示器
        initIndicatorNumbers(imgDotaIds.length);
        // 为viewPager添加监听器
        mAutoScrollViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 放PagerAdapter的实现类
        mAutoScrollViewPager.setAdapter(new MyPagerAdapter(imgDotaIds));
        // 设置多少毫秒后开始自动滚动
        mAutoScrollViewPager.startAutoScroll(PHOTO_CHANGE_TIME);
        // 设置自动滚动的间隔时间
        mAutoScrollViewPager.setInterval(PHOTO_CHANGE_TIME);
        // mViewPager.setScrollBarFadeDuration(2);
        // 设置viewPager滑动动画间隔的速率,达到减慢动画或者改变动画速度的效果
        mAutoScrollViewPager.setScrollDurationFactor(2);
    }

    private void runZoomOutPageTransformer() {
        flag = ZOOM_RUN;
        mAutoScrollViewPager.stopAutoScroll();
        mAutoScrollViewPager.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(imgIds.length);
        mViewPager.setAdapter(new MyPagerAdapter(imgIds));
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        initIndicatorNumbers(imgIds.length);
    }

    /**
     * 初始化指示器
     */
    private void initIndicatorNumbers(int number) {

        mLinearLayout.removeAllViews();
        for (int i = 0; i < number; i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(R.drawable.point_no_selected);
            imageView.setPadding(25, 0, 0, 0);
            mLinearLayout.addView(imageView);
        }
        // 默认选中第一个position
        changePosition(0);
    }

    /**
     * 改变指示器状态̬
     */
    private void changePosition(int position) {
        if (currentImageView != null) {
            currentImageView.setImageResource(R.drawable.point_no_selected);
        }
        currentImageView = (ImageView) mLinearLayout.getChildAt(position);
        if (currentImageView == null) {
            return;
        }
        currentImageView.setImageResource(R.drawable.point_selected);
    }

    /**
     * Adapter
     */
    private class MyPagerAdapter extends PagerAdapter {

        private int[] imgDatas ;

        public MyPagerAdapter(int[] imgDatas) {
            this.imgDatas = imgDatas;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(imgDatas[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //记录位置
            imageView.setTag(position);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取出当前点击位置
                    int currentPosition = (int) v.getTag();
                    Toast.makeText(getApplication(), "currentPosition = " + currentPosition,
                            Toast.LENGTH_LONG).show();
                }
            });
            mImages.add(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImages.get(position));
        }

        @Override
        public int getCount() {
            return imgDatas.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changePosition(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAutoScrollViewPager!=null){
            mAutoScrollViewPager.stopAutoScroll();
        }
    }


    /**
     * ִ执行resume生命周期,让autoScrollViewPager继续开始滚动
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(mAutoScrollViewPager!=null && flag == AUTO_RUN){
            mAutoScrollViewPager.startAutoScroll(PHOTO_CHANGE_TIME);
        }
    }
}
