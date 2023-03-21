package spring.advanced.app.v3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.advanced.trace.TraceId;
import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.logTrace.LogTrace;
import spring.advanced.trace.myTrace.MyTraceV2;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

    private final LogTrace tracer;

    //Service에서 넘겨준 beforeTraceId 추가
    public void save(String itemId) {
        TraceStatus status = null;
        try {
            //beginSync() 사용
            status = tracer.begin("OrderRepositoryV1.save()");

            //저장 로직 - 상황만 가정 -> 상품을 저장할 때는 1초 정도의 시간이 걸리고, Exception 발생할 경우 예외를 던짐
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생");
            }
            sleep(1000);

            tracer.end(status);
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
