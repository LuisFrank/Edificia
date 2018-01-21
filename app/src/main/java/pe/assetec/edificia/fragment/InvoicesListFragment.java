package pe.assetec.edificia.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.InvoicesController;
import pe.assetec.edificia.controller.TicketsController;
import pe.assetec.edificia.model.Building;
import pe.assetec.edificia.model.Invoice;
import pe.assetec.edificia.model.Ticket;
import pe.assetec.edificia.util.HttpGetRequest;
import pe.assetec.edificia.util.ManageSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvoicesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InvoicesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoicesListFragment extends Fragment {

    public static final String REQUEST_METHOD = "GET";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;


    DownloadManager downloadManager;
//   RUTAS
//    String myUrl = "http://edificia.pe/api/v1/buildings";
    //Localhost
    String myUrl = "http://localhost:3000/api/v1/buildings";
    //String to place our result in
    String result;
    ProgressBar pbInvoice;
    View invoiceList;
    ManageSession session;
    List<Invoice> datos;
    ListView listview;
    InvoicesListAdapter listAdapter;

    Integer building_id  = 0;
    Integer departament_id = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InvoicesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvoicesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvoicesListFragment newInstance(String param1, String param2) {
        InvoicesListFragment fragment = new InvoicesListFragment();
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
        View view = inflater.inflate(R.layout.fragment_invoices_list, container, false);

        session = new ManageSession(getActivity());
        building_id = Integer.parseInt(getArguments().getString("building_id"));
        departament_id =  Integer.parseInt(getArguments().getString("departament_id"));


        pbInvoice = (ProgressBar) view.findViewById(R.id.progressBarInvoiceList);
        invoiceList =  view.findViewById(R.id.invoice_list);
        listview = (ListView) view.findViewById(R.id.lvFragmentInvoicesList);
        listAdapter = new InvoicesListAdapter(getActivity(),R.layout.row_layout_invoices,datos);

        showProgress(true);
        String finalUrl = myUrl+ "/"+building_id+"/departaments/"+ departament_id+"/invoices/";
        InvoiceListTask taskInvoice= new InvoiceListTask(session.getTOKEN(),finalUrl);
        taskInvoice.execute();

        return  view;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class InvoiceListTask extends AsyncTask<Void, Void, String> {

        String mtoken;
        String murl_string;
        HttpURLConnection conn;


        InvoiceListTask(String token, String url_string) {

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
                datos = new ArrayList<Invoice>();
                datos = InvoicesController.fromJson(new JSONObject(get_result).getJSONArray("invoices"));

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
            listAdapter = new InvoicesListAdapter(getActivity(),R.layout.row_layout_invoices,datos);
            listview.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Invoice invoice = (Invoice)adapterView.getItemAtPosition(position);

//                    Fragment fr  = new ShowPDFFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putInt("building_id",building_id);
//                    bundle.putInt("departament_id",departament_id);
//                    bundle.putInt("invoice_id",invoice.getId());
//                    fr.setArguments(bundle);
//                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.Contendor, fr).addToBackStack(null).commit();


//                    String FinalUrl =  myUrl + "/"+building_id + "/departaments/"+ departament_id + "/invoices/" + invoice_id +"/print.pdf";

                    //set Fragmentclass Arguments
                    Fragment fr  = new ShowPDFFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("building_id",building_id);
                    bundle.putInt("departament_id",departament_id);
                    bundle.putInt("invoice_id",invoice.getId());
                    fr.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.Contendor, fr).addToBackStack(null).commit();


                }
            });
        }
        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        invoiceList.setVisibility(show ? View.GONE: View.VISIBLE);
        pbInvoice.setVisibility(show ? View.VISIBLE: View.GONE);

    }
}
