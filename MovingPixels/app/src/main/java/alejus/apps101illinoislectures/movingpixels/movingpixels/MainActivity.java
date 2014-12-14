package alejus.apps101illinoislectures.movingpixels.movingpixels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBitmap = Bitmap.createBitmap(4,4, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(mBitmap);
        Paint paint = new Paint();
        //Let's draw something on the bitmap then:
        c.drawColor(0xff808080); //gray color or grey
        //Configure paint color (blue)
        paint.setColor(0xff0000ff);
        //lets draw a line:
        c.drawLine(0,0,3,3,paint);

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
                // Log.d("MainActivity","Scale:"+scaleX+","+scaleY);
                canvas.scale(scaleX, scaleY);

                canvas.drawBitmap(mBitmap,0,0,null);


            }
        };

        //image.setImageBitmap(b);

        setContentView(v);
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
