package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

/**
 * Created by frank on 16/01/18.
 */

public class Block {
    private int id;
    private int final_day_id;
    private String start_time;
    private String final_time;
    private String start_time_name;
    private String final_time_name;
    private String start_day_name;
    private String final_day_name;
    private String min_unit;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinal_day_id() {
        return final_day_id;
    }

    public void setFinal_day_id(int final_day_id) {
        this.final_day_id = final_day_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFinal_time() {
        return final_time;
    }

    public void setFinal_time(String final_time) {
        this.final_time = final_time;
    }

    public String getStart_day_name() {
        return start_day_name;
    }

    public void setStart_day_name(String start_day_name) {
        this.start_day_name = start_day_name;
    }

    public String getFinal_day_name() {
        return final_day_name;
    }

    public void setFinal_day_name(String final_day_name) {
        this.final_day_name = final_day_name;
    }

    public String getMin_unit() {
        return min_unit;
    }

    public void setMin_unit(String min_unit) {
        this.min_unit = min_unit;
    }

    public String getStart_time_name() {
        return start_time_name;
    }

    public void setStart_time_name(String start_time_name) {
        this.start_time_name = start_time_name;
    }

    public String getFinal_time_name() {
        return final_time_name;
    }

    public void setFinal_time_name(String final_time_name) {
        this.final_time_name = final_time_name;
    }


    public String Horarios(){
        return "De " + getStart_day_name() + " " + getStart_time_name() + " a " + getFinal_day_name() + " " + getFinal_time_name();
    }



    public static Block fromJson(JSONObject jsonObject) {
        Block block  = new Block();
        // Deserialize json into object fields
        try {
            block.setId(jsonObject.getInt("id"));
            block.setStart_time(jsonObject.getString("start_time"));
            block.setFinal_time(jsonObject.getString("final_time"));
            block.setStart_time_name(jsonObject.getString("start_time_name"));
            block.setFinal_time_name(jsonObject.getString("final_time_name"));
            block.setStart_day_name(jsonObject.getString("start_day_name"));
            block.setFinal_day_name(jsonObject.getString("final_day_name"));
            block.setMin_unit( jsonObject.getString("min_unit"));


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return block;
    }
}
