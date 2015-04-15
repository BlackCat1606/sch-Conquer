package darrenretinambpcrystalwell.dots;

import java.text.NumberFormat;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Darren Ng on 8/4/15.
 *
 * 1000568
 *
 * ScoreBoard View to display Score
 *
 *
 */
public class ScoreBoard extends TextView {

    RelativeLayout relativeLayout;

    private static final float SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float SCREEN_Y_PERCENTAGE     = .2f;
    private static final float FONT_SIZE               = 20.f;
    private static float       screenDensity;


    private static final NumberFormat SCORE_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    private float              x, y;
    private int                actualScore;

    /**
     * Construtor to initialise scoreboard
     *
     * sets the board according to the default size
     * based on the screen size
     * @param relativeLayout
     * @param context
     *
     *
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ScoreBoard(RelativeLayout relativeLayout, Context context) {
        super(context);
        this.relativeLayout = relativeLayout;
        this.screenDensity  = ScreenDimensions.getDensity(context);

        actualScore         = 0;

        // Formatting the Text
        setText(SCORE_FORMAT.format(actualScore));
        setTextSize(FONT_SIZE*screenDensity);
        setTextIsSelectable(false);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setSingleLine(true);
        setTypeface(Typeface.DEFAULT_BOLD);
        setY(-2.f);
        setTextColor(Color.rgb(255,255,255));
        setGravity(Gravity.CENTER_HORIZONTAL);

    }

    /**
     *
     * @return the size of the font
     */
    public float getFontSize() {
        return FONT_SIZE;
    }

    /**
     * use this to set the score of the current game
     * @param score
     *
     */
    public void setScore(final int score) {
        synchronized (this) {
            actualScore = score;
            setText(SCORE_FORMAT.format(actualScore));

        }
    }

    /**
     * use this to return the current score of the game
     * @return int of the current score
     */
    public int getScore() {
        synchronized (this) {
            return actualScore;
        }
    }

}
