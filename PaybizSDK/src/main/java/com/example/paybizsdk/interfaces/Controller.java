package com.example.paybizsdk.interfaces;

import android.app.Activity;
import android.content.Context;

import com.example.paybizsdk.entity.AuthenticationRequestParameters;
import com.example.paybizsdk.entity.ChallengeParameters;
import com.example.paybizsdk.entity.ChallengeStatusReceiver;
import com.example.paybizsdk.entity.Warning;

import org.json.JSONObject;

import java.util.List;

public interface Controller {

    public void initialize();

    public Transaction createTransaction(String directoryServerID, String messageVersion);

    public void cleanup(Context applicationContext);

    public String getSDKVersion();

    public List<Warning> getWarnings();

    public void doChallenge(Activity activity, ChallengeParameters challengeParameters, ChallengeStatusReceiver challengeStatusReceiver, int timeOut);

    public AuthenticationRequestParameters getAuthParams();

}
