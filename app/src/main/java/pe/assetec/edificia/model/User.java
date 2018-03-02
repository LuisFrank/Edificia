package pe.assetec.edificia.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by frank on 16/02/18.
 */

public class User {

    Integer userId;
    String email;
    String token;
    String userType;
    Boolean status_loguin;
    String tokenFirebase;
    List<Integer> building_ids;
    HashMap<String,List<Integer>>  building_departament_ids;

    public User(){

    }

    public User(Integer userId, String email, String token, String userType, Boolean status_loguin) {
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.userType = userType;
        this.status_loguin = status_loguin;
    }

    public User(Integer userId, String email, String token, String userType, Boolean status_loguin, String tokenFirebase) {
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.userType = userType;
        this.status_loguin = status_loguin;
        this.tokenFirebase = tokenFirebase;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getStatus_loguin() {
        return status_loguin;
    }

    public void setStatus_loguin(Boolean status_loguin) {
        this.status_loguin = status_loguin;
    }

    public String getTokenFirebase() {
        return tokenFirebase;
    }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }

    public List<Integer> getBuilding_ids() {
        return building_ids;
    }

    public void setBuilding_ids(List<Integer> building_ids) {
        this.building_ids = building_ids;
    }


    public HashMap<String, List<Integer>> getBuilding_departament_ids() {
        return building_departament_ids;
    }

    public void setBuilding_departament_ids(HashMap<String, List<Integer>> building_departament_ids) {
        this.building_departament_ids = building_departament_ids;
    }

    public static List<Integer> converBuildingIds(JSONArray jarray) {
        List<Integer> buildin_ids;
        // Deserialize json into object fields
        try {
            buildin_ids = new ArrayList<Integer>();
            int len = jarray.length();
            for (int i=0;i<len;i++){
                buildin_ids.add(jarray.getInt(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return buildin_ids;
    }

    public static HashMap<String,List<Integer>> converBuildingDepartamentIds(JSONArray Arrayobj) throws JSONException {
        HashMap<String,List<Integer>> buildin_departament_ids = new HashMap<>();
        // Deserialize json into object fields
        for (int i = 0; i < Arrayobj.length(); i++) {
            JSONObject object = Arrayobj.optJSONObject(i);
            Iterator<String> iterator = object.keys();
            while(iterator.hasNext()) {
                String currentKey = iterator.next();
                buildin_departament_ids.put(currentKey,JsonToList(object.getJSONArray(currentKey)));
            }
        }
        // Return new object
        return buildin_departament_ids;
    }

    public static List<Integer> JsonToList(JSONArray jarray) {
        List<Integer> departaments;
        // Deserialize json into object fields
        try {
            departaments = new ArrayList<Integer>();
            int len = jarray.length();
            for (int i=0;i<len;i++){
                departaments.add(jarray.getInt(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return departaments;
    }
}
