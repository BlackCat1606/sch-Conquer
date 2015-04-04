package darrenretinambpcrystalwell.dots;

import android.content.Context;
import android.graphics.Point;
import android.media.effect.Effect;
import android.transition.Fade;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Arrays;

import Constants.DotsConstants;
import Dots.Dot;
import Dots.DotColor;


/**
 * Created by DarrenRetinaMBP on 13/3/15.
 *
 * Main Screen when the game is loaded first
 * Takes in 1st randomised matrix from randomiser class
 *
 */
public class DotsScreen {

    private static final float SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float SCREEN_Y_PERCENTAGE = .2f;
    private float              x, y;

    DotView[] dotsList = new DotView[36];
    DotView[] touchList = new DotView[36];
//    private BlueDotView      blue;
//    private RedDotView       red;
//    private GreenDotView     green;
//    private YellowDotView    yellow;

    // Standard Variables call
    RelativeLayout          relativeLayout;
    Context                 context;
    RelativeLayout          dotsLayout;
    int                     screenWidth;
    int                     screenHeight;


    public float                   dotWidth;

//    DotView                 dotView;

    private float[][][] correspondingDotCoordinates;

    public float[][][] getCorrespondingDotCoordinates() {
        return correspondingDotCoordinates;
    }

    // Standard Initialising Constructor
    public DotsScreen(RelativeLayout relativeLayout, Context context) {
        this.context =        context;
        this.relativeLayout = relativeLayout;
//        this.dotView = new    DotView(context);
//
//        red    = new          RedDotView(context);
//        blue   = new          BlueDotView(context);
//        green  = new          GreenDotView(context);
//        yellow = new          YellowDotView(context);

        this.screenWidth =  ScreenDimensions.getWidth(context);
        this.screenHeight = ScreenDimensions.getHeight(context);

        this.dotsLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        dotsLayout.setLayoutParams(rlp);
        relativeLayout.addView(dotsLayout);

        this.dotWidth = SCREEN_WIDTH_PERCENTAGE * screenWidth / 6.f;

        float dotsXOffset = (1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * screenWidth;
        float dotsYOffset = SCREEN_Y_PERCENTAGE * screenHeight;

        this.correspondingDotCoordinates = new float[DotsConstants.BOARD_SIZE][DotsConstants.BOARD_SIZE][2];

        for (int index = 0; index < 36; ++index) {
            // i == row number (0-5)
            // j == col number (0-5)
            int i = index / 6;
            int j = index % 6;

            DotView d = new RedDotView(context);
            DotView t = new TouchedDot(context);

            float x = dotsXOffset + j * dotWidth;
            float y = dotsYOffset + i * dotWidth;


            d.setX(x);
            d.setY(y);
            d.setLayoutParams(new ViewGroup.LayoutParams((int) (dotWidth), (int) dotWidth));

            t.setX(x);
            t.setY(y);
            t.setLayoutParams(new ViewGroup.LayoutParams((int) (dotWidth), (int) (dotWidth)));

            touchList[index] = t;
            dotsLayout.addView(t);
            t.setVisibility(View.INVISIBLE);

            dotsList[index] = d;
            dotsLayout.addView(d);

            this.correspondingDotCoordinates[i][j][0] = (float) (x + dotWidth/2.0);
            this.correspondingDotCoordinates[i][j][1] = (float) (y + dotWidth/2.0);


        }

    }

    public float getDotWidth() {
        return dotWidth;
    }

    public void updateScreen(Dot[][] board) {
        Log.d("Screen", Arrays.deepToString(board));

        final int FADE_DURATION = 1000;
        final int END_ALPHA = 0;

        for (int j = 0; j < 6; j++) {

            for (int i = 0; i < board[j].length; i++) {

                int index = j*6 + i;

                Dot updatedBoardDot = board[j][i];
                DotView currentDotView = dotsList[index];

                // if the updated board color is different from the current dotView's color
                if (!(currentDotView.getColor() == updatedBoardDot.color)) {

                    // do fading effects
//                    Effects.castFadeOutEffect(currentDotView, FADE_DURATION, true, true);
//                    Effects.castFadeInEffect(currentDotView, FADE_DURATION, END_ALPHA, true);


                    // sets the color of the drawable
                    currentDotView.setColor(updatedBoardDot.color);


                }
                
            }

        }

//        for (int index = 0; index < 36; ++index) {
//
//            // i == row number (0-5)
//            // j == col number (0-5)
//            int i = index / 6;
//            int j = index % 6;
//
//            if (board[j][i].color == DotColor.RED) {
//                if (!dotsList[index].getColor().equals("red")) {
//                    Effects.castFadeOutEffect(getDotList()[index], 1000, false, true);
//                    Effects.castFadeInEffect(getDotList()[index], 1000, 1, true);
//                    dotsList[index].setRed();
//                }
//            } else if (board[j][i].color == DotColor.BLUE) {
//                if (!dotsList[index].getColor().equals("blue")) {
//                    Effects.castFadeOutEffect(getDotList()[index], 1000, false, true);
//                    Effects.castFadeInEffect(getDotList()[index], 1000, 1, true);
//                    dotsList[index].setBlue();
//
//                }
//            } else if (board[j][i].color == DotColor.GREEN) {
//                if (!dotsList[index].getColor().equals("green")) {
//                    Effects.castFadeOutEffect(getDotList()[index], 1000, false, true);
//                    Effects.castFadeInEffect(getDotList()[index], 1000, 1, true);
//                    dotsList[index].setGreen();
//                }
//            } else if (board[j][i].color == DotColor.YELLOW) {
//                if (!dotsList[index].getColor().equals("yellow")) {
//                    Effects.castFadeOutEffect(getDotList()[index], 1000, false, true);
//                    Effects.castFadeInEffect(getDotList()[index], 1000, 1, true);
//                    dotsList[index].setYellow();
//                }
//            }
//        }
    }

//    public DotView[] getDotList() {
//        return dotsList;
//    }

    public DotView[] getTouchedList() { return touchList;}
}


