package com.example.ai.weixin60;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by AI on 2018/4/1.
 */

/**
 * 自定义底部tab，会变色
 */
public class ChangeColorIconWithText extends View {

    /**
     * 在这里声明attrs.xml文件里定义的属性，有的属性赋予默认初始值
     * 依次为color、icon、text、text_size
     */

    /**
     * color是字体和文本的颜色
     */
    private int mColor=0xFF45C01A;//赋予默认值

    private Bitmap mIconBitmap;

    private String mText="微信";//赋予默认值

    private int mTextSize= (int)TypedValue.
            applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    12,
                    getResources().getDisplayMetrics());//赋予默认值12sp


    /**
     * 这三个是在内存中绘图，绘制Icon
     */
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    /**
     * 0.0-1.0
     */
    private float mAlpha;

    private Rect mIconRect;
    private Rect mTextBound;

    private Paint mTextPaint;

    /**
     *
     在代码中直接new一个Custom View实例的时候,会调用第一个构造函数.
     在xml布局文件中调用Custom View的时候,会调用第二个构造函数.
     在xml布局文件中调用Custom View,并且Custom View标签中还有自定义属性时,这里调用的还是第二个构造函数.

     也就是说,系统默认只会调用Custom View的前两个构造函数,
     至于第三个构造函数的调用,通常是我们自己在构造函数中主动调用的（例如,在第二个构造函数中调用第三个构造函数）.

     至于自定义属性的获取,通常是在构造函数中通过obtainStyledAttributes函数实现的.
     * @param context
     */

    public ChangeColorIconWithText(Context context) {
        this(context,null);
        Log.d("一个参数的构造函数====》", "ChangeColorIconWithText: ");
    }

    public ChangeColorIconWithText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        Log.d("两个参数的构造函数====》", "ChangeColorIconWithText: ");
    }

    /**
     * 在这里初始化
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ChangeColorIconWithText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /**
         * 在这里获取到attrs.xml文件里定义的属性的值
         */

        /**
         * TypedArray是一个自定义的属性集合，遍历这个集合，赋值给声明的四个变量
         */
        TypedArray a=context.
                obtainStyledAttributes(attrs,R.styleable.ChangeColorIconWithText);
        //获得属性总数4个
        int n=a.getIndexCount();

        for(int i=0;i<n;i++){
            //拿到每个属性
            int attr=a.getIndex(i);

            switch(attr){

                /**
                 * 这里是把布局文件里设置为实际属性值获取到，赋给相应的变量
                 */
                case R.styleable.ChangeColorIconWithText_icon:
                    BitmapDrawable drawable=(BitmapDrawable)a.getDrawable(attr);
                    mIconBitmap=drawable.getBitmap();
                    break;
                case R.styleable.ChangeColorIconWithText_color:
                    mColor=a.getColor(attr,0xFF45C01A);
                    break;
                case R.styleable.ChangeColorIconWithText_text:
                    mText=a.getString(attr);
                    break;
                case R.styleable.ChangeColorIconWithText_text_size:

                    mTextSize=(int)a.getDimension(attr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    12,
                                    getResources().getDisplayMetrics()));
                    break;

                default:
                    break;
            }

        }

        a.recycle();

        mTextBound=new Rect();
        mTextPaint=new Paint();
        mTextPaint.setTextSize(mTextSize);

        mTextPaint.setColor(0xff555555);//灰色

        //测量字的范围
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);

    }


    /**
     * 这两个参数的意思
     * 一个int整数，里面放了测量模式和尺寸大小
     * java中一个int数据占32bit，前两个bit为测量模式，后30个bit表示尺寸大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 在这里确定ChangeColorIconWithText（view）里面的子元素iocn的大小（宽高）以及位置
         */

        /**
         * 假设icon是一个正方形，icon的边长为：
         * view的宽度-leftpadding-rightpadding
         * view的高度-toppadding-bottompadding-mTextBound.height
         * 这两个中取最小值
         * 图例：view是外面的长方形，icon是里面的正方形，icon下是文字(mTextBound)
         * ************************
         * *                      *
         * *   ****               *
         * *   *  *               *
         * *   ****               *
         * *   微信               *
         * ************************
         */
        int iconWidth=Math.
                min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(),
                getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height());

        /**
         * left、top、right、bottom确定icon的绘制位置，以下计算则确定icon绘制在view的居中位置
         */
        int left=getMeasuredWidth()/2-iconWidth/2;
        int top=(getMeasuredHeight()-mTextBound.height())/2-iconWidth/2;
        /**
         * 在这个mIconRec的范围内绘制Iocn
         */
        mIconRect=new Rect(left,top,left+iconWidth,top+iconWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 在mIconRec的范围内绘制了mIconBitmap===>(白色的)
         * 绘制原图标
         */
        canvas.drawBitmap(mIconBitmap,null,mIconRect,null);

        /**
         * Math.ceil会取大于或等于参数的最小整数，向上取整
         */
        int alpha=(int)Math.ceil(255*mAlpha);
        /**
         * 在内存中准备mBitmap,setAlpha，纯色，xfermode，图标
         * 绘制变色图标
         */
        setupTargetBitmap(alpha);

        //1、绘制原文本

        drawSourceText(canvas,alpha);

        //2、绘制变色的文本
        drawTargetText(canvas,alpha);

        //把内存中图绘制出来
        canvas.drawBitmap(mBitmap,0,0,null);

    }


    /**
     * 绘制变色文本
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {

        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);

        //字体左边的起始位置x getMeasuredWidth()/2=mIconRect.left+mIconRect.width()/2
        int x=getMeasuredWidth()/2-mTextBound.width()/2;

        int y=mIconRect.bottom+mTextBound.height();//mTextBound.height()不除以二，字体与icon有一定距离
        canvas.drawText(mText,x,y,mTextPaint);

    }

    /**
     * 绘制原文本
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {

        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255-alpha);
        //字体左边的起始位置x getMeasuredWidth()/2=mIconRect.left+mIconRect.width()/2
        int x=getMeasuredWidth()/2-mTextBound.width()/2;

        int y=mIconRect.bottom+mTextBound.height();//mTextBound.height()不除以二，字体与icon有一定距离
        canvas.drawText(mText,x,y,mTextPaint);

    }

    /**
     * 在内存中绘制可变色icon
     */
    private void setupTargetBitmap(int mAlpha) {

        mBitmap=Bitmap.createBitmap(getMeasuredWidth(),
                        getMeasuredHeight(),
                        Bitmap.Config.ARGB_8888);

        mCanvas=new Canvas(mBitmap);

        mPaint=new Paint();

        mPaint.setColor(mColor);

        mPaint.setAntiAlias(true);//去锯齿

        mPaint.setDither(true);//设置防抖动

        mPaint.setAlpha(mAlpha);


        mCanvas.drawRect(mIconRect,mPaint);///

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mPaint.setAlpha(255);//取值范围为0~255，数值越小越透明
        mCanvas.drawBitmap(mIconBitmap,null,mIconRect,mPaint);////
    }

    /**
     * 让外部可以设置透明度
     */
    public void setIconAlpha(float alpha){
        this.mAlpha=alpha;
        /**
         * invalidate:使无效、使作废
         * 重新绘制之前，要invalidate，才能重新绘制
         */
        invalidateView();

    }

    /**
     * 重新绘制
     */
    private void invalidateView() {
        /**
         * Android中的Looper类，是用来封装消息循环和消息队列的一个类，
         * 用于在android线程中进行消息处理。handler其实可以看做是一个工具类，
         * 用来向消息队列中插入消息的。


         (1) Looper类用来为一个线程开启一个消息循环。
         默认情况下android中新诞生的线程是没有开启消息循环的。
         （主线程除外，主线程系统会自动为其创建Looper对象，开启消息循环。）
         Looper对象通过MessageQueue来存放消息和事件。
         一个线程只能有一个Looper，对应一个MessageQueue。


         (2) 通常是通过Handler对象来与Looper进行交互的。
         Handler可看做是Looper的一个接口，用来向指定的Looper发送消息及定义处理方法。
         默认情况下Handler会与其被定义时所在线程的Looper绑定，
         比如，Handler在主线程中定义，那么它是与主线程的Looper绑定。
         mainHandler = new Handler() 等价于new Handler（Looper.myLooper()）.
         Looper.myLooper()：获取当前进程的looper对象，
         类似的 Looper.getMainLooper() 用于获取主线程的Looper对象。


         (3) 在非主线程中直接new Handler() 会报如下的错误:
         E/AndroidRuntime( 6173): Uncaught handler: thread Thread-8 exiting due to uncaught exception
         E/AndroidRuntime( 6173): java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
         原因是非主线程中默认没有创建Looper对象，需要先调用Looper.prepare()启用Looper。


         (4) Looper.loop(); 让Looper开始工作，从消息队列里取消息，处理消息。


         注意：写在Looper.loop()之后的代码不会被执行，这个函数内部应该是一个循环，
         当调用mHandler.getLooper().quit()后，loop才会中止，其后的代码才能得以运行。
         */

        /**
         * 如果你不带参数的实例化：Handler handler = new Handler();那么这个会默认用当前线程的looper
         一般而言，如果你的Handler是要来刷新操作UI的，那么就需要在主线程下跑。
         情况:
         1.要刷新UI，handler要用到主线程的looper。那么在主线程 Handler handler = new Handler();，如果在其他线程，也要满足这个功能的话，要Handler handler = new Handler(Looper.getMainLooper());
         2.不用刷新ui,只是处理消息。 当前线程如果是主线程的话，Handler handler = new Handler();不是主线程的话，Looper.prepare(); Handler handler = new Handler();Looper.loop();或者Handler handler = new Handler(Looper.getMainLooper());
         若是实例化的时候用Looper.getMainLooper()就表示放到主UI线程去处理。
         如果不是的话，因为只有UI线程默认Loop.prepare();Loop.loop();过，其他线程需要手动调用这两个，否则会报错。
         */

        /**
         * 在android的线程里，是从message loop取出信息来处理的，主线程和子线程、子线程和子线程之间的通讯也是Looper来实现的，接下来我就简单介绍下Looper的使用方法。
         主线程（即UI线程）自身就有message loop，不需要创建，而其他线程就需要手动创建，使用prepare()创建loop，使用 loop()来启动loop，直到loop停止
         */
        /**
         * getMainLooper()
         返回应用主线程的looper

         getThread()
         返回当前线程

         loop()
         在线程里运行信息队列（message queue）

         myLooper()
         返回当前线程的looper

         myQueue()
         返回当前线程的messageQueue对象

         prepare()
         初始化当前线程的Looper

         prepareMainLooper()
         初始化应用主线程的Looper

         quit()
         退出Looper
         */
        //判断当前线程是否在主线程
        if(Looper.getMainLooper()==Looper.myLooper()){
            /**
             * invalidate:使无效、使作废
             * 重新绘制之前，要invalidate，才能重新绘制
             */

            /**
             * Android提供了Invalidate方法实现界面刷新，但是Invalidate不能直接在子线程中调用，因为他是违背了单线程模型：Android UI操作并不是线程安全的，并且这些操作必须在UI线程中调用。

             invalidate()是用来刷新View的，必须是在UI线程中进行工作。比如在修改某个view的显示时，调用invalidate()才能看到重新绘制的界面。invalidate()的调用是把之前的旧的view从主UI线程队列中pop掉。 一个Android 程序默认情况下也只有一个进程，但一个进程下却可以有许多个线程。

             在这么多线程当中，把主要是负责控制UI界面的显示、更新和控件交互的线程称为UI线程，由于onCreate()方法是由UI线程执行的，所以也可以把UI线程理解为主线程。其余的线程可以理解为工作者线程。

             invalidate()得在UI线程中被调动，在工作者线程中可以通过Handler来通知UI线程进行界面更新。

             而postInvalidate()在工作者线程中被调用
             */

            /**
             * 利用invalidate()刷新界面

             　　实例化一个Handler对象，并重写handleMessage方法调用invalidate()实现界面刷新;而在线程中通过sendMessage发送界面更新消息。

             使用postInvalidate()刷新界面

             使用postInvalidate则比较简单，不需要handler，直接在线程中调用postInvalidate即可。
             */


            invalidate();
        }else{
            /**
             *  ① invalidate() ：

             请求重绘View树，即draw()过程。把例子中他是整个刷新着UI，并且从头到尾并不会触发onMeasure（）方法（控制大小用）。如果是View就重绘View,如果是ViewGroup就全部重绘。

             一般引起invalidate()操作的函数如下：

             1、直接调用invalidate()方法，请求重新draw()，但只会绘制调用者本身。

             2、setSelection()方法 ：请求重新draw()，但只会绘制调用者本身。

             3、setVisibility()方法 ： 当View可视状态在INVISIBLE转换VISIBLE时，会间接调用invalidate()方法，继而绘制该View。

             4 、setEnabled()方法 ： 请求重新draw()，但不会重新绘制任何视图包括该调用者本身。


             ②postInvalidate(); 与invalidate()方法区别就是，postInvalidate()方法可以在UI线程执行，也可以在工作线程执行

             而invalidate()只能在UI线程操作。但是从重绘速率讲：invalidate()效率高。

             ③ requestLayout()

             他跟invalidate()相反，他只调用measure()和layout()过程，不会调用draw()。


             不会重新绘制任何视图包括该调用者本身。


             ④局部刷新

             使用 requestFocus()方法，他只刷新你要刷新的地方。
             他是让我们的某一部分获取焦点，获取焦点的会导致view的重绘。
             */
            postInvalidate();
        }
    }


    /**
     * 两个方法
     * 设置恢复和重建（先保存、再重建）
     * 这两个方法可以防止旋转屏幕时的活动恢复（次要，这个可以在配置文件里设置屏幕方向解决）
     * 这两个方法也可以防止长期置于后台的Activity被重启的恢复（这个是主要的）
     * @return
     */

    private static final String INSTANCE_STATUS="instance_status";

    private static final String STATUS_ALPHA="status_alpha";

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState(){

        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE_STATUS,super.onSaveInstanceState());//把父类的东西存进去
        bundle.putFloat(STATUS_ALPHA,mAlpha);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        /**
         * 判断是否是我们存的bundle
         */
        if (state instanceof Bundle){

            Bundle bundle=(Bundle)state;
            mAlpha=bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));//恢复父类的所做的操作
            return;
        }
        super.onRestoreInstanceState(state);
    }


}
