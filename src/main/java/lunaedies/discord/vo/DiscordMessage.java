package lunaedies.discord.vo;


import lombok.Data;

@Data
public class DiscordMessage {
    private String content;

    public DiscordMessage(String content) {
        this.content = content;
    }
}
