package darrenretinambpcrystalwell.dots;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Constants.DotsConstants;
import Dots.Dot;
import Dots.DotsPoint;


/**
 * Created by DarrenRetinaMBP on 13/3/15.
 *
 * Sets the main game screen
 * Constructor to initialise the dotsBoard
 *
 */
public class DotsScreen {

    private static final float SCREEN_WIDTH_PERCENTAGE = .9f;
    private static final float SCREEN_Y_PERCENTAGE = .3f;
    private float              x, y;

    ImageView               score;
    ImageView               opponent;


    ImageView               confused;
    ImageView               freeze;

    // Standard Variables call
    RelativeLayout          relativeLayout;
    Context                 context;
    RelativeLayout          dotsLayout;
    int                     screenWidth;
    int                     screenHeight;
    float                   screenDensity;
    float                   scoreWidth;
    float                   scoreHeight;
    public ScoreBoard       scoreBoard0;
    public ScoreBoard       scoreBoard1;

    //Added by Darren
    //Change this to change the board size Dot X Dot
    private float           numberdotXdot = DotsAndroidConstants.BOARD_SIZE;

    DotView[] dotsList  = new DotView[(int)(numberdotXdot*numberdotXdot)];
    DotView[] touchList = new DotView[(int)(numberdotXdot*numberdotXdot)];

    public float            dotWidth;

    private float[][][] correspondingDotCoordinates;

    final int FADE_DURATION = 300;
    final int END_ALPHA     = 1;

    /**
     * To get the coordinates of the dots
     * @return a nested list of dots with their corresponding x,y coordinates
     */
    public float[][][] getCorrespondingDotCoordinates() {
        return correspondingDotCoordinates;
    }

    /**
     *
     *Standard Initialising Constructor
     * @param relativeLayout
     * @param  context
     *
      */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public DotsScreen(RelativeLayout relativeLayout, final Context context) {
        this.context =        context;
        this.relativeLayout = relativeLayout;


        this.screenWidth    =  ScreenDimensions.getWidth(context);
        this.screenHeight   = ScreenDimensions.getHeight(context);
        this.screenDensity  = ScreenDimensions.getDensity(context);

        scoreBoard0         = new ScoreBoard(relativeLayout, context);
        scoreBoard1         = new ScoreBoard(relativeLayout, context);

        scoreWidth          = (int) screenDensity*100;
        scoreHeight         = (int) screenDensity*30;

        this.dotsLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        dotsLayout.setLayoutParams(rlp);
        relativeLayout.addView(dotsLayout);

        score    = new ImageView(context);
        opponent = new ImageView(context);
        confused = new ImageView(context);
        freeze   = new ImageView(context);
        score.setImageBitmap(BitmapImporter.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.score,
                scoreWidth, scoreHeight
        ));

        opponent.setImageBitmap(BitmapImporter.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.enemy,
                scoreWidth, scoreHeight
        ));

        confused.setImageBitmap(BitmapImporter.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.confuse_page,
                (screenWidth), (screenHeight)
        ));
        freeze.setImageBitmap(BitmapImporter.decodeSampledBitmapFromResource(
                context.getResources(), R.drawable.freeze_page,
                (screenWidth), (screenHeight)
        ));


        freeze.setLayoutParams(new ViewGroup.LayoutParams((int) (screenWidth), (int) (screenHeight)));

        confused.setLayoutParams(new ViewGroup.LayoutParams((int) (screenWidth), (int) (screenHeight)));
        score.setLayoutParams(new ViewGroup.LayoutParams((int) (scoreWidth), (int) scoreHeight));
        opponent.setLayoutParams(new ViewGroup.LayoutParams((int) (scoreWidth), (int) scoreHeight));

        int x_score = (int) (((1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * screenWidth) + 0.5f*(SCREEN_WIDTH_PERCENTAGE * screenWidth / numberdotXdot));
        int x_oppo = (int) (((1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * screenWidth) + 5f*(SCREEN_WIDTH_PERCENTAGE * screenWidth / numberdotXdot));

        scoreBoard0.setWidth((int) scoreWidth);
        scoreBoard0.setX(x_score);

        scoreBoard0.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        scoreBoard1.setWidth((int) scoreWidth);
        scoreBoard1.setX(x_oppo);
        scoreBoard0.setY(screenHeight/8);
        scoreBoard1.setY(screenHeight/8);
        scoreBoard1.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);


        score.setX(x_score);
        score.setY(scoreBoard0.getY() - ((1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * screenWidth));

        opponent.setX(x_oppo);
        opponent.setY(scoreBoard1.getY() - ((1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * screenWidth));



        dotsLayout.addView(scoreBoard1);
        dotsLayout.addView(scoreBoard0);
        dotsLayout.addView(score);
        dotsLayout.addView(opponent);


        this.dotWidth = SCREEN_WIDTH_PERCENTAGE * screenWidth / numberdotXdot;

        float dotsXOffset = (1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * screenWidth;
        float dotsYOffset = SCREEN_Y_PERCENTAGE * screenHeight;

        this.correspondingDotCoordinates = new float[DotsConstants.BOARD_SIZE][DotsConstants.BOARD_SIZE][2];

        for (int index = 0; index < (numberdotXdot*numberdotXdot); ++index) {
            // i == row number (0-5)
            // j == col number (0-5)
            int i = index / (int)numberdotXdot;
            int j = index % (int)numberdotXdot;

            DotView d = new TransparentDotView(context);
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

        dotsLayout.addView(confused);
        dotsLayout.addView(freeze);
        confused.setAlpha(200);
        confused.bringToFront();

        freeze.setAlpha(200);
        freeze.bringToFront();
        freeze.setVisibility(View.GONE);
        confused.setVisibility(View.GONE);




    }
    /**
     * @return the width of the dot in float
     */
    public float getDotWidth() {
        return dotWidth;
    }

    /**
     * updates the current board of dots to a new set of dots
     * @param updatedPoints
     */
    public void updateScreen(ArrayList<DotsPoint> updatedPoints) {

        for (final DotsPoint point : updatedPoints) {

            int index = point.y * DotsConstants.BOARD_SIZE + point.x;

            final DotView currentDotView = dotsList[index];


////                    do fading effects
//            Effects.castFadeOutEffect(currentDotView, FADE_DURATION, true, true);
//            Effects.castFadeInEffect(currentDotView, FADE_DURATION, END_ALPHA, true);
//
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Effects.castFadeOutEffect(currentDotView, FADE_DURATION, false, false);
//                }
//            });

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
                            if (point.getColor() == null) {
                                Log.d("DOtsscreen", "color null");
                            }

                            if (point.getPowerUp() == null) {
                                Log.d("DOtsscreen", "powerup null");
                            }
                            currentDotView.setColor(point.getColor(), point.getPowerUp());

                            // cast the fade in effect
                            Effects.castFadeInEffect(currentDotView, FADE_DURATION, END_ALPHA, true);
                        }
                    });

                }
            });

            fadeIn.start();

        }

    }

    /**
     * returns the list of dots in the board
     * @return the list of DotView
     */
    public DotView[] getDotList() {
        return dotsList;
    }

    public DotView[] getTouchedList() { return touchList;}

    public ImageView getConfused() {
        return confused;
    }

    public ImageView getFreeze() {
        return freeze;
    }


}


