package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication	// 이 Annotation 달아주면 해당 패키지와 이 패키지 하위의 모든 것들 Component scan 실행 => Spring Bean 으로 자동 등록
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

}
