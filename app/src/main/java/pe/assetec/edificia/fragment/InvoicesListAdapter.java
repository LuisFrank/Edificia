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
import pe.assetec.edificia.model.Invoice;

/**
 * Created by frank on 12/09/17.
 */

public class InvoicesListAdapter extends ArrayAdapter<Invoice> {

    private Activity context;
    private int resource;
    private List<Invoice> items;

    public InvoicesListAdapter(Activity mcontext,  int resource, List<Invoice> items) {
        super(mcontext, resource,items);

        this.context = mcontext;
        this.resource = resource;
        this.items = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Invoice item = items.get(position);
        LinearLayout rowView;

        if (convertView == null) {
            rowView = new LinearLayout(getContext());
            inflater.inflate(resource, rowView, true);
        } else {
            rowView = (LinearLayout) convertView;
        }

//        TextView invoice_number = (TextView) rowView.findViewById(R.id.tvNumeroReciboRlInvoices);
        TextView invoice_due_date = (TextView) rowView.findViewById(R.id.tvFechaVencimientoRlInvoices);
        TextView invoice_state = (TextView) rowView.findViewById(R.id.tvEstadoRlInvoices);
        TextView invoice_total = (TextView) rowView.findViewById(R.id.tvMontoTotalRlInvoices);
//        TextView invoice_sub_total = (TextView) rowView.findViewById(R.id.tvSaldoRlInvoices);



        invoice_due_date.setText(item.getPeriods_names().toString());
        String estado = "";
        if (item.getExpired()){
            estado = "Vencido";
        }else{
            estado = "Por vencer";
        }
        invoice_state.setText(estado);
        invoice_total.setText("S/. "+ item.getTotal().toString());
//        invoice_sub_total.setText(item.getDate_due().toString());
        return rowView;
    }
}

