package nl.sjtek.client.android.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.events.AuthFailedEvent;
import nl.sjtek.client.android.events.AuthSuccessfulEvent;
import nl.sjtek.client.android.utils.Storage;
import nl.sjtek.control.data.settings.DataCollection;

public class ActivityLogin extends AppCompatActivity implements
        View.OnClickListener {

    private Holder holder;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        holder = new Holder();
        initClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initClickListeners() {
        holder.buttonSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSignIn) signIn();
    }

    private void signIn() {
        String username = holder.editTextUsername.getText().toString();
        String password = holder.editTextPassword.getText().toString();

        progressDialog.show();

        API.authenticate(getApplicationContext(), username, password);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(AuthSuccessfulEvent event) {
        DataCollection response = event.getDataCollection();
        progressDialog.dismiss();
        String username = holder.editTextUsername.getText().toString();
        String password = holder.editTextPassword.getText().toString();
        Storage.getInstance(this).setCredentials(username, password);
        Storage.getInstance(this).setCheckExtraLights(response.getUsers().get(username).isCheckExtraLight());
        Storage.getInstance(this).setDefaultPlaylist(response.getUsers().get(username).getDefaultPlaylist());
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorResponse(AuthFailedEvent event) {
        VolleyError error = event.getVolleyError();
        progressDialog.dismiss();
        String message = String.format(getString(R.string.sign_in_error),
                (error.networkResponse != null ? "" + error.networkResponse.statusCode : "-"), error.getMessage());
//        new AlertDialog.Builder(getApplicationContext())
//                .setTitle(getString(R.string.oops))
//                .setMessage(message)
//                .create()
//                .show();
    }

    private class Holder {
        private final EditText editTextUsername;
        private final EditText editTextPassword;
        private final Button buttonSignIn;

        private Holder() {
            this.editTextUsername = (EditText) findViewById(R.id.editTextUsername);
            this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            this.buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        }
    }
}
