package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 12/09/17.
 */

public class Departament {

    private  Integer id;
    private  String name;
    private  Integer building_id;

    public Departament() {
        this.id = id;
        this.name = name;
        this.building_id = building_id;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Integer building_id) {
        this.building_id = building_id;
    }

    // Decodes business json into business model object
    public static Departament fromJson(JSONObject jsonObject) {
        Departament b = new Departament();
        // Deserialize json into object fields
        try {
            b.setId(jsonObject.getInt("id"));
            b.setName(jsonObject.getString("name"));
            b.setBuilding_id(jsonObject.getInt("building_id"));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }


    @Override
    public String toString() {
        return getName(); // You can add anything else like maybe getDrinkType()
    }
}
