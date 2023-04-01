package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component{

    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");

        //여기까지는 CacheProxy 처럼 일시적으로 Real 객체에서 수행한 값을 가져오는 과정
        //CacheProxy는 이를 Caching하고 / Decorator는 이를 Decorating 함
        String resultFromRealComp = component.operation();

        //Decorating
        //"data" -> "*****data*****"
        String decoResult = "*****" + resultFromRealComp + "*****";
        log.info("MessageDecorator 적용 전 = {}", resultFromRealComp);
        log.info("MessageDecorator 적용 후 = {}", decoResult);

        return decoResult;
    }
}
