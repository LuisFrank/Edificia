package pe.assetec.edificia.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pe.assetec.edificia.LoginActivity;
import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.BuildingController;
import pe.assetec.edificia.controller.DepartamentsController;
import pe.assetec.edificia.controller.PeriodController;
import pe.assetec.edificia.controller.TicketsController;
import pe.assetec.edificia.model.Building;
import pe.assetec.edificia.model.Departament;
import pe.assetec.edificia.model.Period;
import pe.assetec.edificia.model.Ticket;
import pe.assetec.edificia.util.AppStatus;
import pe.assetec.edificia.util.Constant;
import pe.assetec.edificia.util.HttpGetRequestContext;
import pe.assetec.edificia.util.HttpGetRequestContextDepartaments;
import pe.assetec.edificia.util.ManageSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    public static final String REQUEST_METHOD = "GET";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    String myUrl = Constant.SERVER;

    Spinner spnBuildings, spnPeriods, spnTypeinfo;
    FloatingActionButton faba;
    Button  buttonDownload;
    ProgressBar pbReport, pbPeriod;
    View reportForm, periodForm;
    //Some url endpoint that you may have
    String UrlDetallado = "economic_reports";
    String UrlResumido = "economic_report_groupeds";
//    String myUrl = "http://edificia.pe/api/v1/buildings";
    //String to place our result in


    ManageSession session;
    DownloadManager downloadManager;

    int check = 0;
    List<Period> periods;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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

        downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        // Inflate the layout for this fragment
        session = new ManageSession(getActivity());
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        spnBuildings = view.findViewById(R.id.spnBuildingsFR);
        faba = (FloatingActionButton) view.findViewById(R.id.fabReport);
        spnPeriods = view.findViewById(R.id.spnPeriodsFR);
        spnTypeinfo = view.findViewById(R.id.spnTipoInformeFR);
        buttonDownload = view.findViewById(R.id.btnDescargarFR);

        pbReport = (ProgressBar) view.findViewById(R.id.progressBarReport);
        pbPeriod = (ProgressBar) view.findViewById(R.id.progressBarPeriod);
        reportForm = view.findViewById(R.id.report_form);
        periodForm = view.findViewById(R.id.period_form);


        showProgressReport(true);

        List<Building> datos;
        datos =  new ArrayList<Building>();
        ArrayAdapter<Building> adapter;
        List<Building> uniqueBuildings = new ArrayList<Building>();
        //set contenct
        try {
            datos = BuildingController.fromJson(new JSONArray(session.getBuildings()));
            //Metodo que convierte en unico la lista de edificios
            uniqueBuildings.add(datos.get(0));
            for (Building building : datos) {
                boolean flag = false;
                for (Building uniqueBuilding : uniqueBuildings) {
                    if (uniqueBuilding.getBuilding_id().equals(building.getBuilding_id())) {
                        flag = true;
                    }
                }
                if(!flag)
                    uniqueBuildings.add(building);

            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        if (uniqueBuildings.size() != 0){
            showProgressReport(false);
        }

        adapter = new ArrayAdapter<Building> (getActivity(),android.R.layout.select_dialog_item,uniqueBuildings);
        spnBuildings.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ArrayAdapter<String> spinnerCountShoesArrayAdapter;

        if (session.getUserType().equalsIgnoreCase("Person")){
            spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, getResources().getStringArray(R.array.TipoInformePerson));
        }else{
           spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, getResources().getStringArray(R.array.TipoInformeEmployee));
        }
        spinnerCountShoesArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnTypeinfo.setAdapter(spinnerCountShoesArrayAdapter);


        final List<Building> finalDatos = datos;
        spnBuildings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                    // TODO Auto-generated method stub
                    Building building = finalDatos.get(pos);
                    String selected_item = adapterView.getItemAtPosition(pos).toString();

                    if(selected_item.matches("")){
                        showPeriod(false);
                        return;
                    }
                    showProgressPeriod(true);
                    String auth_token = session.getTOKEN();
                    String finalUrl;
                    finalUrl = myUrl + "/" + building.getBuilding_id() + "/periods/";
                    PeriodListTask taskperiod = new PeriodListTask( auth_token,finalUrl);
                    taskperiod.execute();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Check for Internet Connection first
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                Building building = (Building) spnBuildings.getSelectedItem();
//                Departament departament = (Departament) spnDepartament.getSelectedItem();
                Period period = (Period) spnPeriods.getSelectedItem();
                String tipo = spnTypeinfo.getSelectedItem().toString();

                String newUrl = "";
                if (tipo.equalsIgnoreCase("resumido")) {
                    newUrl = myUrl + "/" + building.getBuilding_id()  + "/" + UrlResumido + "/print.pdf?period_id="+ period.getId();
                }else if (tipo.equalsIgnoreCase("detallado")){
                    newUrl = myUrl + "/" + building.getBuilding_id()  + "/" + UrlDetallado + "/print.pdf?period_id="+ period.getId();
                }
                Uri  Download_Uri = Uri.parse(newUrl);

                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                request.addRequestHeader("Authorization"," "+ session.getTOKEN());
                request.setDescription("Descargando reporte");
                request.setTitle("Reporte Económico");
                //  request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/"  + "/" + "Sample" + ".png");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                downloadManager.enqueue(request);
                Toast.makeText(getActivity(), "Descargando", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            }


            }
        });



        faba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Building building = (Building) spnBuildings.getSelectedItem();
