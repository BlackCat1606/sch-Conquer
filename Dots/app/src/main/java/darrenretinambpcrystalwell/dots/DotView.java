package darrenretinambpcrystalwell.dots;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.io.*;
import android.graphics.drawable.BitmapDrawable;
import android.*;


/**
 * Author: Darren Ng
 * ID: 1000568
 */
class DotView extends ImageView {
    float x,y;
    private String color;
    private Drawable background;
    private Drawable red;
    private Drawable green;
    private Drawable blue;
    private Drawable yellow;
    private Drawable black;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public DotView(Context context) {
        super(context);
        Drawable a = getDrawable(R.drawable.dot);
        setBackground(a);
        red = getDrawable(R.drawable.reddot);
        blue = getDrawable(R.drawable.bluedot);
        green = getDrawable(R.drawable.greendot);
        yellow = getDrawable(R.drawable.yellowdot);
        black = getDrawable(R.drawable.dotcopy);
        black.setAlpha(150);
    }
    public float getSqDist(float x, float y) {
        float a = x - getX() + getHeight() * .5f;
        float b = y - getY() + getWidth() * .5f;
        return a*a + b*b;
    }

    public Drawable getDrawable(int id){
//        int resID = getResources().getIdentifier(id , "drawable", getContext().getPackageName());
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = getResources().openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(is,null, opt);
        try {
            is.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new BitmapDrawable(getResources(),bitmap);
    }
    public void setDrawable(Drawable a){
        setBackgroundDrawable(a);
    }
    public void setRed(){
                setDrawable(red);
    }
    public void setGreen(){
        setDrawable(green);
    }
    public void setBlue(){
        setDrawable(blue);
    }
    public void setYellow(){setDrawable(yellow);}
    public void setBlack(){setDrawable(black);}

    public void setColor(String color){this.color = color;}
    public String getColor(){return color;}
}
class RedDotView extends DotView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public RedDotView(Context context) {
        super(context);
        super.setRed();
        super.setColor("red");
    }

}
class GreenDotView extends DotView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public GreenDotView(Context context) {
        super(context);
        super.setGreen();
        super.setColor("green");}
}
class BlueDotView extends DotView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public BlueDotView(Context context) {
        super(context);
        super.setBlue();
        super.setColor("blue");
    }
}

class YellowDotView extends DotView {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public YellowDotView(Context context) {
        super(context);
        super.setYellow();
        super.setColor("yellow");
    }
}

class BlackDotView extends DotView {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public BlackDotView(Context context) {
        super(context);
        super.setBlack();
        super.setColor("black");
    }

}

