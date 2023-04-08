package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 클라이언트 인스턴스는 생성자를 통해 target으로 주입
 * 클라이언트가 호출하고자 하는 로직은 method meta정보로 수행
 * method 수행에 필요한 parameter는 args로 전달
 *
 */
@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    //Proxy의 target
    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        //클라이언트 인스턴스는 생성자를 통해 target으로 주입
        //클라이언트가 호출하고자 하는 로직은 method meta정보로 수행
        //method 수행에 필요한 parameter는 args로 전달
        Object result = method.invoke(target);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 / resultTime = {}", resultTime);

        return result;
    }
}
