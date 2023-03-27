package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping //*** Spring은 @Controller 또는 @RequestMapping이 있어야 스프링 컨트롤러로 인식 - 인터페이스에도 사용 가능
@ResponseBody //REST 컨트롤러로 사용
public interface OrderControllerV1 {

    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);
    // *** 인터페이스에서는 @RequestParam(parameterName)을 생략하지 않고 명시해줘야 인식 오류가 발생하지 않을 가능성이 큼

    @GetMapping("/v1/no-log")
    String noLog();
}
