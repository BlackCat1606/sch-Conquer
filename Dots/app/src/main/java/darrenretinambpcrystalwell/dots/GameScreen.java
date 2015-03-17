package darrenretinambpcrystalwell.dots;

import android.widget.RelativeLayout;

/**
 * Created by DarrenRetinaMBP on 13/3/15.
 */
public class GameScreen implements Screen {

    private RelativeLayout relativeLayout;
    private MainActivity mainActivity;

    private boolean shown;
    private boolean normalHideDisabled;

    @Override
    public void hide() {
        if (normalHideDisabled) {

        } else {
            shown = false;
        }

    }

    @Override
    public void show() {
        shown = true;
    }
}
