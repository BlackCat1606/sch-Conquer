package darrenretinambpcrystalwell.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Sockets.DotsServerClientParent;
import darrenretinambpcrystalwell.dots.R;

public class GameOverFragment extends Fragment {


    private int mCurrentPlayerId;
    private int[] mScores;

    public GameOverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setArguments(int currentPlayerId, int[] scores) {
        this.mCurrentPlayerId = currentPlayerId;
        this.mScores = scores;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_over, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        this.assignViews();
    }

    private void assignViews() {

        // Todo tell player no more moves or score cap reached?
        // Todo add button to return to main menu
        TextView winningPlayerTextView = (TextView)this.getActivity().findViewById(R.id.winning_player_text_view);
        TextView yourScoreTextView = (TextView)this.getActivity().findViewById(R.id.your_score);
        TextView opponentScoreTextView = (TextView)this.getActivity().findViewById(R.id.opponent_score);

        int winnerId = DotsServerClientParent.getWinner(this.mScores);

        String winnerText;

        if (winnerId == -1) {
            winnerText = "DRAW";
        } else if (winnerId == this.mCurrentPlayerId) {
            winnerText = "YOU WIN";
        } else {
            winnerText = "YOU LOSE";
        }

        winningPlayerTextView.setText(winnerText);


        yourScoreTextView.setText(Integer.toString(mScores[mCurrentPlayerId]));

        int opponentId;

        if (mCurrentPlayerId == 0) {
            opponentId = 1;
        } else {
            opponentId = 0;
        }

        opponentScoreTextView.setText(Integer.toString(mScores[opponentId]));

    }
}
