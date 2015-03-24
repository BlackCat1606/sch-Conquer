package darrenretinambpcrystalwell.dots;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by DarrenRetinaMBP on 13/3/15.
 *
 * Helper class to get dimension of all screen types
 */
public class ScreenDimensions {
    private int screenWidth = -1;
    private int screenHeight = -1;
    private float screenDensity = -1.f;
    private boolean hasDimension = false;

    private final static ScreenDimensions instance = new ScreenDimensions();
    private ScreenDimensions() {

    }

    public static int getWidth(Context context) {
        instance.getDimensions(context);
        return instance.screenWidth;
    }

    public static int getHeight(Context context) {
        instance.getDimensions(context);
        return instance.screenHeight;
    }

    public static float getDensity(Context context) {
        instance.getDimensions(context);
        return instance.screenDensity;
    }

    private void getDimensions(Context context) {
        if (hasDimension == true) {
            return;
        } else {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenDensity = context.getResources().getDisplayMetrics().density;
            // Vertical Display fixed
            if (size.x < size.y) {
                screenWidth = size.x;
                screenHeight = size.y;
            } else {
                screenHeight = size.x;
                screenWidth = size.y;
            }
        }
    }
}