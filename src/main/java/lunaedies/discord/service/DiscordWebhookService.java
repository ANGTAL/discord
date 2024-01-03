package lunaedies.discord.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lunaedies.discord.vo.DiscordMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class DiscordWebhookService {

    // Logger 선언
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordWebhookService.class);

    // 의존성 주입
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // 생성자
    public DiscordWebhookService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    // Discord로 메시지 보내는 메서드
    public void sendMessageToDiscordWebhook(String webhookUrl, String message) {
        try {
            // HTTP POST 요청 생성
            HttpPost httpPost = new HttpPost(webhookUrl);

            // DiscordMessage 객체 생성 및 JSON으로 직렬화
            DiscordMessage discordMessage = new DiscordMessage(message);
            String json = objectMapper.writeValueAsString(discordMessage);

            // JSON을 UTF-8로 인코딩하여 StringEntity 생성
            StringEntity params = new StringEntity(json, StandardCharsets.UTF_8);

            // 헤더 설정 (JSON 타입 지정)
            httpPost.setHeader("Content-type", "application/json");

            // 요청에 본문 설정
            httpPost.setEntity(params);

            // HTTP 요청 보내기
            HttpResponse response = httpClient.execute(httpPost);

            // 응답 상태 코드 확인
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 204) {
                LOGGER.info("Message sent to Discord successfully."); // 성공 로깅
            } else {
                LOGGER.error("Failed to send message to Discord. Response code: {}", statusCode); // 실패 로깅
            }
        } catch (IOException e) {
            LOGGER.error("Error while sending message to Discord: {}", e.getMessage()); // 예외 발생 시 로깅
        }
    }
}