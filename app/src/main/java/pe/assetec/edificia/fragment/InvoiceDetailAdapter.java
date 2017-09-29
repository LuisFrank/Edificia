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
import pe.assetec.edificia.model.InvoiceDetail;

/**
 * Created by frank on 14/09/17.
 */

public class InvoiceDetailAdapter  extends ArrayAdapter<InvoiceDetail> {

    private Activity context;
    private int resource;
    private List<InvoiceDetail> items;

    public InvoiceDetailAdapter(Activity mcontext,  int resource, List<InvoiceDetail> items) {
        super(mcontext, resource,items);
        this.context = mcontext;
        this.resource = resource;
        this.items = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final InvoiceDetail item = items.get(position);
        LinearLayout rowView;

        if (convertView == null) {
            rowView = new LinearLayout(getContext());
            inflater.inflate(resource, rowView, true);
        } else {
            rowView = (LinearLayout) convertView;
        }

        TextView invoice_detail_category = (TextView) rowView.findViewById(R.id.tvInvoiceDetailCategory);
        TextView invoice_detail_description = (TextView) rowView.findViewById(R.id.tvInvoiceDetailDescription);
        TextView invoice_detail_total_amount = (TextView) rowView.findViewById(R.id.tvInvoiceDetailTotalAmout);



        invoice_detail_category.setText( item.getCategory_name());
        invoice_detail_description.setText(item.getDescription());
        invoice_detail_total_amount.setText(item.getTotal_amount().toString());

        return rowView;
    }
}