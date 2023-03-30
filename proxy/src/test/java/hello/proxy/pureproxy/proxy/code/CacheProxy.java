package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject{

    private Subject target;
    private String cacheValue;

    // RealSubject를 의존하기 위한 생성자 주입
    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");

        long startTime = System.currentTimeMillis();

        // 즉, RealSubject 내의 로직이 반환하는 값에 변화가 없을 경우에 한정
        if (cacheValue == null) {
            cacheValue = target.operation();
        }

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("resultTime = {}", resultTime);

        return cacheValue;


    }
}
