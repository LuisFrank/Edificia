package pe.assetec.edificia.fragment;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

import pe.assetec.edificia.model.Building;

/**
 * Created by frank on 21/09/17.
 */

public class CustomSpinnerBuildingAdapter extends ArrayAdapter<Building> {


    public CustomSpinnerBuildingAdapter(Context context, int resource, List<Building> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        return super.getCount() - 1; // This makes the trick: do not show last item
    }

    @Override
    public Building getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
