package spring.advanced.app.v5;

import org.springframework.stereotype.Service;
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
@Service
//@RequiredArgsConstructor
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate template;
    //private final LogTrace tracer;

    //@Autowired
    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace tracer) {
        this.orderRepository = orderRepository;
        this.template = new TraceTemplate(tracer);
    }

    public void orderItem(String itemId) {
        //람다 사용
        template.execute("OrderServiceV5.orderItem()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }
}
