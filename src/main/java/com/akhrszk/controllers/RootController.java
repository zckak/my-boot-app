package com.akhrszk.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.akhrszk.entities.Item;
import com.akhrszk.entities.User;
import com.akhrszk.services.ItemService;
import com.akhrszk.services.UserService;

@Controller
public class RootController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ItemService itemService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(HttpSession session, ModelAndView mav) {
		User user = null;
		if(session.getAttribute("userId") != null) {
			int userId = (int)session.getAttribute("userId");
			user = userService.findById(userId);
		}
		mav.addObject("user", user);
		Iterable<Item> items = itemService.findAll();
		mav.addObject("items", items);
		mav.setViewName("index");
		return mav;
	}
}
