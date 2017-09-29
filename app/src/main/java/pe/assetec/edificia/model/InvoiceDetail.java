package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by frank on 14/09/17.
 */

public class InvoiceDetail {

    public Integer position;
    public String category_name;
    public String description;
    public String consumption;
    public String unit_of_measure;
    public Double total_amount;


    public InvoiceDetail() {
        this.position = position;
        this.category_name = category_name;
        this.description = description;
        this.consumption = consumption;
        this.unit_of_measure = unit_of_measure;
        this.total_amount = total_amount;
    }


    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getUnit_of_measure() {
        return unit_of_measure;
    }

    public void setUnit_of_measure(String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }


    // Decodes business json into business model object
    public static InvoiceDetail fromJson(JSONObject jsonObject) {
        InvoiceDetail b = new InvoiceDetail();
        // Deserialize json into object fields
        try {
            b.setPosition(jsonObject.getInt("position"));
            b.setCategory_name(jsonObject.getString("category_name"));
            b.setDescription(jsonObject.getString("description"));
            b.setCategory_name(jsonObject.getString("category_name"));
            b.setConsumption(jsonObject.getString("consumption"));
            b.setUnit_of_measure(jsonObject.getString("unit_of_measure"));
            b.setTotal_amount( jsonObject.getDouble("total_amount"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }
}


