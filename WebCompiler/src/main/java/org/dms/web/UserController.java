package org.dms.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dms.web.domain.UserVO;
import org.dms.web.service.UserService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String joinGet() {
		return "join";
	}

	@RequestMapping(value = "/join.do", method = RequestMethod.POST)
	public String joinPost(@ModelAttribute("user") UserVO uvo) throws Exception {
		userService.insertUser(uvo);
		return "redirect:/";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet() throws Exception {
		return "signin";
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String loginPost(@ModelAttribute("user") UserVO uvo, Model model, HttpServletRequest request,
			RedirectAttributes attr) throws Exception {
		UserVO user = userService.readUser(uvo);
		if (user != null && !user.getUser_id().equals("") && !user.getUser_passwd().equals("")) {
			request.getSession().setAttribute("user", user);
			return "redirect:/";
		} else {
			request.getSession().setAttribute("user", null);
			attr.addFlashAttribute("msg", false);
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/logout.do")
	public String logout(@ModelAttribute("user") UserVO uvo, HttpServletRequest request) throws Exception {
		request.getSession().removeAttribute("user");
		return "redirect:/";
	}

	@RequestMapping(value = "/checkDuplicating", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkDuplicating(String value) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		boolean isRight = userService.checkId(value);
		map.put("isRight", isRight);

		return map;
	}

}
