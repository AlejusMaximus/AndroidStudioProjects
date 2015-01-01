package alejus.apps101illinoislectures.javafacts.javafacts;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1)  find our text view and set the text in it:
        TextView message = (TextView) findViewById(R.id.message);
        //2) we need our resources
        Resources resources = this.getResources();
        //3) We call this resources
        String[] javafacts = resources.getStringArray(R.array.messages);
        //So now we have our whole array (30 different strings) of java facts
        //How to select one of them ? The second one for instance...
        //String fact = javafacts[1];
        //What if we want to do it random ? we will need to import math:
        int index = (int) (Math.random()*javafacts.length);
        String fact = javafacts[index];
        //Now we pass fact
        message.setText(fact);
        //message.setText("HelloWorld!");

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
