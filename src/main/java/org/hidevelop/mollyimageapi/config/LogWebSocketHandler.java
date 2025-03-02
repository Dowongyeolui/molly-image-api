package org.hidevelop.mollyimageapi.config;

import org.hidevelop.mollyimageapi.exception.CustomException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;
import static org.hidevelop.mollyimageapi.exception.CustomError.FAILED_TRANSFER_LOG;

@Component
public class LogWebSocketHandler extends TextWebSocketHandler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogTestRunner.class);
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final LogTestRunner logTestRunner;

    public LogWebSocketHandler(LogTestRunner logTestRunner) {
        this.logTestRunner = logTestRunner;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        logger.info("{}님이 들어왔어요!!!", session.getId());
        logTestRunner.startLogging(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        logger.info("{}님이 나갔어요~!", session.getId());
    }


    public static void broadcastLog (String logMessage) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(logMessage));
                } catch (Exception e){
                    throw new CustomException(FAILED_TRANSFER_LOG);
                }
            }
        }
    }
}
