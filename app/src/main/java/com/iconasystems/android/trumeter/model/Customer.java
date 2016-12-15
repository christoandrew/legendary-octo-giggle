package com.iconasystems.android.trumeter.model;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/7/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class Customer extends BaseModel {
    @Column
    @PrimaryKey
    @SerializedName("id")
    int id;
    @Column
    @SerializedName("customer_name")
    String name;
    @Column
    @SerializedName("address")
    String address;
    @Column
    @SerializedName("meter_number")
    String meter_number;
    @Column
    @SerializedName("route_name")
    String route_name;
    @Column
    @SerializedName("route_id")
    int route_id;
    @Column
    @SerializedName("meter_id")
    int meter_id;
    @Column
    @SerializedName("posting_group")
    String billing_group;

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeter_number() {
        return meter_number;
    }

    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getMeter_id() {
        return meter_id;
    }

    public void setMeter_id(int meter_id) {
        this.meter_id = meter_id;
    }

    public String getBilling_group() {
        return billing_group;
    }

    public void setBilling_group(String billing_group) {
        this.billing_group = billing_group;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", meter_number='" + meter_number + '\'' +
                ", route_name='" + route_name + '\'' +
                ", route_id=" + route_id +
                ", meter_id=" + meter_id +
                ", billing_group='" + billing_group + '\'' +
                '}';
    }
}
