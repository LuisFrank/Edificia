package pe.assetec.edificia.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pe.assetec.edificia.LoginActivity;
import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.BookingsController;
import pe.assetec.edificia.controller.TicketsController;
import pe.assetec.edificia.model.Booking;
import pe.assetec.edificia.model.Ticket;
import pe.assetec.edificia.util.Constant;
import pe.assetec.edificia.util.ManageSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookingListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingListFragment extends Fragment {


    String myUrl = Constant.SERVER;
    ManageSession session;


    Integer building_id  = 0;
    Integer departament_id = 0;

    ProgressBar pbBookingList;
    View bookingList;

    List<Booking> datos;
    ListView listview ;
    BookingListAdapter listAdapter;
    FloatingActionButton fab;


    public static final String REQUEST_METHOD = "GET";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final int REQUEST_READ_CONTACTS = 0;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BookingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingListFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static BookingListFragment newInstance(String param1, String param2) {
        BookingListFragment fragment = new BookingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view =  inflater.inflate(R.layout.fragment_booking_list, container, false);

        pbBookingList = (ProgressBar) view.findViewById(R.id.progressBarBookingLit);
        bookingList = view.findViewById(R.id.booking_list);
        listview = (ListView) view.findViewById(R.id.lvFragmentBookingList);
        fab = (FloatingActionButton) view.findViewById(R.id.fabBooking);

        session = new ManageSession(getActivity());
        // Inflate the layout for this fragment
        building_id = getArguments().getInt("building_id");
        departament_id = getArguments().getInt("departament_id");

        String finalUrl = myUrl+ "/"+building_id+"/departaments/"+ departament_id+"/bookings/";
        showProgress(true);
        BookingListTask taskTicket = new BookingListTask(session.getTOKEN(),finalUrl);
        taskTicket.execute();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class BookingListTask extends AsyncTask<Void, Void, String> {
        String result;
        String mtoken;
        String murl_string;
        HttpURLConnection conn;


        BookingListTask(String token, String url_string) {

            mtoken = token;
            murl_string = url_string;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                //Create a URL object holding our url
                URL myUrl = new URL(murl_string);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();

                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
//
//
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                String token = " " + new String(mtoken);
                connection.addRequestProperty("Authorization", token);
                //Connect to our url
                connection.connect();

                int response_code = connection.getResponseCode();
                //Create a new InputStreamReader
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    input.close();

                    String get_result = sb.toString();
                    datos = new ArrayList<Booking>();
                    datos = BookingsController.fromJson(new JSONObject(get_result).getJSONArray("bookings"));
                    result= "success";
                }else if (response_code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    result  ="unauthorized";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                result = "error";
            } catch (IOException e) {
                e.printStackTrace();
                result = "error";
            } catch (JSONException e) {
                e.printStackTrace();
                result = "error";
            }

            return result;
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String result) {
                showProgress(false);
            if (result.equalsIgnoreCase("success")) {

                listAdapter = new BookingListAdapter(getActivity(), R.layout.row_layout_booking, datos);
                listview.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();


                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //set Fragmentclass Arguments
                        android.app.Fragment mFrag = new BookingCreateFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("building_id", building_id);
                        bundle.putInt("departament_id", departament_id);
                        mFrag.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.Contendor, mFrag).addToBackStack(null).commit();

                    }
                });
            } else if (result.equalsIgnoreCase("unauthorized")){
                Toast.makeText(getActivity(), "Su sesisón ha expirado.", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                session.logOutUser();
                startActivity(myIntent);
            } else {
                Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {
          showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        bookingList.setVisibility(show ? View.GONE: View.VISIBLE);
        pbBookingList.setVisibility(show ? View.VISIBLE: View.GONE);

    }
}
