package darrenretinambpcrystalwell.dots;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.util.Log;

import java.io.IOException;

import Dots.DotsPoint;
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


    // TODO set player id dynamically
    private final int PLAYER_ID = 0;
//    float x,y;


    private float[][][] correspondingDotCoordinates;
    private DotsServerClientParent dotsServerClientParent;


    public void setCorrespondingDotCoordinates(float[][][] correspondingDotCoordinates) {
        this.correspondingDotCoordinates = correspondingDotCoordinates;
    }


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

        // sets up the previous interaction with an arbitary point
        this.previousInteraction = new DotsInteraction(this.PLAYER_ID, DotsInteractionStates.TOUCH_UP, new DotsPoint(0,0));
    }

//    public void setX(float x) {
//        this.x = x;
//    }
//
//    public void setY(float y) {
//        this.y = y;
//    }

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

        DotsPoint closestPoint = dotPointClosestToTouchedLocation(event.getX(), event.getY());

        DotsInteraction interaction;

        // this part is to deal with touch up, on a strange area of the screen far away from the dotviews
        if (closestPoint == null) {

            if (interactionState == DotsInteractionStates.TOUCH_UP) {
                // sends a touch up interaction with the DotPoint of the previous interaction
                interaction = new DotsInteraction(PLAYER_ID, interactionState, previousInteraction.getDotsPoint());

            } else {
                return false;
            }
        } else {
            interaction = new DotsInteraction(PLAYER_ID, interactionState, closestPoint);
        }


        // check if its the same interaction as before, we return to avoid duplicate messages
        if (interaction.compareWith(this.previousInteraction)) {
            return false;
        }


        this.doPlayerInteraction(interaction);

        // saves the current interaction
        this.previousInteraction = interaction;

        return true;
    }


    /**
     * Creates a DotsPoint with the index of the closest dot that falls within the threshold
     * @param touchedX,touchedY coordinates of touches
     * @return null if no point found
     */
    private DotsPoint dotPointClosestToTouchedLocation(float touchedX, float touchedY) {


        float[][][] correspondingDotCoordinates = this.correspondingDotCoordinates;


        for (int j = 0; j < correspondingDotCoordinates.length; j++) {

            for (int i = 0; i < correspondingDotCoordinates[j].length ; i++) {

                float currentDotViewX = correspondingDotCoordinates[j][i][0];
                float currentDotViewY = correspondingDotCoordinates[j][i][1];

                if (touchedLocationCloseEnoughToReference(touchedX, touchedY, currentDotViewX, currentDotViewY)) {

                    return new DotsPoint(i, j);
                }
            }
        }

        return null;
    }

    private boolean touchedLocationCloseEnoughToReference(float touchedX, float touchedY, float refX, float refY) {

        // Make this change according to the size of the screen
        final float THRESHOLD = 50;

        double distance = Math.hypot((touchedX - refX), (touchedY - refY));

        if (distance < THRESHOLD) {
            return true;
        } else {
            return false;
        }


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

}