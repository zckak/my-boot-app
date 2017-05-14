package com.akhrszk.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akhrszk.entities.Item;
import com.akhrszk.entities.User;
import com.akhrszk.exceptions.FileTypeException;
import com.akhrszk.services.ItemService;
import com.akhrszk.services.UserService;

@Controller
@RequestMapping(value="users")
public class UsersController {

	@Autowired
	UserService userService;
	
	@Autowired
	ItemService itemService;

	@RequestMapping(value="signup", method=RequestMethod.GET)
	public ModelAndView signup(ModelAndView mav) {
		mav.setViewName("signup");
		return mav;
	}
	
	@RequestMapping(value="signup", method=RequestMethod.POST)
	public String create(
			RedirectAttributes redirectAttributes,
			HttpSession session,
			@RequestParam("username") String username,
			@RequestParam("password") String password) {
		List<String> msgs = new ArrayList<String>();
		if (!username.matches("[0-9a-zA-Z]{4,15}+$")) {
			msgs.add("ユーザー名は、4~15文字の半角英数字でご入力ください。");
		}
		if (userService.isExistsUsername(username)) {
			msgs.add("このユーザー名は、既に使われています。");
		}
		if (!password.matches("[0-9a-zA-Z]{4,}+$")) {
			msgs.add("パスワードは、4文字以上の半角英数字でご入力ください。");
		}
		if (msgs.size() > 0) {
			redirectAttributes.addFlashAttribute("msgs", msgs);
			return "redirect:/users/signup";
		}
		userService.create(username, password, username);
		User user = userService.findByUsername(username);
		session.setAttribute("userId", user.getId());
		return "redirect:/";
	}
	
	@RequestMapping(value="signin", method=RequestMethod.GET)
	public ModelAndView signin(ModelAndView mav) {
		mav.setViewName("signin");
		return mav;
	}
	
	@RequestMapping(value="signin", method=RequestMethod.POST)
	public String authenticate(
			RedirectAttributes redirectAttributes,
			HttpSession session,
			@RequestParam("username") String username,
			@RequestParam("password") String password) {
		List<String> msgs = new ArrayList<String>();
		User user = userService.authenticate(username, password);
		if (user == null) {
			msgs.add("認証に失敗しました。");
		}
		if (msgs.size() > 0) {
			redirectAttributes.addFlashAttribute("msgs", msgs);
			return "redirect:/users/signin";
		}
		session.setAttribute("userId", user.getId());
		return "redirect:/";
	}
	
	@RequestMapping(value="signout", method=RequestMethod.GET)
	public String signout(HttpSession session) {
		session.removeAttribute("userId");
		return "redirect:/";
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public ModelAndView profile(HttpSession session, @PathVariable int id, ModelAndView mav) {
		boolean isMyPage = false;
		if (session.getAttribute("userId") != null) {
			int userId = (int)session.getAttribute("userId");
			if(userId == id) {
				isMyPage = true;
			}
		}
		mav.addObject("isMyPage", isMyPage);
		User user = userService.findById(id);
		Iterable<Item> items = itemService.findByUser(user);
		mav.addObject("user", user);
		mav.addObject("items", items);
		mav.setViewName("profile");
		return mav;
	}
	
	@RequestMapping(value="edit", method=RequestMethod.GET)
	public ModelAndView edit(HttpSession session, ModelAndView mav) {
		if (session.getAttribute("userId") != null) {
			int userId = (int)session.getAttribute("userId");
			User user = userService.findById(userId);
			mav.addObject("user", user);
			mav.setViewName("profileEdit");
			return mav;
		}
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value="edit", method=RequestMethod.POST)
	public String update(
			RedirectAttributes redirectAttributes,
			HttpSession session,
			@RequestParam("username") String username,
			@RequestParam("name") String name,
			@RequestParam("icon") MultipartFile icon) {
		if(session.getAttribute("userId") == null) {
			return "redirect:/users/signin";
		}
		List<String> msgs = new ArrayList<String>();
		if (!username.matches("[0-9a-zA-Z]{4,15}+$")) {
			msgs.add("ユーザー名は、4~15文字の半角英数字でご入力ください。");
		}
		int userId = (int)session.getAttribute("userId");
		User user = userService.findById(userId);
		if (!user.getUsername().equals(username)) {
			if(userService.isExistsUsername(username)) {
				msgs.add("このユーザー名は既に使われています。");
			}
		}
		if (msgs.size() > 0) {
			redirectAttributes.addFlashAttribute("msgs", msgs);
			return "redirect:/users/edit";
		}
		try {
			userService.updateProfile(userId, username, name, icon);
		} catch(IOException e) {
			msgs.add("アイコン画像の送信に失敗しました。");
		} catch(FileTypeException e) {
			msgs.add("許可されていないファイル形式です。");
		}
		if(msgs.size() > 0) {
			redirectAttributes.addFlashAttribute("msgs", msgs);
			return "redirect:/users/edit";
		}
		return "redirect:/users/" + userId;
	}
}
