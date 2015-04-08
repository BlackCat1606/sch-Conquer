package darrenretinambpcrystalwell.dots;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
 */
public class ScoreBoard extends TextView {

    RelativeLayout relativeLayout;



    int                     screenWidth;
    int                     screenHeight;


    private static final float SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float SCREEN_Y_PERCENTAGE     = .2f;
    private static final float FONT_SIZE               = 22.f;


    private static final NumberFormat SCORE_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    private float              x, y;
    private int                actualScore;

    public ScoreBoard(RelativeLayout relativeLayout, Context context) {
        super(context);
        this.relativeLayout = relativeLayout;

        actualScore         = 0;


        // Formatting the Text
        setText(SCORE_FORMAT.format(actualScore));
        setTextSize(FONT_SIZE);
        setTextIsSelectable(false);
        setSingleLine(true);
        setTypeface(Typeface.DEFAULT_BOLD);
        setY(-2.f);
        setTextColor(Color.rgb(255,255,255));
        setGravity(Gravity.CENTER_HORIZONTAL);

    }

    public void setScore(int score) {
        actualScore = score;
        this.setText(SCORE_FORMAT.format(actualScore));
    }

}
