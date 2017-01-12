package c.z.log;

import java.io.Serializable;

public class InvokeContext implements Serializable {

    private final Long sessionId;

    private final Long invokeTime;

    public InvokeContext(Long sessionId, Long invokeTime) {
        super();
        this.sessionId = sessionId;
        this.invokeTime = invokeTime;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getInvokeTime() {
        return invokeTime;
    }

}
