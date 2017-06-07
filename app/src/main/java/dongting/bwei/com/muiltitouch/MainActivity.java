package dongting.bwei.com.muiltitouch;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnTouchListener{

    private Matrix getMatrix =new Matrix();
    private Matrix saveMatrix =new Matrix();

    private PointF startPoint =new PointF();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int SCALE = 2;

    private int mode = NONE;
    private PointF middleTwo;
    private Float two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ImageView miv = (ImageView) findViewById(R.id.imageview);

        miv.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ImageView imageView = (ImageView) v;

        //加上  &MotionEvent.ACTION_MASK  支持多点触控
        switch (event.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_DOWN:

                //将属性矩阵设置到当前初始化
                getMatrix.set(imageView.getImageMatrix());

                saveMatrix.set(getMatrix);

                //获取按下位置的坐标
                startPoint.set(event.getX(),event.getY());
               //单指拖动
                mode = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                two = distance(event);
                if(two >10f){
                   saveMatrix.set(getMatrix);
                    middleTwo = middle(event);

                    mode =SCALE;
                }

            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode=NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if(mode==DRAG){
                    getMatrix.set(saveMatrix);
                    //平移的距离
                    getMatrix.postTranslate(event.getX()-startPoint.x,event.getY()-startPoint.y);

                }else if(mode==SCALE){
                    getMatrix.set(saveMatrix);

                    float newTwo =distance(event);
                    float percent = newTwo /two;

                    getMatrix.postScale(percent,percent,middleTwo.x,middleTwo.y);
                }
                break;
        }

        imageView.setImageMatrix(getMatrix);

        return true;
    }

    private PointF middle(MotionEvent event) {

        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);

        return new PointF(x/2,y/2);
    }

    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x*x+y*y);
    }
}
