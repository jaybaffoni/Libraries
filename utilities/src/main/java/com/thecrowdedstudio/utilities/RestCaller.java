package com.thecrowdedstudio.utilities;

import org.json.JSONObject;

public interface RestCaller {

    public void networkReturn(JSONObject json, String callback);

    public void networkFailure(String errorMessage);

}
