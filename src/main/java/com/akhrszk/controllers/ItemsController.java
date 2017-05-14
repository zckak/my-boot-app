package com.akhrszk.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akhrszk.entities.User;
import com.akhrszk.exceptions.FileTypeException;
import com.akhrszk.services.ItemService;
import com.akhrszk.services.UserService;

@Controller
@RequestMapping(value="items")
public class ItemsController {

	@Autowired
	UserService userService;
	
	@Autowired
	ItemService itemService;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(
			RedirectAttributes redirectAttributes,
			HttpSession session,
			@RequestParam("comment") String comment,
			@RequestParam("file") MultipartFile file) {
		if(session.getAttribute("userId") == null) {
			return "redirect:/users/signin";
		}
		if(comment.isEmpty() && file.isEmpty()) {
			return "redirect:/";
		}
		int userId = (int)session.getAttribute("userId");
		User user = userService.findById(userId);
		List<String> msgs = new ArrayList<String>();
		try {
			itemService.create(comment, file, user);
		} catch(IOException e) {
			msgs.add("画像の送信に失敗しました。");
		} catch(FileTypeException e) {
			msgs.add("許可されていないファイル形式です。");
		}
		if(msgs.size() > 0) {
			redirectAttributes.addFlashAttribute("msgs", msgs);
			return "redirect:/";
		}
		return "redirect:/";
	}

}
