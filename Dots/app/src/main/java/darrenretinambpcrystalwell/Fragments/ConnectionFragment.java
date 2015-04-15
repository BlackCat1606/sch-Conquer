//package darrenretinambpcrystalwell.Fragments;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.github.johnpersano.supertoasts.SuperToast;
//import com.parse.FindCallback;
//import com.parse.GetCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//
//import java.math.BigInteger;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.nio.ByteOrder;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import darrenretinambpcrystalwell.dots.DotsAndroidConstants;
//import darrenretinambpcrystalwell.dots.MainActivity;
//import darrenretinambpcrystalwell.dots.R;
//import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * to handle interaction events.
// * Use the {@link ConnectionFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class ConnectionFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//
//    private static final String TAG = "ConnectionFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private ArrayList<String> retrievedIps = null;
//
//
//    private String myIpAddress;
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ConnectionFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ConnectionFragment newInstance(String param1, String param2) {
//        ConnectionFragment fragment = new ConnectionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public ConnectionFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_connection, container, false);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        TextView myIpView = (TextView) this.getActivity().findViewById(R.id.player_ip_address);
//
//        this.myIpAddress = wifiIpAddress(this.getActivity());
//
//        myIpView.setText(this.myIpAddress);
//
//
//
//
//        setUpViews();
//
//    }
//
//    private void setUpViews() {
//        final Fragment thisFragment = this;
//
////
////        Button startServerButton = (Button) this.getActivity().findViewById(R.id.start_server_button);
////        Button startClientButton = (Button) this.getActivity().findViewById(R.id.start_client_button);
////
//////        startClientButton.setBackgroundColor(Color.TRANSPARENT);
//////        startServerButton.setBackgroundColor(Color.TRANSPARENT);
////
////        startServerButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                FragmentTransactionHelper.pushFragment(2, thisFragment, new String[]{"0", "0"}, (MainActivity)getActivity(), true);
////            }
////        });
////        startClientButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                EditText text = (EditText) getActivity().findViewById(R.id.ipAddress);
////                String serverIp = text.getText().toString();
////                FragmentTransactionHelper.pushFragment(2, thisFragment, new String[]{"1", serverIp}, (MainActivity)getActivity(), true);
////            }
////        });
//
//
//        final SmoothProgressBar progressBar = (SmoothProgressBar) this.getActivity().findViewById(R.id.loading_bar);
//        progressBar.setVisibility(View.INVISIBLE);
//
//
//        ImageButton startGameButton = (ImageButton) this.getActivity().findViewById(R.id.start_multi_player_game_button);
//        startGameButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                progressBar.progressiveStart();
//                progressBar.setVisibility(View.VISIBLE);
//
//                // TODO implement loading bar
//
//                // Create a query object
//                ParseQuery<ParseObject> query = ParseQuery.getQuery(DotsAndroidConstants.PARSE_OBJECT_NAME);
//
//                // sort by descending update time
//                query.orderByDescending("updatedAt");
//
//                // find the objects
//                query.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> parseObjects, ParseException e) {
//
//                        // if no error
//                        if (e == null) {
//
//                            ArrayList<String> retrievedIps = new ArrayList<String>();
//
//                            // For each object retrieved, we want to filter them
//                            for (ParseObject parseObject : parseObjects) {
//
//                                String retrievedIp = parseObject.getString(DotsAndroidConstants.PARSE_IP_KEY);
//
//                                // if they are not my current IP address
//                                if (!retrievedIp.equals(myIpAddress)) {
//
//                                    // get the UTC time
//                                    Date updatedAt = parseObject.getUpdatedAt();
//                                    long timeUpdatedAt = updatedAt.getTime();
//
//                                    // compares the system UTC time with the retrieved object and see if
//                                    // it falls into the live window
//
//                                    boolean ipAddressStillLive = System.currentTimeMillis() - timeUpdatedAt < DotsAndroidConstants.IP_LIVE_WINDOW;
//                                    boolean ipAddressesOnSameNetwork = compareIpOnSameWifi(myIpAddress, retrievedIp);
//
//                                    if (ipAddressStillLive) {
//
//                                        if (ipAddressesOnSameNetwork) {
//                                            retrievedIps.add(retrievedIp);
//                                        }
//
//                                    } else {
//                                        // delete old objects that don't fall within the window
//                                        parseObject.deleteInBackground();
//                                    }
//
//                                }
//                            }
//
//                            Log.d("Parse", "Retrieved and filtered IPs: " + retrievedIps.toString());
//
//                            // If ips are retrieved, we want to start a server
//                            if (!retrievedIps.isEmpty()) {
//
//                                // TODO check if its always the first object
//                                // gets the first object
//                                String serverIp = retrievedIps.get(0);
//
//
//                                Log.d(TAG, "Starting client, connecting to IP: " + serverIp);
//                                // starts a client that will connect to the IP
//                                FragmentTransactionHelper.showToast("Connecting...", getActivity(), SuperToast.Duration.MEDIUM);
//                                FragmentTransactionHelper.pushFragment(2, thisFragment, new String[]{"1", serverIp}, (MainActivity)getActivity(), true);
//
//
//                            } else {
//                                // starts a server if no IP addresses are retrieved
//
//                                // Saves my IP address into the cloud so others can connect
//                                ParseObject ipAddressObject = new ParseObject(DotsAndroidConstants.PARSE_OBJECT_NAME);
//                                String myIpAddress = ConnectionFragment.wifiIpAddress(getActivity());
//                                ipAddressObject.put(DotsAndroidConstants.PARSE_IP_KEY, myIpAddress);
//                                ipAddressObject.saveInBackground();
//
//                                Log.d(TAG, "Starting server...");
//                                FragmentTransactionHelper.showToast("Waiting...", getActivity(), SuperToast.Duration.MEDIUM);
//                                FragmentTransactionHelper.pushFragment(2, thisFragment, new String[]{"0", "0"}, (MainActivity) getActivity(), true);
//                            }
//
//                        } else {
//                            Log.d("PARSE", "Error: " + e.getMessage());
//                        }
//
////                        progressBar.setVisibility(View.INVISIBLE);
////                        progressBar.progressiveStop();
//                    }
//                });
//            }
//        });
//
//        // Set a default ip address here so you dont have to type it in every time
//        String placeholderIpAddress = "10.12.20.13";
//        EditText editText = (EditText) this.getActivity().findViewById(R.id.ipAddress);
//        editText.setText(placeholderIpAddress);
//    }
//
//    public static String wifiIpAddress(Context context) {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
//
//        // Convert little-endian to big-endianif needed
//        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
//            ipAddress = Integer.reverseBytes(ipAddress);
//        }
//
//        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
//
//        String ipAddressString;
//        try {
//            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
//        } catch (UnknownHostException ex) {
//            Log.e("WIFIIP", "Unable to get host address.");
//            ipAddressString = null;
//        }
//
//        return ipAddressString;
//
//    }
//
//    /**
//     * Compares two ip addresses and checks if they are on the same wifi
//     * @return true if same wifi, false otherwise
//     */
//    public static boolean compareIpOnSameWifi(String ip0, String ip1) {
//
//        System.out.println("Compareing: " + ip0 + " " + ip1);
//        String[] ip0Parts = ip0.split("\\.");
//        String[] ip1Parts = ip1.split("\\.");
//
//        // perform a check of the first two elements of the ip address
//
//        for (int i = 0; i < 2; i++) {
//
//            if (!ip0Parts[i].equals(ip1Parts[i])) {
//                return false;
//            }
//
//        }
//
//        return true;
//
//
//    }
//
//
//}
