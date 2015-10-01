package developer.sigmamovil.sigmatrackalpha1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import developer.sigmamovil.sigmatrackalpha1.domain.User;
import developer.sigmamovil.sigmatrackalpha1.io.ApiConstants;
import developer.sigmamovil.sigmatrackalpha1.misc.DialogCreator;

/**
 * A login screen that offers login via idAccount/username/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mIdCompany;
    private EditText mUsername;
    private EditText mPasswordView;
    private Context context;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mIdCompany = (EditText) findViewById(R.id.txt_id);
        mUsername = (EditText) findViewById(R.id.txt_username);
        mPasswordView = (EditText) findViewById(R.id.txt_password);
        context = getApplicationContext();

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mIdCompany.setError(null);
        mUsername.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String idCompany = mIdCompany.getText().toString();
        String username = mUsername.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_empty_username));
            focusView = mUsername;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(idCompany)) {
            mIdCompany.setError(getString(R.string.error_empty_id_account));
            focusView = mIdCompany;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(idCompany, username, password);
            mAuthTask.execute((Void) null);
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mIdAccount;
        private final String mUsername;
        private final String mPassword;


        UserLoginTask(String idCompany, String username, String password) {
            mIdAccount = idCompany;
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    ApiConstants.COMPLETE_URL_POST_LOGIN,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            validateResponseLogin(response);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            onCancelled();
                            DialogCreator dialogCreator = new DialogCreator(LoginActivity.this);
                            dialogCreator.createCustomDialog("Se ha presentado un error, por favor intenta más tarde", "ACEPTAR");
                        }
                    }) {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/x-www-form-urlencoded");
                            params.put("idAccount", mIdAccount);
                            params.put("username", mUsername);
                            params.put("password", mPassword);

                            return params;
                        }
            };

            requestQueue.add(stringRequest);

            return true;
        }

        protected void validateResponseLogin(String response) {
            mAuthTask = null;
            showProgress(false);

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = (JSONArray) jsonObject.get("status");

                int status = Integer.parseInt(jsonArray.get(0).toString());
                if (status == 1) {
                    JSONArray userArray = (JSONArray) jsonObject.get("user");
                    JSONObject userObj = (JSONObject) userArray.get(0);
                    User user = new User(userObj.getString("idUser"), userObj.getString("name"), userObj.getString("lastName"));
                    Intent action = new Intent(getApplicationContext(), MainActivity.class);
                    action.putExtra("user", user);
                    startActivity(action);
                } else {
                    mPasswordView.setError(getString(R.string.error_invalid_login));
                    mPasswordView.requestFocus();
                }
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

