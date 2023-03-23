package spring.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * TemplateMethod 패턴의 Template에 해당하는 부분 -> 컨텍스트, 문맥
 * Context는 '인터페이스'인 strategy에 의존
 * -> 기존 TemplateMethod 패턴에서 '상속'을 통한 강한 의존관계형성이 아님
 *    기존의 단점 : 부모 클래스의 기능 사용 X + 부모 클래스 변경 시 영향
 * -> Stragety는 인터페이스이므로 의존관계를 형성한 상위의 변화에 자유롭고 + 구현체 확장/변경 간단
 * *** 이는 Spring에서 의존관계 주입에 사용하는 패턴이 바로 이 전략 패턴 ***
 *
 * 필드에 전략(알고리즘 제품군)을 보관하는 방식
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();

        //비즈니스 로직 실행 가정

        strategy.call();
        // * 이 부분이 전체 맥락에서 특정 전략 알고리즘으로 위임하는 부분 + 현재는 필드 인터페이스로 추상화됨 -> 알맞은 알고리즘 전략 구현체주입

        //비즈니스 로직 종료 가정

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTIme = {}", resultTime);
    }
}
