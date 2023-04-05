package hello.proxy.config.v1_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * target의 의존성을 조립해주는 과정 확인하기
 */
@Configuration
public class ConcreteProxyConfig {

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace tracer) {
        // *** proxy에 의존성으로 주입할 target을 생성할 때, target의 의존성으로 proxy를 주입해줌!
        OrderControllerV2 orderControllerImpl = new OrderControllerV2(orderServiceV2(tracer));
        return new OrderControllerConcreteProxy(orderControllerImpl, tracer);
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace tracer) {
        // *** proxy에 의존성으로 주입할 target을 생성할 때, target의 의존성으로 proxy를 주입해줌!
        OrderServiceV2 orderServiceImpl = new OrderServiceV2(orderRepositoryV2(tracer));
        return new OrderServiceConcreteProxy(orderServiceImpl, tracer);
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace tracer) {
        OrderRepositoryV2 repositoryImpl = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(repositoryImpl, tracer);
    }
}
