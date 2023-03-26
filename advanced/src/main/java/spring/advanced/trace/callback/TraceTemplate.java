package spring.advanced.trace.callback;

import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.logTrace.LogTrace;

public class TraceTemplate {

    private final LogTrace tracer;

    public TraceTemplate(LogTrace tracer) {
        this.tracer = tracer;
    }

    public <T> T execute(String message, TraceCallback<T> callback) {
        TraceStatus status = null;
        try {
            status = tracer.begin(message);

            //로직 호출
            T result = callback.call();

            tracer.end(status);
            return result;
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }
}
