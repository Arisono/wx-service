package com.arison.app.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public  ModelAndView layout(HttpServletRequest request) {
		  ModelAndView  model = new ModelAndView("redirect:bindPhone/:13212321312");//默认forward，可以不用写
		  return model;  
	}
	
	
}