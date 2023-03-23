package spring.advanced.app.v4;

import org.springframework.stereotype.Service;
import spring.advanced.trace.TraceStatus;
import spring.advanced.trace.logTrace.LogTrace;
import spring.advanced.trace.template.AbstractTemplate;

/**
 * 템플릿 메서드 패턴을 적용한 추상 클래스 AbstractTemplate 를 적용
 */
@Service
//@RequiredArgsConstructor
public class OrderServiceV4 {

    private final OrderRepositoryV4 orderRepository;
    private final LogTrace tracer;

    //@Autowired
    public OrderServiceV4(OrderRepositoryV4 orderRepository, LogTrace tracer) {
        this.orderRepository = orderRepository;
        this.tracer = tracer;
    }

    //비즈니스 로직은 단순하게 repository를 호출하는 정도로
    //Controller에서 넘어오는 beforeTraceId 추가
    public void orderItem(String itemId) {

        //익명내부클래스를 통해 추상클래스인 템플릿의 인스턴스를 생성 + 초기화
        AbstractTemplate<Void> template = new AbstractTemplate<>(tracer) {
            @Override
            protected Void call() {
                orderRepository.save(itemId);
                // *** Void는 generic을 처리하기 위한 객체 - void, int 등의 기본형 타입 사용 불가
                // + 실제 save() 호출 후의 리턴은 void로 '캐스팅' 자체를 활용하는 것도 불가능 - 추가적인 공부 필요
                // -> Java 언어 자체의 구성과 문법으로 인한 한계이므로 null을 retun하는 것으로 마무리
                return null;
            }
        };

        //템플릿 인스턴스가 오버라이딩된 calll() 메서드를 포함한 execute() 메서드의 결과값을 반환
        template.execute("OrderServiceV4.orderItem()");
    }
}
