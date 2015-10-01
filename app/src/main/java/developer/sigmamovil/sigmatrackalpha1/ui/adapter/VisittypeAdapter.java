package developer.sigmamovil.sigmatrackalpha1.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;
import developer.sigmamovil.sigmatrackalpha1.domain.VisitType;

/**
 * Created by usuario on 27/09/2015.
 */
public class VisittypeAdapter extends ArrayAdapter<VisitType> {

    private ArrayList<VisitType> values;
    private ArrayList<VisitType> itemsAll;
    private ArrayList<VisitType> suggestions;

    public VisittypeAdapter(Context context, int textViewResourceId, ArrayList<VisitType> values) {
        super(context, textViewResourceId, values);
        this.values = values;
        this.itemsAll = (ArrayList<VisitType>) values.clone();
        this.suggestions = new ArrayList<VisitType>();
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public VisitType getItem(int position) {
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
            String str = ((VisitType)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (VisitType visitType : itemsAll) {
                    if(visitType.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(visitType);
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
            ArrayList<VisitType> filteredList = (ArrayList<VisitType>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (VisitType c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
