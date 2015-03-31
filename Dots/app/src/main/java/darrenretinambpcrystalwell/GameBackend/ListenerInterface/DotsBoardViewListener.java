package darrenretinambpcrystalwell.GameBackend.ListenerInterface;

import darrenretinambpcrystalwell.GameBackend.Dots.DotsBoard;

/**
 * Interface to act as a listener for board updates
 *
 * Created by JiaHao on 31/3/15.
 */
public interface DotsBoardViewListener {

    public void onBoardUpdate(DotsBoard board);

}
