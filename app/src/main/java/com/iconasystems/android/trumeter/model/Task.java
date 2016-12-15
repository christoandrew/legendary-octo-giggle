package com.iconasystems.android.trumeter.model;

import com.google.gson.annotations.SerializedName;
import com.iconasystems.android.trumeter.config.TrumeterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by christoandrew on 10/22/16.
 */
@Table(databaseName = TrumeterDatabase.NAME)
public class Task extends BaseModel{

    public static final String NAME = "Task";


    @Column
    @PrimaryKey
    @SerializedName("id")
    int id;
    @Column
    @SerializedName("task_id")
    int task_id;
    @Column
    @SerializedName("meter_reader_id")
    int meter_reader_id;
    @Column
    @SerializedName("town_name")
    String town_name;
    @Column
    @SerializedName("area_code")
    String area_code;
    @Column
    @SerializedName("zone_name")
    String zone_name;
    @Column
    @SerializedName("zone_code")
    String zone_code;
    @Column
    @SerializedName("status")
    boolean status;
    @Column
    @SerializedName("town_id")
    int town_id;
    @Column
    @SerializedName("zone_id")
    int zone_id;
    @Column
    @SerializedName("route_id")
    int route_id;
    @Column
    @SerializedName("route_name")
    String route_name;

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTown_name() {
        return town_name;
    }

    public void setTown_name(String town_name) {
        this.town_name = town_name;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getZone_code() {
        return zone_code;
    }

    public void setZone_code(String zone_code) {
        this.zone_code = zone_code;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTown_id() {
        return town_id;
    }

    public void setTown_id(int town_id) {
        this.town_id = town_id;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public int getMeter_reader_id() {
        return meter_reader_id;
    }

    public void setMeter_reader_id(int meter_reader_id) {
        this.meter_reader_id = meter_reader_id;
    }

    public boolean isStatus() {
        return status;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", task_id=" + task_id +
                ", town_name='" + town_name + '\'' +
                ", area_code='" + area_code + '\'' +
                ", zone_name='" + zone_name + '\'' +
                ", zone_code='" + zone_code + '\'' +
                ", status=" + status +
                ", town_id=" + town_id +
                ", zone_id=" + zone_id +
                ", route_id=" + route_id +
                ", route_name='" + route_name + '\'' +
                '}';
    }

}
