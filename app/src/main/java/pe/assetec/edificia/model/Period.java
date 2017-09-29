package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by frank on 19/09/17.
 */

public class Period {

    private Integer id;
    private Integer building_id;
    private Date issue;
    private Date due;
    private Date date;
    private String name ;
    private String abbr_name;


    public Period() {
        this.id = id;
        this.building_id = building_id;
        this.issue = issue;
        this.due = due;
        this.date = date;
        this.name = name;
        this.abbr_name = abbr_name;
    }


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

    public Date getIssue() {
        return issue;
    }

    public void setIssue(Date issue) {
        this.issue = issue;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr_name() {
        return abbr_name;
    }

    public void setAbbr_name(String abbr_name) {
        this.abbr_name = abbr_name;
    }


    // Decodes business json into business model object
    public static Period fromJson(JSONObject jsonObject) {
        Period b = new Period();
        // Deserialize json into object fields
        try {
            b.setId(jsonObject.getInt("id"));
            b.setName(jsonObject.getString("name"));
            b.setBuilding_id(jsonObject.getInt("building_id"));
            b.setAbbr_name(jsonObject.getString("abbr_name"));
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
