package spring.advanced.app.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.advanced.app.trace.TraceStatus;
import spring.advanced.app.trace.myTrace.MyTraceV1;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV1 {

    private final MyTraceV1 tracer;

    public void save(String itemId) {
        TraceStatus status = null;
        try {
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
