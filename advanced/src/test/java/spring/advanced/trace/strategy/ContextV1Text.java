package spring.advanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import spring.advanced.trace.strategy.code.strategy.ContextV1;
import spring.advanced.trace.strategy.code.strategy.Strategy;
import spring.advanced.trace.strategy.code.strategy.StrategyLogic1;
import spring.advanced.trace.strategy.code.strategy.StrategyLogic2;

/**
 * *** 결국 Strategy는 선 '조립' 후 '실행'
 *     -> Context와 Strategy를 미리 조립하는 과정을 거침
 * *** Strategy라는 '부품' 단위 분리의 장점
 *     -> templateMethod 패턴의 의미 없는 강한 결합(상속)과 SRP(부모의 변경에 휘둘림)에서 벗어난 구조
 * *** Strategy라는 '부품' 단위 분리의 한계점
 *     -> 결합된 '부품'을 바꿔야 한다면?
 *     -> 1. setter() 변경하기에는 Conetext 인스턴스를 Singleton으로 사용시 동시성이슈
 *     -> 2. 새로운 Context를 조립해서 생성해낸다면, 실시간으로 변경되는 전략 상황에서의 비용
 *     -> 이미 조립된 '부품' 변경의 유연성에 대한 고민으로 이어짐
 */
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

    /**
     * 전략 패턴 + 익명 내부 클래스로 Strategy 구체화 인스턴스 생성
     * Strategy 인터페이스의 구현 클래스를 따로 정의할 필요 없이 익명 내부 클래스로 필요한 인스턴스만 생성
     */
    @Test
    void strategyV2() {

        Strategy strategyLogic1 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 1 실행");
            }
        };

        Strategy strategyLogic2 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 2 실행");
            }
        };

        ContextV1 context1 = new ContextV1(strategyLogic1);
        log.info("strategyLogic1 = {}", strategyLogic1);
        //로그
        //strategyLogic1 = spring.advanced.trace.strategy.ContextV1Text$1@c7045b9
        context1.execute();

        ContextV1 context2 = new ContextV1(strategyLogic2);
        log.info("strategyLogic2 = {}", strategyLogic2);
        //로그
        //strategyLogic2 = spring.advanced.trace.strategy.ContextV1Text$2@5026735c
        context2.execute();

    }

    /**
     * V2 + 익명 내부 클래스 인스턴스를 Context 생성자 의존성주입 과정에서 생성해주기
     */
    @Test
    void strategyV3() {

        ContextV1 context1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 1 실행");
            }
        });
        context1.execute();

        ContextV1 context2 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 2 실행");
            }
        });
        context2.execute();

    }

    /**
     * V3 + 람다 사용
     */
    @Test
    void strategyV4() {

        ContextV1 context1 = new ContextV1(()-> log.info("비즈니스 로직 1 실행"));
        context1.execute();

        ContextV1 context2 = new ContextV1(()-> log.info("비즈니스 로직 2 실행"));
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
