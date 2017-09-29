package pe.assetec.edificia.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

import pe.assetec.edificia.controller.DepartamentsController;
import pe.assetec.edificia.controller.PeriodController;
import pe.assetec.edificia.model.Departament;
import pe.assetec.edificia.model.Period;

/**
 * Created by frank on 20/09/17.
 */

public class HttpGetRequestContextDepartaments extends AsyncTask<String, Void, ArrayList<Departament>>{

    Context myCon;
    ArrayList<Departament> depa;



    public HttpGetRequestContextDepartaments(Context mCon){
        this.myCon=mCon;

    }

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected void onPreExecute() {
//        pbar.setVisibility(View.VISIBLE);
    }
    @Override
    protected ArrayList<Departament> doInBackground(String... params){
        String stringUrl = params[0];
        String stringToken = params[1];
        String result= null;
        String inputLine;
        depa  = new ArrayList<Departament>();
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
//
//
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            String token = " " +new String(stringToken);
            Log.w("entroooooo tokennnn ",token);
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



            result = sb.toString();



            JSONObject jsonObjectDepartament = null;

            String result_departament;
            try {
                result_departament = result;
                jsonObjectDepartament = new JSONObject(result_departament);
                depa = DepartamentsController.fromJson(jsonObjectDepartament.getJSONArray("departaments"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("resultado response ",result);



        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.toString();
        }
        return depa;
    }
    @Override
    protected void onPostExecute(ArrayList<Departament> depa){
     super.onPostExecute(depa);
    }
}
