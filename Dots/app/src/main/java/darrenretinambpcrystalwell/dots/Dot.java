package darrenretinambpcrystalwell.dots;

import android.content.Context;
import android.widget.ImageView;

import darrenretinambpcrystalwell.dots.R;

// Dot type and classes
// RED, BLUE, GREEN
// WHITE for legal removal
class Dot extends ImageView {

    float x,y;

    public Dot(Context context) {
        super(context);
        setBackgroundResource(R.drawable.dotcopy);
    }
    public float getSqDist(float x, float y) {
        float a = x - getX() + getHeight() * .5f;
        float b = y - getY() + getWidth() * .5f;
        return a*a + b*b;
    }
}
class RedDot extends Dot {

    public RedDot(Context context) {
        super(context);
        setBackgroundResource(R.drawable.reddot);
    }
}
class GreenDot extends Dot {

    public GreenDot(Context context) {
        super(context);
        setBackgroundResource(R.drawable.greendot);
    }
}
class BlueDot extends Dot {

    public BlueDot(Context context) {
        super(context);
        setBackgroundResource(R.drawable.bluedot);
    }
}
class ClearDot extends Dot {

    public ClearDot(Context context) {
        super(context);
        setBackgroundResource(R.drawable.cleareddot);
    }
}

