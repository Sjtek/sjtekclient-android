package nl.sjtek.client.android.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.events.AuthFailedEvent;
import nl.sjtek.client.android.events.AuthSuccessfulEvent;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.control.data.settings.DataCollection;

public class ActivityLogin extends AppCompatActivity {

    @BindView(R.id.editTextUsername)
    EditText editTextUsername;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
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

    @OnClick(R.id.buttonSignIn)
    public void signIn() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        progressDialog.show();

        API.authenticate(getApplicationContext(), username, password);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(AuthSuccessfulEvent event) {
        DataCollection response = event.getDataCollection();
        progressDialog.dismiss();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        Preferences.getInstance(this).setCredentials(username, password);
        Preferences.getInstance(this).setCheckExtraLights(response.getUsers().get(username).isCheckExtraLight());
        Preferences.getInstance(this).setDefaultPlaylist(response.getUsers().get(username).getDefaultPlaylist());
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorResponse(AuthFailedEvent event) {
        VolleyError error = event.getVolleyError();
        progressDialog.dismiss();
        String message = String.format(getString(R.string.sign_in_error),
                (error.networkResponse != null ? "" + error.networkResponse.statusCode : "-"), error.getMessage());
        new MaterialDialog.Builder(this)
                .title("Authentication error")
                .content(message)
                .neutralText("OK")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }
}
