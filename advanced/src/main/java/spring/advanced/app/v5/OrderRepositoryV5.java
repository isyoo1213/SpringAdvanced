package spring.advanced.app.v5;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.advanced.trace.callback.TraceTemplate;
import spring.advanced.trace.logTrace.LogTrace;
import spring.advanced.trace.template.AbstractTemplate;

/**
 * 템플릿 콜백 패턴 사용
 * *** Template 생성 방법
 *     1. Singleton인 Controller의 생성자에서 Bean 등록된 tracer(singleton)를 받아 생성하기
 *        -> template은 필요한 메서드에서 호출될 때마다 생성하는 것이 아닌, controller/tracer의 특성을 활용해 1번만 생성
 *     2. Bean으로 등록해서 주입받기
 *        -> 장점 : Test 시에 따로 template 생성에 신경쓰지 않아도 된다
 */
@Repository
//@RequiredArgsConstructor
public class OrderRepositoryV5 {

    private final TraceTemplate template;
    //private final LogTrace tracer;


    public OrderRepositoryV5(LogTrace tracer) {
        this.template = new TraceTemplate(tracer);
    }

    public void save(String itemId) {
        //람다 사용
        template.execute("OrderRepositoryV5.save()", () -> {
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생");
            }
            sleep(1000);
            return null;
        });
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
