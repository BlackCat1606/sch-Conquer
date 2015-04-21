package darrenretinambpcrystalwell.Fragments;

import Sockets.DotsServerClientParent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import darrenretinambpcrystalwell.dots.MainActivity;
import darrenretinambpcrystalwell.dots.R;

/**
 * Created by HuiHui on 4/20/2015.
 */
public class RulesFragment extends Fragment {

    public RulesFragment() {
        // Required empty public constructor
    }

    public static RulesFragment newInstance(String param1, String param2) {
        RulesFragment fragment = new RulesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onStart(){
        super.onStart();

//        ImageButton back = (ImageButton)this.getActivity().findViewById(R.id.back);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_game_over, container, false);
        return inflater.inflate(R.layout.rules, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        final RulesFragment thisFragment = this;

        ImageButton backButton = (ImageButton)this.getActivity().findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransactionHelper.pushFragment(0, thisFragment, new String[2],(MainActivity)getActivity(), true);
            }
        });

//        Button mainMenuButton = (Button)this.getActivity().findViewById(R.id.go_to_main_menu_button);
//        mainMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransactionHelper.pushFragment(0, thisFragment,new String[2], (MainActivity)getActivity(), true);
//            }
//        });

    }

}
