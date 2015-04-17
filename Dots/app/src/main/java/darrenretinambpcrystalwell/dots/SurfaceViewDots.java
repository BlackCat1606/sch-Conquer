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

import Constants.DotsConstants;
import Dots.DotColor;
import Dots.DotsPoint;
import Model.Interaction.DotsInteraction;
import Model.Interaction.DotsInteractionStates;
import Sockets.DotsClient;
import Sockets.DotsServer;
import Sockets.DotsServerClientParent;


/**
 * Created by Darren Ng 1000568 on 20/3/15.
 *
 * surfaceview class that takes care of the interaction
 * of finger gesture
 *
 */

public class SurfaceViewDots extends RelativeLayout
        implements View.OnTouchListener {

    private static final String         TAG = "SurfaceViewDots";
    DotsInteractionStates               interactionState;

    // Standard Variables call
    RelativeLayout                       relativeLayout;
    Context                              context;
    private static final float           SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float           SCREEN_Y_PERCENTAGE     = .2f;
    float                                dotWidth;
    private final int                    PLAYER_ID;
    private final float[][][]            correspondingDotCoordinates;
    private final DotsServerClientParent dotsServerClientParent;


//    public void setCorrespondingDotCoordinates(float[][][] correspondingDotCoordinates) {
//        this.correspondingDotCoordinates = correspondingDotCoordinates;
//    }
//
//
//    public void setDotsServerClientParent(DotsServerClientParent dotsServerClientParent) {
//        this.dotsServerClientParent = dotsServerClientParent;
//    }

    private static final Bitmap BLANK_BITMAP
            = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);
    /**
     * Standard Initialising Constructor
     *
     * @param context
     * @param relativeLayout
     * @param dotsServerClientParent
     * @param correspondingDotCoordinates
     */
    public SurfaceViewDots(
            Context context,
            RelativeLayout relativeLayout,
            DotsServerClientParent dotsServerClientParent,
            float[][][] correspondingDotCoordinates) {
        super(context);
        this.context                     = context;
        this.relativeLayout              = relativeLayout;
        this.dotsServerClientParent      = dotsServerClientParent;
        this.correspondingDotCoordinates = correspondingDotCoordinates;
        this.dotWidth                    = SCREEN_WIDTH_PERCENTAGE * ScreenDimensions.getWidth(context)
                / DotsAndroidConstants.BOARD_SIZE;
        LayoutParams layoutParams        = new LayoutParams(ScreenDimensions.getWidth(context),
                ScreenDimensions.getHeight(context));

        setLayoutParams(layoutParams);

        relativeLayout.addView(this);

        setOnTouchListener(this);


        if (this.dotsServerClientParent instanceof DotsClient) {
            this.PLAYER_ID = 1;
        } else {
            this.PLAYER_ID = 0;
        }

        Log.d(TAG, "PlayerID: " + this.PLAYER_ID);

        // sets up the previous interaction with an arbitary point
        this.previousInteraction = new DotsInteraction(this.PLAYER_ID, DotsInteractionStates.TOUCH_UP, new DotsPoint(0,0));
    }



    private DotsInteraction previousInteraction;

    /**
     * use this to get the input && finger interaction states
     * @param v
     * @param event
     * @return boolean, of the drag status of the finger
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // if game is not started yet, we just return to avoid nullpointer exceptions
        if (!this.dotsServerClientParent.isGameStarted()) {
            return false;
        }


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

        boolean notActionUp = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE;

        if (notActionUp) {

            // return if touch location has not moved far away enough so we reduce calculations
            // We only perform this check if its not a touch up
            if (touchedLocationCloseEnoughToReference(event.getX(), event.getY(), this.previousDetectedDotCoordinate[0], this.previousDetectedDotCoordinate[1])) {
                return false;
            }
        }

        DotsPoint closestPoint = dotPointClosestToTouchedLocation(event.getX(), event.getY());

        DotsInteraction interaction;
//
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
            this.previousDetectedDotCoordinate = this.correspondingDotCoordinates[closestPoint.y][closestPoint.x];
        }
////        } else {
////        }
////
//////        else {
//////
////
//        if (closestPoint == null) {
//            return false;
//        }


        // if its a touch up, we want to clear the previous detected dot coordinate
        if (interactionState == DotsInteractionStates.TOUCH_UP) {
            this.previousDetectedDotCoordinate = new float[] {
                    (float)9999, (float)9999
            };
        }

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

    /**
     *
     * @param touchedX
     * @param touchedY
     * @param refX
     * @param refY
     * @return boolean of if the location finger touched is within the threshold of the dots nearby
     */
    private boolean touchedLocationCloseEnoughToReference(float touchedX, float touchedY, float refX, float refY) {

        double distance = Math.hypot((touchedX - refX), (touchedY - refY));

        if (distance < dotWidth/2.0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * execute the interaction of the player
     * @param interaction
     */
    public void doPlayerInteraction(DotsInteraction interaction) {

        try {
            Log.d(TAG, interaction.toString());
            this.dotsServerClientParent.doInteraction(interaction);
        } catch (IOException e) {
            Log.e(TAG, "Do interaction IO exception: " + e);
        } catch (InterruptedException e) {
            Log.e(TAG, "Do interaction interrupted exception: " + e);
        }


    }

    /**
     * use this to set the touched path of both players
     * if finger touched is within the threshold of dots
     * nearby, path will change color to indicate selection of dots
     * to be cleared
     * @param interaction
     * @param dotsScreen
     */
    public void setTouchedPath(DotsInteraction interaction, DotsScreen dotsScreen) {

        Log.d(TAG, "TOUCHPATH: " + interaction.toString());
//        if (interaction.getPlayerId() == 0) {
//            if (interaction.getState() != DotsInteractionStates.TOUCH_UP) {
//                int index = interaction.getDotsPoint().y* DotsAndroidConstants.BOARD_SIZE + interaction.getDotsPoint().x;
//                dotsScreen.getTouchedList()[index].setOne();
//                dotsScreen.getTouchedList()[index].setVisibility(VISIBLE);
//            } else if (interaction.getState() == DotsInteractionStates.TOUCH_UP) {
//                for (int i=0; i< dotsScreen.getTouchedList().length;i++) {
//                    if (dotsScreen.getTouchedList()[i].getColor().equals(DotColor.PLAYER_0)) {
//                        dotsScreen.getTouchedList()[i].setVisibility(INVISIBLE);
//                        Effects.castFadeOutEffect(dotsScreen.getDotList()[i],300,false,false);
//                        dotsScreen.getTouchedList()[i].setTouchedDot();
//                    }
//                }
//
//            }
//        } else if (interaction.getPlayerId() == 1) {
//            if (interaction.getState() != DotsInteractionStates.TOUCH_UP) {
//                int index = interaction.getDotsPoint().y*DotsAndroidConstants.BOARD_SIZE + interaction.getDotsPoint().x;
//                dotsScreen.getTouchedList()[index].setTwo();
//                dotsScreen.getTouchedList()[index].setVisibility(VISIBLE);
//            } else if (interaction.getState() == DotsInteractionStates.TOUCH_UP) {
//                for (int i=0; i< dotsScreen.getTouchedList().length;i++) {
//                    if (dotsScreen.getTouchedList()[i].getColor().equals(DotColor.PLAYER_1)) {
//                        dotsScreen.getTouchedList()[i].setVisibility(INVISIBLE);
//                        Effects.castFadeOutEffect(dotsScreen.getDotList()[i],300,false,false);
//                        dotsScreen.getTouchedList()[i].setTouchedDot();
//                    }
//                }
//            }
//        }
        boolean animate = interaction.isAnimate();
        boolean clearAll = interaction.isClearAll();

        int player = interaction.getPlayerId();
        DotsPoint point = interaction.getDotsPoint();
        // if not TOUCH_UP, draw the path
        if (interaction.getState() != DotsInteractionStates.TOUCH_UP) {
            // for player, draw touch path at point
            int index = point.y*DotsAndroidConstants.BOARD_SIZE + point.x;
            if (interaction.getPlayerId() == 0) {
                dotsScreen.getTouchedList()[index].setOne();
            } else {
                dotsScreen.getTouchedList()[index].setTwo();
            }
            dotsScreen.getTouchedList()[index].setVisibility(VISIBLE);
        } else {
            DotColor color;
            if (interaction.getPlayerId() == 0) {
                color = DotColor.PLAYER_0;
            } else {
                color = DotColor.PLAYER_1;
            }
            if (!animate && clearAll) {
                // for player, clear all touched paths without animating
                for (int i=0; i< dotsScreen.getTouchedList().length;i++){
                    if (dotsScreen.getTouchedList()[i].getColor().equals(color)) {
                        dotsScreen.getTouchedList()[i].setVisibility(INVISIBLE);
                        dotsScreen.getTouchedList()[i].setTouchedDot();
                    }
                }
            }
            else if (animate && !clearAll) {
                for (int i=0; i< dotsScreen.getTouchedList().length;i++){
                    if (dotsScreen.getTouchedList()[i].getColor().equals(color)) {
                        Effects.castFadeOutEffect(dotsScreen.getDotList()[i],300,false,false);
                    }
                }

            }
            else if (animate && clearAll) {
                // animate and clear all touch paths for player
                for (int i=0; i< dotsScreen.getTouchedList().length;i++){
                    if (dotsScreen.getTouchedList()[i].getColor().equals(color)) {
                        dotsScreen.getTouchedList()[i].setVisibility(INVISIBLE);
                        Effects.castFadeOutEffect(dotsScreen.getDotList()[i],300,false,false);
                        dotsScreen.getTouchedList()[i].setTouchedDot();
                    }
                }
            }

        }

    }




}