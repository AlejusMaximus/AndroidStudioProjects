package alejus.apps101illinoislectures.movingpixels.movingpixels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by aleix on 28/12/14.
 */
//It's not allowed to use the word static if it's a top level class
public class PenguinView extends View {
    //public static class -> problem; cannot see the fields that we defined inside out main activity
    //in order to solve this we can put this fields inside our PenguinView class:
    //Then our PenguinView class barely will need to talk with the outside world
    //Finally our PenguinView is now a separate class definition, so it can become a static class
    //because does not depend upon any of the fields inside the main activity
    //why don't stick it inside a java file ?
    private Bitmap mBitmap;
    private Bitmap mPenguin;
    private int mPHwidth; // Penguin half width
    private int mPHheight; // Penguin half height
    private Paint mPaint;
    private float x;
    private float y;
    private float vx=1;
    private float vy=1;
    private static final String TAG = "Penguin!";
    private boolean mTouching;
    private Canvas mCanvas;
    //Then everything is good except for the init code inside onCreate method->
    //Let's put it inside our constructor

    // On the main menu, choose Code | Generate
    //We must take in count that all 3 methods can be called, thus
    //all three will need the init() code;
    public PenguinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PenguinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //Illinois lecture doesn't use it
    /*public PenguinView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    //The constructor has to the same name as the class
    public PenguinView(Context context) {
        super(context);
        init();

    }

    private void init() {
        //Resources resources = getResources();
        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.rain_penguin_180);
        int desired = getResources().getDimensionPixelSize(R.dimen.penguin);
        mPenguin = Bitmap.createScaledBitmap(original, desired, desired, true);
        // Calculate the half width and height
        mPHwidth = mPenguin.getWidth() / 2;
        mPHheight = mPenguin.getHeight() / 2;

        mBitmap = Bitmap.createBitmap(256,256, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setStrokeWidth(0);
        //Let's draw something on the bitmap then:
        //mCanvas.drawColor(0xff808080); //gray color or grey
        mCanvas.drawColor(0xffff8000); //orange
        //Configure mPaint color (blue)
        //mPaint.setColor(0xff0000ff);
        //lets draw a line:
        //mCanvas.drawLine(0, 0, 3, 3, mPaint);

        OnTouchListener onTouch = new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch!" + event.getAction());
                // Log.d(TAG, "onTouch!" + event.getAction());
                // This app is not multi-touch aware:
                // When the user performs a multi-touch event the app will get
                // some large action values (because the 'action' parameter
                // encodes additional multi-touch information)
                // So they are ignored by the app
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP
                        || action == MotionEvent.ACTION_CANCEL) {
                    mTouching = false;
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    mTouching = true;
                }

                if (action == MotionEvent.ACTION_DOWN
                        || action == MotionEvent.ACTION_MOVE) {
                    //it's possible to get X and Y value where the user is touching
                    x = event.getX() - mPHheight;
                    y = event.getY() - mPHwidth;
                    vx = 0;
                    vy = 0;
                }

                return true;
            }
        };
        //Two options:
        //this.setOnTouchListener(onTouch);
        setOnTouchListener(onTouch);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //The following functions just need a pointer to a canvas,
        // no matter where it comes from
        drawBackground(canvas);
        drawPenguin(canvas);
        doPenguinPhysics();


        //let's make some trick to make the Penguin move:
        //The solution is not use a loop
        // In 20ms (1/50th second) this view will need to be redrawn
        postInvalidateDelayed(20);//it could be used for sensor measurement


    }

    private void doPenguinPhysics() {
        //Let's add some gravity/acceleration
        if( y + 2*mPHheight +vy +1 >= this.getHeight()){
            vy = -0.8f*vy;
        } else {
            vy = vy +1;
        }
        x = x +vx;
        y = y +vy;
    }

    private void drawPenguin(Canvas canvas) {
        //let's draw a circle
        mPaint.setColor(0x80ffffff); // White
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        float angle = SystemClock.uptimeMillis() / 10.0f;
        canvas.translate(x,y);

        if (mTouching){
            // Scale up the canvas coordinates system 20%,
            // Just before drawing the circle and the penguin
            canvas.scale(1.2f, 1.2f, mPHwidth, mPHheight);
        }

        //we can draw the circle after translation

        canvas.drawCircle(mPHwidth, mPHheight, mPHheight, mPaint);
        canvas.rotate(angle, mPHwidth, mPHheight);
        canvas.drawBitmap(mPenguin,0,0,null);
    }

    private void drawBackground(Canvas canvas) {
        //This new canvas corresponds to the new canvas of the all display
        canvas.drawColor(0xffff9900);
        float scaleX = this.getWidth() / ((float) mBitmap.getWidth());
        float scaleY = this.getHeight() / ((float) mBitmap.getHeight());
                /*if the following sentence is not commented it will appear on the logcat
                window each time our penguin rotates
                */
        //Log.d("MainActivity", "Scale:" + scaleX + "," + scaleY);
        canvas.save();
        canvas.scale(scaleX, scaleY);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.restore();
    }
};
