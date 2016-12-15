package com.iconasystems.android.trumeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iconasystems.android.trumeter.adapters.TownZoneAdapter;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.api.TaskResponse;
import com.iconasystems.android.trumeter.model.MeterReader;
import com.iconasystems.android.trumeter.model.Route;
import com.iconasystems.android.trumeter.model.Task;
import com.iconasystems.android.trumeter.model.TownZone;
import com.iconasystems.android.trumeter.utils.DateUtils;
import com.iconasystems.android.trumeter.utils.DividerItemDecoration;
import com.iconasystems.android.trumeter.utils.GPSTracker;
import com.iconasystems.android.trumeter.utils.InterfaceUtils;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trumeter.iconasystems.com.trumeter.R;

public class Towns extends AppCompatActivity {
    private RecyclerView mTownsList;
    private ProgressBar mProgressBar;
    private Set<TownZone> towns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towns);

        mProgressBar = (ProgressBar) findViewById(R.id.load_towns_progress);
        mTownsList = (RecyclerView) findViewById(R.id.towns_list);
        InterfaceUtils.showProgress(this, true, mTownsList, mProgressBar);
        mTownsList.setLayoutManager(new LinearLayoutManager(this));
        mTownsList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        TextView tmDate = (TextView) findViewById(R.id.time_date);
        LocalDate localDateTime = LocalDate.now();
        int day = localDateTime.getDayOfWeek();
        int dayMonth = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonthOfYear();
        int year = localDateTime.getYear();
        String dayOfWeek = DateUtils.parseDayToString(day);
        String monthOfYear = DateUtils.parseMonthToString(month);
        String dt = DateUtils.dateBuilder(dayOfWeek, dayMonth, monthOfYear, year);
        tmDate.setText(dt);

        GPSTracker tracker = new GPSTracker(this);

        if (!tracker.canGetLocation()) {
              tracker.showSettingsAlert();
        }
        AppPreferences prefs = AppPreferences.get(this);
        MeterReader reader = prefs.getUser();


        ApiService apiService = ApiModule.getClient().create(ApiService.class);

        final List<TownZone> townZoneList = new ArrayList<>();
        final List<Route> routeList = new ArrayList<>();
        final Set<TownZone> townZoneSet = new HashSet<>();
        final Set<Route> routeSet = new HashSet<>();
        towns = new HashSet<>();

        Call<TaskResponse> taskListCall = apiService.getTasks(reader.getId());
        taskListCall.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.body() != null) {
                    for (Task task : response.body().getTasks()) {
                        String townName = task.getTown_name();
                        String zoneName = task.getZone_name();
                        String zoneCode = task.getZone_code();
                        String areaCode = task.getArea_code();
                        int townId = task.getTown_id();
                        int zoneId = task.getZone_id();
                        String routeName = task.getRoute_name();
                        int routeId = task.getRoute_id();
                        Route route = new Route();
                        route.setId(routeId);
                        route.setName(routeName);

                        routeSet.add(route);

                        TownZone townZone = new TownZone();
                        townZone.setArea_code(areaCode);
                        townZone.setTown_name(townName);
                        townZone.setTown_id(townId);
                        townZone.setZone_code(zoneCode);
                        townZone.setZone_id(zoneId);
                        townZone.setZone_name(zoneName);

                        towns.add(townZone);
                        townZoneSet.add(townZone);
                    }

                    for(TownZone zone : towns){
                        townZoneList.add(zone);
                    }

                    for (Route route :routeSet){
                        routeList.add(route);
                    }
                    InterfaceUtils.showProgress(getApplicationContext(), false, mTownsList, mProgressBar);

                    mTownsList.setAdapter(new TownZoneAdapter(townZoneList,
                            new TownZoneAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(TownZone townZone) {
                                    String zoneId = String.valueOf(townZone.getZone_id());
                                    String townId = String.valueOf(townZone.getTown_id());
                                    Intent zonesIntent = new Intent(Towns.this, Routes.class);
                                    zonesIntent.putExtra("zone_id", zoneId);
                                    zonesIntent.putExtra("town_id", townId);
                                    Gson gson = new Gson();
                                    String list = gson.toJson(routeList, new TypeToken<List<Route>>() {
                                    }.getType());
                                    zonesIntent.putExtra("routes", list);
                                    startActivity(zonesIntent);
                                }
                            }));


                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.d("Trumeter error", t.getMessage());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_report:
                startActivity(new Intent(this, Report.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, About.class));

        }
        return super.onOptionsItemSelected(item);
    }
}
