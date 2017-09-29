package pe.assetec.edificia.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.BuildingController;
import pe.assetec.edificia.controller.DepartamentsController;
import pe.assetec.edificia.controller.PeriodController;
import pe.assetec.edificia.model.Building;
import pe.assetec.edificia.model.Departament;
import pe.assetec.edificia.model.Period;
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


    Spinner spnBuildings, spnPeriods, spnTypeinfo;
    Button  buttonDownload;
    //Some url endpoint that you may have
    String myUrl = "http://localhost:3000/api/v1/buildings";
    String UrlDetallado = "economic_reports";
    String UrlResumido = "economic_report_groupeds";
//   String myUrl = "http://edificia.pe/api/v1/buildings";
    //String to place our result in
    String result;

    ManageSession session;
    DownloadManager downloadManager;

    int check = 0;



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


//        progressBar = view.findViewById(R.id.login_progressReport);
//        progressBar.setVisibility(View.VISIBLE);
        downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        // Inflate the layout for this fragment
        session = new ManageSession(getActivity());
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        spnBuildings = view.findViewById(R.id.spnBuildingsFR);
//        spnDepartament = view.findViewById(R.id.spnDepartamentsFR);
        spnPeriods = view.findViewById(R.id.spnPeriodsFR);
        spnTypeinfo = view.findViewById(R.id.spnTipoInformeFR);
        buttonDownload = view.findViewById(R.id.btnDescargarFR);


        List<Building> datos;
        datos =  new ArrayList<Building>();
        ArrayAdapter<Building> adapter;
        //set contenct
        try {
            datos = BuildingController.fromJson(new JSONArray(session.getBuildings()));
        } catch (JSONException e) {

            e.printStackTrace();
        }

        adapter = new ArrayAdapter<Building> (getActivity(),android.R.layout.select_dialog_item,datos);
        spnBuildings.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, getResources().getStringArray(R.array.TipoInforme));
        spinnerCountShoesArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnTypeinfo.setAdapter(spinnerCountShoesArrayAdapter);



        final List<Building> finalDatos = datos;
        spnBuildings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                    // TODO Auto-generated method stub
                    Building building = finalDatos.get(pos);

                    if (building.getBuilding_id() != 0) {

                        List<Departament> departaments = new ArrayList<Departament>();
                        List<Period> periods = new ArrayList<Period>();
                        String auth_token = session.getTOKEN();

//                        HttpGetRequestContext getRequestDepartament = new HttpGetRequestContext(getActivity());
                        HttpGetRequestContext getRequestPeriod = new HttpGetRequestContext(getActivity());
                        JSONObject jsonObjectDepartament = null;
                        JSONObject jsonObjectPeriod = null;

                        String result_departaments;
                        String result_periods;

                        try {
//                            result_departaments = getRequestDepartament.execute(myUrl + "/" + building.getBuilding_id() + "/departaments/", auth_token).get();
                            result_periods = getRequestPeriod.execute(myUrl + "/" + building.getBuilding_id() + "/periods/", auth_token).get();

//                            jsonObjectDepartament = new JSONObject(result_departaments);
                            jsonObjectPeriod = new JSONObject(result_periods);
//                            departaments = DepartamentsController.fromJson(jsonObjectDepartament.getJSONArray("departaments"));
                            periods = PeriodController.fromJson(jsonObjectPeriod.getJSONArray("periods"));

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                        ArrayAdapter<Departament> dataAdapterDepartament = new ArrayAdapter<Departament>(getActivity(),
//                                android.R.layout.select_dialog_item, departaments);

                        ArrayAdapter<Period> dataAdapterPeriods = new ArrayAdapter<Period>(getActivity(),
                                android.R.layout.select_dialog_item, periods);

//                        dataAdapterDepartament.setDropDownViewResource(android.R.layout.select_dialog_item);
//                        dataAdapterDepartament.notifyDataSetChanged();
//                        spnDepartament.setAdapter(dataAdapterDepartament);

                        dataAdapterPeriods.setDropDownViewResource(android.R.layout.select_dialog_item);
                        dataAdapterPeriods.notifyDataSetChanged();
                        spnPeriods.setAdapter(dataAdapterPeriods);



                    } else {
                        Toast.makeText(getActivity(), "No existen Periodos", Toast.LENGTH_LONG).show();
                    }


            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                Toast.makeText(getActivity(),newUrl ,Toast.LENGTH_LONG).show();
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

            }
        });


        FloatingActionButton faba = (FloatingActionButton) view.findViewById(R.id.fabReport);

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

}
