package com.example.paybizsdk.interfaces;

import com.example.paybizsdk.entity.CompletionEvent;
import com.example.paybizsdk.entity.ProtocolErrorEvent;
import com.example.paybizsdk.entity.RuntimeErrorEvent;

public interface ChallengeStatusReceiver {

    public void completed(CompletionEvent completionEvent);
    public void cancelled();
    public void timedout();
    public void protocolError(ProtocolErrorEvent protocolErrorEvent);
    public void runtimeError(RuntimeErrorEvent runtimeErrorEvent);

}
