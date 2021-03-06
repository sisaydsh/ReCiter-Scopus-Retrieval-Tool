package reciter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reciter.model.scopus.ScopusArticle;
import reciter.model.scopus.ScopusQuery;
import reciter.scopus.retriever.ScopusArticleRetriever;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/scopus")
@Api(value="ScopusController", description="Operations on querying the Scopus API.")
public class ScopusController {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(ScopusController.class);

    @ApiOperation(value = "Querying Scopus with PMIDs.", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping(value = "/query/", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<ScopusArticle>> retrieve(@RequestBody ScopusQuery scopusQuery) {
        slf4jLogger.info("calling retrieve with pmids size=[" + scopusQuery.getQuery().size() + "]");
        ScopusArticleRetriever scopusArticleRetriever = new ScopusArticleRetriever();
        List<ScopusArticle> scopusArticles = scopusArticleRetriever.retrieveScopus(new ArrayList<>(scopusQuery.getQuery()), scopusQuery.getType());
        slf4jLogger.info("finished retrieving with pmids size=[" + scopusQuery.getQuery().size() + "]");
        return ResponseEntity.ok(scopusArticles);
    }
}
