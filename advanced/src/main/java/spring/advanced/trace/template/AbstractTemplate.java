package spring.advanced.trace.template;

import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.logTrace.LogTrace;

public abstract class AbstractTemplate<T> {

    private final LogTrace tracer;

    public AbstractTemplate(LogTrace tracer) {
        this.tracer = tracer;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = tracer.begin(message);

            //로직 호출
            T result = call();

            tracer.end(status);
            return result;
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }

    protected abstract T call();
}
