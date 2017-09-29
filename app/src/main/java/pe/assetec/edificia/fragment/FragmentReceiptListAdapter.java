package pe.assetec.edificia.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import pe.assetec.edificia.R;
import pe.assetec.edificia.model.Building;

/**
 * Created by frank on 12/09/17.
 */

public class FragmentReceiptListAdapter extends ArrayAdapter<Building> {

    private Activity context;
    private int resource;
    private List<Building> items;

    public FragmentReceiptListAdapter(Activity mcontext,  int resource, List<Building> items) {
        super(mcontext, resource,items);

        this.context = mcontext;
        this.resource = resource;
        this.items = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Building item = items.get(position);
        LinearLayout rowView;

        if (convertView == null) {
            rowView = new LinearLayout(getContext());
            inflater.inflate(resource, rowView, true);
        } else {
            rowView = (LinearLayout) convertView;
        }

        TextView buildingName = (TextView) rowView.findViewById(R.id.tvBuildingNameRlReceipt);
        TextView departamentName = (TextView) rowView.findViewById(R.id.tvDepartamentNameRlRecepit);


        buildingName.setText(item.getBuilding_name());
        departamentName.setText(item.getDepartament_name());


        return rowView;
    }
}
