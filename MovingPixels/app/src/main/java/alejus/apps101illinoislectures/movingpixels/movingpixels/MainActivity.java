package alejus.apps101illinoislectures.movingpixels.movingpixels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import static android.view.View.OnTouchListener;


public class MainActivity extends ActionBarActivity {

    private Bitmap mBitmap;
    private Bitmap mPenguin;
    private int mPHwidth;
    private int mPHheight;
    private Paint mPaint;
    private float x;
    private float y;
    private float vx=1;
    private float vy=1;
    private static final String TAG = "Penguin!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Resources resources = getResources();
        mPenguin = BitmapFactory.decodeResource(getResources(),R.drawable.rain_penguin_180);
        // Calculate the half width and height
        mPHwidth = mPenguin.getWidth() / 2;
        mPHheight = mPenguin.getHeight() / 2;

        mBitmap = Bitmap.createBitmap(4,4, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(mBitmap);
        mPaint = new Paint();
        //Let's draw something on the bitmap then:
        c.drawColor(0xff808080); //gray color or grey
        //Configure mPaint color (blue)
        mPaint.setColor(0xff0000ff);
        //lets draw a line:
        c.drawLine(0,0,3,3, mPaint);

        //Let's say goodbye to ImageView
        //ImageView image = new ImageView(this);

        //Now we will create our own view:
        View v = new View(this){
            @Override
            protected void onDraw(Canvas canvas) {
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

                //let's draw a circle
                mPaint.setColor(0x80ffffff); // White
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

                float angle = SystemClock.uptimeMillis() / 10.0f;
                canvas.translate(x,y);
                //we can draw the circle after translation

                canvas.drawCircle(mPHwidth, mPHheight, mPHheight, mPaint);
                canvas.rotate(angle, mPHwidth, mPHheight);
                canvas.drawBitmap(mPenguin,0,0,null);
                if( y + 2*mPHheight +vy +1 >= this.getHeight()){
                    vy = -0.8f*vy;
                } else {
                    vy = vy +1;
                }
                x = x +vx;
                y = y +vy;
                //Let's add some gravity/acceleration


                //let's make some trick to make the Penguin move:
                //The solution is not use a loop
                // In 20ms (1/50th second) this view will need to be redrawn
                postInvalidateDelayed(20);//it could be used for sensor measurement


            }
        };

        //image.setImageBitmap(b);

        setContentView(v);

        OnTouchListener onTouch = new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,"onTouch!"+event.getAction());
                //it's possible to get X and Y value where the user is touching
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getX();
                    y = event.getY();
                }
                return true;
            }
        };
        v.setOnTouchListener(onTouch);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
