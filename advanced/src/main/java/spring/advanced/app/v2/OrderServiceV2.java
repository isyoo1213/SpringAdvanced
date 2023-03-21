package spring.advanced.app.v2;

import org.springframework.stereotype.Service;
import spring.advanced.trace.TraceId;
import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.myTrace.MyTraceV1;
import spring.advanced.trace.myTrace.MyTraceV2;

@Service
//@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;
    private final MyTraceV2 tracer;

    //@Autowired
    public OrderServiceV2(OrderRepositoryV2 orderRepository, MyTraceV2 tracer) {
        this.orderRepository = orderRepository;
        this.tracer = tracer;
    }

    //비즈니스 로직은 단순하게 repository를 호출하는 정도로
    //Controller에서 넘어오는 beforeTraceId 추가
    public void orderItem(TraceId beforeTraceId, String itemId) {
        TraceStatus status = null;

        try {
            //beginSync() 사용
            status = tracer.beginSync(beforeTraceId, "OrderServiceV1.orderItem()");

            //Repository로 넘겨줄 traceId 처리
            orderRepository.save(status.getTraceId(), itemId);
            tracer.end(status);
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }

}
