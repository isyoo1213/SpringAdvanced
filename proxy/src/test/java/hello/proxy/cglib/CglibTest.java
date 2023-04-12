package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        //Interface가 없는 경우의 target을 설정
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();

        //실제 Proxy원리 처럼, 구체클래스를 상속받는 클래스를 설계하기 위한 구체클래스 '타입' 설정
        enhancer.setSuperclass(ConcreteService.class);

        //실제 target 인스턴스를 받는 Handler를 callback으로 설정
        enhancer.setCallback(new TimeMethodInterceptor(target));

        //Proxy 생성 + target의 타입으로 형변환
        ConcreteService proxy = (ConcreteService) enhancer.create();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
        //로그
        //INFO hello.proxy.cglib.CglibTest - targetClass = class hello.proxy.common.service.ConcreteService
        //INFO hello.proxy.cglib.CglibTest - proxyClass = class hello.proxy.common.service.ConcreteService$$EnhancerByCGLIB$$25d6b0e3

        proxy.call();
    }
}
