package spring.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 전략을 Context가 아닌 Parameter로 전달받는 방식
 */
@Slf4j
public class ContextV2 {

//    private Strategy strategy;
//
//    public ContextV2(Strategy strategy) {
//        this.strategy = strategy;
//    }

    public void execute(Strategy strategy) {
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
