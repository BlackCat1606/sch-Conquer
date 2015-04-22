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
    private float[] previousActualTouchCoordinate;

    private final float dotWidth;

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
        this.dotWidth = (float) (SCREEN_WIDTH_PERCENTAGE * ScreenDimensions.getWidth(context)
                / DotsAndroidConstants.BOARD_SIZE);

        this.touchThreshold = this.dotWidth* 1.5f;

        LayoutParams layoutParams        = new LayoutParams(ScreenDimensions.getWidth(context),
                ScreenDimensions.getHeight(context));

        this.touchEnabled = true;
        this.confused = true;
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
        Log.d("POWERUP", "confused set to : " + confused);
        this.confused = confused;
    }

    private DotsInteraction previousInteraction;


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // dont do if game is not started
        if (!this.dotsServerClientParent.isGameStarted()) {
            return false;
        }

        // dont do if touch is not enabled
        if (!this.touchEnabled) {
            return false;
        }

        // maps the event to an interaction state
        DotsInteractionStates interactionState = this.getInteractionStateFromEvent(event);

        // gets the closest point to the touch that is within a threshold
        DotsPoint closestPoint = dotPointClosestToTouchedLocation(event.getX(), event.getY());

        // initialize as null first, so we only act upon it if it has been reassigned to a useful interaction to be executed
        DotsInteraction interactionToDo = null;

        if (closestPoint != null) {
            // if there is a point detected

            interactionToDo = new DotsInteraction(PLAYER_ID, interactionState, closestPoint);

            // Compare the detected point to the previous interaction stored

            // first check to see if the previous interaction has been assigned
            if (this.previousInteraction != null) {

                // and if its the same as the current point, we simply return to avoid doing the same
                // interaction multiple times
                if (interactionToDo.compareWith(this.previousInteraction)) {
                    return true;
                }
            }

        } else {

            // if there is no closest point, we still want to do interaction if the interaction state is a touch up.
            if (interactionState == DotsInteractionStates.TOUCH_UP) {
                interactionToDo = new DotsInteraction(PLAYER_ID, interactionState, this.previousInteraction.getDotsPoint());
            }
        }

        // only save previous interaction and act on it if it has been reassigned

        if (interactionToDo != null) {

            this.previousInteraction = interactionToDo;
            this.doPlayerInteraction(interactionToDo);

        }

        // return true to mean that we have consumed the current listener
        return true;
    }

    /**
     * Helper method to map a motion event to a DotsInteractionState
     * @param event
     * @return
     */
    private DotsInteractionStates getInteractionStateFromEvent(MotionEvent event) {

        // gets the interaction state
        DotsInteractionStates interactionState;

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            interactionState = DotsInteractionStates.TOUCH_DOWN;
        } else if (action == MotionEvent.ACTION_MOVE) {
            interactionState = DotsInteractionStates.TOUCH_MOVE;
        } else  {
            interactionState = DotsInteractionStates.TOUCH_UP;
        }

        return interactionState;
    }

    /**
     * use this to get the input && finger interaction states
     * @param v
     * @param event
     * @return boolean, of the drag status of the finger
     */
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        // if game is not started yet, we just return to avoid nullpointer exceptions
//        if (!this.dotsServerClientParent.isGameStarted()) {
//            return false;
//        }
//
//        if (!this.touchEnabled) {
//            return false;
//        }
//
//        DotsInteractionStates interactionState;
//
//        int action = event.getAction();
//
//        if (action == MotionEvent.ACTION_DOWN) {
//            interactionState = DotsInteractionStates.TOUCH_DOWN;
//
//        } else if (action == MotionEvent.ACTION_MOVE) {
//            interactionState = DotsInteractionStates.TOUCH_MOVE;
//
//
//        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//            interactionState = DotsInteractionStates.TOUCH_UP;
//
//        } else {
//            Log.d(TAG, "Unhandled motion event: " + action);
//            return false;
//        }
//
//        boolean notActionUp = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE;
//
//        if (notActionUp) {
//
//            // return if touch location has not moved far away enough so we reduce calculations
//            // We only perform this check if its not a touch up
//            if (this.previousDetectedDotCoordinate != null) {
//                if (touchedLocationCloseEnoughToReference(event.getX(), event.getY(), this.previousDetectedDotCoordinate[0], this.previousDetectedDotCoordinate[1])) {
//                    return false;
//                }
//
//            }
//        }
//
//        // changes the touch if you are confused
//        float changedTouchedX;
//        float changedTouchedY;
//        if (!this.confused || interactionState == DotsInteractionStates.TOUCH_DOWN) {
//            changedTouchedX = event.getX();
//            changedTouchedY = event.getY();
//
//        } else {
//
//            // transform the point
//
//            // distance between touchpoint and previous detected dot
//            float previousYDistance = this.previousDetectedDotCoordinate[1] - this.previousActualTouchCoordinate[1];
//            float currentYDistance = this.previousDetectedDotCoordinate[1] - event.getY();
//
//            float previousXDistance = this.previousDetectedDotCoordinate[0] - this.previousActualTouchCoordinate[0];
//            float currentXDistance = this.previousDetectedDotCoordinate[0] - event.getX();
//
//            int actualTouchHasGoneUp;
//            if (previousYDistance < currentYDistance) {
//                actualTouchHasGoneUp = 1;
//                changedTouchedY = this.previousDetectedDotCoordinate[1] + this.dotWidth;
//            } else if (previousYDistance > currentYDistance) {
//                actualTouchHasGoneUp = -1;
//                changedTouchedY = this.previousDetectedDotCoordinate[1] - this.dotWidth;
//            } else {
//                actualTouchHasGoneUp = 0;
//                changedTouchedY = this.previousDetectedDotCoordinate[1];
//            }
//
//            int actualTouchHasGoneLeft;
//            if (previousXDistance < currentXDistance) {
//                actualTouchHasGoneLeft = 1;
//                changedTouchedX = this.previousDetectedDotCoordinate[0] + this.dotWidth;
//            } else if (previousXDistance > currentXDistance) {
//                actualTouchHasGoneLeft = -1;
//                changedTouchedX = this.previousDetectedDotCoordinate[0] - this.dotWidth;
//            } else {
//                actualTouchHasGoneLeft = 0;
//                changedTouchedX = this.previousDetectedDotCoordinate[0];
//            }
//
//        }
//
//
//        // point that will be used
//        DotsPoint closestPoint = dotPointClosestToTouchedLocation(changedTouchedX, changedTouchedY);
//
//        // may not be used depending on confusion
//        DotsPoint touchedPoint = dotPointClosestToTouchedLocation(event.getX(), event.getY());
//
//        DotsInteraction interaction;
////
//        // this part is to deal with touch up, on a strange area of the screen far away from the dotviews
//        if (closestPoint == null) {
//
//            if (interactionState == DotsInteractionStates.TOUCH_UP) {
//                // sends a touch up interaction with the DotPoint of the previous interaction
//                interaction = new DotsInteraction(PLAYER_ID, interactionState, previousInteraction.getDotsPoint());
//
//            } else {
//                return false;
//            }
//        } else {
//            interaction = new DotsInteraction(PLAYER_ID, interactionState, closestPoint);
//            this.previousDetectedDotCoordinate = this.correspondingDotCoordinates[closestPoint.y][closestPoint.x];
//            this.previousActualTouchCoordinate = this.correspondingDotCoordinates[touchedPoint.y][touchedPoint.x];
//        }
//////        } else {
//////        }
//////
////////        else {
////////
//////
////        if (closestPoint == null) {
////            return false;
////        }
//
//
//        // if its a touch up, we want to clear the previous detected dot coordinate
//        if (interactionState == DotsInteractionStates.TOUCH_UP) {
////            this.previousDetectedDotCoordinate = new float[] {
////                    (float)9999, (float)9999
////            };
//            this.previousDetectedDotCoordinate = null;
//        }
//
//        if (interaction.compareWith(this.previousInteraction)) {
//            return false;
//        }
//
//        this.doPlayerInteraction(interaction);
//
//        // saves the current interaction
//        this.previousInteraction = interaction;
//
//        return true;
//
//    }


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

                    // if we are confused, swap the touched points
//                    if (!confused) {
                        pointToReturn = new DotsPoint(i, j);

//                    } else {
//                        pointToReturn = new DotsPoint(j, i);
//                    }

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
            Log.d(TAG, "Doing interaction: " + interaction.toString());
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