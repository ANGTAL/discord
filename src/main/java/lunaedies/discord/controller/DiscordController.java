package lunaedies.discord.controller;

import lunaedies.discord.service.DiscordWebhookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;

@Controller
@RequestMapping("/discord")
public class DiscordController {

    private final DiscordWebhookService discordWebhookService;
    private final String webhookUrl;
    private final String defaultMessage;

    // DiscordController 생성자
    public DiscordController(DiscordWebhookService discordWebhookService,
                             @Value("${discord.webhook.url}") String webhookUrl,
                             @Value("${discord.default.message}") String defaultMessage) {
        this.discordWebhookService = discordWebhookService;
        this.webhookUrl = webhookUrl;
        this.defaultMessage = defaultMessage;
    }

    // Discord에 메시지를 보내는 엔드포인트
    @GetMapping("/send-message")
    public ResponseEntity<String> sendMessageToDiscord(@RequestParam("defaultMessage") String defaultMessage) {
        try {
            // DiscordWebhookService를 사용하여 메시지를 보냄
            discordWebhookService.sendMessageToDiscordWebhook(webhookUrl, defaultMessage);
            // 성공적으로 메시지를 보냈음을 반환
            return ResponseEntity.ok("Message sent to Discord!");
        } catch (Exception e) {
            // 예외 처리: 메시지 보내기 실패
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send message to Discord: " + e.getMessage());
        }
    }

    // senderMessage 템플릿을 반환하는 엔드포인트
    @GetMapping("/senderMessage")
    public String showIndex(){
        // senderMessage 템플릿을 클라이언트에 반환하여 보여줌
        return "senderMessage";
    }
}