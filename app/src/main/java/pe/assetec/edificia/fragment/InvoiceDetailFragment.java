package pe.assetec.edificia.fragment;

import android.app.DownloadManager;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.InvoiceDetailController;

import pe.assetec.edificia.model.Invoice;
import pe.assetec.edificia.model.InvoiceDetail;
import pe.assetec.edificia.util.ManageSession;


public class InvoiceDetailFragment extends Fragment {
    DownloadManager downloadManager;
    //   RUTAS
    //http://edificia.pe/api/v1/buildings/1/departaments/1/invoices/1/print.pdf
   //   String myUrl = "http://edificia.pe/api/v1/buildings";
    //Localhost
    String myUrl = "http://localhost:3000/api/v1/buildings";
    ManageSession session;

    private TextView mInvoiceNameView;
    private TextView mInvoiceNumberView;
    private TextView mInvoicePeriodNamesView;
    private TextView mInvoiceTotalView;
    private TextView mInvoiceBalanceView;
    private Button mDownloadPdf;
    private FloatingActionButton fab;

     Integer invoice_id;
     Integer building_id;
     Integer departament_id;


    private OnFragmentInteractionListener mListener;

    public InvoiceDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        session = new ManageSession(getActivity());
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        View view = inflater.inflate(R.layout.fragment_invoice_detail, container, false);

        String InvoicesText=getArguments().getString("invoices_details");
        invoice_id =   getArguments().getInt("invoice_id");
        building_id =   getArguments().getInt("building_id");
        departament_id = getArguments().getInt("departament_id");


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(InvoicesText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Invoice invoice =  new Invoice();

          mInvoiceNameView = (TextView)  view.findViewById(R.id.tvdInvoiceName);
          mInvoiceNumberView = (TextView)  view.findViewById(R.id.tvdInvoiceNumber);
          mInvoicePeriodNamesView = (TextView)  view.findViewById(R.id.tvdInvoicePeriodName);
          mInvoiceTotalView =  (TextView)  view.findViewById(R.id.tvdInvoiceTotal);
          mInvoiceBalanceView = (TextView)  view.findViewById(R.id.tvdInvoiceBalance);
          mDownloadPdf = (Button) view.findViewById(R.id.button);

        //LLenar el detalle
        List<InvoiceDetail> datos =  new ArrayList<InvoiceDetail>();
        InvoiceDetailAdapter listAdapter;
        try {
            invoice =  invoice.fromJson(jsonObject.getJSONObject("full_invoice").put("id","1"));
            mInvoiceNameView.setText(invoice.getName());
            mInvoiceNumberView.setText(invoice.getNumber());
            mInvoicePeriodNamesView.setText(invoice.getPeriods_names());
            mInvoiceTotalView.setText(invoice.getTotal().toString());
            mInvoiceBalanceView.setText(invoice.getBalance().toString());

            datos = InvoiceDetailController.fromJson(jsonObject.getJSONObject("full_invoice").getJSONArray("invoice_details"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView listview = (ListView) view.findViewById(R.id.lvFragmentInvoiceDetail);
        listAdapter = new InvoiceDetailAdapter(getActivity(),R.layout.row_layout_invoice_detail,datos);
        listview.setAdapter(listAdapter);

        mDownloadPdf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String newUrl = myUrl + "/"+building_id + "/departaments/"+ departament_id + "/invoices/" + invoice_id +"/print.pdf";

                Uri  Download_Uri = Uri.parse(newUrl);

                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                request.addRequestHeader("Authorization"," "+ session.getTOKEN());
                request.setDescription("Descargando " + "Recibo");
                request.setTitle("Recibo");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                downloadManager.enqueue(request);
                Toast.makeText(getActivity(), "Descargando", Toast.LENGTH_LONG).show();

            }
            // do something


        });

        FloatingActionButton faba = (FloatingActionButton) view.findViewById(R.id.fabInvoice);

        faba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String FinalUrl =  myUrl + "/"+building_id + "/departaments/"+ departament_id + "/invoices/" + invoice_id +"/print.pdf";

                //set Fragmentclass Arguments
                Fragment fr  = new ShowPDFFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("building_id",building_id);
                bundle.putInt("departament_id",departament_id);
                bundle.putInt("invoice_id",invoice_id);
                fr.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Contendor, fr).addToBackStack(null).commit();

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

    public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(getActivity(), "Archivo Descargado", Toast.LENGTH_LONG).show();

        }
    };



}