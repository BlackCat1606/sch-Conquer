package darrenretinambpcrystalwell.dots;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import Game.Dot;
import Game.DotColor;
import Game.DotsBoard;
import Game.DotsLogic;

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
    private float x, y;
    DotView[] dotsList = new DotView[36];
    private BlueDotView      blue;
    private RedDotView       red;
    private GreenDotView     green;
    private YellowDotView    yellow;

    // Standard Variables call
    RelativeLayout  relativeLayout;
    Context         context;
    RelativeLayout  dotsLayout;
    int             screenWidth;
    int             screenHeight;

    float           intersectDistSqThreshold;

    DotView[]       dotViews;
    float           dotWidth;
    final DotsBoard board;
    final DotsLogic logic;
    DotView         dotView;




    // Standard Initialising Constructor
    public DotsScreen(RelativeLayout relativeLayout, Context context) {
        this.context =        context;
        this.relativeLayout = relativeLayout;
        this.dotView = new    DotView(context);

        red    = new    RedDotView(context);
        blue   = new    BlueDotView(context);
        green  = new    GreenDotView(context);
        yellow = new    YellowDotView(context);

        this.screenWidth =  ScreenDimensions.getWidth(context);
        this.screenHeight = ScreenDimensions.getHeight(context);

        this.dotsLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        dotsLayout.setLayoutParams(rlp);
        relativeLayout.addView(dotsLayout);

        this.dotWidth = SCREEN_WIDTH_PERCENTAGE * screenWidth / 6.f;
        this.intersectDistSqThreshold = (dotWidth * .5f) * (dotWidth * .5f);

        float dotsXOffset = (1.f - SCREEN_WIDTH_PERCENTAGE) * .5f * screenWidth;
        float dotsYOffset = SCREEN_Y_PERCENTAGE * screenHeight;

        this.board = new DotsBoard(6);
        this.logic = new DotsLogic(board);
        Dot[][] dotBoard = board.getBoardArray();



        Log.d("Screen", board.toString());
        dotViews = new DotView[36];
        for (int index = 0; index < 36; ++index) {
            // i == row number (0-5)
            // j == col number (0-5)
            int i = index / 6;
            int j = index % 6;
            if (dotBoard[i][j].color == DotColor.RED) {
                DotView d = new RedDotView(context);
                d.setX(dotsXOffset + j * dotWidth);
                d.setY(dotsYOffset + i * dotWidth);
                d.setLayoutParams(new ViewGroup.LayoutParams((int) dotWidth, (int) dotWidth));
                dotViews[index] = d;
                dotsList[index] = d;
                dotsLayout.addView(d);
            } else if (dotBoard[i][j].color == DotColor.BLUE) {
                DotView d = new BlueDotView(context);
                d.setX(dotsXOffset + j * dotWidth);
                d.setY(dotsYOffset + i * dotWidth);
                d.setLayoutParams(new ViewGroup.LayoutParams((int) dotWidth, (int) dotWidth));
                dotViews[index] = d;
                dotsList[index] = d;
                dotsLayout.addView(d);
            } else if (dotBoard[i][j].color == DotColor.GREEN) {
                DotView d = new GreenDotView(context);
                d.setX(dotsXOffset + j * dotWidth);
                d.setY(dotsYOffset + i * dotWidth);
                d.setLayoutParams(new ViewGroup.LayoutParams((int) dotWidth, (int) dotWidth));
                dotViews[index] = d;
                dotsList[index] = d;
                dotsLayout.addView(d);
            } else if (dotBoard[i][j].color == DotColor.YELLOW) {
                DotView d = new YellowDotView(context);
                d.setX(dotsXOffset + j * dotWidth);
                d.setY(dotsYOffset + i * dotWidth);
                d.setLayoutParams(new ViewGroup.LayoutParams((int) dotWidth, (int) dotWidth));
                dotViews[index] = d;
                dotsList[index] = d;
                dotsLayout.addView(d);
            }
        }
    }

    public float getDotWidth() {
        return dotWidth;
    }

    public void updateScreen() {
        this.board.initializeBoard(6);
        Dot[][] dotBoard = board.getBoardArray();

        Log.d("Screen", board.toString());
        dotViews = new DotView[36];
        for (int index = 0; index < 36; ++index) {

            int i = index / 6;
            int j = index % 6;
            if (dotBoard[i][j].color == DotColor.RED) {
                if (!dotsList[index].getColor().equals("red")) {
                    dotsList[index].setRed();
                }
            } else if (dotBoard[i][j].color == DotColor.BLUE) {
                if (!dotsList[index].getColor().equals("blue")) {
                    dotsList[index].setBlue();
                }
            } else if (dotBoard[i][j].color == DotColor.GREEN) {
                if (!dotsList[index].getColor().equals("green")) {
                    dotsList[index].setGreen();
                }
            } else if (dotBoard[i][j].color == DotColor.YELLOW) {
                if (!dotsList[index].getColor().equals("yellow")) {
                    dotsList[index].setYellow();
                }
            }
        }
    }

    public DotView[] getDotList() {
        return dotsList;
    }


}


