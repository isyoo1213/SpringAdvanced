package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyBasicConfig {

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace tracer) {
        //Interface를 구현하는 target 클래스의 인스턴스 생성
        OrderControllerV1 orderServiceV1 = new OrderControllerV1Impl(orderServiceV1(tracer));

        //InvacationHandler에 필요한 target으로 위에 생성한 target 설정 및 의존성 전달
        OrderControllerV1 proxy = (OrderControllerV1) Proxy.newProxyInstance(
                OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                new LogTraceBasicHandler(orderServiceV1, tracer)
        );

        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace tracer) {
        //Interface를 구현하는 target 클래스의 인스턴스 생성
        OrderServiceV1 orderServiceV1 = new OrderServiceV1Impl(orderRepositoryV1(tracer));

        //InvacationHandler에 필요한 target으로 위에 생성한 target 설정 및 의존성 전달
        OrderServiceV1 proxy = (OrderServiceV1) Proxy.newProxyInstance(
                OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class},
                new LogTraceBasicHandler(orderServiceV1, tracer)
        );

        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace tracer) {
        //Interface를 구현하는 target 클래스의 인스턴스 생성
        OrderRepositoryV1 orderRepositoryV1 = new OrderRepositoryV1Impl();

        //InvacationHandler에 필요한 target으로 위에 생성한 target 설정 및 의존성 전달
        OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(
                OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceBasicHandler(orderRepositoryV1, tracer)
        );

        //실제로 반환하는 것은 proxy
        return proxy;
    }

}
