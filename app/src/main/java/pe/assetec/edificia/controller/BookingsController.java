package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.assetec.edificia.model.Booking;
import pe.assetec.edificia.model.Booking;

/**
 * Created by frank on 16/10/17.
 */

public class BookingsController {

    public static ArrayList<Booking> fromJson(JSONArray jsonArray) {
        JSONObject bookingJson;
        ArrayList<Booking> bookings = new ArrayList<Booking>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                bookingJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Booking booking = Booking.fromJson(bookingJson);
            if (booking != null) {
                bookings.add(booking);
            }
        }
        return bookings;
    }
}
