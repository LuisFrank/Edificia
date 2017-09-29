package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by frank on 22/09/17.
 */

public class Comment {
    private Integer id;
    private Integer building_id;
    private Integer departament_id;
    private String body;
    private String created_at;
    private Integer commentable_id;
    private String commentable_type;
    private String authorable_id;
    private String authorable_type;

    private String first_name;
    private String last_name;



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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getCommentable_id() {
        return commentable_id;
    }

    public void setCommentable_id(Integer commentable_id) {
        this.commentable_id = commentable_id;
    }

    public String getCommentable_type() {
        return commentable_type;
    }

    public void setCommentable_type(String commentable_type) {
        this.commentable_type = commentable_type;
    }

    public String getAuthorable_id() {
        return authorable_id;
    }

    public void setAuthorable_id(String authorable_id) {
        this.authorable_id = authorable_id;
    }

    public String getAuthorable_type() {
        return authorable_type;
    }

    public void setAuthorable_type(String authorable_type) {
        this.authorable_type = authorable_type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public static Comment fromJson(JSONObject jsonObject) {
        Comment comment  = new Comment();
        // Deserialize json into object fields
        try {
            comment.setId(jsonObject.getInt("id"));
            comment.setDepartament_id(jsonObject.getInt("departament_id"));
            comment.setBuilding_id(jsonObject.getInt("building_id"));
            comment.setBody(jsonObject.getString("body"));
            comment.setCreated_at(jsonObject.getString("created_at_format"));
            comment.setFirst_name( jsonObject.getJSONObject("authorable").getString("firstname"));
            comment.setLast_name( jsonObject.getJSONObject("authorable").getString("lastname"));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return comment;
    }

}
