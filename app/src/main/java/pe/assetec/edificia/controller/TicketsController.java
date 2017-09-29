package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.assetec.edificia.model.Period;
import pe.assetec.edificia.model.Ticket;

/**
 * Created by frank on 22/09/17.
 */

public class TicketsController {

    public static ArrayList<Ticket> fromJson(JSONArray jsonArray) {
        JSONObject ticketJson;
        ArrayList<Ticket> tickets = new ArrayList<Ticket>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                ticketJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Ticket ticket = Ticket.fromJson(ticketJson);
            if (ticket != null) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}
