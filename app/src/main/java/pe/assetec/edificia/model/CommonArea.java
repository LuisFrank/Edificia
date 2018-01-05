package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 16/10/17.
 */

public class CommonArea {

    private Integer id;
    private Integer building_id;
    private String name;
    private String created_at;
    private String description;
    private String regulation;
    private Integer minimun;
    private Boolean automatic_approvement;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Integer building_id) {
        this.building_id = building_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public Integer getMinimun() {
        return minimun;
    }

    public void setMinimun(Integer minimun) {
        this.minimun = minimun;
    }

    public Boolean getAutomatic_approvement() {
        return automatic_approvement;
    }

    public void setAutomatic_approvement(Boolean automatic_approvement) {
        this.automatic_approvement = automatic_approvement;
    }

    public static CommonArea fromJson(JSONObject jsonObject) {
        CommonArea commom_area  = new CommonArea();
        // Deserialize json into object fields
        try {
            commom_area.setId(jsonObject.getInt("id"));
            commom_area.setBuilding_id(jsonObject.getInt("building_id"));
            commom_area.setName(jsonObject.getString("name"));
            commom_area.setDescription(jsonObject.getString("description"));
            commom_area.setRegulation(jsonObject.getString("regulation"));
            if(!jsonObject.isNull("automatic_approvement"))
            {
                commom_area.setAutomatic_approvement(jsonObject.getBoolean("automatic_approvement"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return commom_area;
    }


    @Override
    public String toString() {
        return getName(); // You can add anything else like maybe getDrinkType()
    }


}
