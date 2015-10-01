package developer.sigmamovil.sigmatrackalpha1.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import developer.sigmamovil.sigmatrackalpha1.LocationActivity;
import developer.sigmamovil.sigmatrackalpha1.MainActivity;
import developer.sigmamovil.sigmatrackalpha1.R;
import developer.sigmamovil.sigmatrackalpha1.domain.User;
import developer.sigmamovil.sigmatrackalpha1.domain.VisitHistory;
import developer.sigmamovil.sigmatrackalpha1.io.ApiConstants;
import developer.sigmamovil.sigmatrackalpha1.misc.DialogCreator;
import developer.sigmamovil.sigmatrackalpha1.misc.GeocoderAdapter;
import developer.sigmamovil.sigmatrackalpha1.misc.MyLocationListener;

/**
 * Created by william.montiel on 14/09/2015.
 */
public class VisitHistoryAdapter extends RecyclerView.Adapter<VisitHistoryAdapter.VisitHistoryHolder> {

    ArrayList<VisitHistory> history;
    Context context;
    User user;

    public VisitHistoryAdapter(Context context, User user) {
        this.context = context;
        this.history = new ArrayList<>();
        this.user = user;
    }

    @Override
    public VisitHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_visit_history, parent, false);

        return new VisitHistoryHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(VisitHistoryHolder holder, int position) {
        VisitHistory currentItem = history.get(position);
        holder.setVisitHistory(currentItem.getIdVisit(), currentItem.getType(), currentItem.getClient(), currentItem.getDate(), currentItem.getElapsed(), currentItem.getFinished(), currentItem.getIlatitude(), currentItem.getIlongitude());
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public void addAll(@NonNull ArrayList<VisitHistory> history) {
        if (history == null) {
            throw new NullPointerException("The items cannot be null");
        }

        this.history.clear();
        this.history.addAll(history);
        notifyDataSetChanged();
    }

    public class VisitHistoryHolder extends RecyclerView.ViewHolder {
        TextView txt_type;
        TextView txt_client;
        TextView txt_date;
        TextView txt_elapsed;
        ImageView img_close_visit;
        ImageView img_add_obs;
        ImageView img_location;
        VisitHistoryAdapter adapter;

        public VisitHistoryHolder(View itemView, VisitHistoryAdapter Adapter) {
            super(itemView);
            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            txt_client = (TextView) itemView.findViewById(R.id.txt_client);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_elapsed = (TextView) itemView.findViewById(R.id.txt_elapsed);
            img_close_visit = (ImageView) itemView.findViewById(R.id.img_close_visit);
            img_add_obs = (ImageView) itemView.findViewById(R.id.img_add_observation);
            img_location = (ImageView) itemView.findViewById(R.id.img_location);
            adapter = Adapter;
        }

        public void setVisitHistory(final String idVisit, String type, String client, String date, String elapsed, int finished, final double lat, final double lon) {
            txt_type.setText(type);
            txt_client.setText(client);
            txt_date.setText(date);
            txt_elapsed.setText(elapsed);

            img_location.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LocationActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("lat", String.valueOf(lat));
                    intent.putExtra("lon", String.valueOf(lon));

                    context.startActivity(intent);
                }
            });

            if (finished == 1) {
                img_close_visit.setVisibility(View.INVISIBLE);
                img_add_obs.setVisibility(View.INVISIBLE);
            }
            else {
                img_close_visit.setVisibility(View.VISIBLE);
                img_add_obs.setVisibility(View.VISIBLE);

                img_close_visit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("¿Esta seguro que desea cerrar esta visita");
                        dialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeVisit(idVisit);
                            }
                        });
                        AlertDialog alert = dialog.create();
                        alert.show();
                    }
                });

                img_add_obs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("Agregar una observación");
                        final EditText input = new EditText(context);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        dialog.setView(input);
                        dialog.setPositiveButton("AGREGAR", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String obs = input.getText().toString();

                                if (obs != null) {
                                    addObservation(idVisit, obs);
                                } else {
                                    DialogCreator dialogCreator = new DialogCreator(context);
                                    dialogCreator.createCustomDialog("Para agregar una observación, debes enviar una observación", "ACEPTAR");
                                }
                            }
                        });
                        AlertDialog alert = dialog.create();
                        alert.show();
                    }
                });
            }
        }

        public void addObservation(String idvisit, String obs) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            final String idVisit = idvisit;
            final String observation = obs;

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    ApiConstants.COMPLETE_URL_ADD_OBSERVATION,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Se ha agregado la observación", Toast.LENGTH_LONG).show();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            transactionFailure();
                        }
                    }) {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("idVisit", idVisit);
                    params.put("observation", observation);

                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }


        public void closeVisit(String idVisit) {
            Location location = getLocation();
            if (location != null) {
                sendDataToServer(idVisit, getLocation());
            }
            else {
                DialogCreator dialogCreator = new DialogCreator(context);
                dialogCreator.createCustomDialog("No se ha podido obtener la ubicación, por favor verifique que los servicios de ubicación y de internet esten activos y con precisión alta e intente de nuevo. En algunos dispositivos puede tomar un poco más de tiempo", "ACEPTAR");
            }
        }


        public void sendDataToServer(String idVisit, Location location) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            final String idvisit = idVisit;
            final String fLatitude = String.valueOf(location.getLatitude());
            final String fLongitude = String.valueOf(location.getLongitude());
            GeocoderAdapter ga = new GeocoderAdapter(context, location);
            final String loc = ga.getLocation();

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    ApiConstants.COMPLETE_URL_CLOSE_VISIT,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Se ha cerrado la visita", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("user", user);
                            context.startActivity(intent);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            transactionFailure();
                        }
                    }) {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("idUser", user.getIdUser());
                    params.put("idVisit", idvisit);
                    params.put("fLongitude", fLatitude);
                    params.put("fLatitude", fLongitude);
                    params.put("fLocation", loc);

                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }

        public void transactionFailure() {
            DialogCreator dialogCreator = new DialogCreator(context);
            dialogCreator.createCustomDialog("Ha ocurrido un error, por favor intenta más tarde", "ACEPTAR");
        }

        public Location getLocation() {
            MyLocationListener myLocationListener = new MyLocationListener(context);
            if (!myLocationListener.checkPermission()) {
                DialogCreator dialogCreator = new DialogCreator(context);
                dialogCreator.createCustomDialog("La aplicación no tiene permiso para obtener tu ubicación, por favor reinstala y agrega permisos de ubicación", "ACEPTAR");
            }

            if (!myLocationListener.checkLocationServices()) {
                DialogCreator dialogCreator = new DialogCreator(context);
                dialogCreator.createActivateGPSDialog();
            }

            myLocationListener.start();

            return  myLocationListener.getLocation();
        }
    }
}
