package by.korovkin.restClient.rest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpStatus;

import by.korovkin.restClient.AbstractAsyncActivity;
import by.korovkin.restClient.R;
import by.korovkin.restClient.bean.Message;
import by.korovkin.restClient.rest.helper.SessionManager;
import by.korovkin.restClient.rest.service.IssueService;

public class LoginActivity extends AbstractAsyncActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private SessionManager session;
    private IssueService issueService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Session manager
        session = new SessionManager(getApplicationContext());
        issueService = new IssueService(getApplicationContext());

        // Check if user is already logged in
        if (!session.isLoggedIn().equals("")) {
            new loginWithToken().execute(session.isLoggedIn());
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    new loginTask().execute(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //  Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                //  startActivity(i);
                finish();
            }
        });

    }

    /**
     * Try to login with login and password
     */
    private class loginTask extends AsyncTask<String, Void, Message> {

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected Message doInBackground(String... voids) {
            return issueService.login(voids[0], voids[1]);
        }

        @Override
        protected void onPostExecute(Message message) {
            dismissProgressDialog();
            check(message);
        }
    }

    /**
     * Try to login with token
     */
    private class loginWithToken extends AsyncTask<String, Void, Message> {
        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected Message doInBackground(String... strings) {
            return issueService.login(strings[0]);
        }

        @Override
        protected void onPostExecute(Message message) {
            dismissProgressDialog();
            check(message);
        }
    }

    /**
     * Open next activity if message have status ok
     * @param message status message
     */
    private void check(Message message) {
        if (message.getStatus() == HttpStatus.OK) {
            session.setLogin( message.getMessage());
            Intent intent = new Intent(LoginActivity.this, IssueListActivity.class);
            startActivity(intent);
            finish();
        } else {
            session.setLogin("");
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
