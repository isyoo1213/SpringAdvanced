package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 이제 pattern/methodName에 따른 부가기능 로직 수행의 여부를 분기함
 *     by patternMatchUtils
 * patterns는 handler의 생성자로 주입받으므로 이를 위한 설정 필요
 *     -> Config 소스 수정
 */
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace tracer;
    //Method 이름에 따른 로깅 여부를 위한 필드 생성
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace tracer, String[] patterns) {
        this.target = target;
        this.tracer = tracer;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //메서드 이름 필터 - by patternMatchUtils 패키지 사용
        //ex) save, request, reqe**, *esat...
        String methodName = method.getName();

        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            //Patterns에 methodName이 일치하는 경우가 없을 경우
            // -> Logging을 위한 로직을 수행하지 않고 곧바로 target의 메서드 실행
            return method.invoke(target, args);
        }

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
