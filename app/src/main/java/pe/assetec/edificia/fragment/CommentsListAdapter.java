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
import pe.assetec.edificia.model.Comment;
import pe.assetec.edificia.model.Invoice;

/**
 * Created by frank on 25/09/17.
 */

public class CommentsListAdapter  extends ArrayAdapter<Comment> {

    private Activity context;
    private int resource;
    private List<Comment> items;

    public CommentsListAdapter(Activity mcontext,  int resource, List<Comment> items) {
        super(mcontext, resource,items);

        this.context = mcontext;
        this.resource = resource;
        this.items = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Comment item = items.get(position);
        LinearLayout rowView;

        if (convertView == null) {
            rowView = new LinearLayout(getContext());
            inflater.inflate(resource, rowView, true);
        } else {
            rowView = (LinearLayout) convertView;
        }


        TextView user_name = (TextView) rowView.findViewById(R.id.tvUserNameRLComment);
        TextView created_at = (TextView) rowView.findViewById(R.id.tvCreatedAtRLComment);
        TextView comment_text = (TextView) rowView.findViewById(R.id.tvComentRLCommnet);




        user_name.setText(item.getFirst_name().toString() + " "+ item.getLast_name());
        created_at.setText(item.getCreated_at());
        comment_text.setText(item.getBody());
        return rowView;
    }
}
