package spring.advanced.trace.myTrace;

import org.junit.jupiter.api.Test;
import spring.advanced.trace.TraceStatus;

class MyTraceV2Test {

    @Test
    void begin_end() {
        MyTraceV2 trace = new MyTraceV2();
        TraceStatus status = trace.begin("hello");

        //V2의 beginSync() 테스트를 위해 추가
        TraceStatus status2 = trace.beginSync(status.getTraceId(), "hello2");
        trace.end(status2);

        trace.end(status);
        //로그
        // INFO spring.advanced.trace.myTrace.MyTraceV2 - [54d2f49f] hello
        // INFO spring.advanced.trace.myTrace.MyTraceV2 - [54d2f49f] |-->hello2
        // INFO spring.advanced.trace.myTrace.MyTraceV2 - [54d2f49f] |<--hello2 times=1ms
        // INFO spring.advanced.trace.myTrace.MyTraceV2 - [54d2f49f] hello times=10ms
    }

    @Test
    void begin_exception() {
        MyTraceV2 trace = new MyTraceV2();
        TraceStatus status = trace.begin("hello");

        //V2 테스트를 위한 추가
        TraceStatus status2 = trace.beginSync(status.getTraceId(), "hello2");
        trace.exception(status2, new IllegalStateException());

        trace.exception(status, new IllegalStateException());
        //로그
        // INFO spring.advanced.trace.myTrace.MyTraceV2 - [bfc04da0] hello
        // INFO spring.advanced.trace.myTrace.MyTraceV2 - [bfc04da0] |-->hello2
        // INFO spring.advanced.trace.myTrace.MyTraceV2 - [bfc04da0] |<X-hello2 times=1ms ex=java.lang.IllegalStateException
        // INFO spring.advanced.trace.myTrace.MyTraceV2 - [bfc04da0] hello times=10ms ex=java.lang.IllegalStateException
    }

}