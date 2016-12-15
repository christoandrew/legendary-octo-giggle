package com.iconasystems.android.trumeter.api;

import com.iconasystems.android.trumeter.model.TownZone;

import java.util.List;

/**
 * Created by christoandrew on 11/26/16.
 */

public class TownZoneResponse {
    private List<TownZone> townZoneList;


    public List<TownZone> getTownZoneList() {
        return townZoneList;
    }

    public void setTownZoneList(List<TownZone> townZoneList) {
        this.townZoneList = townZoneList;
    }
}
