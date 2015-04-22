package darrenretinambpcrystalwell.dots;


import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import Dots.DotColor;
import Dots.DotsPoint;
import Model.Interaction.DotsInteraction;
import Model.Interaction.DotsInteractionStates;
import Sockets.DotsClient;
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
    float touchThreshold;
    private final int                    PLAYER_ID;
    private final float[][][]            correspondingDotCoordinates;
    private final DotsServerClientParent dotsServerClientParent;
    private int countOne = 0;
    private int countTwo = 0;
    private int temp;


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
    private boolean touchEnabled;
    private boolean confused = true;

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
        this.touchThreshold = (float) (SCREEN_WIDTH_PERCENTAGE * ScreenDimensions.getWidth(context)
                / DotsAndroidConstants.BOARD_SIZE * 1.5);
        LayoutParams layoutParams        = new LayoutParams(ScreenDimensions.getWidth(context),
                ScreenDimensions.getHeight(context));

        this.touchEnabled = true;
        this.confused = false;
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

    public void setTouchEnabled(boolean touchEnabled) {
        this.touchEnabled = touchEnabled;
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
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
        Log.d(TAG, "confused " + this.confused);
        if (!this.dotsServerClientParent.isGameStarted()) {
            return false;
        }

        if (!this.touchEnabled) {
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

                    DotsPoint pointToReturn;
                    if (!confused) {
                        pointToReturn = new DotsPoint(i, j);

                    } else {
                        pointToReturn = new DotsPoint(j, i);
                    }

                    return pointToReturn;
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

        if (distance < touchThreshold /2.0) {
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

        int player = interaction.getPlayerId();
        DotsPoint point = interaction.getDotsPoint();

        // get pointers to the corresponding views for the interaction point
        int index = point.y*DotsAndroidConstants.BOARD_SIZE + point.x;
        DotView correspondingTouchSquare = dotsScreen.getTouchedList()[index];
        DotView correspondingDot = dotsScreen.getDotList()[index];

        // if it is not a touch up, it we simply draw the interaction on the screen for the
        // correct player
        if (interaction.getState() != DotsInteractionStates.TOUCH_UP) {

            // for player, draw touch path at point
            if (player == 0) {
                correspondingTouchSquare.setOne();
            } else {
                correspondingTouchSquare.setTwo();
            }
            correspondingTouchSquare.setVisibility(VISIBLE);

        } else {

            // if the interaction is a touchUp, we need to handle the update of the display carefully
            // according to the state variable that is stored in the interaction

            // retrieves and sets up states from the interaction
            boolean animate = interaction.isAnimate();
            boolean clearAll = interaction.isClearAll();

            // assigns a color to for comparison
            DotColor playerColor;
            if (player == 0) {
                playerColor = DotColor.PLAYER_0;
            } else {
                playerColor = DotColor.PLAYER_1;
            }

            // Initialise arrayLists used to store views that need to be updated
            ArrayList<DotView> touchSquaresToClear = new ArrayList<>();
            ArrayList<DotView> dotsToAnimate = new ArrayList<>();

            // if we need to clear all dots, we find all the needed views for the player
            // and add it to the arrayList
            if (clearAll) {

                // iterate through all the touchSquares
                for (int i=0; i< dotsScreen.getTouchedList().length;i++){
                    DotView currentSquare = dotsScreen.getTouchedList()[i];

                    // if the square corresponds to the player
                    if (currentSquare.getColor().equals(playerColor)) {

                        // add the touch square view to the arrayList
                        touchSquaresToClear.add(currentSquare);

                        // if we need to animate
                        if (animate) {

                            // add the corresponding view of the circular dot to the dotToAnimate arrayList
                            DotView dotToAnimate = dotsScreen.getDotList()[i];
                            dotsToAnimate.add(dotToAnimate);
                        }

                    }
                }
            } else {

                // no need to clear all, we simply add the corresponding view for the point stored in the interaction
                // to the arrayLists

                touchSquaresToClear.add(correspondingTouchSquare);

                if (animate) {
                    dotsToAnimate.add(correspondingDot);
                }
            }

            // make touch squares that need to be cleared invisible
            for (DotView touchSquareToClear : touchSquaresToClear) {
                touchSquareToClear.setVisibility(INVISIBLE);
                touchSquareToClear.setTouchedDot();

            }

            if (animate) {
                // fade out dots that need to be animated
                for (DotView dotToAnimate : dotsToAnimate) {
                    Effects.castFadeOutEffect(dotToAnimate,300,false,false);
                }
            }
        }
    }



//    public void setTouchedPath(DotsInteraction interaction, DotsScreen dotsScreen) {
//
//        Log.d(TAG, "TOUCHPATH: " + interaction.toString());
//        // get player ID and the point
//        int player = interaction.getPlayerId();
//        DotsPoint point = interaction.getDotsPoint();
//
//        // get pointers to the corresponding views for the interaction point
//        int index = point.y * DotsAndroidConstants.BOARD_SIZE + point.x;
//        DotView correspondingTouchSquare = dotsScreen.getTouchedList()[index];
////        DotView correspondingDot = dotsScreen.getDotList()[index];
//
//        // if it is not a touch up, it we simply draw the interaction on the screen for the
//        // correct player
//        if (interaction.getState() != DotsInteractionStates.TOUCH_UP) {
//
//            // for player, draw touch path at point
//            if (player == 0) {
//                correspondingTouchSquare.setOne();
//                countOne++;
//            } else {
//                correspondingTouchSquare.setTwo();
//                countTwo++;
//            }
//            correspondingTouchSquare.setVisibility(VISIBLE);
//
//        } else {
//            // if the interaction is a touchUp, we need to handle the update of the display carefully
//            // according to the state variable that is stored in the interaction
//
//            // retrieves and sets up states from the interaction
//            boolean animate = interaction.isAnimate();
//            boolean clearAll = interaction.isClearAll();
//
//            // assigns a color to for comparison
//            DotColor playerColor;
//            if (player == 0) {
//                playerColor = DotColor.PLAYER_0;
//                temp = countOne;
//            } else {
//                playerColor = DotColor.PLAYER_1;
//                temp = countTwo;
//            }
////            // Initialise arrayLists used to store views that need to be updated
////            ArrayList<DotView> touchSquaresToClear = new ArrayList<>();
////            ArrayList<DotView> dotsToAnimate = new ArrayList<>();
////
////            // if we need to clear all dots, we find all the needed views for the player
////            // and add it to the arrayList
////            if (clearAll) {
////
////                // iterate through all the touchSquares
////                for (int i=0; i< dotsScreen.getTouchedList().length;i++){
////                    DotView currentSquare = dotsScreen.getTouchedList()[i];
////
////                    // if the square corresponds to the player
////                    if (currentSquare.getColor().equals(playerColor)) {
////
////                        // add the touch square view to the arrayList
////                        touchSquaresToClear.add(currentSquare);
////
////                        // if we need to animate
////                        if (animate) {
////
////                            // add the corresponding view of the circular dot to the dotToAnimate arrayList
////                            DotView dotToAnimate = dotsScreen.getDotList()[i];
////                            dotsToAnimate.add(dotToAnimate);
////                        }
////
////                    }
////                }
////            } else {
////
////                // no need to clear all, we simply add the corresponding view for the point stored in the interaction
////                // to the arrayLists
////
////                touchSquaresToClear.add(correspondingTouchSquare);
////
////                if (animate) {
////                    dotsToAnimate.add(correspondingDot);
////                }
////
////            }
////
////            // make touch squares that need to be cleared invisible
////            for (DotView touchSquareToClear : touchSquaresToClear) {
////                touchSquareToClear.setVisibility(INVISIBLE);
////                touchSquareToClear.setTouchedDot();
////
////            }
////
////
////            if (animate) {
////                // fade out dots that need to be animated
////                for (DotView dotToAnimate : dotsToAnimate) {
////                    Effects.castFadeOutEffect(dotToAnimate,300,false,false);
////                }
////
////            }
////
//            if (!animate && clearAll) {
//                // for player, clear all touched paths without animating
//                for (int i = 0; i < dotsScreen.getTouchedList().length; i++) {
//                    // check for all the DotView && set back to TouchedDot
//                    if (dotsScreen.getTouchedList()[i].getColor().equals(playerColor)) {
//                        dotsScreen.getTouchedList()[i].setVisibility(INVISIBLE);
//                        dotsScreen.getTouchedList()[i].setTouchedDot();
//                    }
//                }
//            } else if (animate && clearAll) {
//                // to animate and to clear the touched path
//                for (int i = 0; i < dotsScreen.getTouchedList().length; i++) {
//                    // if it is not a single touch, legal move
//                    if (temp > 1) {
//                        if (dotsScreen.getTouchedList()[i].getColor().equals(playerColor)) {
//                            dotsScreen.getTouchedList()[i].setVisibility(INVISIBLE);
//                            Effects.castFadeOutEffect(dotsScreen.getDotList()[i], 300, false, false);
//                            dotsScreen.getTouchedList()[i].setTouchedDot();
//                        }
//                    }
//                    // if it is a single highlighted path, clearAll without Animating
//                    else {
//                        dotsScreen.getTouchedList()[i].setVisibility(INVISIBLE);
//                        dotsScreen.getTouchedList()[i].setTouchedDot();
//                    }
//
//                }
//                // reset counter again for the next set of moves by player
//                temp = 0;
//                countOne = 0;
//                countTwo = 0;
//            }
//        }
//    }
}