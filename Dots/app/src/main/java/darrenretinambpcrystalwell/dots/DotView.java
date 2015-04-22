package darrenretinambpcrystalwell.dots;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.io.*;

import android.graphics.drawable.BitmapDrawable;
import android.*;

import Dots.DotColor;
import Dots.DotPowerUp;


/**
 * Author: Darren Ng
 * ID: 1000568
 *
 * class of the various dotviews
 * use this to initialise dots
 */
class DotView extends ImageView {
    float x, y;
    // private String color;
    private DotColor color;
    private Drawable red;
    private Drawable green;
    private Drawable blue;
    private Drawable yellow;
    private Drawable toucheddot;
    private Drawable purple;
    private Drawable one;
    private Drawable two;

    // Added new sprites for power ups
    private Drawable icered;
    private Drawable icegreen;
    private Drawable iceblue;
    private Drawable iceyellow;
    private Drawable icepurple;
    private Drawable firered;
    private Drawable firegreen;
    private Drawable fireblue;
    private Drawable fireyellow;
    private Drawable firepurple;


    /**
     * Constructor
     * @param context
     *
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public DotView(Context context) {
        super(context);
        Drawable a = getDrawable(R.drawable.bluedot);
        setBackground(a);

        red         = getDrawable(R.drawable.reddot);
        blue        = getDrawable(R.drawable.bluedot);
        green       = getDrawable(R.drawable.greendot);
        yellow      = getDrawable(R.drawable.yellowdot);
        purple      = getDrawable(R.drawable.clearerpurpledot);
        toucheddot  = getDrawable(R.drawable.toucheddot);
        one         = getDrawable(R.drawable.onetoucheddot);
        two         = getDrawable(R.drawable.twotoucheddot);

        icered      = getDrawable(R.drawable.icereddot);
        iceblue     = getDrawable(R.drawable.icebluedot);
        icegreen    = getDrawable(R.drawable.icegreendot);
        iceyellow   = getDrawable(R.drawable.iceyellowdot);
        icepurple   = getDrawable(R.drawable.iceclearerpurpled);

        firered     = getDrawable(R.drawable.firereddot);
        fireblue    = getDrawable(R.drawable.firebluedot);
        firegreen   = getDrawable(R.drawable.firegreendot);
        fireyellow  = getDrawable(R.drawable.fireyellowdot);
        firepurple  = getDrawable(R.drawable.fireclearerpurpleddot);


        toucheddot.setAlpha(220);
        one.setAlpha(220);
        two.setAlpha(220);

    }

    /**
     * use this to get the square distance of the view
     * @param x
     * @param y
     * @return float, the square distance of the view
     */
    public float getSqDist(float x, float y) {
        float a = x - getX() + getHeight() * .5f;
        float b = y - getY() + getWidth() * .5f;
        return a * a + b * b;
    }

    /**
     * use this to resize the drawable
     * @param id
     * @return drawable, resized imageview according to reduce
     * memory
     */
    public Drawable getDrawable(int id) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = getResources().openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        try {
            is.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new BitmapDrawable(getResources(), bitmap);
    }

    /**
     * use this to set the drawable
     * @param a
     */
    public void setDrawable(Drawable a) {
        setBackgroundDrawable(a);
    }

    // added by jiahao
    public void setColor(DotColor dotColor, DotPowerUp dotsPowerUp) {

        switch (dotColor) {
            case RED:
                if (dotsPowerUp == DotPowerUp.BOMB) {
                    setFirered();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.FREEZE) {
                    setIcered();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.NONE || dotsPowerUp == null) {
                    setRed();
                    break;
                }
            case GREEN:
                if (dotsPowerUp == DotPowerUp.BOMB) {
                    setFiregreen();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.FREEZE) {
                    setIcegreen();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.NONE || dotsPowerUp == null) {
                    setGreen();
                    break;
                }

            case BLUE:
                if (dotsPowerUp == DotPowerUp.BOMB) {
                    setFireblue();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.FREEZE) {
                    setIceblue();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.NONE || dotsPowerUp == null) {
                    setBlue();
                    break;
                }
            case YELLOW:
                if (dotsPowerUp == DotPowerUp.BOMB) {
                    setFireyellow();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.FREEZE) {
                    setIceyellow();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.NONE || dotsPowerUp == null) {
                    setYellow();
                    break;
                }
            case PURPLE:
                if (dotsPowerUp == DotPowerUp.BOMB) {
                    setFirepurple();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.FREEZE) {
                    setIcepurple();
                    break;
                }
                else if (dotsPowerUp == DotPowerUp.NONE || dotsPowerUp == null) {
                    setPurple();
                    break;
                }
            case PLAYER_0:
                setOne();
                break;
            case PLAYER_1:
                setTwo();
            default:
                System.err.println("Unknown color");
                setDrawable(red);
                break;
        }
    }

