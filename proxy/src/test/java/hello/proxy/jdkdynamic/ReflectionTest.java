package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class 클래스를 통해 class/method의 meta 정보 획득
 * -> 기존의 인스턴스에서 바로 method들을 호출하는 것과 달리 '추상화된 Method(class)'를 통해 method를 실행
 * -> meta정보를 통한 '선택'의 가능성 생김
 */
@Slf4j
public class ReflectionTest {

    @Test
    void reflection() {
        Hello target = new Hello();

        //공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); //로직1/로직2는 호출하는 메서드만 다름
        log.info("result = {}", result1);
        //공통 로직1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB(); //로직1/로직2는 호출하는 메서드만 다름
        log.info("result = {}", result2);
        //공통 로직2 종료
    }

    @Test
    void reflection1() throws Exception {
        //클래스 meta정보 획득하기
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        //callA의 메서드 meta정보 가져오기 - by String
        Method methodCallA = classHello.getMethod("callA");
        //meta정보를 통해 target 인스턴스에 있는 callA()를 실행하기
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        //callB의 메서드 meta정보 가져오기 - by String
        Method methodCallB = classHello.getMethod("callB");
        //meta정보를 통해 target 인스턴스에 있는 callB()를 실행하기
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }

    @Test
    void reflection2() throws Exception {
        //클래스 meta정보 획득하기
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        //callA의 메서드 meta정보 가져오기 - by String
        Method methodCallA = classHello.getMethod("callA");

        //로직수행을 공통처리할 수 있는 method를 만들어서 실행
        dynamicCall(methodCallA, target);

    /*
        //meta정보를 통해 target 인스턴스에 있는 callA()를 실행하기
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);
    */

        //callB의 메서드 meta정보 가져오기 - by String
        Method methodCallB = classHello.getMethod("callB");

        //로직수행을 공통처리할 수 있는 method를 만들어서 실행
        dynamicCall(methodCallB, target);

    /*
        //meta정보를 통해 target 인스턴스에 있는 callB()를 실행하기
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    */
    }

    /**
     * 기존 @Test reclection()에서 target.callA()/target.callB() -> 공통처리 불가능
     * method와 target을 parameter로 받아 로직수행을 공통처리
     */
    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");

        //기존의 공통처리 불가능했던 로직수행
        //String result1 = target.callA();

        //method와 target을 활용한 로직수행
        Object result1 = method.invoke(target);
        log.info("result = {}", result1);
    }

    static class Hello{
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }

}
