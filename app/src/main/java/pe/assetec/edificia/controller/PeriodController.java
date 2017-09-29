package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.assetec.edificia.model.Departament;
import pe.assetec.edificia.model.Period;

/**
 * Created by frank on 19/09/17.
 */

public class PeriodController {
    public static ArrayList<Period> fromJson(JSONArray jsonArray) {
        JSONObject periodJson;
        ArrayList<Period> periods = new ArrayList<Period>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                periodJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Period period = Period.fromJson(periodJson);
            if (period != null) {
                periods.add(period);
            }
        }
        return periods;
    }
}
