package hello.proxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v1_proxy.ConcreteProxyConfig;
import hello.proxy.config.v1_proxy.InterfaceProxyConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyFilterConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * *** @Import
 *     - 괄호 내의 클래스를 스프링 Bean으로 등록해줌
 *     - @Configuration 이 붙은 클래스 또한 Bean 등록이 되어야 클래스 내부의 @Bean이  Bean 등록됨
 * *** scanBasePackages
 *     - AppVX.class를 바꿔가며 해당 클래스와 내부 @Bean들을 Bean 등록하기 위해 @ComponentScan 대상 패키지를 좁힘
 *     - default는 @SpringBootApplcation이 붙은 ProxyApplication.class가 속한 패키지 hello.proxy와 그 하위
 */
@Import({DynamicProxyFilterConfig.class, ConcreteProxyConfig.class})
//@Import({DynamicProxyBasicConfig.class, ConcreteProxyConfig.class})
//@Import({InterfaceProxyConfig.class, ConcreteProxyConfig.class})
//@Import(InterfaceProxyConfig.class)
//@Import({AppV1Config.class, AppV2Config.class})
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	//LogTrace를 Bean 등록해주기
	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}

}
