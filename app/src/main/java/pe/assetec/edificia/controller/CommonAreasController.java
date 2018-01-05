package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.assetec.edificia.model.Booking;
import pe.assetec.edificia.model.CommonArea;

/**
 * Created by frank on 18/10/17.
 */

public class CommonAreasController {
    public static ArrayList<CommonArea> fromJson(JSONArray jsonArray) {
        JSONObject commonAreaJson;
        ArrayList<CommonArea> commonAreas = new ArrayList<CommonArea>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                commonAreaJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            CommonArea commonArea = CommonArea.fromJson(commonAreaJson);
            if (commonArea != null) {
                commonAreas.add(commonArea);
            }
        }
        return commonAreas;
    }
}
