package darrenretinambpcrystalwell.dots;

import android.content.Context;

/**
 * Created by DarrenRetinaMBP on 7/4/15.
 * hopefully jh can plug this method in
 * checks the middle rows and column for ifNeighbor exist
 * if false do shuffle
 */
public class Shuffle {
    DotsScreen dotsScreen;
    Context    context;
    boolean    neighbour = false;

    public Shuffle(Context context, DotsScreen dotsScreen) {
        this.context = context;
        this.dotsScreen = dotsScreen;
    }

    public boolean isNeighbour(){
        for(int i=6; i< dotsScreen.getDotList().length-6; i++) {
           if (i % 6 == 0) {
               if (dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i+1].getColor()
                       || dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i-6].getColor()
                       || dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i+6].getColor()) {
                   return neighbour = true;
               }
           } else if ((i+1) % 6 == 0) {
               if (dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i-1].getColor()
                       || dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i-6].getColor()
                       || dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i+6].getColor()) {
                   return neighbour = true;
               }
           } else {
               if (dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i-1].getColor()
                       || dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i-6].getColor()
                       || dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i+6].getColor()
                       || dotsScreen.getDotList()[i].getColor() == dotsScreen.getDotList()[i+1].getColor()) {
                   return neighbour = true;
               }
           }
        }
        return false;
    }
}
