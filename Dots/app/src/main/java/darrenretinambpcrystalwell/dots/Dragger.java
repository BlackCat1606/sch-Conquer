package darrenretinambpcrystalwell.dots;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DarrenRetinaMBP on 13/3/15.
 */
public class Dragger implements View.OnTouchListener {
    RelativeLayout relativeLayout;
    Context context;
    float x,y;

    // Variables for dag motion
    private int                 dragStatus;
    private static final int STOP_DRAGGING = 0;
    private static final int CURRENTLY_DRAGGING = 1;
    private static final int START_DRAGGING = 2;
    int                      currentIndex;
    float                    intersectDistSqThreshold;
    ArrayList<Integer>       visitedIndexes;

    DotsScreen dotsScreen;

    public Dragger (RelativeLayout relativeLayout, Context context) {
        this.relativeLayout = relativeLayout;
        this.context = context;
        dotsScreen = new DotsScreen(relativeLayout, context);
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        x = event.getX();
        y = event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            dragStatus = START_DRAGGING;
            currentIndex = getTouchedDotIndex();
            String msg = Integer.toString(currentIndex);
            Log.d(msg, msg);
        }
        if (action == MotionEvent.ACTION_UP) {
            dragStatus = STOP_DRAGGING;
            currentIndex = -1;
        }
        if (action == MotionEvent.ACTION_MOVE) {
            if (dragStatus == START_DRAGGING) {
                dragStatus = CURRENTLY_DRAGGING;
            } else if (dragStatus == CURRENTLY_DRAGGING) {
                int touchedDotIndex = getTouchedDotIndex();

                // Logic for correct touch
//                if (touchedDotIndex != currentIndex) { // Means the finger drag till new dot
//                    if (isNeighbor(touchedDotIndex, currentIndex)) { // Valid neighbor hitted
//                        if(dotsScreen.getDots()[currentIndex] == dotsScreen.getDots()[touchedDotIndex]) {
//                            dotsScreen.getDots()[currentIndex] = DotsScreen.ClearDot(context);
//                            visitedIndexes.add(touchedDotIndex);
//                        } else {
//                            dragStatus = STOP_DRAGGING;
//                            currentIndex = -1;
//                        }
//                    }
//                }
            }
        }
        return true;
    }

    public boolean isNeighbor(int currentIndex, int newIndex) {
        int t = currentIndex % 6;
        boolean hasRight = t != 5;
        boolean hasLeft  = t != 0;
        boolean hasUp    = currentIndex <= 29;
        boolean hasDown  = currentIndex >= 6;
        return ((hasRight && currentIndex+1 == newIndex) ||
                (hasLeft  && currentIndex-1 == newIndex) ||
                (hasUp    && currentIndex-6 == newIndex) ||
                (hasDown  && currentIndex+6 == newIndex) ||
                (hasUp    && hasLeft  && currentIndex-6-1 == newIndex) ||
                (hasUp    && hasRight && currentIndex-6+1 == newIndex) ||
                (hasDown  && hasLeft  && currentIndex+6-1 == newIndex) ||
                (hasDown  && hasRight && currentIndex+6+1 == newIndex));
    }

    //finger touched approximation using
    //Sqrt Threshold formula
    public int getTouchedDotIndex() {
        for (int index=0; index<36; ++index) {
            if (dotsScreen.getDots()[index].getSqDist(x, y) < intersectDistSqThreshold) {
                return index;
            }
        }
        return -1;
    }
}
