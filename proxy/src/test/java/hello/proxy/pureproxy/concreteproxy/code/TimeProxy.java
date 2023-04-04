package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic {

    private ConcreteLogic target;

    public TimeProxy(ConcreteLogic target) {
        this.target = target;
    }

    @Override
    public String operation() { // Proxy이긴 하나, 접근제어보다는 부가 기능 수행의 DecoratorPattern의 성격
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();

        //실제 target이 메서드 호출
        // -> operation() 메서드의 골자를 공유하므로 return 타입이 동일 -> Override된 operation() 내에서 operation() 호출
        String result = target.operation();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("TimeDecorator 종료");
        log.info("resultTime = {}", resultTime);

        return result;
    }
}
