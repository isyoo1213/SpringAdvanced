package spring.advanced.app.v0;

import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class OrderServiceV0 {

    private final OrderRepositoryV0 orderRepository;

    //@Autowired
    public OrderServiceV0(OrderRepositoryV0 orderRepository) {
        this.orderRepository = orderRepository;
    }

    //비즈니스 로직은 단순하게 repository를 호출하는 정도로
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }

}
