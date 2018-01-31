package pe.assetec.edificia.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import pe.assetec.edificia.R;
import pe.assetec.edificia.model.Invoice;
import pe.assetec.edificia.model.Ticket;

/**
 * Created by frank on 22/09/17.
 */

public class TicketsListAdapter extends ArrayAdapter<Ticket> {

    private Activity context;
    private int resource;
    private List<Ticket> items;

    public TicketsListAdapter(Activity mcontext,  int resource, List<Ticket> items) {
        super(mcontext, resource,items);

        this.context = mcontext;
        this.resource = resource;
        this.items = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Ticket item = items.get(position);
        LinearLayout rowView;

        if (convertView == null) {
            rowView = new LinearLayout(getContext());
            inflater.inflate(resource, rowView, true);
        } else {
            rowView = (LinearLayout) convertView;
        }

//        TextView closed_ticket = (TextView) rowView.findViewById(R.id.tvClosedTicketRL);
        TextView name_ticket = (TextView) rowView.findViewById(R.id.tvNameTicketRL);
        TextView created_at = (TextView) rowView.findViewById(R.id.tvDateTicketRL);
        ImageView image_ticket = (ImageView) rowView.findViewById(R.id.ivImageTicketRL);

        image_ticket.setImageResource(R.drawable.ic_chat_black_24dp);
        image_ticket.setColorFilter(getContext().getResources().getColor(R.color.colorAproved));

//        closed_ticket.setText( item.getState());
        name_ticket.setText(item.getSummary());
        created_at.setText(item.getCreated_at());

        return rowView;
    }
}
