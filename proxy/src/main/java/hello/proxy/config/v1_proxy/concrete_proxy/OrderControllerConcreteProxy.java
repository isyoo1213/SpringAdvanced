package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

/**
 * 이제 Interface와 그 구현 클래스들 사이에서의 proxy가 아닌 구현 클래스들과의 사이에서 proxy를 구현
 * *** target을 상속 받고 + target의 인스턴스를 의존하지만, target의 의존성을 직접적으로 사용하지 않는다!
 *     == 지금 당장의 Proxy의 명세는, 부모 클래스인 target의 '기능'들을 사용하지 않고, 다형성을 구현하는 '구조'만 취한다
 *     -> JAVA는 기본적으로 부모 클래스의 기본 생성자 super()를 호출한다
 *     -> but, 부모 클래스에 기본 생성자가 아닌 커스텀된 생성자를 사용한다면 이를 구현해주어야한다
 *     -> 상속을 구현하는 target의 커스텀 생성자, super(...)에 기존 target의 의존성이 필요없다! -> super(null)
 * *** 이는 Config에서 target을 생성해서 가지는 proxy를 Bean등록할 때, target이 실제 사용할 의존성을 주입해주는 방식으로 구성
 */
public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final OrderControllerV2 target;
    private final LogTrace tracer;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace tracer) {
        // *** 부모 클래스는 기본 생성자가 아닌 final이 붙은 필드를 주입받는 생성자를 사용하므로 이를 구현해주어야함 by super(...);
        //     -> 현재 proxy는 target 클래스의 '구조'만 취하고 실제 target 클래스의 의존성은 Config에서 조립
        super(null);
        this.target = target;
        this.tracer = tracer;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;

        try {
            status = tracer.begin("OrderController.requet() by Concrete proxy");

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
