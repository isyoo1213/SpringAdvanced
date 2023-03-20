package spring.advanced.app.v1;

import org.springframework.stereotype.Service;
import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.myTrace.MyTraceV1;

@Service
//@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepositoryV1 orderRepository;
    private final MyTraceV1 tracer;

    //@Autowired
    public OrderServiceV1(OrderRepositoryV1 orderRepository, MyTraceV1 tracer) {
        this.orderRepository = orderRepository;
        this.tracer = tracer;
    }

    //비즈니스 로직은 단순하게 repository를 호출하는 정도로
    public void orderItem(String itemId) {
        TraceStatus status = null;

        try {
            status = tracer.begin("OrderServiceV1.orderItem()");
            orderRepository.save(itemId);
            tracer.end(status);
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }

}
