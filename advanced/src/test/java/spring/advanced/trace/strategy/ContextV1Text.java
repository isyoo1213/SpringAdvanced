package spring.advanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import spring.advanced.trace.strategy.code.strategy.ContextV1;
import spring.advanced.trace.strategy.code.strategy.Strategy;
import spring.advanced.trace.strategy.code.strategy.StrategyLogic1;
import spring.advanced.trace.strategy.code.strategy.StrategyLogic2;

@Slf4j
public class ContextV1Text {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    /**
     * 전략 패턴 사용
     */
    @Test
    void strategyV1() {
        //전략 구현체 생성
        Strategy strategyLogic1 = new StrategyLogic1();
        Strategy strategyLogic2 = new StrategyLogic2();

        //Context 생성 + 전략구현체 주입
        ContextV1 context1 = new ContextV1(strategyLogic1);
        ContextV1 context2 = new ContextV1(strategyLogic2);

        //Strategy가 결합된 완성된 Context 실행
        context1.execute();
        context2.execute();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행 가정
        log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료 가정
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTIme = {}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행 가정
        log.info("비즈니스 로직2 실행");
        //비즈니스 로직 종료 가정
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTIme = {}", resultTime);
    }
}
