package pe.assetec.edificia.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.app.FragmentManager;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.CommentsController;
import pe.assetec.edificia.controller.InvoicesController;
import pe.assetec.edificia.controller.TicketsController;
import pe.assetec.edificia.model.Invoice;
import pe.assetec.edificia.model.Ticket;
import pe.assetec.edificia.util.HttpGetRequest;
import pe.assetec.edificia.util.ManageSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketsListFragment extends Fragment {

    //   RUTAS
//    String myUrl = "http://edificia.pe/api/v1/buildings";
    //Localhost
    String myUrl = "http://localhost:3000/api/v1/buildings";
    //String to place our result in
    String result;
    ManageSession session;


    Integer building_id  = 0;
    Integer departament_id = 0;

    ProgressBar pbTicketList;
    View ticketList;
    List<Ticket> datos;
    ListView listview ;
    TicketsListAdapter listAdapter;
    FloatingActionButton fab;


    public static final String REQUEST_METHOD = "GET";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final int REQUEST_READ_CONTACTS = 0;





    private OnFragmentInteractionListener mListener;

    public TicketsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketsListFragment newInstance(String param1, String param2) {
        TicketsListFragment fragment = new TicketsListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tickets_list, container, false);
        pbTicketList = (ProgressBar) view.findViewById(R.id.progressBarTicketList);
        ticketList = view.findViewById(R.id.ticket_list);
        listview = (ListView) view.findViewById(R.id.lvFragmentTicketsList);
        fab = (FloatingActionButton) view.findViewById(R.id.fabTicket);

        session = new ManageSession(getActivity());
        // Inflate the layout for this fragment
        String ticketsText=getArguments().getString("tickets");
        building_id = getArguments().getInt("building_id");
        departament_id = getArguments().getInt("departament_id");
        String finalUrl = myUrl+ "/"+building_id+"/departaments/"+ departament_id+"/tickets/";
        showProgress(true);
        TicketListTask taskTicket = new TicketListTask(session.getTOKEN(),finalUrl);
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


    public class TicketListTask extends AsyncTask<Void, Void, String> {

        String mtoken;
        String murl_string;
        HttpURLConnection conn;


        TicketListTask(String token, String url_string) {

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
                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                input.close();

                String get_result = sb.toString();
                datos = new ArrayList<Ticket>();
                datos = TicketsController.fromJson(new JSONObject(get_result).getJSONArray("tickets"));


            } catch (MalformedURLException e) {
                e.printStackTrace();
                result = null;
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "success";
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String success) {
            showProgress(false);
            listAdapter = new TicketsListAdapter(getActivity(),R.layout.row_layout_ticket,datos);
            listview.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
//            mProgressBar.setVisibility(View.GONE);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Ticket ticket = (Ticket)adapterView.getItemAtPosition(position);

                    Log.d("val:",building_id.toString());
                    Log.d("val:",departament_id.toString());




                    //set Fragmentclass Arguments
                    Fragment mFrag = new CommentsListFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("ticket_id",ticket.getId());
                    bundle.putInt("building_id",building_id);
                    bundle.putInt("departament_id",departament_id);
                    bundle.putSerializable("ticket",ticket);
                    mFrag.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.Contendor, mFrag).addToBackStack(null).commit();

                }
            });

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //set Fragmentclass Arguments
                    Fragment mFrag = new TicketFormFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("building_id",building_id);
                    bundle.putInt("departament_id",departament_id);
                    mFrag.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.Contendor, mFrag).addToBackStack(null).commit();

                }
            });
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }


    private void showProgress(final boolean show) {
        ticketList.setVisibility(show ? View.GONE: View.VISIBLE);
        pbTicketList.setVisibility(show ? View.VISIBLE: View.GONE);

    }
}
