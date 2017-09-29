package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by frank on 12/09/17.
 */

public class Building {

    private Integer building_id;
    private String building_name;
    private Integer departament_id;
    private String departament_name;

    public Building (){
        this.building_id = building_id;
        this.building_name = building_name;
        this.departament_id = departament_id;
        this.departament_name = departament_name;
    }

    public Building(Integer building_id, Integer departament_id, String building_name, String departament_name) {
        this.building_id = building_id;
        this.building_name = building_name;
        this.departament_id = departament_id;
        this.departament_name = departament_name;
    }


    public Integer getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Integer building_id) {
        this.building_id = building_id;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public Integer getDepartament_id() {
        return departament_id;
    }

    public void setDepartament_id(Integer departament_id) {
        this.departament_id = departament_id;
    }

    public String getDepartament_name() {
        return departament_name;
    }

    public void setDepartament_name(String departament_name) {
        this.departament_name = departament_name;
    }

    // Decodes business json into business model object
    public static Building fromJson(JSONObject jsonObject) {
        Building b = new Building();
        // Deserialize json into object fields
        try {
            b.setBuilding_id(jsonObject.getInt("building_id"));
            b.setBuilding_name(jsonObject.getString("building_name"));
            b.setDepartament_id(jsonObject.getInt("departament_id"));
            b.setDepartament_name( jsonObject.getString("departament_name"));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }

    public static Building fromJsonTwo(JSONObject jsonObject) {
        Building b = new Building();
        // Deserialize json into object fields
        try {
            b.setBuilding_id(jsonObject.getInt("id"));
            b.setBuilding_name(jsonObject.getString("name"));


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }

    @Override
    public String toString() {
        return getBuilding_name(); // You can add anything else like maybe getDrinkType()
    }


}
