package darrenretinambpcrystalwell.dots;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Arrays;

import Constants.DotsConstants;
import Dots.Dot;


/**
 * Created by DarrenRetinaMBP on 13/3/15.
 *
 * Main Screen when the game is loaded first
 * Takes in 1st randomised matrix from randomising class
 *
 */
public class DotsScreen {

    private static final float SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float SCREEN_Y_PERCENTAGE = .3f;
    private float              x, y;


    DotView[] dotsList = new DotView[36];
    DotView[] touchList = new DotView[36];

    ImageView score;
    ImageView opponent;
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
    float                   screenDensity;
    public ScoreBoard              scoreBoard0;
    public ScoreBoard              scoreBoard1;


    public float            dotWidth;

//    DotView                 dotView;

    private float[][][] correspondingDotCoordinates;


    final int FADE_DURATION = 300;
    final int END_ALPHA = 1;

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

        this.screenWidth    =  ScreenDimensions.getWidth(context);
        this.screenHeight   = ScreenDimensions.getHeight(context);
        this.screenDensity  = ScreenDimensions.getDensity(context);

        scoreBoard0         = new ScoreBoard(relativeLayout, context);
        scoreBoard1         = new ScoreBoard(relativeLayout, context);



        this.dotsLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        dotsLayout.setLayoutParams(rlp);
        relativeLayout.addView(dotsLayout);

        score    = new ImageView(context);
        opponent = new ImageView(context);
        score.setImageBitmap(BitmapImporter.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.score,
                (int)screenDensity*300,(int)screenDensity*300
        ));

        opponent.setImageBitmap(BitmapImporter.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.enemy,
                (int)screenDensity*300,(int)screenDensity*300
        ));

        score.setX((screenWidth/4)- scoreBoard0.getFontSize() - (300/4));
        score.setY((screenHeight/7) - scoreBoard0.getFontSize()*2);

        scoreBoard0.setX((screenWidth / 4) - scoreBoard0.getFontSize());
        scoreBoard0.setY((screenHeight/7) - scoreBoard0.getFontSize());
        dotsLayout.addView(scoreBoard0);

        opponent.setX(((screenWidth /4)*3) - scoreBoard1.getFontSize() - (300/3));
        opponent.setY((screenHeight/7)    - scoreBoard1.getFontSize()*2);

        scoreBoard1.setX(((screenWidth /4)*3) - scoreBoard1.getFontSize());
        scoreBoard1.setY((screenHeight/7)    - scoreBoard1.getFontSize());
        dotsLayout.addView(scoreBoard1);
        dotsLayout.addView(score);
        dotsLayout.addView(opponent);

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



        for (int j = 0; j < 6; j++) {

            for (int i = 0; i < board[j].length; i++) {

                int index = j*6 + i;

                final Dot updatedBoardDot = board[j][i];
                final DotView currentDotView = dotsList[index];

                // if the updated board color is different from the current dotView's color

                if (!(currentDotView.getColor() == updatedBoardDot.color)) {


//                    do fading effects
                    Effects.castFadeOutEffect(currentDotView, FADE_DURATION, true, true);
                    Effects.castFadeInEffect(currentDotView, FADE_DURATION, END_ALPHA, true);

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Effects.castFadeOutEffect(currentDotView, FADE_DURATION, true, false);
                        }
                    });

                    // Create a thread to cast a fade in animation
                    Thread fadeIn = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            // first sleep for the duration where the views are fading out
                            try {
                                Thread.sleep(FADE_DURATION);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // run this on the UI thread
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                     // update the color
                                    currentDotView.setColor(updatedBoardDot.color);

                                    // cast the fade in effect
                                    Effects.castFadeInEffect(currentDotView, FADE_DURATION, END_ALPHA, true);
                                }
                            });

                        }
                    });

                    fadeIn.start();

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

    public DotView[] getDotList() {
        return dotsList;
    }

    public DotView[] getTouchedList() { return touchList;}
}


