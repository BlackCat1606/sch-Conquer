package darrenretinambpcrystalwell.dots;


import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import AndroidCallback.DotsAndroidCallback;
import Dots.DotsBoard;
import Dots.DotsGame;
import Model.DotsInteraction;
import Sockets.DotsClient;
import Sockets.DotsServer;
import Sockets.DotsServerClientParent;
import darrenretinambpcrystalwell.Fragments.ConnectionFragment;
import darrenretinambpcrystalwell.Fragments.GameFragment;
import darrenretinambpcrystalwell.Fragments.MainFragment;


public class MainActivity extends ActionBarActivity {
    private final String TAG = "Main Activity";

    private DotsScreen      dotsScreen;
    private DotView         dotView;
    private SurfaceViewDots surfaceViewDots;
//
    private DotsServerClientParent dotsServerClientParent;
    private DotsAndroidCallback androidCallback;

    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //   TODO Android does not like running network requests on the main thread.
        // This is a temporary workaaround.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




        setUpFragment(savedInstanceState);









    }


    private void setUpFragment(Bundle savedInstanceState) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.root_layout) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            Fragment firstFragment = this.getFragment(0);

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.root_layout, firstFragment).commit();
        }
    }

    /**
     * Get fragment with no arguments
     * @param i
     * @return
     */
    public Fragment getFragment(int i) {


        return this.getFragment(i, new String[2]);
    }

    public Fragment getFragment(int i, String args[]) {

        /**
         * 0 Main fragment
         * 1 Connection fragment
         * 2 Game fragment
         *
         */

        if (this.fragments == null) {
            this.fragments = new Fragment[DotsAndroidConstants.NO_OF_FRAGMENTS];
        }

        // This if else is to check if the fragment has already been created and start it again
        // Somehow we cant reuse fragments that have already been created
//        if (this.fragments[i] == null) {

            Fragment fragmentToCreate;
            if (i == 0) {
                fragmentToCreate = MainFragment.newInstance(args[0], args[1]);
            } else if (i == 1) {
                fragmentToCreate = ConnectionFragment.newInstance(args[0], args[1]);
            } else if (i == 2) {
                fragmentToCreate = GameFragment.newInstance(args[0], args[1]);
            } else {
                Log.e(TAG, "Unknown Fragment id");
                return null;
            }


            this.fragments[i] = fragmentToCreate;


        return this.fragments[i];


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

    public void updateScreenButton(View view) throws InterruptedException {
//        Effects.castFadeOutEffect(dotsScreen.getDotList()[24], 500, false, false);
//        Effects.castFadeOutEffect(dotsScreen.getDotList()[25], 500, false, false);
    }



}
