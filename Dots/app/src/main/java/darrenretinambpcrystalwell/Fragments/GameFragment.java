package darrenretinambpcrystalwell.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import AndroidCallback.DotsAndroidCallback;
import Dots.DotsBoard;
import Model.DotsInteraction;
import Sockets.DotsClient;
import Sockets.DotsServer;
import Sockets.DotsServerClientParent;
import darrenretinambpcrystalwell.dots.DotsScreen;
import darrenretinambpcrystalwell.dots.R;
import darrenretinambpcrystalwell.dots.SurfaceViewDots;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    private final String TAG = "GameFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


//                SurfaceView v = (SurfaceView) findViewById(R.id.surfaceView);
//        GifRun gifRun = new GifRun(this);
//        gifRun.LoadGiff(v, this, R.drawable.my_animated_gif);

        int playerId = Integer.parseInt(this.mParam1);
        startServerOrClient(playerId);
    }

    /**
     * Starts a server or client
     * @param playerId 0 for server, 1 for client
     */
    private void startServerOrClient(int playerId) {

        Log.d(TAG, "Starting game, playerId: " + playerId + " ip: " + this.mParam2);
        final int PORT = 4321;


        //TODO uncomment this to set a dynamic ip address
//        String serverIp = this.mParam2;
        String serverIp = "192.168.1.4";

        DotsServerClientParent dotsServerClientParent;

        if (playerId == 0) {

            dotsServerClientParent = new DotsServer(PORT);
        } else {

            dotsServerClientParent = new DotsClient(serverIp, PORT);
        }


        RelativeLayout rootLayout = (RelativeLayout) this.getActivity().findViewById(R.id.gameFragment);
        final DotsScreen dotsScreen = new DotsScreen(rootLayout, this.getActivity());

        final SurfaceViewDots surfaceViewDots = new SurfaceViewDots(this.getActivity(), rootLayout, dotsServerClientParent, dotsScreen.getCorrespondingDotCoordinates());

//        surfaceViewDots.setDotsServerClientParent(this.dotsServerClientParent);
//        surfaceViewDots.setCorrespondingDotCoordinates(dotsScreen.getCorrespondingDotCoordinates());


        DotsAndroidCallback androidCallback = new DotsAndroidCallback() {
            @Override
            public void onValidPlayerInteraction(final DotsInteraction dotsInteraction) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        surfaceViewDots.setTouchedPath(dotsInteraction, dotsScreen);
                    }
                });
            }

            @Override
            public void onBoardChanged(final DotsBoard dotsBoard) {
//                dotsScreen.updateScreen(dotsBoard.getBoardArray());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dotsScreen.updateScreen(dotsBoard.getBoardArray());
                    }
                });
            }

            @Override
            public void onGameOver() {

            }
        };


        dotsServerClientParent.setAndroidCallback(androidCallback);

        try {
            dotsServerClientParent.start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }

    }
}
