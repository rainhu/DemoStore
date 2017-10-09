package rainhu.com.demostore.viewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import rainhu.com.demostore.R;

/**
 * Created by Ryan on 2017/9/18.
 */

public class CustomizedView extends View{
    public CustomizedView(Context context) {
        super(context);
    }

    public CustomizedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("Ryan", "onDraw");
        drawCircle();
    }

    private void drawCircle() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        Canvas canvas = new Canvas();
        canvas.drawCircle(200,200,100,paint);
    }
}
