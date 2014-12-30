package alejus.apps101illinoislectures.movingpixels.movingpixels;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment; //Added manually
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aleix on 29/12/14.
 */
public class SettingsActivity extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Why this method is croosed out ??*/
        //showPreferencesPreHoneyComb();
        /*This method were available in early variations of Android
        * is duplicated -> that means is not the best way for modern phones
        * How to run this line of code just if we are working on android 3.0 device or earlier
        * How to do it ? using the Build number as follows: */
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            showPreferencesPreHoneyComb();
        }else{
            showPreferencesFragmentStyle(savedInstanceState);
            
        }
     }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    /*We know that this code will crush in old devices, thus we have taken in count this by hand,
    and this code will be executed on earlier devices > Honeycomb (API 11)*/
    private void showPreferencesFragmentStyle(Bundle savedInstanceState) {
        //This is only needed once, then the framework will save the instance
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new MyPreferencesFragment();
            transaction.replace(android.R.id.content, fragment);
            transaction.commit();
        }

    }

    @SuppressWarnings("deprecation") //bombilla -> suppress for method
    private void showPreferencesPreHoneyComb() {
        Log.d("Hurrah","Pre-Honeycomb!");
        addPreferencesFromResource(R.xml.penguin_prefs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MyPreferencesFragment extends PreferenceFragment {
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            Log.d("F", "I'm attached to an activity - I have a context!");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("Hurrah","onCreateView");
            this.addPreferencesFromResource(R.xml.penguin_prefs);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    };
}
