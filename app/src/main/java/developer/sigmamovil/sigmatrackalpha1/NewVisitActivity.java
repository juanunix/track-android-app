package developer.sigmamovil.sigmatrackalpha1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import developer.sigmamovil.sigmatrackalpha1.domain.Client;
import developer.sigmamovil.sigmatrackalpha1.domain.User;
import developer.sigmamovil.sigmatrackalpha1.domain.VisitType;
import developer.sigmamovil.sigmatrackalpha1.io.ApiConstants;
import developer.sigmamovil.sigmatrackalpha1.io.des_serializer.ClientDeserializer;
import developer.sigmamovil.sigmatrackalpha1.io.des_serializer.VisittypeDeserializer;
import developer.sigmamovil.sigmatrackalpha1.misc.DialogCreator;
import developer.sigmamovil.sigmatrackalpha1.misc.GeocoderAdapter;
import developer.sigmamovil.sigmatrackalpha1.misc.MyLocationListener;
import developer.sigmamovil.sigmatrackalpha1.ui.adapter.ClientAdapter;
import developer.sigmamovil.sigmatrackalpha1.ui.adapter.VisitHistoryAdapter;
import developer.sigmamovil.sigmatrackalpha1.ui.adapter.VisittypeAdapter;

public class NewVisitActivity extends AppCompatActivity {
    public User user;
    public Client client;
    public VisitType visitType;
    public ClientAdapter clientAdapter;
    public VisittypeAdapter visittypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getSerializableExtra("user") == null) {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        }

        //if (getIntent().getStringExtra("disableNew") != null) {
        //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        //    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //}

        user = (User) getIntent().getSerializableExtra("user");

        getClientAndVisittypesData();

        final AutoCompleteTextView autoCompleteTextView_client = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_client);
        autoCompleteTextView_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
               client = (Client) arg0.getItemAtPosition(arg2);
            }
        });

        AutoCompleteTextView autoCompleteTextView_visittype = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_visittype);
        autoCompleteTextView_visittype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                visitType = (VisitType) arg0.getItemAtPosition(arg2);
            }
        });

        Button btn_new_visit = (Button) findViewById(R.id.btn_new_visit);
        btn_new_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);

                Location location = getLocation();

                if (location != null) {
                    if (visitType == null) {
                        showProgress(false);
                        DialogCreator dialogCreator = new DialogCreator(NewVisitActivity.this);
                        dialogCreator.createCustomDialog("Debes seleccionar un tipo de visita", "ACEPTAR");
                    }
                    if (client == null) {
                        String name = autoCompleteTextView_client.getText().toString();
                        if (name != "") {
                            createNewClient(location, name);
                        }
                        else {
                            showProgress(false);
                            DialogCreator dialogCreator = new DialogCreator(NewVisitActivity.this);
                            dialogCreator.createCustomDialog("Debes seleccionar o digitar el nombre del cliente", "ACEPTAR");
                        }
                    }
                    else {
                        createNewVisit(location, client, visitType);
                    }
                } else {
                    showProgress(false);
                    DialogCreator dialogCreator = new DialogCreator(NewVisitActivity.this);
                    dialogCreator.createCustomDialog("No se ha podido obtener la ubicación, por favor verifique que los servicios de ubicación y de internet esten activos y con precisión alta e intente de nuevo. En algunos dispositivos puede tomar un poco más de tiempo", "ACEPTAR");
                }
            }
        });
    }

    private void createNewClient(final Location location, final String name) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ApiConstants.COMPLETE_URL_NEW_CLIENT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = (JSONArray) jsonObject.get("client");
                            JSONObject clientObj = (JSONObject) jsonArray.get(0);
                            Client cl = new Client(clientObj.getString("idClient"), clientObj.getString("name"));
                            createNewVisit(location, cl, visitType);
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DialogCreator dialogCreator = new DialogCreator(NewVisitActivity.this);
                        dialogCreator.createCustomDialog("Ha ocurrido un error, por favor intente de nuevo", "ACEPTAR");
                        showProgress(false);
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("idUser", user.getIdUser());
                params.put("client", name);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    };

    private void getClientAndVisittypesData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiConstants.COMPLETE_URL_CLIENTS_AND_VISIT_TYPES + user.getIdUser(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ClientDeserializer clientDeserializer = new ClientDeserializer(response);
                        VisittypeDeserializer visittypeDeserializer = new VisittypeDeserializer(response);
                        setDataOnSpinners(clientDeserializer.deserialize(), visittypeDeserializer.deserialize());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RES", "That didn't work!");
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void setDataOnSpinners(ArrayList<Client> clients, ArrayList<VisitType> visitTypes) {
        //clientAdapter = new ClientAdapter(NewVisitActivity.this, android.R.layout.simple_spinner_item, clients);
        //Spinner spin_client = (Spinner) findViewById(R.id.spin_client);
        //spin_client.setAdapter(clientAdapter);

        clientAdapter = new ClientAdapter(NewVisitActivity.this, android.R.layout.simple_dropdown_item_1line, clients);
        AutoCompleteTextView autoCompleteTextView_client = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_client);
        autoCompleteTextView_client.setAdapter(clientAdapter);

        //visittypeAdapter = new VisittypeAdapter(NewVisitActivity.this, android.R.layout.simple_spinner_item, visitTypes);
        //Spinner spin_visittypes = (Spinner) findViewById(R.id.spinner_visit_types);
        //spin_visittypes.setAdapter(visittypeAdapter);

        visittypeAdapter = new VisittypeAdapter(NewVisitActivity.this, android.R.layout.simple_dropdown_item_1line, visitTypes);
        AutoCompleteTextView autoCompleteTextView_visittype = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_visittype);
        autoCompleteTextView_visittype.setAdapter(visittypeAdapter);
    }

    private Location getLocation() {
        MyLocationListener myLocationListener = new MyLocationListener(getApplicationContext());
        DialogCreator dialogCreator = new DialogCreator(NewVisitActivity.this);

        if (!myLocationListener.checkPermission()) {
            dialogCreator.createCustomDialog("La aplicación no tiene permiso para obtener tu ubicación, por favor reinstala y agrega permisos de ubicación", "ACEPTAR");
        }

        if (!myLocationListener.checkLocationServices()) {
            dialogCreator.createActivateGPSDialog();
        }

        myLocationListener.start();

        return myLocationListener.getLocation();
    }

    public void createNewVisit(Location location, Client client, VisitType visitType) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String idClient = client.getIdClient();
        final String idVisittype = visitType.getIdVisittype();
        final String latitude = String.valueOf(location.getLatitude());
        final String longitude = String.valueOf(location.getLongitude());
        GeocoderAdapter ga = new GeocoderAdapter(getApplicationContext(), location);
        final String loc = ga.getLocation();
        final String battery = String.valueOf(getBatteryLevel());

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ApiConstants.COMPLETE_URL_NEW_VISIT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        validateNewVisitStatus(response);
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
                params.put("idClient", idClient);
                params.put("idVisittype", idVisittype);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("battery", battery);
                params.put("location", loc);

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }

    public void transactionFailure() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        DialogCreator dialogCreator = new DialogCreator(NewVisitActivity.this);
        dialogCreator.createRedirectCustomDialog("Ha ocurrido un error, por favor intente de nuevo", "ACEPTAR", intent);
        finish();
    }

    public void validateNewVisitStatus(String response) {
        showProgress(false);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get("status");

            int status = Integer.parseInt(jsonArray.get(0).toString());

            if (status == 1) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(), "Se ha iniciado una nueva visita", Toast.LENGTH_LONG).show();
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
            else {
                transactionFailure();
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        //final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.form_new_visit);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.new_visit_progress);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
            //linearLayout.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
            //linearLayout.setVisibility(show ? View.VISIBLE : View.GONE);
            //linearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
