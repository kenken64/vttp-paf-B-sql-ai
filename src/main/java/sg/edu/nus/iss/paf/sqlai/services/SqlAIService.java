package sg.edu.nus.iss.paf.sqlai.services;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.paf.sqlai.exception.SqlGenerationException;
import sg.edu.nus.iss.paf.sqlai.models.Answer;
import sg.edu.nus.iss.paf.sqlai.models.Question;

@Service
public class SqlAIService {
    
    @Value("classpath:/schema.sql")
    private Resource ddlResource;

    @Value("classpath:/sql-prompt-template.st")
    private Resource sqlPromptTemplateResource;

    
    private final ChatClient chatClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SqlAIService(ChatClient.Builder aiClientBuilder) {
        this.chatClient = aiClientBuilder.build();
    }

    public Answer askSqlAI(Question sqlRequest) throws IOException{
        String schema = ddlResource
                .getContentAsString(Charset.defaultCharset());
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
