package nl.sjtek.client.android.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.events.AuthFailedEvent;
import nl.sjtek.client.android.events.AuthSuccessfulEvent;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;
import nl.sjtek.control.data.parsers.UserHolder;
import nl.sjtek.control.data.staticdata.User;

/**
 * Activity for singing in to SjtekControl.
 */
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
        List<User> response = event.getUsers();
        StateManager.INSTANCE.onUsersUpdate(new UserHolder(response, null));
        StateManager.INSTANCE.save(getApplicationContext());

        Preferences preferences = Preferences.getInstance(this);
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        preferences.setCredentials(username, password);

        AccountManager accountManager = AccountManager.get(this);
        accountManager.addAccountExplicitly(new Account(username, "nl.sjtek"), preferences.getToken(), null);

        API.data(this);

        progressDialog.dismiss();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorResponse(AuthFailedEvent event) {
        VolleyError error = event.getVolleyError();
        progressDialog.dismiss();
        String message = String.format(getString(R.string.sign_in_error),
                (error.networkResponse != null ? "" + error.networkResponse.statusCode : "-"), error.getMessage());
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_in_title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }
}
