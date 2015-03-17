package darrenretinambpcrystalwell.dots;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private DotsScreen dotsScreen;
    private Dragger dragger;
    private Dot dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        dragger = new Dragger(rootLayout, this);
//        dotsScreen = new DotsScreen(rootLayout, this);
        dot = new Dot(this);
//        while (true) {
//            ArrayList<float[]> number = dragger.getArrayMotion();
//            for(float[] i: number) {
//                if (dotsScreen.dotsLayout.getX() == i[0] && dotsScreen.dotsLayout.getY() == i[1])
//                    System.out.println("touched");
//            }
//        }


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
