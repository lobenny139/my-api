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
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Getter
@Setter
@RestController
@Api(tags = "Member 操作", value = "Member 操作 API 說明")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired(required=true)
    @Qualifier("memberService")
    IMemberService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value="取出一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
            @ApiResponse(code=401, message="無法授權"),
            @ApiResponse(code=404, message="找不到資料")
//          @ApiResponse(code=409, message="資料衝突")
    })
    @GetMapping("/member/{account}")
    public Member get(
                        @ApiParam(required=true, value="請傳入物件(member)的 account")
                        @PathVariable String account) throws Exception {
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

    @ApiOperation(value="啟用一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
            @ApiResponse(code=401, message="無法授權"),
            @ApiResponse(code=404, message="找不到資料")
//          @ApiResponse(code=409, message="資料衝突")
    })
    @PutMapping("/member/{account}/activate")
    public Member activate(
                            @ApiParam(required=true, value="請傳入物件(member)的 account")
                            @PathVariable String account,
                            HttpServletRequest request) throws Exception {
        String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
        request.getSession().setAttribute("currentUser", currentUser);
        try{
            getService().updateEntityStatusByAccount(account, 1);
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

    @ApiOperation(value="停用一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
            @ApiResponse(code=401, message="無法授權"),
            @ApiResponse(code=404, message="找不到資料")
//          @ApiResponse(code=409, message="資料衝突")
    })
    @PutMapping("/member/{account}/deactivate")
    public void deactivate(
                            @ApiParam(required=true, value="請傳入物件(member)的 account")
                            @PathVariable String account,
                            HttpServletRequest request) throws Exception {
        String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
        request.getSession().setAttribute("currentUser", currentUser);

        try{
            getService().updateEntityStatusByAccount(account, 0);
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

    @ApiOperation(value="刪除一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
            @ApiResponse(code=401, message="無法授權")
//            @ApiResponse(code=404, message="找不到資料")
//          @ApiResponse(code=409, message="資料衝突")
    })
    @DeleteMapping("/member/{account}")
    public void delete(
                            @ApiParam(required=true, value="請傳入物件(member)的 account")
                            @PathVariable String account) throws Exception {
        try{
            getService().deleteEntityByAccount(account);
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
            @ApiResponse(code=401, message="無法授權"),
//          @ApiResponse(code=404, message="找不到資料"),
//          @ApiResponse(code=409, message="資料衝突")

    })
    @GetMapping("/members")
    public Iterable<Member> getAll() throws Exception {
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

    @ApiOperation(value="新增一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
            @ApiResponse(code=401, message="無法授權"),
//          @ApiResponse(code=404, message="找不到資料"),
            @ApiResponse(code=409, message="資料衝突")
    })
    @PostMapping("/member")
    public Member create(
                            @ApiParam(required=true, value="請傳入物件(Member)的 JSON 格式")
                            @RequestBody(required=true) Member entity,
                            HttpServletRequest request) throws Exception {
        String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
        request.getSession().setAttribute("currentUser", currentUser);
        try {
//            String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
//            entity.setCreateBy( currentUser );
//            entity.setPassword(bcryptEncoder.encode(entity.getPassword()));
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


    @ApiOperation(value="更新一物件(Member)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="請求成功"),
            @ApiResponse(code=400, message="請求失敗"),
            @ApiResponse(code=401, message="無法授權"),
            @ApiResponse(code=404, message="找不到資料"),
            @ApiResponse(code=409, message="資料衝突")
    })
    @PutMapping("member/{account}")
    public Member update(
                            @ApiParam(required=true, value="請傳入物件(member)的account")
                            @PathVariable String account,
                            @ApiParam(required=true, value="請傳入物件(member)的 JSON 格式")
                            @RequestBody(required=true) Member entity,
                            HttpServletRequest request) {
        String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
        request.getSession().setAttribute("currentUser", currentUser);
        try{
//            String currentUser = getJwtTokenUtil().getUsernameFromToken( request.getHeader("Authorization") );
//            entity.setUpdateBy(currentUser);
            return getService().updateEntityByAccount(account, entity);
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
