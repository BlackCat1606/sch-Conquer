package darrenretinambpcrystalwell.dots;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static darrenretinambpcrystalwell.dots.BitmapImporter.*;
import static darrenretinambpcrystalwell.dots.ScreenDimensions.*;

/**
 * Created by DarrenRetinaMBP on 11/4/15.
 *
 */
public class MainScreen implements View.OnClickListener{

    private Context        context;
    private FrameLayout    frameLayout;
    private FrameLayout    mainScreenLayout;


    private int            screenWidth;
    private int            screenHeight;
    private float          screenDensity;
    private ImageView      screen;
    private ImageView      logo;
    private ImageButton         singlePlayer;
    private ImageButton         multiPLayer;


    private static final int LOGO_WIDTH                      = 300;
    private static final int LOGO_HEIGHT                     = 61;
    private static final int QUICK_GAME_BUTTOM_MARGIN_TOP    = 35;
    private static final int QUICK_GAME_BUTTON_WIDTH         = 310;
    private static final int QUICK_GAME_BUTTON_HEIGHT        = 57;
    private static final int QUICK_GAME_BUTTON_MARGIN_BOTTOM = 17;
    private static final int ACCESSORY_BUTTON_WIDTH          = 93;
    private static final int INSTRUCTIONS_BUTTON_WIDTH       = 43;
    private static final int INSTRUCTIONS_BUTTON_HEIGHT      = 86;
    private static final int LAYOUT_HEIGHT                   = 250;

    public MainScreen(FrameLayout frameLayout,Context context) {

       this.frameLayout = frameLayout;
       this.context     = context;



        screenHeight = ScreenDimensions.getHeight(context);
        screenWidth  = ScreenDimensions.getWidth(context);
        screenDensity = ScreenDimensions.getDensity(context);




//        mainScreenLayout = new FrameLayout(context);
//        mainScreenLayout.setLayoutParams(
//                new FrameLayout.LayoutParams(screenWidth,screenHeight));

        initLayout();



    }

    private void initLayout() {
        Resources resources = context.getResources();

        float screenCenter = screenWidth*0.5f;
        float currentY = (screenHeight-LAYOUT_HEIGHT*screenDensity)*0.5f;

        int logoFinalWidth = (int) (screenDensity*LOGO_WIDTH);
        int logoFinalHeight = (int) (screenDensity*LOGO_HEIGHT);
        logo = new ImageView(context);
        logo.setImageBitmap(
                BitmapImporter.decodeSampledBitmapFromResource(
                        resources, R.id.imageView3,
                        logoFinalWidth, logoFinalHeight
                )
        );
        logo.setLayoutParams(new ViewGroup.LayoutParams(logoFinalWidth, logoFinalHeight));
//        logo.setX(currentY);
//        logo.setY(screenCenter-0.5f*logoFinalHeight);




    }

    @Override
    public void onClick(View v) {
        if (v == singlePlayer) {
            singlePlayer.setOnClickListener((View.OnClickListener) v);
        }
        else if (v == multiPLayer) {
            multiPLayer.setOnClickListener((View.OnClickListener) v);
        }
    }
}