    /**
     * use this to get the color of the dot
     * @return color of the dot
     */
    public DotColor getColor() {
        return color;
    }

    public void setRed() {
        this.color = DotColor.RED;
        setDrawable(red);
    }

    public void setGreen() {
        this.color = DotColor.GREEN;
        setDrawable(green);
    }

    public void setBlue() {
        this.color = DotColor.BLUE;
        setDrawable(blue);
    }

    public void setYellow() {
        this.color = DotColor.YELLOW;
        setDrawable(yellow);
    }

    public void setPurple() {
        this.color = DotColor.PURPLE;
        setDrawable(purple);
    }



    public void setOne() {
        this.color = DotColor.PLAYER_0;
        setDrawable(one);
    }

    public void setTwo() {
        this.color = DotColor.PLAYER_1;
        setDrawable(two);
    }



    public void setTouchedDot() {
        // doesnt matter what color we set here
        this.color = DotColor.RED;
        setDrawable(toucheddot);
    }

    public void setIcered() {
        this.color = DotColor.RED;
        setDrawable(icered);
    }
    public void setIcegreen() {
        this.color = DotColor.GREEN;
        setDrawable(icegreen);
    }
    public void setIceblue() {
        this.color = DotColor.BLUE;
        setDrawable(iceblue);
    }
    public void setIceyellow() {
        this.color = DotColor.YELLOW;
        setDrawable(iceyellow);
    }
    public void setIcepurple() {
        this.color = DotColor.PURPLE;
        setDrawable(icepurple);
    }

    public void setFirered() {
        this.color = DotColor.RED;
        setDrawable(firered);
    }
    public void setFiregreen() {
        this.color = DotColor.GREEN;
        setDrawable(firegreen);
    }
    public void setFireblue() {
        this.color = DotColor.BLUE;
        setDrawable(fireblue);
    }
    public void setFireyellow() {
        this.color = DotColor.YELLOW;
        setDrawable(fireyellow);
    }
    public void setFirepurple() {
        this.color = DotColor.PURPLE;
        setDrawable(firepurple);
    }

}

class RedDotView extends DotView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public RedDotView(Context context) {
        super(context);
        super.setRed();
//        super.setColor("red");
    }

}

class GreenDotView extends DotView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public GreenDotView(Context context) {
        super(context);
        super.setGreen();
//        super.setColor("green");
    }
}

class BlueDotView extends DotView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public BlueDotView(Context context) {
        super(context);
        super.setBlue();
//        super.setColor("blue");
    }
}

class YellowDotView extends DotView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public YellowDotView(Context context) {
        super(context);
        super.setYellow();
//        super.setColor("yellow");
    }
}




class TouchedDot extends DotView {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public TouchedDot(Context context) {
        super(context);
        super.setTouchedDot();
//        super.setColor("toucheddot");
    }
}

class PurpleDotView extends DotView {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public PurpleDotView(Context context) {
        super(context);
        super.setTouchedDot();
    }
}

class OneDotView extends DotView {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public OneDotView(Context context) {
        super(context);
        super.setOne();
    }
}

//class IceBlue extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public IceBlue(Context context) {
//        super(context);
//        super.setIceBlue();
//    }
//}
//
//class IceRed extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public IceRed(Context context) {
//        super(context);
//        super.setIceRed();
//    }
//}
//class IceYellow extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public IceYellow(Context context) {
//        super(context);
//        super.setIceYellow();
//    }
//}
//class IceGreen extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public IceGreen(Context context) {
//        super(context);
//        super.setIceGreen();
//    }
//}
//class IcePurple extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public IcePurple(Context context) {
//        super(context);
//        super.setIcePurple();
//    }
//}
//class FireBlue extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public FireBlue(Context context) {
//        super(context);
//        super.setIceBlue();
//    }
//}
//
//class FireRed extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public FireRed(Context context) {
//        super(context);
//        super.setFireRed();
//    }
//}
//class FireYellow extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public FireYellow(Context context) {
//        super(context);
//        super.setFireYellow();
//    }
//}
//class FireGreen extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public FireGreen(Context context) {
//        super(context);
//        super.setFireGreen();
//    }
//}
//class FirePurple extends DotView {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public FirePurple(Context context) {
//        super(context);
//        super.setFirePurple();
//    }
//}
