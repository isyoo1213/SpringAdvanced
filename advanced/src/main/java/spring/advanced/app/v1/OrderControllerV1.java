package spring.advanced.app.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.advanced.app.trace.TraceStatus;
import spring.advanced.app.trace.myTrace.MyTraceV1;

@RestController //@Controller + @ResponseBody
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderService;
    private final MyTraceV1 tracer;

    @GetMapping("/v1/request")
    public String request(String itemId) {

        TraceStatus status = null; //catch 블럭에서도 status를 사용할 수 있도록 scope를 try 블럭 밖으로 확장시키는 위치
        try {
            status = tracer.begin("OrderControllerV1.request()");
            orderService.orderItem(itemId);
            tracer.end(status);
            return "ok";
        } catch (Exception e) {
            tracer.exception(status, e);
            // * 발생한 Exception을 핸들링해서 처리하긴 하나,
            // 1. 자연스러운 비즈니스 로직 흐름을 제어함 -> 기존처럼 Exception을 Throw하도록 유지해야함
            // 2. request() 메서드의 반환값을 위한 처리도 필요 -> Exception을 그냥 Throw하면 해당 Exception을 처리하며 반환
            throw e;
        }

    }

}
