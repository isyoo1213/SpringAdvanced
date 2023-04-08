package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

/**
 * JDK 동적 프록시를 사용하기 위해서는 'Interface'가 필수
 *     -> 즉, 앞서 공부했던 Interface가 존재하는 경우 Proxy를 적용하는 케이스
 * + 해당 Proxy를 구현체인 target마다 구현하는 것이 아닌 'reflection'을 통해 '동적으로 method를 실행하는 Proxy를 생성해주는 방식
 *     -> 즉 proxy를 통해 로직을 수행하는 부분 + proxy를 생성하는 부분으로 나누어서 생각
 *     1. proxy를 통해 로직을 수행하는 부분
 *        - InvocationHandler('Intercafe')를 구현함으로서 작성
 *     2. proxy를 생성하는 부분
 *        - Proxy.newProxyInstance()를 통해 생성
 *        - java.lang.reflect.Proxy -> basic한 JAVA가 제공
 *        - 1st Param : Proxy를 생성할 Interface by Class.getClassLoader()
 *        - 2nd Param : Proxy가 구현할 Interface by new Class[]{} (Class 배열인 이유는 Interface는 구현이 다양하므로)
 *        - 3rd Param : Proxy가 수행할 로직 by InvacationHandler를 통해 생성한 handler
 * 생성된 proxy는 실제 구현해놓은 InvocationHandler 인스턴스의 invoke()를 호출
 *    - 즉 InvocationHandler가 사용할 method/args 정보를 전달해주고,
 *    - InvocationHandler.invoke()는 proxy가 구성해준 method/args를 통해 내부 로직을 따라 method.invoke()를 실행
 *    - 사용자는 invoke() 내부에 proxy를 통해 적용하고 싶은 로직을 구성해주면 되는 형식
 * *** 즉 target 인스턴스, 클라이언트 객체정보, InvacationHandler의 invoke() 메서드 내부 로직만 작성해주면
 *     newProxy()를 생성하는 과정에서 method/args를 구성하고 Invocationhandler.invoke를 호출하는 로직을 자동으로 구현해주는 것
 *     - 실제로 target인스턴스와 클라이언트 객체 정보는 이미 구성되어 있는 경우가 대부분
 * *** 즉 Client는 기존 runtime 객체 의존 관계에서 clinet -> proxy -> impl 로 직접 호출하는 로직이 아닌,
 *     client -> (동적생성된 proxy) -> InvocationHandler -> ampl 로 변경된다
 *     * () < 이 부분은 개발자가 직접 만들지 않고, newProxy()를 통해 제공되는 것만 연결해주면 된다는 뜻
 */
@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        //프록시를 얻고싶은 객체의 인스턴스화
        AInterface target = new AImpl();

        // *** proxy가 호출하는 로직 != proxy를 생성하는 로직
        //Proxy생성을 위해 target을 넘겨주어야 함
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // *** proxy를 생성하는 로직
        // *** Interface의 구현은 다양하므로 원하는 자료형으로 return 타입을 다운 캐스팅
        AInterface proxy = (AInterface) Proxy.newProxyInstance(
                AInterface.class.getClassLoader(),
                new Class[]{AInterface.class},
                handler);

        proxy.call();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        //프록시를 얻고싶은 객체의 인스턴스화
        BInterface target = new BImpl();

        // *** proxy가 호출하는 로직 != proxy를 생성하는 로직
        //Proxy생성을 위해 target을 넘겨주어야 함
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // *** proxy를 생성하는 로직
        // *** Interface의 구현은 다양하므로 원하는 자료형으로 return 타입을 다운 캐스팅
        BInterface proxy = (BInterface) Proxy.newProxyInstance(
                BInterface.class.getClassLoader(),
                new Class[]{BInterface.class},
                handler);

        proxy.call();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }
}
