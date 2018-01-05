package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 16/10/17.
 */

public class Booking {

    private Integer id;
    private Integer building_id;
    private Integer departament_id;
    private Integer common_area_id;
    private String  name;
    private String description;
    private Integer status_cd;
    private String created_at;
    private String observations;
    private String date;
    private String start_time;
    private String end_time;
    private String period_id;
    private String person_id;

//    -----------------------
    private String common_area_name;
    private String status_name;
    private String person_name;


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

    public Integer getDepartament_id() {
        return departament_id;
    }

    public void setDepartament_id(Integer departament_id) {
        this.departament_id = departament_id;
    }

    public Integer getCommon_area_id() {
        return common_area_id;
    }

    public void setCommon_area_id(Integer common_area_id) {
        this.common_area_id = common_area_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(Integer status_cd) {
        this.status_cd = status_cd;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(String period_id) {
        this.period_id = period_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getCommon_area_name() {
        return common_area_name;
    }

    public void setCommon_area_name(String common_area_name) {
        this.common_area_name = common_area_name;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public static Booking fromJson(JSONObject jsonObject) {
        Booking booking  = new Booking();
        // Deserialize json into object fields
        try {
            booking.setId(jsonObject.getInt("id"));
            booking.setDepartament_id(jsonObject.getInt("departament_id"));
            booking.setBuilding_id(jsonObject.getInt("building_id"));
            booking.setCommon_area_id(jsonObject.getInt("common_area_id"));
            booking.setName(jsonObject.getString("name"));
            booking.setDescription(jsonObject.getString("description"));
            booking.setStatus_cd(jsonObject.getInt("status_cd"));
            booking.setObservations(jsonObject.getString("observations"));
            booking.setDate( jsonObject.getString("date"));
            booking.setStart_time( jsonObject.getString("initial_format"));
            booking.setEnd_time( jsonObject.getString("final_format"));
            booking.setPeriod_id( jsonObject.getString("period_id"));
            booking.setPerson_id( jsonObject.getString("person_id"));
            booking.setCommon_area_name(jsonObject.getJSONObject("common_area").getString("name"));
            booking.setStatus_name( jsonObject.getString("status_name"));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return booking;
    }


    @Override
    public String toString() {
        return getName(); // You can add anything else like maybe getDrinkType()
    }



}
