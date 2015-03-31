package darrenretinambpcrystalwell.dots;


import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by DarrenRetinaMBP on 20/3/15.
 *
 * Cursor to show where the finger is touching
 * Get the arraylist of move from getPointArrayList();
 *
 */

public class SurfaceViewDots extends RelativeLayout
        implements View.OnTouchListener {

    // Standard Variables call
    RelativeLayout  relativeLayout;
    Context         context;
    private static final float SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float SCREEN_Y_PERCENTAGE = .2f;

    float x,y;
    ArrayList<Game.Point> pointArrayList = new ArrayList<Game.Point>();


    // Variables for dag motion
    private int                 dragStatus;
    private static final int STOP_DRAGGING = 0;
    private static final int CURRENTLY_DRAGGING = 1;
    private static final int START_DRAGGING = 2;


    private static final Bitmap BLANK_BITMAP
            = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);

//    BlackDotView blackDotView;
    CrossHairView blackDotView;

    float           dotWidth;

    public SurfaceViewDots(Context context, RelativeLayout relativeLayout) {
        super(context);
        this.context = context;
        this.relativeLayout = relativeLayout;
        float dotsXOffset = (1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * ScreenDimensions.getWidth(context);
        float dotsYOffset = SCREEN_Y_PERCENTAGE * ScreenDimensions.getHeight(context);
        this.dotWidth = SCREEN_WIDTH_PERCENTAGE * ScreenDimensions.getWidth(context) / 6.f;
        LayoutParams layoutParams = new LayoutParams(ScreenDimensions.getWidth(context)-(int)(dotsXOffset),
                ScreenDimensions.getHeight(context)-(int)(8*dotsXOffset));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        setLayoutParams(layoutParams);

        relativeLayout.addView(this);
        blackDotView = new CrossHairView(context);

        blackDotView.setX(dotsXOffset + dotWidth);
        blackDotView.setY(dotsYOffset + dotWidth);
        blackDotView.setLayoutParams(new ViewGroup.LayoutParams((int) dotWidth, (int) dotWidth));

        addView(blackDotView);
        blackDotView.setVisibility(INVISIBLE);
        //blackDotView.setVisibility(View.INVISIBLE);
        setOnTouchListener(this);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       int action = event.getAction();
       Log.d("Motion",pointArrayList.toString());
       if (action == MotionEvent.ACTION_DOWN) {
           dragStatus = START_DRAGGING;
           blackDotView.setVisibility(VISIBLE);
           blackDotView.setX(event.getX() - blackDotView.getWidth() / 2);
           blackDotView.setY(event.getY() - blackDotView.getHeight() / 2);

           pointArrayList.add(new Game.Point((int) event.getX(), (int) event.getY()));
           return true;
       }

       if (action == MotionEvent.ACTION_MOVE) {
           dragStatus = CURRENTLY_DRAGGING;
           blackDotView.setVisibility(VISIBLE);
           blackDotView.setX(event.getX() - blackDotView.getWidth()/2);
           blackDotView.setY(event.getY() - blackDotView.getHeight()/2);

           pointArrayList.add(new Game.Point((int) event.getX(), (int) event.getY()));
           return true;
       }

       if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
           pointArrayList.add(new Game.Point((int) event.getX(), (int) event.getY()));
           dragStatus = STOP_DRAGGING;
           blackDotView.setVisibility(INVISIBLE);
           pointArrayList.clear();
           return false;
       }
        return false;
    }
    public ArrayList<Game.Point> getPointArrayList() {
        return pointArrayList;
    }
}