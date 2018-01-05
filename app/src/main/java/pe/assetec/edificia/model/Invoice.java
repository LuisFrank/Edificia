package pe.assetec.edificia.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by frank on 12/09/17.
 */

public class Invoice {

    private Integer id;
    private Integer building_id;
    private Integer departament_id;
    private Double total;
    private String date_issue;
    private String date_due;
    private String number;
    private String departament_name;
    private String departament_address;
    private String owner_name;
    private Date cancellation_date;
    private Integer period_id;
    private Double balance;
    private Double sub_total;
    private Integer kind;
    private Boolean paid;
    private Integer lft;
    private Integer rgt;
    private Integer parent_id;
    private Boolean by_overdue;
    private String messages;
    private String name;
    private String message_top;
    private String message_bottom;
    private Boolean by_previous;
    private Integer currency_id;
    private Double exchange_rate;
    private Double foreign_sub_total;
    private Double foreign_total;
    private Double foreign_balance;
    private String periods_names;
    private String document;
    private String homeowners_document;
    private String departament_detail;
    private Double general_total;


    private Boolean expired;



    public Invoice() {
        this.id = id;
        this.building_id = building_id;
        this.departament_id = departament_id;
        this.total = total;
        this.date_issue = date_issue;
        this.date_due = date_due;
        this.number = number;
        this.departament_name = departament_name;
        this.departament_address = departament_address;
        this.owner_name = owner_name;
        this.cancellation_date = cancellation_date;
        this.period_id = period_id;
        this.balance = balance;
        this.sub_total = sub_total;
        this.kind = kind;
        this.paid = paid;
        this.lft = lft;
        this.rgt = rgt;
        this.parent_id = parent_id;
        this.by_overdue = by_overdue;
        this.messages = messages;
        this.name = name;
        this.message_top = message_top;
        this.message_bottom = message_bottom;
        this.by_previous = by_previous;
        this.currency_id = currency_id;
        this.exchange_rate = exchange_rate;
        this.foreign_sub_total = foreign_sub_total;
        this.foreign_total = foreign_total;
        this.foreign_balance = foreign_balance;
        this.periods_names = periods_names;
        this.document = document;
        this.homeowners_document = homeowners_document;
        this.departament_detail = departament_detail;
        this.general_total = general_total;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDate_issue() {
        return date_issue;
    }

    public void setDate_issue(String date_issue) {
        this.date_issue = date_issue;
    }

    public String getDate_due() {
        return date_due;
    }

    public void setDate_due(String date_due) {
        this.date_due = date_due;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDepartament_name() {
        return departament_name;
    }

    public void setDepartament_name(String departament_name) {
        this.departament_name = departament_name;
    }

    public String getDepartament_address() {
        return departament_address;
    }

    public void setDepartament_address(String departament_address) {
        this.departament_address = departament_address;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public Date getCancellation_date() {
        return cancellation_date;
    }

    public void setCancellation_date(Date cancellation_date) {
        this.cancellation_date = cancellation_date;
    }

    public Integer getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(Integer period_id) {
        this.period_id = period_id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getSub_total() {
        return sub_total;
    }

    public void setSub_total(Double sub_total) {
        this.sub_total = sub_total;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Integer getLft() {
        return lft;
    }

    public void setLft(Integer lft) {
        this.lft = lft;
    }

    public Integer getRgt() {
        return rgt;
    }

    public void setRgt(Integer rgt) {
        this.rgt = rgt;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public Boolean getBy_overdue() {
        return by_overdue;
    }

    public void setBy_overdue(Boolean by_overdue) {
        this.by_overdue = by_overdue;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage_top() {
        return message_top;
    }

    public void setMessage_top(String message_top) {
        this.message_top = message_top;
    }

    public String getMessage_bottom() {
        return message_bottom;
    }

    public void setMessage_bottom(String message_bottom) {
        this.message_bottom = message_bottom;
    }

    public Boolean getBy_previous() {
        return by_previous;
    }

    public void setBy_previous(Boolean by_previous) {
        this.by_previous = by_previous;
    }

    public Integer getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(Integer currency_id) {
        this.currency_id = currency_id;
    }

    public Double getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(Double exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public Double getForeign_sub_total() {
        return foreign_sub_total;
    }

    public void setForeign_sub_total(Double foreign_sub_total) {
        this.foreign_sub_total = foreign_sub_total;
    }

    public Double getForeign_total() {
        return foreign_total;
    }

    public void setForeign_total(Double foreign_total) {
        this.foreign_total = foreign_total;
    }

    public Double getForeign_balance() {
        return foreign_balance;
    }

    public void setForeign_balance(Double foreign_balance) {
        this.foreign_balance = foreign_balance;
    }

    public String getPeriods_names() {
        return periods_names;
    }

    public void setPeriods_names(String periods_names) {
        this.periods_names = periods_names;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getHomeowners_document() {
        return homeowners_document;
    }

    public void setHomeowners_document(String homeowners_document) {
        this.homeowners_document = homeowners_document;
    }

    public String getDepartament_detail() {
        return departament_detail;
    }

    public void setDepartament_detail(String departament_detail) {
        this.departament_detail = departament_detail;
    }

    public Double getGeneral_total() {
        return general_total;
    }

    public void setGeneral_total(Double general_total) {
        this.general_total = general_total;
    }


    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    // Decodes business json into business model object
    public static Invoice fromJson(JSONObject jsonObject) {
        Invoice b = new Invoice();
        // Deserialize json into object fields
        try {
            b.setId(jsonObject.getInt("id"));
            b.setNumber(jsonObject.getString("number"));
            b.setName(jsonObject.getString("name"));
            b.setPeriods_names(jsonObject.getString("periods_names"));
            b.setTotal(jsonObject.getDouble("total"));
            b.setBalance( jsonObject.getDouble("balance"));
            b.setDate_due( jsonObject.getString("date_due_format"));
            b.setDate_issue( jsonObject.getString("date_issue_format"));
            b.setPaid(jsonObject.getBoolean("paid"));
            b.setExpired( jsonObject.getBoolean("expired"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }
}
