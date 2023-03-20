package spring.advanced.trace.myTrace;

import org.junit.jupiter.api.Test;
import spring.advanced.trace.TraceStatus;

class MyTraceV1Test {

    @Test
    void begin_end() {
        MyTraceV1 trace = new MyTraceV1();
        TraceStatus status = trace.begin("hello");
        trace.end(status);
        //로그
        // INFO spring.advanced.trace.myTrace.MyTraceV1 - [882177b1] hello
        // INFO spring.advanced.trace.myTrace.MyTraceV1 - [882177b1] hello times=13ms
    }

    @Test
    void begin_exception() {
        MyTraceV1 trace = new MyTraceV1();
        TraceStatus status = trace.begin("hello");
        trace.exception(status, new IllegalStateException());
        //로그
        // INFO spring.advanced.trace.myTrace.MyTraceV1 - [f289c791] hello
        // INFO spring.advanced.trace.myTrace.MyTraceV1 - [f289c791] hello times=14ms ex=java.lang.IllegalStateException
    }

}