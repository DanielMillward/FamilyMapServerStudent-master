package LocalJSONObjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationList {
    @SerializedName("data")
    private ArrayList<Location> locationList;

    public ArrayList<Location> getList() {
        return locationList;
    }
}
