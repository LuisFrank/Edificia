package pe.assetec.edificia.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import pe.assetec.edificia.R;
import pe.assetec.edificia.util.ManageSession;


public class ShowPDFFragment extends Fragment {

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

//    String myUrl = "http://localhost:3000/api/v1/buildings";
    String myUrl = "http://edificia.pe/api/v1/buildings";

    WebView wv;
    PDFView pdfv;
    ManageSession session;


    // Progress dialog
    private View pDialog;

    private OnFragmentInteractionListener mListener;

    public ShowPDFFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(getActivity());



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        session = new ManageSession(getActivity());
        View v= inflater.inflate(R.layout.fragment_show_pdf, container, false);
        pdfv = (PDFView) v.findViewById(R.id.pdfView);
        pDialog = (ProgressBar) v.findViewById(R.id.progress_pdf);
         Integer invoice_id =   getArguments().getInt("invoice_id");
         Integer building_id =   getArguments().getInt("building_id");
         Integer departament_id = getArguments().getInt("departament_id");

//        String myUrl= "https://graduateland.com/api/v2/users/jesper/cv?_locale=es";



        String newUrl = myUrl + "/"+building_id + "/departaments/"+ departament_id + "/invoices/" + invoice_id +"/print.pdf";
        final String token = session.getTOKEN();
//        final String newtoken = " " +new String(token);

        DownloadFile DF = new DownloadFile();
        DF.execute(newUrl,token);


        return v;
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

    private class DownloadFile extends AsyncTask<String, String, String> {
        private static final int  MEGABYTE = 1024 * 1024;
        private static final int BUFFER_SIZE = 4096;
        String result = "";
        String fileName = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            String stringToken = strings[1];
            try {


                URL url = null;
                url = new URL(stringUrl);

                HttpURLConnection httpConn = null;
                httpConn = (HttpURLConnection) url.openConnection();
                //Set methods and timeouts
//                httpConn.setRequestMethod(REQUEST_METHOD);
//                httpConn.setReadTimeout(READ_TIMEOUT);
//                httpConn.setConnectTimeout(CONNECTION_TIMEOUT);
                String token = " " +new String(stringToken);
                httpConn.addRequestProperty ("Authorization", token);
                //Connect to our url

                int responseCode = 0;
                responseCode = httpConn.getResponseCode();

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {

                     fileName = "";
                    String disposition = httpConn.getHeaderField("Content-Disposition");
                    String contentType = httpConn.getContentType();
                    int contentLength = httpConn.getContentLength();

                    if (disposition != null) {
                        // extracts file name from header field
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            fileName = disposition.substring(index + 10,
                                    disposition.length() - 1);
                        }
                    }
                    try {
                        // opens input stream from the HTTP connection
                        InputStream inputStream = null;

                        inputStream = httpConn.getInputStream();

                        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                        File folder = new File(extStorageDirectory);
                        folder.mkdir();

                        File pdfFile = new File(folder, fileName);
                        // opens an output stream to save into file

                        FileOutputStream outputStream = null;
                        outputStream = new FileOutputStream(pdfFile);

                        int totalSize = httpConn.getContentLength();

                        int bytesRead = -1;
                        byte[] buffer = new byte[BUFFER_SIZE];

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        outputStream.close();
                        inputStream.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    result = "success";

                    } else {
                                result = "failed";
                    }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

            @Override
        protected void onPostExecute(String result) {
                pDialog.setVisibility(View.GONE);
                if (result.equalsIgnoreCase("success")){

                    Toast.makeText(getActivity(), fileName, Toast.LENGTH_SHORT).show();
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName);
                     float STARTZOOM = 2.0f;

                    pdfv.fromFile(file).enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    .spacing(0)
                    .load();
                    pdfv.zoomTo(STARTZOOM);
                }else{
                     Toast.makeText(getActivity(), "No se pudo visualizar el PDF", Toast.LENGTH_SHORT).show();
                }
        }
    }



    private boolean shouldAskPermission(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }







}
