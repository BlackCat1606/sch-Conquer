package darrenretinambpcrystalwell.Fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import darrenretinambpcrystalwell.dots.MainActivity;
import darrenretinambpcrystalwell.dots.R;

/**
 * Created by JiaHao on 5/4/15.
 */
public class FragmentTransactionHelper {

    public static void pushFragment(int fragmentToPushIn, Fragment currentFragment, String[] args, MainActivity activity, boolean animate) {

        Fragment connectionFragment = activity.getFragment(fragmentToPushIn, args);

        if (animate) {
            activity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.frag_slide_in_from_right, R.anim.frag_slide_out_from_left)
                    .replace(R.id.root_layout, connectionFragment)
                    .remove(currentFragment)
                    .commit();
        } else {
            //TODO cannot turn off animations
            activity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_NONE)
                    .replace(R.id.root_layout, connectionFragment)
                    .remove(currentFragment)
                    .commit();
        }

    }
}
