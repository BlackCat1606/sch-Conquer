package darrenretinambpcrystalwell.dots;


import android.os.Handler;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Darren Ng on 24/3/15.
 *
 * Helper Class with all the Effects
 * To use an effect, simply just call the method you want
 * each method takes in a point with TIME_DELTA as time transition control.
 *
 */
public class Effects {

    private static int TIME_DELTA = 15;
    private final static Handler handler = new Handler();
    private final static HashMap<View, ArrayList<TweenRunnable>>
            animationRunnables = new HashMap<>();

    private static class TweenRunnable implements Runnable {
        protected View view;
        TweenRunnable(View view) {
            this.view = view;
        }
        @Override
        public void run() {}
        public void stop() {
            handler.removeCallbacks(this);
        }
    }

    private static class AlphaTweenRunnable extends TweenRunnable {

        float alphaDelta;
        float startAlpha;
        float endAlpha;
        boolean fillAfter;
        boolean hideViewAfter;
        int step;
        int totalSteps;

        AlphaTweenRunnable(View view, int duration,
                           float startAlpha, float endAlpha,
                           boolean fillAfter, boolean hideViewAfter) {
            super(view);
            this.alphaDelta = (endAlpha-startAlpha) * (float)(TIME_DELTA) / duration;
            this.totalSteps = (int)(duration / (float)(TIME_DELTA));
            this.step = 0;
            this.fillAfter = fillAfter;
            this.hideViewAfter = hideViewAfter;
            this.startAlpha = startAlpha;
            this.endAlpha = endAlpha;
            view.setAlpha(startAlpha);
        }
        @Override
        public void run() {
            if (step < totalSteps) {
                view.setAlpha(view.getAlpha() + alphaDelta);
                handler.postDelayed(this, TIME_DELTA);
                ++step;
            } else {
                if (fillAfter) {
                    view.setAlpha(endAlpha);
                } else {
                    view.setAlpha(startAlpha);
                }
                if (hideViewAfter) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     *
     * @param view
     * @param runnable
     */
    private static void recordAnimationRunnable(View view, TweenRunnable runnable) {
        handler.postDelayed(runnable, TIME_DELTA);
        if (animationRunnables.containsKey(view) == false) {
            animationRunnables.put(view, new ArrayList<TweenRunnable>());
        }
        animationRunnables.get(view).add(runnable);
    }

    /**
     * clears the animation currently running to prevent
     * faults in animation
     * @param view
     */
    private static void clearAllAnimations(View view) {
        if (animationRunnables.containsKey(view) == false) return;
        ArrayList<TweenRunnable> runnables = animationRunnables.get(view);
        for (int i=runnables.size()-1; i>-1; --i) {
            TweenRunnable runnable = runnables.get(i);
            runnable.stop();
            runnables.remove(i);

        }
    }

    /**
     * use this to cast fading out effect on view
     * @param view
     * @param duration
     * @param fillAfter
     * @param hideViewAfter
     */
    public static void castFadeOutEffect(View view,
                                         int duration, boolean fillAfter, boolean hideViewAfter) {
        clearAllAnimations(view);
        TweenRunnable animationRunnable = new AlphaTweenRunnable(
                view, duration,
                1.f, 0.f,
                fillAfter, hideViewAfter
        );
        recordAnimationRunnable(view, animationRunnable);
    }

    /**
     * use this to cast fading in effect on view
     * @param view
     * @param endAlpha
     * @param duration
     * @param fillAfter
     */
    public static void castFadeInEffect(View view, float endAlpha,
                                        int duration, boolean fillAfter) {
        clearAllAnimations(view);
        view.setVisibility(View.VISIBLE);
        TweenRunnable animationRunnable = new AlphaTweenRunnable(
                view, duration,
                0.f, endAlpha,
                fillAfter, false
                );
        recordAnimationRunnable(view, animationRunnable);
    }
}
