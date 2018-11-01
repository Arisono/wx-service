package com.arison.app.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.arison.app.core.Result;
import com.arison.app.core.ResultCode;
import com.arison.app.core.ResultGenerator;
import com.github.kevinsawicki.http.HttpRequest;



/**
 * 微信推送业务处理
 * @author Arison
 *
 */
@RestController
public class WeiXinPushController {
	
	private static final String APPID="wxbc1f8607137d3b8a";
	
	private static final String  AppSecret ="cadf13c4e21c2c122cb2341b341e5c22";
	
	
	/**
	 * 绑定手机号码与微信公众号OpenId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wxlogin")
	public ModelAndView wxlogin(HttpServletRequest request) {
		String code=request.getParameter("code");
		String phone =request.getParameter("state");
		
		HashMap<String, Object> params=new HashMap<>();
		params.put("appid", APPID);
		params.put("secret", AppSecret);
		params.put("code", code);
		params.put("grant_type", "authorization_code");
		HttpRequest response= HttpRequest.get("https://api.weixin.qq.com/sns/oauth2/access_token", params, true);
        String result=response.body();
		String openid=JSON.parseObject(result).getString("openid");
		
		HttpRequest hRequest=HttpRequest.get("https://mobile.ubtob.com/user/appWecharId")
				 .form("telephone", phone)
				 .form("openid", openid);
				String isBind= hRequest.body();
				HashMap<String, Object> parameters=new HashMap<>();
				parameters.put("openid", openid);
				parameters.put("phone", phone);
				parameters.put("isBind", isBind);
		if (JSON.parseObject(isBind).getString("result").equals("true")) {
			String resultParams="手机号码 "+phone+" 绑定成功!您的openid是"+openid;
			try {
				resultParams=URLEncoder.encode(resultParams, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return  new ModelAndView("redirect:bindResult/:"+resultParams);
		}else{
			String resultParams="手机号码 "+phone+" 绑定失败!您的openid是"+openid;
			try {
				resultParams=URLEncoder.encode(resultParams, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return  new ModelAndView("redirect:bindResult/:"+resultParams);
		}
	}
	
	@RequestMapping(value="/wxBind" ,produces = "application/json; charset=utf-8")
	public Result wxBind(HttpServletRequest request){
		String phone =request.getParameter("phone");
		String openid =request.getParameter("openid");
		if (StringUtils.isEmpty(phone)) {
			return ResultGenerator.genFailResult("缺少参数：phone");
		}
		if (StringUtils.isEmpty(openid)) {
			return ResultGenerator.genFailResult("缺少参数：openid");
		}
	    HttpRequest hRequest=HttpRequest.get("https://mobile.ubtob.com/user/appWecharId")
		 .form("telephone", phone)
		 .form("openid", openid);
		String isBind= hRequest.body();
		if (JSON.parseObject(isBind).getString("result").equals("true")) {
			return ResultGenerator.genSuccessResult("绑定成功！").setData(isBind);
		}else{
			return ResultGenerator.genFailResult("绑定失败！");
		}
	}
	
	
//	    "phone":"13510107574",
//	    "password":"a1111111",
//	    "master":"UAS",
//	    "nodeId":59180421,
//	    "baseUrl":"http://192.168.253.58:8080/ERP/ 

	@RequestMapping(value="/wxPush" ,produces = "application/json; charset=utf-8")
	public Result wxPush(HttpServletRequest request){
	    String phone=request.getParameter("phone");//必填
		if (StringUtils.isEmpty(phone)) {
			return ResultGenerator.genFailResult("缺少参数：phone")
					.setData("请查阅接口文档！")
					.setCode(ResultCode.INTERNAL_SERVER_ERROR);
		}
		HttpRequest eRequest=  HttpRequest.get("https://mobile.ubtob.com/user/appGetOpenid").form("telephone", phone);
		String hresult= eRequest.body();
		String openid=JSON.parseObject(hresult).getString("openid");
		if ("0".equals(openid)) {
			return ResultGenerator.genFailResult("该用户未进行微信公众登录绑定").setData(hresult);
		}
		String fieldMap=request.getParameter("fieldMap");
		String url=request.getParameter("url");
		String urlParam=request.getParameter("urlParam");//必填
		if (StringUtils.isEmpty(fieldMap)) {
			return ResultGenerator.genFailResult("缺少参数：fieldMap");
		}
		if (StringUtils.isEmpty(url)) {
			return ResultGenerator.genFailResult("缺少参数：url");
		}
		if(StringUtils.isEmpty(urlParam)){
			return ResultGenerator.genFailResult("缺少参数：urlParam");
		}
		
		String uasUrl=  JSON.parseObject(urlParam).getString("url");//"https://demo.usoftchina.com:9443/uas/";
		String uasPhone= JSON.parseObject(urlParam).getString("phone");//"13510107574";
		String uasPassword= JSON.parseObject(urlParam).getString("password");//"a1111111";
		String uasMaster=JSON.parseObject(urlParam).getString("master");//"UAS";
		Integer nodeId=JSON.parseObject(urlParam).getInteger("nodeId");//59490196;
		try {
			uasUrl=URLEncoder.encode(uasUrl, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String jsonParam="{\"phone\":\""+uasPhone+"\",\"password\":\""+uasPassword+"\",\"master\":\""+uasMaster+"\",\"nodeId\":"+nodeId+",\"baseUrl\":\""+uasUrl+"\"}";
		try {
			jsonParam=URLEncoder.encode(jsonParam, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url="https://www.akuiguoshu.com/wxService/approval/"+jsonParam;
		
//		String 	url="http://www.aliyunyh.com/480.html";
//		String fieldMap="{\"title\":\"UAS流程审批提醒\",\"time\":\"2018年10月16日星期二 14:56\",\"content\":\"陈虎的员工转正申请单等待您的审批！\"}";
		
		String title=JSON.parseObject(fieldMap).getString("title");
		String time=JSON.parseObject(fieldMap).getString("time");
		String content=JSON.parseObject(fieldMap).getString("content");
		String json="{\"touser\":\""+openid+"\","
				+ "\"template_id\":\"siCbcBD_czFdqQpgs0q-PTboFg-SjaUpDadPEqzdpJc\","
				+ "\"url\":\""+""+url+""+"\","
				+ "\"data\":{"
				+ "\"first\":{\"value\":\""+title+"\","
				+"\"color\":\"#173177\"},"
				+ "\"keyword1\":{\"value\":\""+content+"\",\"color\":\"#173177\"},"
				+ "\"keyword2\":{\"value\":\""+time+"\",\"color\":\"#173177\"},"
				+ "\"remark\":{\"value\":\"点击查看详情！！！\",\"color\":\"#173177\"}}}";
		String access_token=  getWxAcessToken();	
		System.out.println("wx token:"+access_token);
        String token=JSON.parseObject(access_token).getString("access_token");
        System.out.println("token:"+token);
        HttpRequest hRequest=  HttpRequest.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token)
        		.header("Content-Type", "application/json")
        		.send(json.getBytes());
        String result= hRequest.body();
   
        if(JSON.parseObject(result).getInteger("errcode")==0&&JSON.parseObject(result).getString("errmsg").equals("ok")){
        	return ResultGenerator.genSuccessResult("推送成功！").setData(result);
        }else{
        	return ResultGenerator.genFailResult("推送失败！access_token:"+access_token+" token:"+token).setData(result);        	
        }
	}
	
	
	public String getWxAcessToken(){
		HashMap<String, Object> params=new HashMap<>();
		params.put("appid", APPID);
		params.put("secret", AppSecret);
		params.put("grant_type", "client_credential");
		HttpRequest httpRequest=HttpRequest.get("https://api.weixin.qq.com/cgi-bin/token",params,false);
		String content=httpRequest.body();
		return content;
	}
	
	public static void testSendBody(){
		String json="{\"touser\":\""+"o8lZ9uGnn074M2wiP_5cWsZ3NL8s"+"\","
				+ "\"template_id\":\"oi4SokVV7Is0kZz5w8VJG1b3zrLWtApqftCN4iJ3Iyc\","
				+ "\"url\":\""+"http://www.aliyunyh.com/480.html"+"\","
				+ "\"data\":{"
				+ "\"first\":{\"value\":\"刘杰向您提交了请假条！\","
				+"\"color\":\"#173177\"},"
				+ "\"keyword1\":{\"value\":\"骑车去旅行\",\"color\":\"#173177\"},"
				+ "\"keyword2\":{\"value\":\"事假\",\"color\":\"#173177\"},"
				+ "\"keyword3\":{\"value\":\"2018年09月30日 12:00到18:00\",\"color\":\"#173177\"},"
				+ "\"keyword4\":{\"value\":\"半天\",\"color\":\"#173177\"},"
				+ "\"remark\":{\"value\":\"点击模板URL进入调转界面！！！\",\"color\":\"#173177\"}}}";
		    //http://192.168.253.200/postBodyByString
		    HttpRequest httpRequest = null;
			httpRequest = HttpRequest.post("http://192.168.253.200/postBodyByString")
					.header("Content-Type", "application/json")
					.send(json.getBytes());
		System.out.println(httpRequest.body());
	}
	
//	public static void main(String[] args) {
////		 HttpRequest jRequest=  HttpRequest.get("https://mobile.ubtob.com/user/appGetOpenid")
////				 .form("telephone", "13510107573");
////		 String jresult= jRequest.body();
////		 System.out.println(""+jresult);
////		 
////		 HttpRequest hRequest=HttpRequest.get("https://mobile.ubtob.com/user/appWecharId")
////				 .form("telephone", "13510107573")
////				 .form("openid", "o8lZ9uFG2FswQt_kPkBu2G_ac2eU");
////		 System.out.println(hRequest.body());
//		 
//		 HttpRequest https=HttpRequest.get("https://218.17.158.219:9443/uas/mobile/login.action");
//		//Accept all certificates
//		 https.trustAllCerts();
//		 //Accept all hostnames
//		 https.trustAllHosts();
//		 System.out.println(https.body());
//	}
//	
}
