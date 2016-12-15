package com.iconasystems.android.trumeter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.iconasystems.android.trumeter.adapters.BillingPeriodsAdapter;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.model.BillingPeriod;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trumeter.iconasystems.com.trumeter.R;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class SelectBillingPeriod extends AppCompatActivity {
    private Button mFinish;
    private Spinner mBillingPeriod;
    private int billingPeriodId;
    private String endDate;
    private String startDate;
    private AppPreferences preferences;

    @RequiresApi(api = JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_billing_period);
        mFinish = (Button) findViewById(R.id.finish_select_bp);
        mBillingPeriod = (Spinner) findViewById(R.id.billing_period);

        preferences = AppPreferences.get(this);

        final ApiService apiService = ApiModule.getClient().create(ApiService.class);

        Call<List<BillingPeriod>> billingPeriodListCall = apiService.getBillingPeriods();
        billingPeriodListCall.enqueue(new Callback<List<BillingPeriod>>() {
            @Override
            public void onResponse(Call<List<BillingPeriod>> call, Response<List<BillingPeriod>> response) {
                List<BillingPeriod> billingPeriodList = response.body();
                mBillingPeriod.setAdapter(new BillingPeriodsAdapter(SelectBillingPeriod.this, billingPeriodList));
            }

            @Override
            public void onFailure(Call<List<BillingPeriod>> call, Throwable t) {
                Log.d("Trumeter error", t.getMessage());
            }

        });

        if (Build.VERSION.SDK_INT >= JELLY_BEAN) {
            //mBillingPeriod.setPopupBackgroundResource(R.color.background);
        }

        mBillingPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView idTextView = (TextView) view.findViewById(R.id.billing_period_id);
                TextView startDateTextView = (TextView) view.findViewById(R.id.start_date);
                TextView endDateTextView = (TextView) view.findViewById(R.id.end_date);
                billingPeriodId = Integer.parseInt(idTextView.getText().toString());
                startDate = startDateTextView.getText().toString();
                endDate = endDateTextView.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingPeriod billingPeriod = new BillingPeriod();
                billingPeriod.setStart_date(startDate);
                billingPeriod.setEnd_date(endDate);
                billingPeriod.setId(billingPeriodId);

                preferences.setBillingPeriod(billingPeriod);
                startActivity(new Intent(SelectBillingPeriod.this, Towns.class));
            }
        });
    }
}
