//package org.occidere.githubnotifier.configuration;
//
//import com.linecorp.bot.client.LineMessagingClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author occidere
// * @Blog: https://occidere.blog.me
// * @Github: https://github.com/occidere
// * @since 2020-04-01
// */
//@Configuration
//public class LineBotConfiguration {
//
//    @Bean
//    public LineMessagingClient lineMessagingClient(@Value("${line.channel.token}") String lineChannel) {
//        return LineMessagingClient.builder(lineChannel).build();
//    }
//}
