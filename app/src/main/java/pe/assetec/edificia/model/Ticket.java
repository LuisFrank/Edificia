package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;

/**
 * Created by frank on 22/09/17.
 */

public class Ticket {


    private Integer id;
    private Integer building_id;
    private Integer departament_id;
    private Integer person_id;
    private String summary;
    private String description ;
    private Boolean closed;
    private Boolean read_by_client;
    private Boolean read_by_company;
    private Integer status;
    private String name;
    private String created_at;
    private String created_at_format;

    public Ticket() {

        this.id = id;
        this.building_id = building_id;
        this.departament_id = departament_id;
        this.person_id = person_id;
        this.summary = summary;
        this.description = description;
        this.closed = closed;
        this.read_by_client = read_by_client;
        this.read_by_company = read_by_company;
        this.status = status;
        this.name = name;
        this.created_at = created_at;
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

    public Integer getDepartament_id() {
        return departament_id;
    }

    public void setDepartament_id(Integer departament_id) {
        this.departament_id = departament_id;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getRead_by_client() {
        return read_by_client;
    }

    public void setRead_by_client(Boolean read_by_client) {
        this.read_by_client = read_by_client;
    }

    public Boolean getRead_by_company() {
        return read_by_company;
    }

    public void setRead_by_company(Boolean read_by_company) {
        this.read_by_company = read_by_company;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getCreated_at_format() {
        return created_at_format;
    }

    public void setCreated_at_format(String created_at_format) {
        this.created_at_format = created_at_format;
    }

    public static Ticket fromJson(JSONObject jsonObject) {
        Ticket ticket  = new Ticket();
        // Deserialize json into object fields
        try {
            ticket.setId(jsonObject.getInt("id"));
            ticket.setDepartament_id(jsonObject.getInt("departament_id"));
            ticket.setBuilding_id(jsonObject.getInt("building_id"));
            ticket.setClosed(jsonObject.getBoolean("closed"));
            ticket.setCreated_at(jsonObject.getString("created_at_format"));
            ticket.setName( jsonObject.getString("name"));
            ticket.setSummary( jsonObject.getString("summary"));
            ticket.setDescription( jsonObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return ticket;
    }


    public String getState() {
        String state = "";
        if (closed) {
            state = "Cerrado";
        }else{
            state = "Abierto";
        }
        return  state;
    }


}
