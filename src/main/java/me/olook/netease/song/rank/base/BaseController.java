package me.olook.netease.song.rank.base;

import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-02-08 14:34
 */
public class BaseController<Biz extends BaseBiz,Entity> {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected Biz baseBiz;

    @ApiOperation(value = "新增")
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> add(@RequestBody @Valid Entity entity, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            return ResponseEntity.status(500).body(errorList.get(0).getDefaultMessage());
        }
        int result = baseBiz.insertSelective(entity);
        //唯一键重复
        if(result==-1){
            return ResponseEntity.status(500).body("不能重复添加");
        }
        return ResponseEntity.status(200).body("新增成功");
    }

    @ApiOperation(value = "通过id查询")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Entity> get(@PathVariable Object id){
        int intId  = Integer.parseInt(id.toString());
        Entity object = (Entity) baseBiz.selectById(intId);
        return ResponseEntity.status(200).body(object);
    }

    @ApiOperation(value = "通过id更新")
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> update(@PathVariable Object id,@RequestBody Entity entity){
        try {
            ReflectionUtils.setPrimaryKey(entity,id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("主键查询失败");
        }
        int num = baseBiz.updateSelectiveById(entity);
        if(num>0){
            return ResponseEntity.status(500).body("更新数目为0");
        }else{
            return ResponseEntity.status(200).body("更新成功");
        }
    }

    @ApiOperation(value = "通过id删除")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> remove(@PathVariable Object id){
        int intId  = Integer.parseInt(id.toString());
        int num = baseBiz.deleteById(intId);
        if(num>0){
            return ResponseEntity.status(500).body("删除数目为0");
        }else{
            return ResponseEntity.status(200).body("删除成功");
        }
    }

    @ApiOperation(value = "查询所有")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ResponseBody
    public List<Entity> all(){
        return baseBiz.selectListAll();
    }

    protected String getCurrentUserName(){
        String authorization = request.getHeader("Authorization");
        if(authorization==null){
            return null;
        }
        return new String(Base64Utils.decodeFromString(authorization));
    }
}