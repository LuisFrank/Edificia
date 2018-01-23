package pe.assetec.edificia.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by frank on 12/09/17.
 */

public class ManageSession {

    //key for username
    private String USERNAME="username";
    //key for password
    private String PASSWORD="password";
    //key for email
    private String EMAIL="email";
    //key for token
    private String TOKEN="token";
    //key for preferences
    private String PREFERENCES="edificia";
    //key for is user login
    private String ISLOGIN="login";

    private String BUILDINGS="buildings";

    private String USERTYPE="usertype";

    // Shared Preferences variable
    SharedPreferences spSession;
    //editor for shared preference
    SharedPreferences.Editor editor;
    public ManageSession(Context context)
    {
        spSession=context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        editor=spSession.edit();
    }
    // function store user details
    public void storeUser(String name,String pass,String email,String usertype)
    {
        editor.putString(USERNAME,name);
        editor.putString(PASSWORD,pass);
        editor.putString(EMAIL,email);
        editor.putString(USERTYPE,usertype);
        editor.commit();
    }
    // to login user
    public void loginUser(String name, String password, String token, boolean login, JSONArray object)
    {
        editor.putString(USERNAME,name);
        editor.putString(PASSWORD,password);
        editor.putString(TOKEN,token);
        editor.putBoolean(ISLOGIN,login);
        editor.putString(BUILDINGS,object.toString());
        editor.commit();
    }
    //to get username
    public String getUserName()
    {
        return spSession.getString(USERNAME,"");
    }

    //to get username
    public String getUserType()
    {
        return spSession.getString(USERTYPE,"");
    }
    //to get userpassword
    public String getUserPassword()
    {
        return spSession.getString(PASSWORD,"");
    }
    //to get useremail
    public String getUserEmail()
    {
        return spSession.getString(EMAIL,"");
    }
    //to get token
    public String getTOKEN()
    {
        return spSession.getString(TOKEN,"");
    }
    //to get token
    public String getBuildings()
    {
        return spSession.getString(BUILDINGS,"");
    }
    //to check whether user is login or not
    public boolean isUserLogedIn()
    {
        return spSession.getBoolean(ISLOGIN,false);
    }
    // to delete the user and clear the preferences


    public void logOutUser()
    {
        editor.remove(PASSWORD);
        editor.remove(USERTYPE);
        editor.remove(TOKEN);
        editor.remove(ISLOGIN);
        editor.remove(BUILDINGS);
        editor.commit();
    }

    public String checkSession()
    {

        return spSession.getString(USERNAME,"");
    }

}
