package com.thecrowdedstudio.utilities;

public interface RestCaller {

    public void networkSuccess(Object json, String callback);

    public void networkFailure(String errorMessage);

}
