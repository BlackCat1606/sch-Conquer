package darrenretinambpcrystalwell.dots;


import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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


public class MainActivity extends ActionBarActivity {

    private DotsScreen      dotsScreen;
    private DotView         dotView;
    private SurfaceViewDots surfaceViewDots;
//
    private DotsServerClientParent dotsServerClientParent;
    private DotsAndroidCallback androidCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView v = (SurfaceView) findViewById(R.id.surfaceView);

//        GifRun gifRun = new GifRun(this);
//
//        gifRun.LoadGiff(v, this, R.drawable.my_animated_gif);






        // TODO Android does not like running network requests on the main thread.
        // This is a temporary workaaround.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Button startServerButton = (Button) this.findViewById(R.id.startServerButton);
        Button startClientButton = (Button) this.findViewById(R.id.startClientButton);

        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServerOrClient(0);
            }
        });

        startClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServerOrClient(1);
            }
        });
    }



    /**
     * Starts a server or client
     * @param playerId 0 for server, 1 for client
     */
    private void startServerOrClient(int playerId) {

        final int PORT = 4321;
//        final String SERVER_ADDRESS = "10.12.20.13";

// mac
//        final String SERVER_ADDRESS = "192.168.1.13";

        // note

        final String SERVER_ADDRESS = "192.168.1.4";

        if (playerId == 0) {

            this.dotsServerClientParent = new DotsServer(PORT);
        } else if (playerId == 1) {

            this.dotsServerClientParent = new DotsClient(SERVER_ADDRESS, PORT);
        }


        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        this.dotsScreen = new DotsScreen(rootLayout, this);
        this.surfaceViewDots = new SurfaceViewDots(this, rootLayout, this.dotsServerClientParent, this.dotsScreen.getCorrespondingDotCoordinates());

//        surfaceViewDots.setDotsServerClientParent(this.dotsServerClientParent);
//        surfaceViewDots.setCorrespondingDotCoordinates(dotsScreen.getCorrespondingDotCoordinates());


        this.androidCallback = new DotsAndroidCallback() {
            @Override
            public void onValidPlayerInteraction(final DotsInteraction dotsInteraction) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        surfaceViewDots.setTouchedPath(dotsInteraction, dotsScreen);
                    }
                });
            }

            @Override
            public void onBoardChanged(final DotsBoard dotsBoard) {
//                dotsScreen.updateScreen(dotsBoard.getBoardArray());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dotsScreen.updateScreen(dotsBoard.getBoardArray());
                    }
                });
            }

            @Override
            public void onGameOver() {

            }
        };


        this.dotsServerClientParent.setAndroidCallback(this.androidCallback);



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
