package com.cedricziel.demo.sse;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableScheduling
@SpringBootApplication
public class SseDemoApplication {

    private static final Logger log = Logger.getLogger(SseDemoApplication.class);

    private final List<SseEmitter> emitters = new ArrayList<>();

    public static void main(String[] args) {

        SpringApplication.run(SseDemoApplication.class, args);
    }

    @RequestMapping(path = "/stream", method = RequestMethod.GET)
    public SseEmitter stream() throws IOException {

        SseEmitter emitter = new SseEmitter();

        synchronized ( emitters ) { emitters.add(emitter); }
        emitter.onCompletion(() -> emitters.remove(emitter));

        return emitter;
    }

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {

        String message = "{\"timestamp\": \"" + String.valueOf(System.currentTimeMillis()) + "\"}";
 
        emitters.forEach((SseEmitter emitter) -> {
            try {
               log.info("Emitting... " + message + " to emitter: " + emitter.toString());
               emitter.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
                log.info(e.getMessage());;
            }
        });
     }
}
