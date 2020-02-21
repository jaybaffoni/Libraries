package com.thecrowdedstudio.utilities;

import org.json.JSONObject;

public interface RestCaller {

    public void networkSuccess(JSONObject json, String callback);

    public void networkFailure(String errorMessage);

}
