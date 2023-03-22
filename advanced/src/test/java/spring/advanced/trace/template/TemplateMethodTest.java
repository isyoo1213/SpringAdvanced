package spring.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import spring.advanced.trace.template.code.AbstractTemplate;
import spring.advanced.trace.template.code.SubClassLogic1;

@Slf4j
class TemplateMethodTest {

    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    /**
     * 템플릿 메서드 패턴을 적용
     */
    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();
    }

    /**
     * 익명내부클래스를 통해 TemplateMethod의 AbstractTemplate 클래스를 상속받는 구현 클래스 정의를 생략
     */
    @Test
    void templateMethodV2() {
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직1 실행");
            }
        };

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직2 실행");
            }
        };

        // * 익명내부클래스는 이름이 없으므로 이를 생성하는 위치에서 &를 통해 구분
        log.info("익명내부클래스1 이름 = {}", template1.getClass());
        log.info("익명내부클래스2 이름 = {}", template2.getClass());
        template1.execute();
        template2.execute();
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
