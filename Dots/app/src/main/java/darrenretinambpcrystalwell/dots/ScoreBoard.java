package darrenretinambpcrystalwell.dots;

import android.app.Activity;
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

    private static final float SCREEN_WIDTH_PERCENTAGE = .8f;
    private static final float SCREEN_Y_PERCENTAGE     = .2f;
    private static final float FONT_SIZE               = 50.f;


    private static final NumberFormat SCORE_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    private float              x, y;
    private int                actualScore;

    private final Context context;

    public ScoreBoard(RelativeLayout relativeLayout, Context context) {
        super(context);
        this.relativeLayout = relativeLayout;
        this.context = context;
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

    public float getFontSize() {
        return FONT_SIZE;
    }

    public void setScore(final int score) {
        synchronized (this) {
//            actualScore = score;

            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    while (actualScore != score) {


                        setText(SCORE_FORMAT.format(actualScore));
                        actualScore+=1;
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }

                }

//            Thread animateThread = new Thread(
//            });
//            animateThread.start();
            });
        }
    }

    public int getScore() {
        synchronized (this) {
            return actualScore;
        }
    }

}
