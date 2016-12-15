package com.iconasystems.android.trumeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.iconasystems.android.trumeter.adapters.CustomersAdapter;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.model.Customer;
import com.iconasystems.android.trumeter.utils.DividerItemDecoration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trumeter.iconasystems.com.trumeter.R;

public class Households extends AppCompatActivity {
    private int zoneId;
    private RecyclerView mCustomersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_households);
        mCustomersList = (RecyclerView) findViewById(R.id.customer_list);
        mCustomersList.setLayoutManager(new LinearLayoutManager(this));
        mCustomersList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        zoneId = Integer.parseInt(getIntent().getStringExtra("route_id"));
        final AppPreferences preferences = AppPreferences.get(getApplicationContext());


        ApiService apiService = ApiModule.getClient().create(ApiService.class);
        Call<List<Customer>> custListCall = apiService.getCustomers(zoneId);
        custListCall.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                List<Customer> customerList = response.body();
                if(customerList.size() > 0) {
                    mCustomersList.setAdapter(new CustomersAdapter(customerList, getApplicationContext(),
                            preferences, new CustomersAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Customer customer) {
                            Intent bill = new Intent(getApplicationContext(), Bill.class);
                            Gson gson = new Gson();
                            String customerJson = gson.toJson(customer);
                            bill.putExtra("customer",customerJson);
                            startActivity(bill);
                        }
                    }));
                }

            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {

            }
        });
    }
}
