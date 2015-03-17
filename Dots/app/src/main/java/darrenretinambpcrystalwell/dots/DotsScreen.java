package darrenretinambpcrystalwell.dots;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by DarrenRetinaMBP on 13/3/15.
 *
 * Main Screen when the game is loaded first
 * Takes in 1st randomised matrix from randomiser class
 *
 */
public class DotsScreen extends Activity {

    private static final float  SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float  SCREEN_Y_PERCENTAGE = .2f;
    private float               x,y;
    private static String[][]   order = {{"R","R","B","G","R","B"},
                                        {"G","G","G","G","B","R"},
                                        {"B","B","B","G","R","G"},
                                        {"B","B","B","R","R","G"},
                                        {"R","G","G","G","R","G"},
                                        {"B","B","G","G","R","R"},
                                        {"R","B","G","R","R","B"},
                                        {"R","G","B","B","R","B"}};

    // Standard Variables call
    RelativeLayout           relativeLayout;
    Context                  context;
    RelativeLayout           dotsLayout;
    int                      screenWidth;
    int                      screenHeight;

    // Variables for dag motion
    private int              dragStatus;
    private static final int STOP_DRAGGING = 0;
    private static final int CURRENTLY_DRAGGING = 1;
    private static final int START_DRAGGING = 2;
    int                      currentIndex;
    float                    intersectDistSqThreshold;
    ArrayList<Integer>       visitedIndexes;

    Dot []                   dots;
    float                    dotWidth;

    // Standard Initialising Constructor
    public DotsScreen(RelativeLayout relativeLayout, Context context) {
        this.relativeLayout = relativeLayout;
        this.context = context;

        this.screenWidth = ScreenDimensions.getWidth(context);
        this.screenHeight = ScreenDimensions.getHeight(context);

        this.dotsLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        dotsLayout.setLayoutParams(rlp);
        relativeLayout.addView(dotsLayout);

        this.dotWidth = SCREEN_WIDTH_PERCENTAGE * screenWidth / 6.f;
        this.intersectDistSqThreshold = (dotWidth * .5f) * (dotWidth * .5f);

        float dotsXOffset = (1.f-SCREEN_WIDTH_PERCENTAGE) * .5f * screenWidth;
        float dotsYOffset = SCREEN_Y_PERCENTAGE * screenHeight;

        this.visitedIndexes = new ArrayList<>();

        dots = new Dot[36];
        for (int index=0; index<36; ++index) {
            // i == row number (0-5)
            // j == col number (0-5)
            int i = index/6;
            int j = index%6;

            //Need to change to compare with randomiser class function
            if(order[i][j] == "R") {
                Dot d = new RedDot(context);
                d.setX(dotsXOffset + j*dotWidth);
                d.setY(dotsYOffset + i*dotWidth);
                d.setLayoutParams(new ViewGroup.LayoutParams((int)dotWidth, (int)dotWidth));
                dots[index] = d;
                dotsLayout.addView(d);
            }
            else if (order[i][j] == "B") {
                Dot d = new BlueDot(context);
                d.setX(dotsXOffset + j*dotWidth);
                d.setY(dotsYOffset + i*dotWidth);
                d.setLayoutParams(new ViewGroup.LayoutParams((int)dotWidth, (int)dotWidth));
                dots[index] = d;
                dotsLayout.addView(d);
            }
            else if (order[i][j] == "G") {
                Dot d = new GreenDot(context);
                d.setX(dotsXOffset + j*dotWidth);
                d.setY(dotsYOffset + i*dotWidth);
                d.setLayoutParams(new ViewGroup.LayoutParams((int)dotWidth, (int)dotWidth));
                dots[index] = d;
                dotsLayout.addView(d);
            }
        }
    }

    public Dot[] getDots() {
        return dots;
    }



}
