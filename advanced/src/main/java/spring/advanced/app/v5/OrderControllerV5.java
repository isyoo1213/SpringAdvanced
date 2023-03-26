package spring.advanced.app.v5;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.advanced.trace.callback.TraceCallback;
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
@RestController //@Controller + @ResponseBody
//@RequiredArgsConstructor
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template;
    // *** tracer 주입은 Controller의 생성자 주입 시 Bean 등록된 인스턴스를 주입받으면서 template을 생성
    //     -> Controller 자체가 singleton이므로 생성자는 1번 호출
    //        + Bean 등록된 tracer는 모두 Singleton 이므로 template 또한 singleton처럼 생성됨
    //private final LogTrace tracer;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace tracer) {
        this.orderService = orderService;
        this.template = new TraceTemplate(tracer);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {

        return template.execute(
                "OrderControllerV5.request()",
                new TraceCallback<>() {
                    @Override
                    public String call() {
                        orderService.orderItem(itemId);
                        return "ok";
                    }
                });

    /*
        //익명내부클래스를 통해 추상클래스인 템플릿의 인스턴스를 생성 + 초기화
        AbstractTemplate<String> template = new AbstractTemplate<>(tracer) {
            @Override
            protected String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };

        //템플릿 인스턴스가 오버라이딩된 calll() 메서드를 포함한 execute() 메서드의 결과값을 반환
        return template.execute("OrderControllerV5.request()");
    */

    }

}
