package darrenretinambpcrystalwell.Fragments;

import android.support.v4.app.Fragment;

import darrenretinambpcrystalwell.dots.MainActivity;
import darrenretinambpcrystalwell.dots.R;

/**
 * Created by JiaHao on 5/4/15.
 */
public class FragmentTransactionHelper {

    public static void pushFragment(int fragmentToPushIn, Fragment currentFragment, String[] args, MainActivity activity) {

        Fragment connectionFragment = activity.getFragment(fragmentToPushIn, args);

        activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.frag_slide_in_from_right, R.anim.frag_slide_out_from_left)
                .replace(R.id.root_layout, connectionFragment)
                .remove(currentFragment)
                .commit();

    }
}
