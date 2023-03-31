package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecoratorPatternClient {

    private Component component;

    public DecoratorPatternClient(Component component) {
        this.component = component;
    }

    public void execute() {

        long startTime = System.currentTimeMillis();

        String result = component.operation();
        log.info("result = {}", result);

        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }
}
