package com.iconasystems.android.trumeter.api;

import com.iconasystems.android.trumeter.model.BillingPeriod;
import com.iconasystems.android.trumeter.model.Customer;
import com.iconasystems.android.trumeter.model.Issue;
import com.iconasystems.android.trumeter.model.Meter;
import com.iconasystems.android.trumeter.model.MeterReader;
import com.iconasystems.android.trumeter.model.MeterReading;
import com.iconasystems.android.trumeter.model.Route;
import com.iconasystems.android.trumeter.model.Session;
import com.iconasystems.android.trumeter.model.Town;
import com.iconasystems.android.trumeter.model.TownZone;
import com.iconasystems.android.trumeter.model.Zone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by christoandrew on 10/7/16.
 */

public interface ApiService {
    @GET("admin/towns.json")
    Call<List<Town>> getTowns();
    @GET("admin/town/zones.json")
    Call<List<TownZone>> getTownZones();
    @GET("admin/zones/{route_id}/customers.json")
    Call<List<Customer>> getCustomers(@Path("route_id") int zoneId);
    @GET("admin/meter/{customer_id}.json")
    Call<Meter> getMeter(@Path("customer_id") int customer_id);
    @GET("admin/meter_reading/{meter_id}.json")
    Call<MeterReading> getLastReading(@Path("meter_id") int meter_id);
    @GET("admin/billing_periods.json")
    Call<List<BillingPeriod>> getBillingPeriods();
    @GET("admin/towns/{town_id}/zones.json")
    Call<List<Zone>> getZones(@Path("town_id") int townId);
    @GET("admin/billing_period/{billing_period_id}.json")
    Call<BillingPeriod> getBillingPeriod(@Path("billing_period_id") int billingPeriodId);
    @GET("admin/zones/{zone_id}/routes.json")
    Call<List<Route>> getRoutes(@Path("zone_id") int zone_id);
    @GET("admin/reader_tasks/{meter_reader_id}.json")
    Call<TaskResponse> getTasks(@Path("meter_reader_id") int meter_reader_id);
    @POST("admin/meter_readers/auth.json")
    Call<MeterReader> authenticateUser(@Body Session session);
    @POST("admin/meter_readings.json")
    Call<MeterReading> saveReading(@Body MeterReading meterReading);
    @POST("admin/issues.json")
    Call<Issue> reportIssue(@Body Issue issue);
}
