package nl.sjtek.client.android.services;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import nl.sjtek.client.android.activities.ActivityLogin;

public class AccountService extends Service {

    private static SjtekAuthenticator authenticator;

    public AccountService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder ret = null;
        if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            ret = getAuthenticator().getIBinder();
        }
        return ret;
    }

    private SjtekAuthenticator getAuthenticator() {
        if (authenticator == null) {
            authenticator = new SjtekAuthenticator(this);
        }

        return authenticator;
    }

    private static class SjtekAuthenticator extends AbstractAccountAuthenticator {

        private Context context;

        public SjtekAuthenticator(Context context) {
            super(context.getApplicationContext());
            Log.d(getClass().getSimpleName(), "Creating Sjtek authenticator");
            this.context = context.getApplicationContext();
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String s, String s1, String[] strings, Bundle bundle) throws NetworkErrorException {
            Bundle reply = new Bundle();

            Intent i = new Intent(context, ActivityLogin.class);
            i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
            reply.putParcelable(AccountManager.KEY_INTENT, i);

            return reply;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public String getAuthTokenLabel(String s) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
            // This call is used to query whether the Authenticator supports
            // specific features. We don't expect to get called, so we always
            // return false (no) for any queries.
            Bundle result = new Bundle();
            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
            return result;
        }
    }
}
