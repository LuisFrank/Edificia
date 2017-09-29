package pe.assetec.edificia.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.assetec.edificia.model.Comment;

/**
 * Created by frank on 25/09/17.
 */

public class CommentsController {
    public static ArrayList<Comment> fromJson(JSONArray jsonArray) {
        JSONObject commentJson;
        ArrayList<Comment> comments = new ArrayList<Comment>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                commentJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Comment comment = Comment.fromJson(commentJson);
            if (comment != null) {
                comments.add(comment);
            }
        }
        return comments;
    }


}
