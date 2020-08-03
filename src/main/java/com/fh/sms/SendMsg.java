package com.fh.sms;

//验证码

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.util.MessageVerifyUtils;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("send")
public class SendMsg {


    //吧验证码放入缓存中
    @Ignore
    @RequestMapping("sendMsg")
    public ServerResponse  sendMsg(String phone){
        //获取验证码
        String newcode = MessageVerifyUtils.getNewcode();
        try {

            //获取对应手机号和验证码
            SendSmsResponse sendSmsResponse = MessageVerifyUtils.sendSms(phone, newcode);
            //如果手机号验证码都不为空   并且返回的ok与code相等时
            if (sendSmsResponse !=null && "OK".equals(sendSmsResponse.getCode())){
                //吧code放入redis中
                RedisUtil.setex(phone,newcode,SystemConstant.REDIS_EXPIRY_TIME);
                return ServerResponse.success();
            }

        } catch (ClientException e) {
            e.printStackTrace();
            return ServerResponse.error(e.getErrMsg());
        }
        return ServerResponse.success();
    }
}
