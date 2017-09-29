package pe.assetec.edificia.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pe.assetec.edificia.LoginActivity;
import pe.assetec.edificia.MainActivity;
import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.BuildingController;
import pe.assetec.edificia.controller.CommentsController;
import pe.assetec.edificia.model.Building;
import pe.assetec.edificia.model.Comment;
import pe.assetec.edificia.model.Departament;
import pe.assetec.edificia.model.Period;
import pe.assetec.edificia.util.HttpGetRequest;
import pe.assetec.edificia.util.ManageSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsListFragment extends Fragment {
    //   RUTAS
   String myUrl = "http://edificia.pe/api/v1/buildings";
    //Localhost
//    String myUrl = "http://localhost:3000/api/v1/buildings";
    //String to place our result in
    String result;
    ManageSession session;

    List<Comment> comments;
    CommentsListAdapter listAdapter;
    ListView listview;
    Button buttonComment;
    EditText etComment;
    private ProgressBar mProgressBar;

    public static final String REQUEST_METHOD = "GET";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static final int REQUEST_READ_CONTACTS = 0;

    Integer ticket_id = 0;
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

    public CommentsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentsListFragment newInstance(String param1, String param2) {
        CommentsListFragment fragment = new CommentsListFragment();
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
        View view = inflater.inflate(R.layout.fragment_comments_list, container, false);
        listview  = (ListView) view.findViewById(R.id.lvFragmentComments);
        session = new ManageSession(getActivity());
        buttonComment = (Button) view.findViewById(R.id.btnComment);
        etComment= (EditText)view.findViewById(R.id.etComment);
        mProgressBar = (ProgressBar) view.findViewById(R.id.login_progressComment);

        // Inflate the layout for this fragment

          ticket_id =getArguments().getInt("ticket_id");
          building_id = getArguments().getInt("building_id");
          departament_id =  getArguments().getInt("departament_id");

        comments = new ArrayList<Comment>();
        final String auth_token =  session.getTOKEN();
        String finalUrl =myUrl+ "/"+building_id+"/departaments/"+ departament_id+"/tickets/"+ticket_id;
        CommentsTask tas = new CommentsTask(auth_token,finalUrl);
        tas.execute();


        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalUrl = "";
                finalUrl = myUrl + "/"+building_id+"/departaments/"+ departament_id+"/tickets/"+ticket_id + "/comments";
                CommentPostTask cTaks = new CommentPostTask(auth_token,finalUrl,etComment.getText().toString());
                cTaks.execute();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class CommentsTask extends AsyncTask<Void, Void, String>  {

        String mtoken;
        String murl_string;
        HttpURLConnection conn;


        CommentsTask(String token, String url_string) {

            mtoken = token;
            murl_string = url_string;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                //Create a URL object holding our url
                URL myUrl = new URL(murl_string);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();

                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);//
//
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                String token = " " +new String(mtoken);
                connection.addRequestProperty ("Authorization", token);
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

                String get_result =  sb.toString();

                comments = CommentsController.fromJson(new JSONObject(get_result).getJSONObject("ticket").getJSONArray("comments"));
                result = sb.toString();
                Log.e("resultado response ",result);


            } catch (MalformedURLException e) {
                e.printStackTrace();
                result = null;
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  "success";

        }

        @Override
        protected void onPostExecute(String success) {
            listAdapter = new CommentsListAdapter(getActivity(),R.layout.row_layout_comment,comments);
            listview.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
            mProgressBar.setVisibility(View.GONE);
//            mAuthTask = null;
//            showProgress(false);
        }
    }



    public class CommentPostTask extends AsyncTask<Void, Void, String> {


        String mtoken;
        String murl_string;
        String comment;



        URL url = null;
        HttpURLConnection conn;
        CommentPostTask(String token, String url_string,String commentText) {
            mtoken = token;
            murl_string = url_string;
            comment = commentText;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
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
                String token = " " +new String(mtoken);
                conn.addRequestProperty ("Authorization", token);
                conn.setRequestMethod("POST");




                // setDoInput and setDoOutput method depict handling of both send and receive

                // Append parameters to URL
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());


                JSONObject jsonobj = new JSONObject();
                jsonobj.put("body", comment);


                out.writeBytes(jsonobj.toString());
                out.flush();
                out.close();
                conn.connect();
                Log.e("url", url.toString());

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
                    StringBuilder result = new StringBuilder();
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    input.close();

                    String jsonText = sb.toString();

                    // Pass data to onPostExecute method
                    return jsonText;

                }else{

                    return "false";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }



            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String success) {

            mProgressBar.setVisibility(View.GONE);
            Log.e("succes",success);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(success);
                if(jsonObject.isNull("success"))
                {
                   Toast.makeText(getActivity(),"Hubo un problema al enviar datos",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (jsonObject.getBoolean("success")){
                    Toast.makeText(getActivity(),"Se envio correctamente",Toast.LENGTH_LONG).show();
                        Fragment frg = new CommentsListFragment();

//                        FragmentTransaction ftr = getFragmentManager().beginTransaction();
//                        ftr.detach(frg).attach(frg).commit();
                        String finalUrl =myUrl+ "/"+building_id+"/departaments/"+ departament_id+"/tickets/"+ticket_id;
                        CommentsTask tas = new CommentsTask(mtoken,finalUrl);
                        tas.execute();

                    }else{
                        Toast.makeText(getActivity(),"No se pudo enviar ",Toast.LENGTH_LONG).show();
                    }

                    //it's not contain key club or isnull so do this operation here
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