//                Departament departament = (Departament) spnDepartament.getSelectedItem();
                Period period = (Period) spnPeriods.getSelectedItem();
                String tipo = spnTypeinfo.getSelectedItem().toString();


                String newUrl = "";
                if (tipo.equalsIgnoreCase("resumido")) {
                    newUrl = myUrl + "/" + building.getBuilding_id() + "/" + UrlResumido + "/print.pdf?period_id="+ period.getId();
                }else if (tipo.equalsIgnoreCase("detallado")){
                    newUrl = myUrl + "/" + building.getBuilding_id()  + "/" + UrlDetallado + "/print.pdf?period_id="+ period.getId();
                }
                //set Fragmentclass Arguments
                Fragment fr  = new ShowPdfReportFragment();
                Bundle bundle=new Bundle();
                bundle.putString("url_report",newUrl);
                fr.setArguments(bundle);
                android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Contendor, fr).addToBackStack(null).commit();

            }
        });

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

    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(getActivity(), "Archivo Descargado", Toast.LENGTH_LONG).show();

        }
    };


    public class PeriodListTask extends AsyncTask<Void, Void, String> {
        String result;
        String mtoken;
        String murl_string;
        HttpURLConnection conn;


        PeriodListTask(String token, String url_string) {

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

                    JSONObject jsonObjectPeriod = new JSONObject(get_result);
                    periods = new ArrayList<Period>();
                    periods = PeriodController.fromJson(jsonObjectPeriod.getJSONArray("periods"));
                    result = "success";
                }else if (response_code == HttpURLConnection.HTTP_UNAUTHORIZED){
                    result = "unauthorized";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                result = "error";
            } catch (IOException e) {
                e.printStackTrace();
                result = "error";
            } catch (JSONException e) {
                result = "error";
                e.printStackTrace();
            }

            return result;
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String result) {
            showProgressPeriod(false);

            if (result.equalsIgnoreCase("success")) {
                ArrayAdapter<Period> dataAdapterPeriods = new ArrayAdapter<Period>(getActivity(),
                        android.R.layout.select_dialog_item, periods);
                dataAdapterPeriods.setDropDownViewResource(android.R.layout.select_dialog_item);
                spnPeriods.setAdapter(dataAdapterPeriods);
                dataAdapterPeriods.notifyDataSetChanged();

                if (periods.size() == 0) {
                    Toast.makeText(getActivity(), "No existen Periodos", Toast.LENGTH_LONG).show();
                    showPeriod(false);
                } else {
                    showPeriod(true);
                }
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
            showProgressPeriod(false);

        }
    }

    private void showProgressReport(final boolean show) {
        reportForm.setVisibility(show ? View.GONE: View.VISIBLE);
        pbReport.setVisibility(show ? View.VISIBLE: View.GONE);

    }

    private void showProgressPeriod(final boolean show) {
        periodForm.setVisibility(show ? View.GONE: View.VISIBLE);
        pbPeriod.setVisibility(show ? View.VISIBLE: View.GONE);

    }

    private void showPeriod(final boolean show) {
        periodForm.setVisibility(show ? View.VISIBLE: View.GONE);
    }

}
