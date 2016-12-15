package com.iconasystems.android.trumeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.model.MeterReader;
import com.iconasystems.android.trumeter.model.Session;
import com.iconasystems.android.trumeter.utils.GPSTracker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trumeter.iconasystems.com.trumeter.R;

public class Login extends AppCompatActivity {
    private Button mLoginButton;
    private EditText mUsername;
    private EditText mPassword;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.account_name);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) this.findViewById(R.id.login_button);
        mProgress = (ProgressBar) findViewById(R.id.login_progress);
        final RelativeLayout scrollingView = (RelativeLayout) findViewById(R.id.login_content);

        final GPSTracker tracker = new GPSTracker(Login.this);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mProgress.setVisibility(View.VISIBLE);
                scrollingView.setVisibility(View.GONE);
                ApiService apiService = ApiModule.getClient().create(ApiService.class);
                Session session = new Session();
                session.setUsername(mUsername.getText().toString());
                session.setPassword(mPassword.getText().toString());

                Call<MeterReader> loginSessionCall = apiService.authenticateUser(session);
                loginSessionCall.enqueue(new Callback<MeterReader>() {
                    @Override
                    public void onResponse(Call<MeterReader> call, Response<MeterReader> response) {

                        if (response.body() != null) {
                            mProgress.setVisibility(View.GONE);

                            int id = response.body().getId();

                            AppPreferences preferences = AppPreferences.get(getApplicationContext());
                            preferences.clear();
                            MeterReader reader = new MeterReader();
                            reader.setId(id);
                            preferences.setLoggedIn(reader);
                            startActivity(new Intent(getApplicationContext(), SelectBillingPeriod.class));
                            scrollingView.setVisibility(View.VISIBLE);

                        } else {
                            Snackbar.make(v, "Invalid Login", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MeterReader> call, Throwable t) {
                        Log.e("Error logging in", t.getMessage());
                    }
                });
            }
        });


    }


}
