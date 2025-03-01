package org.hidevelop.mollyimageapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogTestRunner{

    private static final Logger logger = LoggerFactory.getLogger(LogTestRunner.class);

    public void startLogging(String sessionId) {
        List<String> welcomeMessages = List.of(
                "5XX-Cat : {}님 컴퓨터는 틀리지 않아요~~!, 연결은 됬으닌 확인해보세요 ㅋ",
                "4XX-Cat : {}님 그럼 연결이 쉬울 주 아셨나요..? ㅋ",
                "2XX-Cat : {}절 보고싶다고요..? 좀 더 강해져서 돌아오세요 ㅋ"
        );
        new Thread( () -> { // 별도 스레드에서 실행
            try {
                    for (String message : welcomeMessages) {
                        logger.info( message , sessionId);
                        Thread.sleep(2000);
                    }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("LogTestRunner 중단됨: {}", e.getMessage());
            }
        }).start();
    }
}
