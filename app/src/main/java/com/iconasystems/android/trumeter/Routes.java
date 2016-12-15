package com.iconasystems.android.trumeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iconasystems.android.trumeter.adapters.RoutesAdapter;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.api.TaskResponse;
import com.iconasystems.android.trumeter.model.MeterReader;
import com.iconasystems.android.trumeter.model.Route;
import com.iconasystems.android.trumeter.model.Task;
import com.iconasystems.android.trumeter.utils.DateUtils;
import com.iconasystems.android.trumeter.utils.DividerItemDecoration;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trumeter.iconasystems.com.trumeter.R;

public class Routes extends AppCompatActivity {
    private RecyclerView routesListView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        routesListView = (RecyclerView) findViewById(R.id.routes_list);
        mProgressBar = (ProgressBar) findViewById(R.id.load_progress);
        TextView tmDate = (TextView) findViewById(R.id.time_date);
        routesListView.setLayoutManager(new LinearLayoutManager(this));
        routesListView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        String rt = getIntent().getStringExtra("routes");
        Gson gson =  new Gson();
        List<Route> routes = gson.fromJson(rt,new TypeToken<List<Route>>(){}.getType());
        LocalDate localDateTime = LocalDate.now();
        int day = localDateTime.getDayOfWeek();
        int dayMonth = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonthOfYear();
        int year = localDateTime.getYear();
        String dayOfWeek = DateUtils.parseDayToString(day);
        String monthOfYear = DateUtils.parseMonthToString(month);
        String dt = DateUtils.dateBuilder(dayOfWeek,dayMonth,monthOfYear,year);
        tmDate.setText(dt);

        ApiService apiService = ApiModule.getClient().create(ApiService.class);
        final List<Route> routesList = new ArrayList<>();
        AppPreferences prefs = AppPreferences.get(this);
        MeterReader reader = prefs.getUser();

        final Set<Route> routeSet = new HashSet<>();

        Call<TaskResponse> taskListCall = apiService.getTasks(reader.getId());
        taskListCall.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.body() != null){
                    for(Task task : response.body().getTasks()){
                        String townName = task.getTown_name();
                        String zoneName = task.getZone_name();
                        String zoneCode = task.getZone_code();
                        String areaCode = task.getArea_code();
                        String routeName = task.getRoute_name();
                        int townId = task.getTown_id();
                        int zoneId = task.getZone_id();
                        int routeId = task.getRoute_id();

                        Route route = new Route();
                        route.setId(routeId);
                        route.setName(routeName);
                        //routesList.add(route);
                        routeSet.add(route);
                    }
                    for (Route route : routeSet){
                        routesList.add(route);
                        Log.d("Route", route.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.d("Trumeter error", t.getMessage());
            }
        });

        routesListView.setAdapter(new RoutesAdapter(routes, getApplicationContext(), new RoutesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Route zone) {
                Intent customers = new Intent(Routes.this, Households.class);
                customers.putExtra("route_id", String.valueOf(zone.getId()));
                startActivity(customers);
            }
        }));


    }
}
