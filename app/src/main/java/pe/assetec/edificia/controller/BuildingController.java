package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.assetec.edificia.model.Building;
import pe.assetec.edificia.model.Invoice;

/**
 * Created by frank on 12/09/17.
 */

public class BuildingController {

    public static ArrayList<Building> fromJson(JSONArray jsonArray) {
        JSONObject buildingJson;
        ArrayList<Building> buildings = new ArrayList<Building>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                buildingJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Building building = Building.fromJson(buildingJson);
            if (building != null) {
                buildings.add(building);
            }
        }

        return buildings;
    }
}
