package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1 {

    private final OrderRepositoryV1 target;
    private final LogTrace tracer;

    public OrderRepositoryInterfaceProxy(OrderRepositoryV1 target, LogTrace tracer) {
        this.target = target;
        this.tracer = tracer;
    }

    @Override
    public void save(String itemId) {

        TraceStatus status = null;
        try {
            status = tracer.begin("OrderRepository.save() by proxy");

            //target 호출
            target.save(itemId);

            tracer.end(status);
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }
}
