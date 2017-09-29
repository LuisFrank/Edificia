package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.assetec.edificia.model.Departament;
import pe.assetec.edificia.model.InvoiceDetail;

/**
 * Created by frank on 19/09/17.
 */

public class DepartamentsController {
    public static ArrayList<Departament> fromJson(JSONArray jsonArray) {
        JSONObject departamentJson;
        ArrayList<Departament> depatarments = new ArrayList<Departament>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                departamentJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Departament departament = Departament.fromJson(departamentJson);
            if (departament != null) {
                depatarments.add(departament);
            }
        }
        return depatarments;
    }
}
