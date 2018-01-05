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
import pe.assetec.edificia.model.Booking;
import pe.assetec.edificia.model.Ticket;

/**
 * Created by frank on 16/10/17.
 */

public class BookingListAdapter extends ArrayAdapter<Booking> {

    private Activity context;
    private int resource;
    private List<Booking> items;

    public BookingListAdapter(Activity mcontext,  int resource, List<Booking> items) {
        super(mcontext, resource,items);

        this.context = mcontext;
        this.resource = resource;
        this.items = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Booking item = items.get(position);
        LinearLayout rowView;

        if (convertView == null) {
            rowView = new LinearLayout(getContext());
            inflater.inflate(resource, rowView, true);
        } else {
            rowView = (LinearLayout) convertView;
        }

//        TextView closed_ticket = (TextView) rowView.findViewById(R.id.tvClosedTicketRL);
        TextView commom_area_name = (TextView) rowView.findViewById(R.id.tvCommonAreaBooking);
        TextView status = (TextView) rowView.findViewById(R.id.tvStatusBooking);
        TextView name = (TextView) rowView.findViewById(R.id.tvNameBooking);
        TextView date_initial = (TextView) rowView.findViewById(R.id.tvDateInitialBooking);
//        TextView time_initial = (TextView) rowView.findViewById(R.id.tvTimeInitialBooking);
        TextView date_final = (TextView) rowView.findViewById(R.id.tvToDateFinalBooking);
//        TextView time_final = (TextView) rowView.findViewById(R.id.tvToTimeFinalBooking);




//        closed_ticket.setText( item.getState());
        commom_area_name.setText(item.getCommon_area_name());
        status.setText(item.getStatus_name());
        name.setText(item.getName());
        date_initial.setText(item.getStart_time());
//        time_initial.setText(item.getSummary());
        date_final.setText(item.getEnd_time());
//        time_final.setText(item.getSummary());



        return rowView;
    }
}
