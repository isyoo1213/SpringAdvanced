package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB를 사용하기 위해 Spring은 ProxyFactory 라이브러리 형태로 제공
 * 기존 JDKproxy에서 InvokeHandler를 구현 하듯, CGLIB에서는 MethodInterceptor를 구현
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    // *** 항상 proxy는 대상 target이 필요함
    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    // *** JDKproxy의 InvocationHandler와 비슷한 parameter/로직을 가짐
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        //method.invoke()보다 methodProxy를 사용하는 것이 빠르다고 함
        Object result = methodProxy.invoke(target, args);

        //기존 InvokeHandler에서의 로직
        //Object result = method.invoke(target);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 / resultTime = {}", resultTime);

        return result;
    }
}
