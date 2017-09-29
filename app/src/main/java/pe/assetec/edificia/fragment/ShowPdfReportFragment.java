package pe.assetec.edificia.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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

import pe.assetec.edificia.R;
import pe.assetec.edificia.util.ManageSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowPdfReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowPdfReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowPdfReportFragment extends Fragment {

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    String UrlDetallado = "economic_reports";
    String UrlResumido = "economic_report_groupeds";
//    String myUrl = "http://edificia.pe/api/v1/buildings";

    String myUrl = "http://localhost:3000/api/v1/buildings";
//    String myUrl = "http://edificia.pe/api/v1/buildings";

    WebView wv;
    PDFView pdfv;
    ManageSession session;


    // Progress dialog
    private View pDialog;

    private OnFragmentInteractionListener mListener;

    public ShowPdfReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowPdfReport.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowPdfReportFragment newInstance(String param1, String param2) {
        ShowPdfReportFragment fragment = new ShowPdfReportFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_show_pdf_report, container, false);
        session = new ManageSession(getActivity());

        pdfv = (PDFView) v.findViewById(R.id.pdfViewReport);
        pDialog = (ProgressBar) v.findViewById(R.id.progress_pdf_report);

        String url_report =   getArguments().getString("url_report");

        final String token = session.getTOKEN();
//        final String newtoken = " " +new String(token);

        DownloadFileReport dfr = new DownloadFileReport();
        dfr.execute(url_report,token);
        // Inflate the layout for this fragment
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


    private class DownloadFileReport extends AsyncTask<String, String, String> {
        private static final int  MEGABYTE = 1024 * 1024;
        private static final int BUFFER_SIZE = 4096;
        String result = "";
        String fileName = "";
        @Override
        protected void onPreExecute() {
            Log.w("ENTRO PRE", "------------------------------");
            super.onPreExecute();
            pDialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.w("ENTRO BACK", "------------------------------");
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

                Log.w("ENTRO URL CONN", "------------------------------");
                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.w("ENTRO HTTTP OK", "------------------------------");
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
                        //                } else {
                        //                    // extracts file name from URL
                        //                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        //                            fileURL.length());
                    }

                    System.out.println("Content-Type = " + contentType);
                    System.out.println("Content-Disposition = " + disposition);
                    System.out.println("Content-Length = " + contentLength);
                    System.out.println("fileName = " + fileName);
                    try {
                        // opens input stream from the HTTP connection
                        InputStream inputStream = null;

                        inputStream = httpConn.getInputStream();

                        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                        File folder = new File(extStorageDirectory);
                        folder.mkdir();

                        File pdfFile = new File(folder, fileName);

                        // opens an output stream to save into file
                        System.out.println("FILEEEEEEEEEE");
                        System.out.println(pdfFile.getAbsolutePath());
                        System.out.println("====");
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
                    System.out.println("File downloaded");
                    result = "success";
                } else {
                    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
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
            Log.w("ENTRO POST EXECUTE", "------------------------------");
            pDialog.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("success")){


                Toast.makeText(getActivity(), "Download PDf successfully", Toast.LENGTH_SHORT).show();

                Log.d("Download complete", "----------");
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName);
                pdfv.fromFile(file).enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                        .password(null)
                        .scrollHandle(null)
                        .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                        // spacing between pages in dp. To define spacing color, set view background
                        .spacing(0)
                        .load();
            }else{
                Toast.makeText(getActivity(), "Download PDf noooooo", Toast.LENGTH_SHORT).show();

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
