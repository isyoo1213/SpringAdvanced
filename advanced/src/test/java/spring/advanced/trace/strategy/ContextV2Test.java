package spring.advanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import spring.advanced.trace.strategy.code.strategy.ContextV2;
import spring.advanced.trace.strategy.code.strategy.Strategy;
import spring.advanced.trace.strategy.code.strategy.StrategyLogic1;
import spring.advanced.trace.strategy.code.strategy.StrategyLogic2;

/**
 * *** Strategy라는 '부품' 단위 분리의 한계점
 *     -> 결합된 '부품'을 바꿔야 한다면?
 *     -> 1. setter() 변경하기에는 Conetext 인스턴스를 Singleton으로 사용시 동시성이슈
 *     -> 2. 새로운 Context를 조립해서 생성해낸다면, 실시간으로 변경되는 전략 상황에서의 비용
 *     -> 이미 조립된 '부품' 변경의 유연성에 대한 고민으로 이어짐
 * *** Parameter로 전달하는 방식의 장점
 *     -> 문제 1 -> 하나의 Context 인스턴스만 생성 + 필드 X + Parameter로 전달하므로 일부 동시성문제 벗어남
 *     -> 문제 2 -> 기존에 선 '조립'의 과정이 완결된 인스턴스 자체를 생성하지 않고 실시간으로 Parameter로 전달받음
 * *** Parameter로 전달하는 방식의 단점
 *     -> 전략을 실행할 때 지정하고 싶은 전략을 계속 Parameter로 전달해주어야함
 */
@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1() {
        ContextV2 conetxt = new ContextV2();
        conetxt.execute(new StrategyLogic1());
        conetxt.execute(new StrategyLogic2());
    }

    /**
     * 전략 패턴 적용 + 익명 내부 클래스로 전략 생성
     */
    @Test
    void strategyV2() {
        ContextV2 conetxt = new ContextV2();
        conetxt.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 수행");
            }
        });
        conetxt.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 수행");
            }
        });
    }

    /**
     * 전략 패턴 적용 + 람다 표현식으로 전략 생성
     */
    @Test
    void strategyV3() {
        ContextV2 conetxt = new ContextV2();
        conetxt.execute(() -> log.info("비즈니스 로직1 수행"));
        conetxt.execute(() -> log.info("비즈니스 로직2 수행"));
    }

}
