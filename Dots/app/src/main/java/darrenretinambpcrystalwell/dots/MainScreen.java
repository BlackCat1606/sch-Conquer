package darrenretinambpcrystalwell.dots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static darrenretinambpcrystalwell.dots.BitmapImporter.*;

/**
 * Created by DarrenRetinaMBP on 11/4/15.
 *
 */
public class MainScreen{

    private Context        context;
    private FrameLayout    frameLayout;
    private FrameLayout    mainScreenLayout;

    private int            screenWidth;
    private int            screenHeight;
    private ImageView      screen;


    public MainScreen(FrameLayout frameLayout,Context context) {
        this.context             = context;
        this.frameLayout      = frameLayout;

        this.screenWidth         = ScreenDimensions.getWidth(context);
        this.screenHeight        = ScreenDimensions.getHeight(context);

        this.mainScreenLayout    = new FrameLayout(context);
        FrameLayout.LayoutParams rlp = new FrameLayout.LayoutParams
                (screenWidth,screenHeight);
        mainScreenLayout.setLayoutParams(rlp);
        frameLayout.addView(mainScreenLayout);

        screen = new ImageView(context);
        screen.setImageBitmap(
              BitmapImporter.decodeSampledBitmapFromResource(
                      context.getResources(), android.R.drawable.screen_background_dark,
                      screenWidth,screenHeight
              )
        );

        screen.setLayoutParams(new FrameLayout.LayoutParams(screenWidth,screenHeight));
        mainScreenLayout.addView(screen);
        mainScreenLayout.bringToFront();


    }
}
