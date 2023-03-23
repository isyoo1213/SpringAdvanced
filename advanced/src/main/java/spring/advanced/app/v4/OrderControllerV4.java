package spring.advanced.app.v4;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.logTrace.LogTrace;
import spring.advanced.trace.template.AbstractTemplate;

/**
 * 템플릿 메서드 패턴을 적용한 추상 클래스 AbstractTemplate 를 적용
 */
@RestController //@Controller + @ResponseBody
@RequiredArgsConstructor
public class OrderControllerV4 {

    private final OrderServiceV4 orderService;
    private final LogTrace tracer;

    @GetMapping("/v4/request")
    public String request(String itemId) {

        //익명내부클래스를 통해 추상클래스인 템플릿의 인스턴스를 생성 + 초기화
        AbstractTemplate<String> template = new AbstractTemplate<>(tracer) {
            @Override
            protected String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };

        //템플릿 인스턴스가 오버라이딩된 calll() 메서드를 포함한 execute() 메서드의 결과값을 반환
        return template.execute("OrderControllerV4.request()");
    }

}
