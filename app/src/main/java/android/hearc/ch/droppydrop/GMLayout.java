package android.hearc.ch.droppydrop;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.RelativeLayout;

public class GMLayout extends RelativeLayout {

    private static final String TAG = "GAME"; //GameManager.class.getSimpleName();

    private AccelerometerPointer accPointer;
    private Level level;

    public GMLayout(Context context) {
        this(context, null);
    }

    public GMLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        int h = this.getMeasuredHeight();
        int w = this.getMeasuredWidth();

        accPointer = new AccelerometerPointer(context, h, w);
        level = new Level(context);

        init(attrs);
    }

    private void init(AttributeSet set)
    {
        addView(level);
        if(set == null) return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.GameManager);
        // assign custom attribs
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "is in onMeasure");
        final int newHeight= MeasureSpec.getSize(heightMeasureSpec);
        final int newWidth= MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        accPointer.resetPointer(newHeight, newWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "touch the game");

        // TODO show on pause menu
        if(level.addPoint(accPointer.getPointer())){
            Log.i(TAG, "onTouchEvent: succesfully add a point");
            level.invalidate();
        } else {
            Log.e(TAG, "onTouchEvent: cannot add point");
        }
        return false;
    }
}
