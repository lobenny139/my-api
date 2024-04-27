package com.my.api.controller;

import com.my.db.entity.Member;
import com.my.db.exception.*;
import com.my.db.service.IMemberService;
import com.my.jwt.JwtTokenUtil;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Getter
@Setter
@RestController
@Api(tags = "Member Table 操作", value = "Member Table 操作 API 說明")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired(required=true)
    @Qualifier("memberService")
    IMemberService service;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @ApiOperation(value="以 Member 的 account 取出單一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
            @ApiResponse(code=404, message="找不到資料"),
            @ApiResponse(code=401, message="無法授權"),
    })
    @GetMapping("/member/{account}")
    public Member get(
            @ApiParam(required=true, value="請傳入物件(member)的 account")
            @PathVariable String account,
            HttpServletResponse response) throws Exception {
        try{
            return getService().getEntityByAccount(account);
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(DataException e) {
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(SQLGrammerConflictException e){
            //406
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }catch(UnSupportException e){
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="取出所有物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
//            @ApiResponse(code=404, message="找不到資料"),
            @ApiResponse(code=401, message="無法授權"),
    })
    @GetMapping("/members")
    public Iterable<Member> getAll(
            HttpServletResponse response) throws Exception {
        try{
            return getService().getAllEntities();
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(DataException e) {
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(SQLGrammerConflictException e){
            //406
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }catch(UnSupportException e){
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="新增單一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
//          @ApiResponse(code=404, message="找不到資料"),
            @ApiResponse(code=409, message="資料衝突"),
            @ApiResponse(code=401, message="無法授權"),
    })
    @PostMapping("/member")
    public Member create(
            @ApiParam(required=true, value="請傳入物件(Member)的 JSON 格式")
            @RequestBody(required=true) Member entity,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
//            entity.setCreateBy( null == entity.getCreateBy() ? "System" : entity.getCreateBy()   );

            String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
            entity.setCreateBy( currentUser );

            entity.setPassword(bcryptEncoder.encode(entity.getPassword()));

            entity.setCreateDate(new Date());
            return getService().createEntity(entity);
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(DataException e) {
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(SQLGrammerConflictException e){
            //406
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }catch(UnSupportException e){
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
