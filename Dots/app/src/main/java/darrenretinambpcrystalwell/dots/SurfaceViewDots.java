package darrenretinambpcrystalwell.dots;


import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import Dots.DotsPoint;
import Model.DotsInteraction;
import Model.DotsInteractionStates;
import Sockets.DotsServerClientParent;


/**
 * Created by Darren Ng 1000568 on 20/3/15.
 *
 * Cursor to show where the finger is touching
 * Get the arraylist of move from getPointArrayList();
 *
 */

public class SurfaceViewDots extends RelativeLayout
        implements View.OnTouchListener {

    private static final String TAG = "SurfaceViewDots";
    DotsInteractionStates interactionState;


    // Standard Variables call
    RelativeLayout             relativeLayout;
    Context                    context;
    private static final float SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float SCREEN_Y_PERCENTAGE     = .2f;

//    DotsScreen dotsScreen;


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





    private static final Bitmap BLANK_BITMAP
            = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);

    CrossHairView   blackDotView;

    float           dotWidth;

    public SurfaceViewDots(Context context, RelativeLayout relativeLayout) {
        super(context);
        this.context              = context;
        this.relativeLayout       = relativeLayout;


        float dotsXOffset         = (1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * ScreenDimensions.getWidth(context);
        float dotsYOffset         = SCREEN_Y_PERCENTAGE * ScreenDimensions.getHeight(context);
        this.dotWidth             = SCREEN_WIDTH_PERCENTAGE * ScreenDimensions.getWidth(context) / 6.f;
        LayoutParams layoutParams = new LayoutParams(ScreenDimensions.getWidth(context),
                ScreenDimensions.getHeight(context));

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



    private DotsInteraction previousInteraction;

    private boolean printed = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (!printed) {

            String correspondingDotPosition = Arrays.deepToString(this.correspondingDotCoordinates);
//            Log.d(TAG, correspondingDotPosition);
            printed = true;
        }

        int action = event.getAction();
//        Log.d(TAG, "Touched : " + event.getX() + ", " + event.getY());


        if (action == MotionEvent.ACTION_DOWN) {
            interactionState = DotsInteractionStates.TOUCH_DOWN;
//            blackDotView.setVisibility(VISIBLE);
//            blackDotView.setX(event.getX() - blackDotView.getWidth() / 2);
//            blackDotView.setY(event.getY() - blackDotView.getHeight() / 2);
        } else if (action == MotionEvent.ACTION_MOVE) {
            interactionState = DotsInteractionStates.TOUCH_MOVE;
//            blackDotView.setVisibility(VISIBLE);
//            blackDotView.setX(event.getX() - blackDotView.getWidth() / 2);
//            blackDotView.setY(event.getY() - blackDotView.getHeight() / 2);

        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            interactionState = DotsInteractionStates.TOUCH_UP;
//            blackDotView.setVisibility(INVISIBLE);


        } else {
            Log.d(TAG, "Unhandled motion event: " + action);
            return false;
        }




        // return if touch location has not moved far away enough so we reduce calculations
        if (touchedLocationCloseEnoughToReference(event.getX(), event.getY(), this.previousDetectedDotCoordinate[0], this.previousDetectedDotCoordinate[1])) {
            return false;
        }


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

            this.previousDetectedDotCoordinate = this.correspondingDotCoordinates[closestPoint.y][closestPoint.x];


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


    private float[] previousDetectedDotCoordinate = new float[2];


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



        double distance = Math.hypot((touchedX - refX), (touchedY - refY));

        if (distance < dotWidth/2.0) {
            return true;
        } else {
            return false;
        }


    }



    // Filled by Jiahao
    // TODO
    public void doPlayerInteraction(DotsInteraction interaction) {

        Log.d("Interaction", interaction.toString());
//        String answer = setTouchedPath(interaction) + "";
//        Log.d("Index", answer);
//        setTouchedPath(interaction);

        try {
            this.dotsServerClientParent.doInteraction(interaction);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    // Filled by Darren
    public void setTouchedPath(DotsInteraction interaction, DotsScreen dotsScreen) {
        if (interaction.getState() != DotsInteractionStates.TOUCH_UP) {
            int index = interaction.getDotsPoint().x*6 + interaction.getDotsPoint().y;
            dotsScreen.getTouchedList()[index].setVisibility(VISIBLE);
        } else if (interaction.getState() == DotsInteractionStates.TOUCH_UP) {
            for (DotView touched : dotsScreen.getTouchedList()) {
                touched.setVisibility(INVISIBLE);
            }
        }
    }




}