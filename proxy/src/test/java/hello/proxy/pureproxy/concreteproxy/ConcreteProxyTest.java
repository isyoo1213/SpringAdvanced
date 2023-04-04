package hello.proxy.pureproxy.concreteproxy;

import hello.proxy.pureproxy.concreteproxy.code.ConcreteClient;
import hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concreteproxy.code.TimeProxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ConcreteProxyTest {

    @Test
    void noProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient client = new ConcreteClient(concreteLogic);
        client.execute();
    }

    @Test
    void addProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        TimeProxy timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient client = new ConcreteClient(timeProxy);
        //TimeProxy는 ConcreteLogic을 상속하므로 자료형으로 사용 가능
        //즉, client의 생성자에 ContcreteLogic 자료형의 인스턴스를 주입하는데 가능한 경우는
        //1. ConcreteLogic - 자기 자신의 타입 할당 / 2. TimeProxy - 자식 타입의 할당
        // -> *** 인터페이스 뿐만 아니라 상속으로도 다형성 구현
        client.execute();
    }
}
