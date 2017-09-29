package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.assetec.edificia.model.Invoice;
import pe.assetec.edificia.model.InvoiceDetail;

/**
 * Created by frank on 14/09/17.
 */

public class InvoiceDetailController {

    public static ArrayList<InvoiceDetail> fromJson(JSONArray jsonArray) {
        JSONObject invoiceDetailJson;
        ArrayList<InvoiceDetail> invoice_details = new ArrayList<InvoiceDetail>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                invoiceDetailJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            InvoiceDetail invoice_detail = InvoiceDetail.fromJson(invoiceDetailJson);
            if (invoice_detail != null) {
                invoice_details.add(invoice_detail);
            }
        }
        return invoice_details;
    }
}
