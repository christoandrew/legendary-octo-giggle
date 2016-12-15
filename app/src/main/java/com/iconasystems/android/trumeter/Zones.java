package com.iconasystems.android.trumeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.iconasystems.android.trumeter.adapters.ZonesAdapter;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.model.Zone;
import com.iconasystems.android.trumeter.utils.DividerItemDecoration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trumeter.iconasystems.com.trumeter.R;

public class Zones extends AppCompatActivity {
    private RecyclerView mZoneList;
    private int townId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zones);
        townId = Integer.parseInt(getIntent().getStringExtra("town_id"));
        mZoneList = (RecyclerView) findViewById(R.id.zone_list);
        mZoneList.setLayoutManager(new LinearLayoutManager(this));
        mZoneList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        ApiService apiService = ApiModule.getClient().create(ApiService.class);
        Call<List<Zone>> zoneCall = apiService.getZones(townId);
        zoneCall.enqueue(new Callback<List<Zone>>() {
            @Override
            public void onResponse(Call<List<Zone>> call, Response<List<Zone>> response) {
                List<Zone> zonesList = response.body();
                mZoneList.setAdapter(new ZonesAdapter(zonesList, getApplicationContext(),
                        new ZonesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Zone zone) {
                        String zoneId = String.valueOf(zone.getId());
                        Intent households = new Intent(getApplicationContext(), Households.class);
                        households.putExtra("zone_id",zoneId);
                        startActivity(households);
                    }
                }));
            }

            @Override
            public void onFailure(Call<List<Zone>> call, Throwable t) {
                Log.e("Trumeter", t.toString());
            }
        });
    }
}
