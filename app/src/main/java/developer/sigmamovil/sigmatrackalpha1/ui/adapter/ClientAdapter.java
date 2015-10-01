package developer.sigmamovil.sigmatrackalpha1.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;

import developer.sigmamovil.sigmatrackalpha1.R;
import developer.sigmamovil.sigmatrackalpha1.domain.Client;

/**
 * Created by usuario on 27/09/2015.
 */
public class ClientAdapter extends ArrayAdapter<Client>{

    private ArrayList<Client> values;
    private ArrayList<Client> itemsAll;
    private ArrayList<Client> suggestions;

    public ClientAdapter(Context context, int viewResourceId, ArrayList<Client> values) {
        super(context, viewResourceId, values);
        this.values = values;
        this.itemsAll = (ArrayList<Client>) values.clone();
        this.suggestions = new ArrayList<Client>();
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Client getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());

        return label;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Client)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Client client : itemsAll) {
                    if(client.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(client);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Client> filteredList = (ArrayList<Client>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Client c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
