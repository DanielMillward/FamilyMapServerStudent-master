package LocalJSONObjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NamesList {
    @SerializedName("data")
    private ArrayList<String> fNamesList;

    public ArrayList<String> getList() {
        return fNamesList;
    }
}
