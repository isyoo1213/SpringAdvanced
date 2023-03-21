package spring.advanced.trace.logTrace;

import org.junit.jupiter.api.Test;
import spring.advanced.trace.TraceStatus;

class ThreadLocalLogTraceTest {

    ThreadLocalLogTrace tracer = new ThreadLocalLogTrace();

    @Test
    void begin_end_level2() {
        TraceStatus status1 = tracer.begin("hello1");
        TraceStatus status2 = tracer.begin("hello2");
        tracer.end(status2);
        tracer.end(status1);
    }

    @Test
    void begin_exception_level2() {
        TraceStatus status1 = tracer.begin("hello1");
        TraceStatus status2 = tracer.begin("hello2");
        tracer.exception(status2, new IllegalStateException());
        tracer.exception(status1, new IllegalStateException());
    }

}