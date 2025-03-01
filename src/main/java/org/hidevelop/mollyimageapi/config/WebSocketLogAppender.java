package org.hidevelop.mollyimageapi.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class WebSocketLogAppender extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        LogWebSocketHandler.broadcastLog(iLoggingEvent.getFormattedMessage());
    }
}
