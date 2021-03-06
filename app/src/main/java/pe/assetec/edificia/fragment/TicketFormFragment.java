package pe.assetec.edificia.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import pe.assetec.edificia.LoginActivity;
import pe.assetec.edificia.R;
import pe.assetec.edificia.model.Comment;
import pe.assetec.edificia.util.Constant;
import pe.assetec.edificia.util.ManageSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFormFragment extends Fragment {

    String myUrl = Constant.SERVER;
    ManageSession session;

    Button buttonTicket;
    EditText etSummary;
    EditText etDescription;
    ProgressBar pbTicketForm;
    View tickeForm;

    public static final String REQUEST_METHOD = "GET";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final int REQUEST_READ_CONTACTS = 0;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Integer building_id  = 0;
    Integer departament_id = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TicketFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketFormFragment newInstance(String param1, String param2) {
        TicketFormFragment fragment = new TicketFormFragment();
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

        View view = inflater.inflate(R.layout.fragment_ticket_form, container, false);

        session = new ManageSession(getActivity());
        pbTicketForm = (ProgressBar) view.findViewById(R.id.progressBarTicketForm);
        tickeForm =  view.findViewById(R.id.ticket_form);
        buttonTicket = (Button) view.findViewById(R.id.btnTicket);
        etSummary= (EditText)view.findViewById(R.id.txtTicketSummary);
        etDescription= (EditText)view.findViewById(R.id.txtTicketDescription);
//        mProgressBar = (ProgressBar) view.findViewById(R.id.login_progress);

        // Inflate the layout for this fragment

        building_id = getArguments().getInt("building_id");
        departament_id =  getArguments().getInt("departament_id");

        final String auth_token =  session.getTOKEN();
        final String finalUrl =myUrl+ "/"+building_id+"/departaments/"+ departament_id+"/tickets";



        buttonTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = ValidateForm();

                if (cancel) {

                } else {
                    TicketPostTask task = new TicketPostTask(auth_token,finalUrl, etSummary.getText().toString(),etDescription.getText().toString());
                    task.execute();
                }

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

    public class TicketPostTask extends AsyncTask<Void, Void, String> {
        String jsonText;
        String result;
        String mtoken;
        String murl_string;
        String msummary,mdescription;



        URL url = null;
        HttpURLConnection conn;
        TicketPostTask(String token, String url_string ,String summary, String description) {
            mtoken = token;
            murl_string = url_string;
            msummary = summary;
            mdescription =  description;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
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
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                String token = " " +new String(mtoken);
                conn.addRequestProperty ("Authorization", token);
                conn.setRequestMethod("POST");




                // setDoInput and setDoOutput method depict handling of both send and receive

                // Append parameters to URL
//                DataOutputStream out = new DataOutputStream(conn.getOutputStream());


                JSONObject jsonobj = new JSONObject();
                jsonobj.put("summary", msummary);
                jsonobj.put("description", mdescription);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                bw.write(jsonobj.toString());
                bw.flush();
                bw.close();

                conn.connect();
                Log.e("url", url.toString());
                Log.e("token",token);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                int response_code = conn.getResponseCode();
                Log.e("response code", response_code + "codigo");
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    Log.e("coneccion", "entrooo");
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    input.close();

                    jsonText = sb.toString();
                    result = "success";

                }else if (response_code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    result= "unauthorized";
                }

            } catch (IOException e) {
                e.printStackTrace();
                result ="error";
            } finally {
                conn.disconnect();
            }
            return  result;
        }

        @Override
        protected void onPostExecute(String result) {

            showProgress(false);
            if (result.equalsIgnoreCase("success")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonText);
                    if (jsonObject.isNull("success")) {
                        Toast.makeText(getActivity(), "Hubo un problema al enviar datos", Toast.LENGTH_LONG).show();
                    } else {
                        if (jsonObject.getBoolean("success")) {
                            Toast.makeText(getActivity(), "Se envió correctamente", Toast.LENGTH_LONG).show();
                            Fragment mFrag = new TicketsListFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("building_id", building_id);
                            bundle.putInt("departament_id", departament_id);
                            mFrag.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.Contendor, mFrag).addToBackStack(null).commit();
                        } else {
                            Toast.makeText(getActivity(), "No se pudo enviar :    \n " + jsonObject.getString("errors").replace(",", "\n"), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Ha ocurrido un error en el servidor ", Toast.LENGTH_LONG).show();
                }

            } else if (result.equalsIgnoreCase("unauthorized")) {
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
//            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showProgress(final boolean show) {
        tickeForm.setVisibility(show ? View.GONE: View.VISIBLE);
        pbTicketForm.setVisibility(show ? View.VISIBLE: View.GONE);

    }


    public boolean ValidateForm(){

        boolean cancel = false;

        if (TextUtils.isEmpty(etSummary.getText())  ) {
            etSummary.setError(getString(R.string.error_field_required));

            cancel = true;
        }
        if (TextUtils.isEmpty(etDescription.getText())  ) {
            etDescription.setError(getString(R.string.error_field_required));

            cancel = true;
        }

        return cancel;
    }
}
