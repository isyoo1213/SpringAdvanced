package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK 동적 Proxy는 'Interface'에만 적용 가능하모, 기존 App의 v1에만 적용 가능
 * Test의 예제에서는 InvocationHandler의 구현체가 주입받는 target의 자료형이 고정된 형태로 사용했음
 *    ex) AImpl / BImpl
 * 실제 AppV1에 적용할 InvocationHandler 구현체는 Controller/Service/Repository의 모든 구현체를 target으로 설정가능
 *    -> Object 타입으로 target을 주입받음
 * *** 적용을 위해 해결해야 할 부분들
 *     1. logging을 위한 클래스/메서드 정보 message 전달 -> newProxy()에서 전달한 method의 meta정보 활용
 *     2. 로직 호출 -> method.invoke()로 간단하게 처리
 * *** 한계점
 *     현재 proxy는 Interface 내의 모든 메서드에 적용됨 ex) 각각의 메서드 + *** noLog()
 *     로깅을 적용하지 않을 메서드에도 적용되므로, 이를 처리하기 위한 작업은 다음 부분에서 이어짐
 */
public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace tracer;

    public LogTraceBasicHandler(Object target, LogTrace tracer) {
        this.target = target;
        this.tracer = tracer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TraceStatus status = null;

        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "() by Dynamic Proxy";
            status = tracer.begin(message);

            //로직 호출
            Object result = method.invoke(target, args);
            tracer.end(status);

            return result;
        } catch (Exception e) {
            tracer.exception(status, e);
            throw e;
        }
    }
}
