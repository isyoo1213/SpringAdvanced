package spring.advanced.app.v4;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.logTrace.LogTrace;
import spring.advanced.trace.template.AbstractTemplate;

/**
 * 템플릿 메서드 패턴을 적용한 추상 클래스 AbstractTemplate 를 적용
 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

    private final LogTrace tracer;

    //Service에서 넘겨준 beforeTraceId 추가
    public void save(String itemId) {

        //익명내부클래스를 통해 추상클래스인 템플릿의 인스턴스를 생성 + 초기화
        AbstractTemplate<Void> template = new AbstractTemplate<>(tracer) {
            @Override
            protected Void call() {
                //저장 로직 - 상황만 가정 -> 상품을 저장할 때는 1초 정도의 시간이 걸리고, Exception 발생할 경우 예외를 던짐
                if (itemId.equals("ex")) {
                    throw new IllegalStateException("예외 발생");
                }
                sleep(1000);
                return null;
            }
        };

        //템플릿 인스턴스가 오버라이딩된 calll() 메서드를 포함한 execute() 메서드의 결과값을 반환
        template.execute("OrderRepositoryV4.save()");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
