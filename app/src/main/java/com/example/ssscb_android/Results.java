package com.example.ssscb_android;

import com.google.gson.annotations.SerializedName;

public class Results {

    @SerializedName("crimeScreenshot")
    public String CrimeScreenshot;
    @SerializedName("anomalyDateTime")
    public String AnomalyDateTime;
    @SerializedName("anomalyType")
    public String anomalyType;
    @SerializedName("actionPriority")
    public String ActionPriority;
    @SerializedName("ZoneID")
    public int ZoneID;



    public Results(String name,String dt1, String Zone, String AnomalyType,String priority1) {
        this.CrimeScreenshot = name;
        this.AnomalyDateTime = dt1;
        this.ZoneID = Integer.parseInt(Zone);
        this.anomalyType = AnomalyType;
        this.ActionPriority = priority1;

    }


}