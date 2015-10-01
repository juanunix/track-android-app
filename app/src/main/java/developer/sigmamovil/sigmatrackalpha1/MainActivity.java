package developer.sigmamovil.sigmatrackalpha1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.security.PublicKey;

import developer.sigmamovil.sigmatrackalpha1.domain.User;
import developer.sigmamovil.sigmatrackalpha1.ui.fragment.VisitHistoryFragment;

public class MainActivity extends AppCompatActivity {

    public User user;
    public String disableNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getSerializableExtra("user") == null) {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        }

        user = (User) getIntent().getSerializableExtra("user");
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);

        if (savedInstanceState == null) {
            VisitHistoryFragment visitHistoryFragment = new VisitHistoryFragment();
            visitHistoryFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, visitHistoryFragment)
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent action = new Intent(getApplicationContext(), NewVisitActivity.class);
                if (disableNew != null) {
                    action.putExtra("disableNew", disableNew);
                }
                action.putExtra("user", user);
                startActivity(action);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        if (id == R.id.action_new_visit) {
            newVisit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        Intent action = new Intent(getApplicationContext(), LoginActivity.class);
        action.removeExtra("user");
        startActivity(action);
    }

    public void newVisit() {
        Intent action = new Intent(getApplicationContext(), NewVisitActivity.class);
        action.putExtra("user", user);
        startActivity(action);
    }

    public void setDisableNewVisit(String string) {
        disableNew = string;
    }
}
