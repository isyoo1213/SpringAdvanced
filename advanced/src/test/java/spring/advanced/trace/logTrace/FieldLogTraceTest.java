package spring.advanced.trace.logTrace;

import org.junit.jupiter.api.Test;
import spring.advanced.trace.TraceStatus;

import static org.junit.jupiter.api.Assertions.*;

class FieldLogTraceTest {

    //아직 Bean 등록하지 않았으므로 직접 생성해서 사용
    FieldLogTrace tracer = new FieldLogTrace();

    @Test
    void begin_end_level2() {
        TraceStatus status1 = tracer.begin("hello1");
        TraceStatus status2 = tracer.begin("hello2");
        tracer.end(status2);
        tracer.end(status1);

        //로그
        // INFO spring.advanced.trace.logTrace.FieldLogTrace - [d6dd320a] hello1
        // INFO spring.advanced.trace.logTrace.FieldLogTrace - [d6dd320a] |-->hello2
        // INFO spring.advanced.trace.logTrace.FieldLogTrace - [d6dd320a] |<--hello2 times=0ms
        // INFO spring.advanced.trace.logTrace.FieldLogTrace - [d6dd320a] hello1 times=9ms
    }

    @Test
    void begin_exception_level2() {
        TraceStatus status1 = tracer.begin("hello1");
        TraceStatus status2 = tracer.begin("hello2");
        tracer.exception(status2, new IllegalStateException());
        tracer.exception(status1, new IllegalStateException());

        //로그
        // FieldLogTrace - [ec65bff4] hello1
        // FieldLogTrace - [ec65bff4] |-->hello2
        // FieldLogTrace - [ec65bff4] |<X-hello2 times=0ms ex=java.lang.IllegalStateException
        // FieldLogTrace - [ec65bff4] hello1 times=10ms ex=java.lang.IllegalStateException
    }

}