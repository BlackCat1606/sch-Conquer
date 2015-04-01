package darrenretinambpcrystalwell.dots;


import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import Dots.Point;
import Model.DotsInteraction;
import Model.DotsInteractionStates;
import Sockets.DotsServerClientParent;


/**
 * Created by DarrenRetinaMBP on 20/3/15.
 *
 * Cursor to show where the finger is touching
 * Get the arraylist of move from getPointArrayList();
 *
 */

public class SurfaceViewDots extends RelativeLayout
        implements View.OnTouchListener {

    private static final String TAG = "SurfaceViewDots";

    // Standard Variables call
    RelativeLayout             relativeLayout;
    Context                    context;
    private static final float SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float SCREEN_Y_PERCENTAGE     = .2f;

    float x,y;
    ArrayList<Point> pointArrayList               = new ArrayList<Point>();
    DotsInteraction interaction;
    Point                 tempPoint;


    private DotsServerClientParent dotsServerClientParent;

    public void setDotsServerClientParent(DotsServerClientParent dotsServerClientParent) {
        this.dotsServerClientParent = dotsServerClientParent;
    }

    // Variables for dag motion
//    private static final int               TOUCH_UP      = 0;
//    private static final int               TOUCH_DOWN = 1;
//    private static final int               TOUCH_MOVE     = 2;


    private static final Bitmap BLANK_BITMAP
            = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);

    CrossHairView   blackDotView;

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
        setOnTouchListener(this);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    private DotsInteraction previousInteraction;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        DotsInteractionStates interactionState;

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            interactionState = DotsInteractionStates.TOUCH_DOWN;
        } else if (action == MotionEvent.ACTION_MOVE) {
            interactionState = DotsInteractionStates.TOUCH_MOVE;

        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            interactionState = DotsInteractionStates.TOUCH_UP;

        } else {
            Log.d(TAG, "Unhandled motion event: " + action);
            return false;
        }


        blackDotView.setVisibility(VISIBLE);
        blackDotView.setX(event.getX() - blackDotView.getWidth() / 2);
        blackDotView.setY(event.getY() - blackDotView.getHeight() / 2);

        tempPoint = new Point((int) event.getX(), (int) event.getY());






    }
    // Filled by Jiahao
    // TODO
    public void doPlayerInteraction(DotsInteraction interaction) {

        Log.d("Interaction", interaction.toString());
        try {
            this.dotsServerClientParent.doInteraction(interaction);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public ArrayList<Point> getPointArrayList() {
        return pointArrayList;
    }
}