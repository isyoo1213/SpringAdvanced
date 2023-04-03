package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {

    private final OrderControllerV1 target;
    private final LogTrace tracer;

    @Override
    public String request(String itemId) {
        TraceStatus status = null;

        try {
            status = tracer.begin("OrderController.requet() by proxy");

            //target 호출
            String result = target.request(itemId);

            tracer.end(status);

            return result;
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
