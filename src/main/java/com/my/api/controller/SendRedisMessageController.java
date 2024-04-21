package com.my.api.controller;

import com.my.redis.service.IRedisService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
@RestController
@Api(tags = "傳送Redis訊息 操作", value = "傳送Redis訊息 操作 API 說明")
public class SendRedisMessageController {

    private static final Logger logger = LoggerFactory.getLogger(SendRedisMessageController.class);

    @Autowired(required = true)
    @Qualifier("redisService")
    protected IRedisService service;

    @ApiOperation(value="傳送Redis訊息")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
            @ApiResponse(code=401, message="無法授權"),
    })
    @GetMapping("/redis/{topic}/send/{message}")
    public void send(
                        @ApiParam(required=true, value="請傳入topic")
                        @PathVariable String topic,
                        @ApiParam(required=true, value="請傳入訊息")
                        @PathVariable String message,
            HttpServletResponse response) throws Exception {
        try{
            logger.info(Boolean.toString( getService().publish(topic, message) ));
        } catch (Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
