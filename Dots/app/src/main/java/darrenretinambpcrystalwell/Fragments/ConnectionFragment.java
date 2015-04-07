package darrenretinambpcrystalwell.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import darrenretinambpcrystalwell.dots.GifRun;
import darrenretinambpcrystalwell.dots.MainActivity;
import darrenretinambpcrystalwell.dots.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectionFragment extends Fragment {
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
     * @return A new instance of fragment ConnectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectionFragment newInstance(String param1, String param2) {
        ConnectionFragment fragment = new ConnectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ConnectionFragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView myIpView = (TextView) this.getActivity().findViewById(R.id.player_ip_address);
        myIpView.setText(this.wifiIpAddress(this.getActivity()));

        ImageButton startServerButton = (ImageButton) this.getActivity().findViewById(R.id.startServerButton);
        ImageButton startClientButton = (ImageButton) this.getActivity().findViewById(R.id.startClientButton);
        startClientButton.setBackgroundColor(Color.TRANSPARENT);
        startServerButton.setBackgroundColor(Color.TRANSPARENT);

        final Fragment thisFragment = this;


        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransactionHelper.pushFragment(2, thisFragment, new String[]{"0", "0"}, (MainActivity)getActivity(), true);

            }
        });

        startClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText text = (EditText) getActivity().findViewById(R.id.ipAddress);
                String serverIp = text.getText().toString();
                FragmentTransactionHelper.pushFragment(2, thisFragment, new String[]{"1", serverIp}, (MainActivity)getActivity(), true);
            }
        });

        // Set a default ip address here so you dont have to type it in everytime
        String placeholderIpAddress = "10.12.20.13";

        EditText editText = (EditText) this.getActivity().findViewById(R.id.ipAddress);

        editText.setText(placeholderIpAddress);

    }

    private String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }


}
