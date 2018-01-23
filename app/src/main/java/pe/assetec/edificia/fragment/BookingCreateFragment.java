package pe.assetec.edificia.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pe.assetec.edificia.LoginActivity;
import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.CommonAreasController;
import pe.assetec.edificia.controller.PeriodController;
import pe.assetec.edificia.model.Block;
import pe.assetec.edificia.model.CommonArea;
import pe.assetec.edificia.model.Period;
import pe.assetec.edificia.util.Constant;
import pe.assetec.edificia.util.CustomTimePickDialog;
import pe.assetec.edificia.util.HttpGetRequest;
import pe.assetec.edificia.util.ManageSession;

import static android.app.AlertDialog.THEME_HOLO_DARK;
import static android.app.AlertDialog.THEME_HOLO_LIGHT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookingCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BookingCreateFragment extends Fragment {

//    String myUrl = "http://localhost:3000/api/v1/buildings";
  String myUrl = Constant.SERVER;


    public static final String REQUEST_METHOD = "GET";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final int REQUEST_READ_CONTACTS = 0;

    Integer building_id  = 0;
    Integer departament_id = 0;

    ManageSession session;

    Button btnCreate;
    ProgressBar pbBooking;
    View bookingform;
    Spinner spnAreaCommon;

    TextView tvTerminos;
    TextView tvDetalles;

    EditText etTitle;
    CheckBox chxTerms;
    EditText etInitialDate;
    EditText etInitialTime;
    EditText etFinalDate;
    EditText etFinalTime;

    TextInputLayout tilTitle,tilTerms,tilInitialDate,tilInitialTime,tilFinalDate, tilFinalTime;
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialogF;
    CustomTimePickDialog customTimePickerDialog, customTimePickerDialogF;

    public  List<CommonArea> datosCommonAreas;

    private OnFragmentInteractionListener mListener;

    public BookingCreateFragment() {
        // Required empty public constructor
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

        View view = inflater.inflate(R.layout.fragment_booking_create, container, false);

        session = new ManageSession(getActivity());
        building_id = getArguments().getInt("building_id");
        departament_id = getArguments().getInt("departament_id");
        pbBooking = (ProgressBar) view.findViewById(R.id.progressBarBooking);
        bookingform = view.findViewById(R.id.booking_form);
        spnAreaCommon  = (Spinner) view.findViewById(R.id.spnBookingAreaCommon);
        etTitle  = (EditText) view.findViewById(R.id.etBookingName);
        etInitialDate  = (EditText) view.findViewById(R.id.etBookingInitialDate);
        etInitialTime  = (EditText) view.findViewById(R.id.etBookingInitialTime);
        etFinalDate  = (EditText) view.findViewById(R.id.etBookingFinalDate);
        etFinalTime  = (EditText) view.findViewById(R.id.etBookingFinalTime);
        chxTerms =  (CheckBox) view.findViewById(R.id.chxBookingTerms);
        tvTerminos = (TextView) view.findViewById(R.id.tvTerminos);
        tvDetalles =  (TextView) view.findViewById(R.id.tvDetalles);
        btnCreate = (Button) view.findViewById(R.id.btnBooking);


        tilTitle =  (TextInputLayout) view.findViewById(R.id.tilBookingName);
        tilTerms =  (TextInputLayout) view.findViewById(R.id.tilBookingTerms);
        tilInitialDate =  (TextInputLayout) view.findViewById(R.id.tilBookingInitialDate);
        tilInitialTime =  (TextInputLayout) view.findViewById(R.id.tilBookingInitialTime);
        tilFinalDate =  (TextInputLayout) view.findViewById(R.id.tilBookingFinalDate);
        tilFinalTime =  (TextInputLayout) view.findViewById(R.id.tilBookingFinalTime);


        //Obtener Areas comunes
        showProgress(true);


        String urlReserva = myUrl + "/" + building_id + "/" + "common_areas";
        AreaCommonListTask areaTask = new AreaCommonListTask(session.getTOKEN(),urlReserva);
        areaTask.execute();

        InitialDateTimes();
        InitialDetails();
        InitialButton();

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


    public void InitialDateTimes(){


        etInitialDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        etInitialDate.setText(dayOfMonth + "/"
                                + String.format("%02d", (monthOfYear + 1)) + "/" + year);

                    }
                } , mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        etInitialTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                customTimePickerDialog = new CustomTimePickDialog(getActivity(), R.style.TimePicker, new CustomTimePickDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etInitialTime.setText( selectedHour + ":" + String.format("%02d",selectedMinute));
                    }
                }, hour, minute,15, true);
                customTimePickerDialog.show();
            }
        });

        etFinalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialogF = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                etFinalDate.setText(dayOfMonth + "/"
                                        + String.format("%02d",(monthOfYear + 1)) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogF.show();
            }
        });

        etFinalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                customTimePickerDialogF = new CustomTimePickDialog(getActivity(),R.style.TimePicker ,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etFinalTime.setText( selectedHour + ":" + String.format("%02d",selectedMinute));
                    }
                }, hour, minute,15, true);
                customTimePickerDialogF.show();
            }
        });

    }

    public void InitialDetails(){
        tvTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Reglamento");
                CommonArea area_common = (CommonArea) spnAreaCommon.getSelectedItem();
                builder1.setMessage(area_common.getRegulation());
                builder1.setCancelable(true);
                builder1.setNegativeButton(
                        "Cerrar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });


        tvDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderDetail = new AlertDialog.Builder(
                        new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light));


                CommonArea area_common = (CommonArea) spnAreaCommon.getSelectedItem();

                builderDetail.setTitle(area_common.getName());
//                builder1.setMessage(area_common.getDescription());

                StringBuilder horarios = new StringBuilder();
                horarios.append("Descripción:");
                horarios.append("\n");
                horarios.append(area_common.getDescription());
                horarios.append("\n");
                horarios.append("\n");
                horarios.append("Horarios de Reserva:");
                horarios.append("\n");
                for(Block block : area_common.getBloks()) {
                    horarios.append("*");
                    horarios.append(block.Horarios());
                    horarios.append("\n");
                }
                builderDetail.setMessage(horarios.toString());
                builderDetail.setCancelable(true);
                builderDetail.setNegativeButton(
                        "Cerrar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert22 = builderDetail.create();
                alert22.show();
            }
        });
    }

    public void InitialButton(){


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean cancel = ValidateForm();

                if (cancel) {

                }
                else {
                    showProgress(true);
                    String finalUrl = myUrl + "/" + building_id + "/departaments/" + departament_id + "/bookings/";
                    CommonArea area_common = (CommonArea) spnAreaCommon.getSelectedItem();
                    String common_area_id = area_common.getId().toString();
                    String name = etTitle.getText().toString();
                    String initial_date = etInitialDate.getText().toString();
                    String initial_time = etInitialTime.getText().toString();
                    String final_date = etFinalDate.getText().toString();
                    String final_time = etFinalTime.getText().toString();
                    String terms_of_service = "";
                    if (chxTerms.isChecked()) {
                        terms_of_service = "1";
                    } else {
                        terms_of_service = "0";
                    }

                    BookingPostTask bookinsTaks = new BookingPostTask(finalUrl, session.getTOKEN(), common_area_id, name, initial_date, initial_time, final_date, final_time, terms_of_service);
                    bookinsTaks.execute();
                }
            }
        });


    }

    public class BookingPostTask extends AsyncTask<Void, Void, String> {

        String murl_string,stringToken,common_area_id,name,initial_date,initial_time,final_date,final_time,terms_of_service;
        String result;
        String result_json;
        URL url = null;
        HttpURLConnection conn;
        BookingPostTask(String url,String token,String common_area_id,String name,String initial_date,String initial_time,String final_date, String final_time, String terms_of_service) {
            this.stringToken = token;
            this.murl_string = url;
            this.common_area_id = common_area_id;
            this.name = name;
            this.initial_date = initial_date;
            this.initial_time = initial_time;
            this.final_date = final_date;
            this.final_time = final_time;
            this.terms_of_service = terms_of_service;


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
                Log.e("background", "ini");
                // Enter URL address where your php file resides

                url = new URL(murl_string);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }

            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                String token = " " +new String(stringToken);
                conn.addRequestProperty ("Authorization", token);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive

                // Append parameters to URL
                JSONObject jsonobj = new JSONObject();
                jsonobj.put("common_area_id", common_area_id);
                jsonobj.put("name", name);
                jsonobj.put("initial_date", initial_date);
                jsonobj.put("initial_time", initial_time);
                jsonobj.put("final_date", final_date);
                jsonobj.put("final_time", final_time);
                jsonobj.put("terms_of_service", terms_of_service);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                bw.write(jsonobj.toString());
                bw.flush();
                bw.close();
                conn.connect();
                Log.e("url", url.toString());

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "error";
            } catch (JSONException e) {
                e.printStackTrace();
                return "error";
            }

            try {

                int response_code = conn.getResponseCode();
                Log.e("response code", response_code + "codigo");
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    input.close();
                     result_json = sb.toString();
                    result= "success";
                    return result_json;
                }else if (response_code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    result= "unauthorized";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            } finally {
                conn.disconnect();
            }
            return result_json;


            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String success_obj) {
            showProgress(false);
            if(result.equalsIgnoreCase("success")) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(success_obj);
                    if (jsonObject.isNull("success")) {
                        Toast.makeText(getActivity(), "Hubo un problema al enviar datos", Toast.LENGTH_LONG).show();
                    } else {
                        if (jsonObject.getBoolean("success")) {
                            Toast.makeText(getActivity(), "Se envio correctamente", Toast.LENGTH_LONG).show();
                            Fragment mFrag = new BookingListFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("building_id", building_id);
                            bundle.putInt("departament_id", departament_id);
                            mFrag.setArguments(bundle);

                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.Contendor, mFrag).addToBackStack(null).commit();
                        } else {
                            Toast.makeText(getActivity(), "No se pudo enviar " + jsonObject.getString("errors"), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
            showProgress(false);
//            mProgressBar.setVisibility(View.GONE);
        }
    }

    public class AreaCommonListTask extends AsyncTask<Void, Void, String> {
        String result;
        String mtoken;
        String murl_string;
        HttpURLConnection conn;


        AreaCommonListTask(String token, String url_string) {

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

                    JSONObject jsonObjectAreas = new JSONObject(get_result);
                    datosCommonAreas = new ArrayList<CommonArea>();
                    datosCommonAreas = CommonAreasController.fromJson(jsonObjectAreas.getJSONArray("common_areas"));
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
            }

            return result;
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String result) {
            showProgress(false);
            if(result.equalsIgnoreCase("success")) {
                ArrayAdapter<CommonArea> adapter;
                adapter = new ArrayAdapter<CommonArea>(getActivity(), android.R.layout.select_dialog_item, datosCommonAreas);
                spnAreaCommon.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else if (result.equalsIgnoreCase("unauthorized")){
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
        bookingform.setVisibility(show ? View.GONE: View.VISIBLE);
        pbBooking.setVisibility(show ? View.VISIBLE: View.GONE);
    }

    public boolean ValidateForm(){

        boolean cancel = false;

        if (TextUtils.isEmpty(etTitle.getText())  ) {

            tilTitle.setError(getString(R.string.error_field_required));
            cancel = true;
        }else{
            tilTitle.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(etInitialDate.getText())  ) {
            tilInitialDate.setError(getString(R.string.error_field_required));
            cancel = true;
        }else{
            tilInitialDate.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(etInitialTime.getText())  ) {
            tilInitialTime.setError(getString(R.string.error_field_required));
            cancel = true;
        }else{
            tilInitialTime.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(etFinalDate.getText())  ) {
            tilFinalDate.setError(getString(R.string.error_field_required));
            cancel = true;
        }else{
            tilFinalDate.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(etFinalTime.getText())  ) {
            tilFinalTime.setError(getString(R.string.error_field_required));
            cancel = true;
        }else{
            tilFinalTime.setErrorEnabled(false);
        }

        if (!(chxTerms.isChecked())  ) {
            tilTerms.setError(getString(R.string.error_field_required));
            cancel = true;
        }else{
            tilTerms.setErrorEnabled(false);
        }



        return cancel;
    }


}
