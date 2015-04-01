package darrenretinambpcrystalwell.dots;


import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import AndroidCallback.DotsAndroidCallback;
import Dots.DotsBoard;
import Dots.DotsGame;
import Model.DotsInteraction;
import Sockets.DotsServer;
import Sockets.DotsServerClientParent;


public class MainActivity extends ActionBarActivity {

    private DotsScreen      dotsScreen;
    private DotView         dotView;
    private SurfaceViewDots surfaceViewDots;
//
    private DotsServerClientParent dotsServerClientParent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView v = (SurfaceView) findViewById(R.id.surfaceView);

//        GifRun gifRun = new GifRun(this);
//
//        gifRun.LoadGiff(v, this, R.drawable.my_animated_gif);


        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root_layout);



        dotsScreen = new DotsScreen(rootLayout, this);
        //dotView = new DotView(this);
        surfaceViewDots = new SurfaceViewDots(this, rootLayout);
//        GifWebView view = new GifWebView(this, "/Users/DarrenRetinaMBP/AndroidStudioProjects/dotsMultiplayer/Dots/app/src/main/assets/my_animated_gif.gif");
//        setContentView(view);

        this.dotsServerClientParent = new DotsServer(4321);

        surfaceViewDots.setDotsServerClientParent(this.dotsServerClientParent);

        this.dotsServerClientParent.setAndroidCallback(new DotsAndroidCallback() {
            @Override
            public void onValidPlayerInteraction(DotsInteraction dotsInteraction) {

            }

            @Override
            public void onBoardChanged(DotsBoard dotsBoard) {
                dotsScreen.updateScreen(dotsBoard.getBoardArray());
            }

            @Override
            public void onGameOver() {

            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            this.dotsServerClientParent.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


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
