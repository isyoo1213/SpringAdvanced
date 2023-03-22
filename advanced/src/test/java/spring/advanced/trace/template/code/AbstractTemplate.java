package spring.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {
    //인터페이스와 추상클래스의 다형성에 관한 부분도 꼭 깊이있게 접근할 것
    //상속은 풍부할 수록 좋다 by what of SOLID..?
    //인터페이스는 크지 않을 수록 좋다 by ISP of SOLID
    // *** TemplateMethod 패턴이 중요하게 활용하는 부분은 OCP

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행 가정
        call(); //상속
        //log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료 가정
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTIme = {}", resultTime);
    }

    protected abstract void call();
}
