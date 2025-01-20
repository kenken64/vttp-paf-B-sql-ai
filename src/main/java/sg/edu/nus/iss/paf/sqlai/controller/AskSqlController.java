package sg.edu.nus.iss.paf.sqlai.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.paf.sqlai.models.Answer;
import sg.edu.nus.iss.paf.sqlai.models.Question;
import sg.edu.nus.iss.paf.sqlai.services.SqlAIService;

@RestController
public class AskSqlController {
    @Autowired
    private SqlAIService sqlAiSvc;

    @PostMapping("/ask-sql")
    public Answer sql(@RequestBody Question sqlRequest) throws IOException{
        return sqlAiSvc.askSqlAI(sqlRequest);
    }
}
