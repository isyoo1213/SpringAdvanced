package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceInterfaceProxy implements OrderServiceV1{

    private final OrderServiceV1 target;
    private final LogTrace tracer;

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;

        try {
            status = tracer.begin("OrderService.orderItem() by proxy");

            //target 호출
            target.orderItem(itemId);

            tracer.end(status);
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }
}
