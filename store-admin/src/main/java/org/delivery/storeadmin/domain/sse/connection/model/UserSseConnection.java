package org.delivery.storeadmin.domain.sse.connection.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.delivery.storeadmin.domain.sse.connection.ifs.ConnectionPoolIfs;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Getter
@ToString
@EqualsAndHashCode
public class UserSseConnection {
    private final String uniqueKey; // 밖에서 못 바꾸게 final (캡슐화)
    private final SseEmitter sseEmitter; // 밖에서 못 바꾸게 final (캡슐화)
    private final ConnectionPoolIfs<String, UserSseConnection> connectionPoolIfs; // 밖에서 못 바꾸게 final (캡슐화)
    private final ObjectMapper objectMapper; // 밖에서 못 바꾸게 final (캡슐화)

    // 생성자 사용 못하게 private 강제 + static connect 메소드로 생성자 생성, 명시적으로
    private UserSseConnection(String uniqueKey,
                              ConnectionPoolIfs<String, UserSseConnection> connectionPoolIfs,
                              ObjectMapper objectMapper) {

        this.uniqueKey = uniqueKey;

        this.sseEmitter = new SseEmitter(1000L * 60);

        // callback 초기화
        this.connectionPoolIfs = connectionPoolIfs;

        // objectMapper 초기화
        this.objectMapper = objectMapper;

        // on completion
        this.sseEmitter.onCompletion(() -> {
            // connection pool remove
            this.connectionPoolIfs.onCompletionCallback(this);
        });

        // on timeout
        this.sseEmitter.onTimeout(() ->{
            this.sseEmitter.complete();
        });
        // on open 메시지
        sendMessage("onopen", "connect");
    }

    // 밖에서 생성자를 통해 UserSseConnection객체를 생성하면 연결이 된건지 알기 어려우니
    // 명시적으로 connect라고 메소드를 만듦 (그리고 생성자는 private로 감춤)
    public static UserSseConnection connect(String uniqueKey,
                                            ConnectionPoolIfs<String, UserSseConnection> connectionPoolIfs,
                                            ObjectMapper objectMapper){

        return new UserSseConnection(uniqueKey, connectionPoolIfs, objectMapper);
    }

    public void sendMessage(String eventName, Object data){
        try {
            // json데이터로만 통신할 것이기 때문에 objectMapper
            var json = this.objectMapper.writeValueAsString(data);
            var event = SseEmitter.event()
                    .name(eventName)
                    .data(json);
            this.sseEmitter.send(event);
        } catch (IOException e) {
            this.sseEmitter.completeWithError(e);
        }
    }

    // 이벤트 이름 지정 안할 수도 있으니.. 추가
    public void sendMessage(Object data){
        try {
            var json = this.objectMapper.writeValueAsString(data);
            var event = SseEmitter.event()
                    .data(data);
            this.sseEmitter.send(event);
        } catch (IOException e) {
            this.sseEmitter.completeWithError(e);
        }
    }
}
