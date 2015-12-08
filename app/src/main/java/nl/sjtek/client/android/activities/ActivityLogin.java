package nl.sjtek.client.android.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.AuthenticationRequest;
import nl.sjtek.client.android.utils.Storage;

public class ActivityLogin extends AppCompatActivity implements
        View.OnClickListener,
        Response.Listener<Boolean>,
        Response.ErrorListener {

    private static final String REQUEST_TAG = ActivityLogin.class.getSimpleName() + "_login";
    private Holder holder;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.start();

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                requestQueue.cancelAll(REQUEST_TAG);
            }
        });

        holder = new Holder();
        initClickListeners();
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

        Request request = new AuthenticationRequest(username, password, this, this);
        request.setTag(REQUEST_TAG);
        requestQueue.add(request);
    }

    @Override
    public void onResponse(Boolean response) {
        progressDialog.dismiss();
        String username = holder.editTextUsername.getText().toString();
        String password = holder.editTextPassword.getText().toString();
        Storage.getInstance().setCredentials(username, password);
        finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        String message = String.format(getString(R.string.sign_in_error),
                (error.networkResponse != null ? "" + error.networkResponse.statusCode : "-"), error.getMessage());
        new AlertDialog.Builder(getApplicationContext())
                .setTitle(getString(R.string.oops))
                .setMessage(message)
                .create()
                .show();
    }

    private class Holder {
        private EditText editTextUsername;
        private EditText editTextPassword;
        private Button buttonSignIn;

        private Holder() {
            this.editTextUsername = (EditText) findViewById(R.id.editTextUsername);
            this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            this.buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        }
    }
}
