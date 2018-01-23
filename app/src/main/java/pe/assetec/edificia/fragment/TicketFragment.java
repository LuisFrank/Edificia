package pe.assetec.edificia.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import pe.assetec.edificia.R;
import pe.assetec.edificia.controller.BuildingController;
import pe.assetec.edificia.model.Building;
import pe.assetec.edificia.model.Departament;
import pe.assetec.edificia.util.Constant;
import pe.assetec.edificia.util.HttpGetRequest;
import pe.assetec.edificia.util.ManageSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment {


    String myUrl = Constant.SERVER;
    String result;

    List<Building> departaments;
    private CustomSearchBuildingAdapter adapter;
    ListView listview;
    EditText editTextSearch;
    ManageSession session;
    ArrayList<Building> mAllData=new ArrayList<Building>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
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
        session = new ManageSession(getActivity());
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ticket, container, false);
        editTextSearch = view.findViewById(R.id.txtSearchTicket);

        listview = view.findViewById(R.id.lvDepartamentsTicket);
       mAllData.clear();
        listview.setTextFilterEnabled(true);
        InitLisDepartaments();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Building building = (Building)adapterView.getItemAtPosition(position);

                String auth_token =  session.getTOKEN();
                //Instantiate new instance of our class


                    Fragment mFrag = new TicketsListFragment();
                    //set Fragmentclass Arguments
                    Bundle bundle=new Bundle();
                    bundle.putInt("building_id",building.getBuilding_id());
                    bundle.putInt("departament_id",building.getDepartament_id());
                    mFrag.setArguments(bundle);

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.Contendor, mFrag).addToBackStack(null).commit();


            }
        });


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editTextSearch.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
            }
        });

        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        departaments .clear();
        if (charText.length() == 0) {
            departaments.addAll(mAllData);
        } else {
            for (Building wp : mAllData) {
                if (wp.getDepartament_name().toLowerCase(Locale.getDefault())
                        .contains(charText) || wp.getBuilding_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    departaments.add(wp);
                }
            }
        }
        adapter.notifyDataSetChanged();
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



    public void InitLisDepartaments(){

        departaments = new ArrayList<Building>();
        try {
            departaments = BuildingController.fromJson(new JSONArray(session.getBuildings()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAllData.addAll(departaments);
        adapter = new CustomSearchBuildingAdapter(getActivity(),R.layout.row_layout_item_search,departaments);
        listview.setAdapter(adapter);

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
