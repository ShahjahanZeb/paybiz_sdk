package com.example.paybizsdk.interfaces;

import android.app.Activity;

import com.example.paybizsdk.entity.AuthenticationRequestParameters;
import com.example.paybizsdk.entity.ChallengeParameters;
import com.example.paybizsdk.entity.ChallengeStatusReceiver;
import com.example.paybizsdk.entity.ProgressDialog;

import org.json.JSONException;

public interface Transaction {

    public AuthenticationRequestParameters getAuthenticationRequestParameters();

    public void doChallenge(Activity currentActivity, ChallengeParameters challengeParameters, ChallengeStatusReceiver challengeStatusReceiver,
                            int timeOut) throws JSONException;

    public ProgressDialog getProgressView(Activity currentActivity);

    public void close();

}
