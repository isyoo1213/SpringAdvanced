package spring.advanced.app.v3;

import org.springframework.stereotype.Service;
import spring.advanced.trace.TraceId;
import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.logTrace.LogTrace;
import spring.advanced.trace.myTrace.MyTraceV2;

@Service
//@RequiredArgsConstructor
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepository;
    private final LogTrace tracer;

    //@Autowired
    public OrderServiceV3(OrderRepositoryV3 orderRepository, LogTrace tracer) {
        this.orderRepository = orderRepository;
        this.tracer = tracer;
    }

    //비즈니스 로직은 단순하게 repository를 호출하는 정도로
    //Controller에서 넘어오는 beforeTraceId 추가
    public void orderItem(String itemId) {
        TraceStatus status = null;

        try {
            //beginSync() 사용
            status = tracer.begin("OrderServiceV1.orderItem()");

            //Repository로 넘겨줄 traceId 처리
            orderRepository.save(itemId);
            tracer.end(status);
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }

}
