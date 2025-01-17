package sg.edu.nus.iss.paf.sqlai.controller;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.paf.sqlai.exception.SqlGenerationException;
import sg.edu.nus.iss.paf.sqlai.models.Answer;
import sg.edu.nus.iss.paf.sqlai.models.Question;

@RestController
public class AskSqlController {
    @Value("classpath:/schema.sql")
    private Resource ddlResource;

    @Value("classpath:/sql-prompt-template.st")
    private Resource sqlPromptTemplateResource;

    
    private final ChatClient chatClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public AskSqlController(ChatClient.Builder aiClientBuilder) {
        this.chatClient = aiClientBuilder.build();
    }

    @PostMapping("/ask-sql")
    public Answer sql(@RequestBody Question sqlRequest) throws IOException{
        String schema = ddlResource
                .getContentAsString(Charset.defaultCharset());
        System.out.println("xx" + chatClient);
        System.out.println("xx" + chatClient.prompt());
        System.out.println(sqlPromptTemplateResource);
        System.out.println(sqlRequest);
        System.out.println(sqlRequest.getQuestion());
        System.out.println(schema);

        String query = chatClient.prompt()
            .user(userSpec -> 
                userSpec.text(sqlPromptTemplateResource)
                .param("question", sqlRequest.getQuestion())
                .param("ddl", schema)).call().content();
        
        if(query.toLowerCase().startsWith("select")){
            return new Answer(query, jdbcTemplate.queryForList(query));
        }
        throw new SqlGenerationException(query);
    }
}
