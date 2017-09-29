package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.assetec.edificia.model.Building;
import pe.assetec.edificia.model.Invoice;

/**
 * Created by frank on 12/09/17.
 */

public class InvoicesController {



    public static ArrayList<Invoice> fromJson(JSONArray jsonArray) {
        JSONObject invoiceJson;
        ArrayList<Invoice> invoices = new ArrayList<Invoice>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                invoiceJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Invoice invoice = Invoice.fromJson(invoiceJson);
            if (invoice != null) {
                invoices.add(invoice);
            }
        }

        return invoices;
    }


}
