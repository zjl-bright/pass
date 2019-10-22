package com.zjl.paas.common.model;

import com.google.common.base.Strings;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.cool.common.enums.ResponseCode;

import java.io.Serializable;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-17
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class Response<T> implements Serializable {

  private static final long serialVersionUID = -750644833749014619L;

  //状态码
  private Integer code;

  //消息
  private String message;

  //返回结果集
  private T result;

  //是否成功
  private boolean success;

  public Response() {}

  private Response(ResponseCode responseCode) {
    this.code = responseCode.code();
    this.message = responseCode.message();
  }

  public boolean isSuccess() {
    return this.success;
  }

  public static JsonObject ok() {
    Response resp = new Response(ResponseCode.SUCCESS).setSuccess(true);
    return JsonObject.mapFrom(resp);
  }

  public static <T> JsonObject ok(T data) {
    Response resp = new Response(ResponseCode.SUCCESS).setResult(data).setSuccess(true);
    return JsonObject.mapFrom(resp);
  }

  public static JsonObject fail401(String message) {
    return code(ResponseCode.LOST, message);
  }

  public static JsonObject fail(String message) {
    return code(ResponseCode.TIP_ERROR, message);
  }

  public static JsonObject fail500() {
    return code(ResponseCode.UNKNOWN_ERROR, null);
  }

  public static JsonObject fail500(String message) {
    return code(ResponseCode.UNKNOWN_ERROR, message);
  }

  private static JsonObject code(ResponseCode responseCode, String message){
    Response resp = new Response(responseCode).setSuccess(false);
    if(Strings.isNullOrEmpty(message)){
      return JsonObject.mapFrom(resp);
    }else{
      return JsonObject.mapFrom(resp.setMessage(message));
    }
  }
}
