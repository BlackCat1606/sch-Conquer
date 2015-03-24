package darrenretinambpcrystalwell.dots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import Game.DotsGame;
import Game.DotsLogic;
import Game.Point;


public class MainActivity extends ActionBarActivity {

    private DotsScreen      dotsScreen;
    private DotView         dotView;
    private DotsGame        dotsGame;
    private DotsLogic       dotsLogic;
    private SurfaceViewDots surfaceViewDots;

    ArrayList<Point> moves = new ArrayList<Point>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        dotsScreen = new DotsScreen(rootLayout, this);
        dotView = new DotView(this);
        surfaceViewDots = new SurfaceViewDots(this, rootLayout);
        dotsGame = new DotsGame();

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

    public void updateScreenButton(View view) {

        dotsScreen.updateScreen();
    }

    public ArrayList<Point> getMoves() {
        return moves;
    }



}
