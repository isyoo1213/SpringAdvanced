package spring.advanced.trace.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import spring.advanced.trace.threadlocal.code.ThreadLocalService;

@Slf4j
class ThreadLocalServiceTest {

    private final ThreadLocalService service = new ThreadLocalService();

    @Test
    void field() {
        log.info("main start");

        //Thread들 끼리 경합하는 상황을 가정
        Runnable userA = new Runnable() {
            @Override
            public void run() {
                service.logic("userA");
            }
        };
        // Runnable 생성하는 간단한 람다식
        Runnable userB = () -> service.logic("userB");

        //Thread 생성
        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        //hread 실행
        threadA.start();

        //1. threadA 실행 후 2초 정도 sleep을 통해 ThreadA의 실행이 완전히 끝나도록 시간 구성
        //sleep(2000); // -> *** 동시성 문제 발생하지 않음

        //2. ThreadA 실행 후 sleep을 짧게 가져가면서 동시성 문제가 발생하도록 구성
        sleep(100);

        threadB.start();

        sleep(3000); //메인 쓰레드가 Test 수행 후 곧바로 종료함으로써 threadB의 로그 출력이 끊김을 방지

        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
