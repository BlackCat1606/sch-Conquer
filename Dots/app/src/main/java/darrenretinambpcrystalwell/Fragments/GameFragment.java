package darrenretinambpcrystalwell.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

import AndroidCallback.DotsAndroidCallback;
import Constants.DotsConstants;
import Dots.DotsBoard;
import Model.Interaction.DotsInteraction;
import Model.Interaction.DotsInteractionStates;
import darrenretinambpcrystalwell.Game.DotsGameTask;
import darrenretinambpcrystalwell.SoundHelper;
import darrenretinambpcrystalwell.dots.DotsAndroidConstants;
import darrenretinambpcrystalwell.dots.DotsScreen;
import darrenretinambpcrystalwell.dots.MainActivity;
import darrenretinambpcrystalwell.dots.R;
import darrenretinambpcrystalwell.dots.ScoreBoard;
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

//        SurfaceView v = (SurfaceView) this.getActivity().findViewById(R.id.surfaceView);
//        GifRun gifRun = new GifRun(this.getActivity());
//        gifRun.LoadGiff(v, this.getActivity(), R.drawable.my_animated_gif);


    }


    @Override
    public void onResume() {
        super.onResume();

        TextView view = (TextView)this.getActivity().findViewById(R.id.latency);


        int playerId = Integer.parseInt(this.mParam1);
        try {
            startServerOrClient(playerId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();


            // Connection failed, go back to connection fragment
            FragmentTransactionHelper.pushFragment(1, this, new String[2], (MainActivity)getActivity(), false);

            FragmentTransactionHelper.showToast("Connection Failed!", this.getActivity(), DotsAndroidConstants.SCORE_TOAST_LENGTH);

        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }

        view.bringToFront();
    }



    /**
     * Starts a server or client
     * @param playerId 0 for server, 1 for client
     */
    private void startServerOrClient(final int playerId) throws InterruptedException, IOException, java.lang.InstantiationException {

        Log.d(TAG, "Starting game, playerId: " + playerId + " ip: " + this.mParam2);
        final int PORT = 4321;

        String serverIp = this.mParam2;

        RelativeLayout rootLayout = (RelativeLayout) this.getActivity().findViewById(R.id.gameFragment);
        final DotsScreen dotsScreen = new DotsScreen(rootLayout, this.getActivity());
        final ScoreBoard scoreBoard = new ScoreBoard(rootLayout, this.getActivity());

//        final DotsGameTask dotsGameTask = new DotsGameTask(playerId, PORT, serverIp);
        final DotsGameTask dotsGameTask = new DotsGameTask(playerId, DotsConstants.SINGLE_PLAYER_PORT, serverIp);

        final SurfaceViewDots surfaceViewDots = new SurfaceViewDots(this.getActivity(), rootLayout, dotsGameTask.getDotsServerClientParent(), dotsScreen.getCorrespondingDotCoordinates());

        final SoundHelper soundHelper = new SoundHelper(this.getActivity());

        final Fragment thisFragment = this;

        DotsAndroidCallback androidCallback = new DotsAndroidCallback() {
            @Override
            public void onValidPlayerInteraction(final DotsInteraction dotsInteraction) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        surfaceViewDots.setTouchedPath(dotsInteraction, dotsScreen);
                    }
                });

                playSoundForInteraction(dotsInteraction, soundHelper);

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
            public void onGameOver(int i, int[] ints) {
                final String message = "GAME OVER, WINNER: " + i + " FINAL SCORE: " + Arrays.toString(ints);
                Log.d(TAG, message);

                // kill threads and stop the game
                dotsGameTask.getDotsServerClientParent().stopGame();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransactionHelper.showToast(message, getActivity(), DotsAndroidConstants.SCORE_TOAST_LENGTH);

                    }
                });

                // initialise the game over fragments with arguments
                GameOverFragment gameOverFragment = new GameOverFragment();
                gameOverFragment.setArguments(playerId, ints);

                // Push the game over fragment out
                FragmentTransactionHelper.pushFragment(gameOverFragment, thisFragment, (MainActivity)getActivity(), true);

            }


            @Override
            public void onScoreUpdated(final int[] ints) {
                Log.d(TAG, "Score: " + Arrays.toString(ints));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("SHOWING TOAST");
                        FragmentTransactionHelper.showToast(Arrays.toString(ints), getActivity(), DotsAndroidConstants.SCORE_TOAST_LENGTH);
                    }
                });
            }

            @Override
            public void latencyChanged(long l) {
                Log.d(TAG, "Latency: " + l);
            }
        };

        dotsGameTask.setDotsAndroidCallback(androidCallback);

//        surfaceViewDots.setDotsServerClientParent(this.dotsServerClientParent);
//        surfaceViewDots.setCorrespondingDotCoordinates(dotsScreen.getCorrespondingDotCoordinates());

        Thread thread = new Thread(dotsGameTask);
        thread.start();


    }

    private void playSoundForInteraction(DotsInteraction interaction, SoundHelper soundHelper) {

        DotsInteractionStates state = interaction.getState();

        int soundId = state.ordinal();

        // Temporarily comment out bad sound
//        soundHelper.playSoundForInteraction(soundId);

    }


}
