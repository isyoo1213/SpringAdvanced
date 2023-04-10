package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {

    //proxy를 사용하는 Interface에 명세된 method 중 어떤 이름의 method에 logging을 적용할지
    private static final String[] PATTERNS = {"request*", "order*", "save*"};
    //private static final String[] PATTERNS = {"request*", "xxx*", "save*"};
    //이 경우, log에 Service가 호출한 method의 log는 생략됨

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace tracer) {
        //Interface를 구현하는 target 클래스의 인스턴스 생성
        OrderControllerV1 orderServiceV1 = new OrderControllerV1Impl(orderServiceV1(tracer));

        //InvacationHandler에 필요한 target으로 위에 생성한 target 설정 및 의존성 전달
        OrderControllerV1 proxy = (OrderControllerV1) Proxy.newProxyInstance(
                OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                //기존의 BasicHandler에서 filterHandler로 변경하고, PATTERNS를 전달
                new LogTraceFilterHandler(orderServiceV1, tracer, PATTERNS)
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
                //기존의 BasicHandler에서 filterHandler로 변경하고, PATTERNS를 전달
                new LogTraceFilterHandler(orderServiceV1, tracer, PATTERNS)
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
                //기존의 BasicHandler에서 filterHandler로 변경하고, PATTERNS를 전달
                new LogTraceFilterHandler(orderRepositoryV1, tracer, PATTERNS)
        );

        //실제로 반환하는 것은 proxy
        return proxy;
    }

}
