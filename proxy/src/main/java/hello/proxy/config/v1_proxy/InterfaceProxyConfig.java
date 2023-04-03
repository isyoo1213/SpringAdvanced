package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean으로 등록되는 것은 Proxy의 인스턴스
 * target은 Impl을 생성해서 주입 -> *** 각각의 Impl들의 의존성 주입은 *** Proxy를 주입받도록 - 이를 위해 Bean으로 Proxy를 등록하는 반복!
 * Runtime 객체 의존 관계를 확인해보면 이해가 쉬움
 *  *** 즉, Proxy를 Bean으로 등록하지만, 실제 target이 생성되어 참조하고 있으므로 Proxy/Impl 모두 활용 가능
 *  -> 실제 target 객체는 SpringContainer와는 상관이 없음 + heap에 올라가지만 container와는 전혀 상관없음
 */
@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace tracer) { //Tracer는 뒷 단에서 등록했다고 가정 -> Parameter로 받기
        //target Impl 설정
        // ***** Proxy가 실제 호출하는 Impl은 Service의 Impl이 아닌 Proxy를 호출하도록!! -> Service도 Proxy를 빈으로 등록
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(tracer));

        //이제 Impl 빈을 사용하지 않고 Proxy 인스턴스를 사용하도록 설정
        return new OrderControllerInterfaceProxy(controllerImpl, tracer);
        //return new OrderControllerV1Impl(orderServiceV1());
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace tracer) {
        //target Impl 설정
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(tracer));

        //Proxy를 빈으로 등록하도록 설정
        return new OrderServiceInterfaceProxy(serviceImpl, tracer);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace tracer) {
        //target Impl 설정
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();

        //Proxy를 빈으로 등록하도록 설정
        return new OrderRepositoryInterfaceProxy(repositoryImpl, tracer);
    }

}
