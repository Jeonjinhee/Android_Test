package com.example.jeonjinhee.loginapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JeonJinhee on 2017-06-24.
 */

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://1.214.43.1/Register.php";
    private Map<String, String> paramaters;

    public RegisterRequest(String userId, String userPassword, String userAddress, String isAdmin, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        paramaters = new HashMap<>();
        paramaters.put("userId", userId);
        paramaters.put("userPassword", userPassword);
        paramaters.put("userAddress", userAddress);
        paramaters.put("isAdmin", isAdmin);
    }

    public Map<String, String> getParams(){
        return paramaters;
    }
}
