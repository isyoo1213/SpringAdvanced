package spring.advanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import spring.advanced.trace.strategy.code.template.Callback;
import spring.advanced.trace.strategy.code.template.TimeLogTemplate;

@Slf4j
public class TemplateCallbackTest {

    /**
     * 템플릿 콜백 패턴 - 익명 내부 클래스로 콜백 생성
     */
    @Test
    void callbackV1() {

        TimeLogTemplate template = new TimeLogTemplate();

        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 수행");
            }
        });

        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 수행");
            }
        });

    }

    /**
     * 템플릿 콜백 패턴 - 람다 표현식으로 콜백 생성
     */
    @Test
    void callbackV2() {
        TimeLogTemplate template = new TimeLogTemplate();

        template.execute(() -> log.info("비즈니스 로직1 수행"));
        template.execute(() -> log.info("비즈니스 로직2 수행"));
    }
}