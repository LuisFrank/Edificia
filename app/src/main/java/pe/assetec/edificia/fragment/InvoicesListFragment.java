package pe.assetec.edificia.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.InvoicesController;
import pe.assetec.edificia.model.Building;
import pe.assetec.edificia.model.Invoice;
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

    DownloadManager downloadManager;
//   RUTAS
    String myUrl = "http://edificia.pe/api/v1/buildings";
    //Localhost
//    String myUrl = "http://localhost:3000/api/v1/buildings";
    //String to place our result in
    String result;
    ManageSession session;


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
        // Inflate the layout for this fragment

        String InvoicesText=getArguments().getString("invoices");
        final Integer building_id = Integer.parseInt(getArguments().getString("building_id"));
        final Integer departament_id =  Integer.parseInt(getArguments().getString("departament_id"));
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(InvoicesText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Invoice> datos =  new ArrayList<Invoice>();
        InvoicesListAdapter listAdapter;
        try {
            datos = InvoicesController.fromJson(jsonObject.getJSONArray("invoices"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView listview = (ListView) view.findViewById(R.id.lvFragmentInvoicesList);
        listAdapter = new InvoicesListAdapter(getActivity(),R.layout.row_layout_invoices,datos);
        listview.setAdapter(listAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Invoice invoice = (Invoice)adapterView.getItemAtPosition(position);

                Log.d("val:",building_id.toString());
                Log.d("val:",departament_id.toString());


                String auth_token =  session.getTOKEN();
                //Instantiate new instance of our class
                HttpGetRequest getRequest = new HttpGetRequest();
                //Perform the doInBackground method, passing in our url
                try {
                    result = getRequest.execute(myUrl+ "/"+building_id+"/departaments/"+ departament_id+"/invoices/"+invoice.getId()+"/invoice_details/",auth_token).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (result != null ){
                    Log.e("result",result);

                    //set Fragmentclass Arguments
//                    Fragment mFrag = new InvoiceDetailFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("invoices_details",result );
//                    bundle.putInt("invoice_id",invoice.getId());
//                    bundle.putInt("building_id",building_id);
//                    bundle.putInt("departament_id",departament_id);
//                    mFrag.setArguments(bundle);
//                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.Contendor, mFrag).addToBackStack(null).commit();

//
//                    String FinalUrl =  myUrl + "/"+building_id + "/departaments/"+ departament_id + "/invoices/" + invoice.getId() +"/print.pdf";

                    //set Fragmentclass Arguments
                    Fragment fr  = new ShowPDFFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("building_id",building_id);
                    bundle.putInt("departament_id",departament_id);
                    bundle.putInt("invoice_id",invoice.getId());
                    fr.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.Contendor, fr).addToBackStack(null).commit();


                }else{
                    Toast.makeText(getActivity(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
}
