package org.dms.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dms.web.domain.CategoryVO;
import org.dms.web.domain.CodeBoardVO;

import org.dms.web.domain.CodeVO;
import org.dms.web.domain.Criteria;
import org.dms.web.domain.PageMaker;

import org.dms.web.domain.UserVO;
import org.dms.web.service.CategoryService;
import org.dms.web.service.CodeBoardService;
import org.dms.web.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MyPageController {

	@Autowired(required = true)
	UserService userService;

	@Autowired(required = true)
	CodeBoardService codeBoardService;

	@Autowired(required = true)
	CategoryService categoryService;

	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public String getMyPage(Model model, HttpSession session, Criteria criteria) throws Exception {
		UserVO user = (UserVO) session.getAttribute("user");
		List<CodeVO> CodeList = userService.getCodeList(user.getUser_id());
		List<CodeBoardVO> CodeBoardList_ = codeBoardService.getCodeBoardList(user.getUser_id());
		List<CategoryVO> CVO = categoryService.readCategoryList();

		PageMaker pageMaker = new PageMaker();
		criteria.setPerPageNum(5);
		pageMaker.setCri(criteria);
		pageMaker.setTotalCount(CodeBoardList_.size());

		List<CodeBoardVO> CodeBoardList = codeBoardService.getCodeBoardList(user.getUser_id(), criteria);
		model.addAttribute("user", user);
		model.addAttribute("category", CVO);
		model.addAttribute("CodeList", CodeList);
		model.addAttribute("CodeBoardList", CodeBoardList);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("criteria", criteria);

		return "mypage";
	}

	@RequestMapping(value = "/mypage", method = RequestMethod.POST)
	public String updateUser(UserVO user, HttpSession session) throws Exception {
		userService.updateUser(user);

		UserVO newUser = userService.readUser(user.getUser_id());
		session.setAttribute("user", newUser);

		return "redirect:/mypage";
	}

	@RequestMapping(value = "/mypage/saveImage", method = RequestMethod.POST)
	public String saveImage(@RequestParam("user_id") String userId,
			@RequestParam("user_img") MultipartFile imgFile,
			HttpSession session) throws Exception {
		UserVO user = new UserVO();

		user.setUser_id(userId);
		user.setUser_img(imgFile.getBytes());

		userService.saveImage(user);

		UserVO newUser = userService.readUser(user.getUser_id());
		session.setAttribute("user", newUser);

		return "redirect:/mypage";
	}

	@RequestMapping(value = "/getByteImage/{user_id}")
	public ResponseEntity<byte[]> getByteImage(@PathVariable("user_id") String userId, HttpServletRequest request)
			throws Exception {
		UserVO user = userService.readUser(userId);
		byte[] ImageContent = user.getUser_img();

		if (ImageContent == null) {
			String path = request.getSession().getServletContext().getRealPath("/resources/images/user.png");
			BufferedImage originalImage = ImageIO.read(new File(path));
		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "png", baos);
			baos.flush();

			ImageContent = baos.toByteArray();

			baos.close();

		}

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		return new ResponseEntity<byte[]>(ImageContent, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/mypage/delete/{user_id}", method = RequestMethod.POST)
	public String withdrawUser(@PathVariable("user_id") String userId, HttpSession session) throws Exception {
		userService.deleteUser(userId);
		session.invalidate();

		return "redirect:/mypage";
	}

	@RequestMapping(value = "/modal", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> openModal(
			int index,
			int page,
			int problem_level,
			String category_name,
			String search_category,
			String search,
			HttpSession session,
			Criteria criteria) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserVO user = (UserVO) session.getAttribute("user");
		criteria.setPerPageNum(5);
		criteria.setPage(page);
		List<CodeBoardVO> CodeBoardList = null;
		if (search != "") {
			CodeBoardList = codeBoardService.getCodeBoardListBySearch(user.getUser_id(), search_category, search,
					criteria);
		}

		else if (problem_level == 0 && category_name.equals("unselected")) {
			CodeBoardList = codeBoardService.getCodeBoardList(user.getUser_id(), criteria);
		}

		else {
			CodeBoardList = codeBoardService.getCodeBoardList_filter(user.getUser_id(), criteria, problem_level,
					category_name);
		}

		map.put("codeBoard", CodeBoardList.get(index));

		return map;
	}

	@RequestMapping(value = "/codeBoardPaging", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> codeBoardPaging(
			HttpSession session,
			Locale locale,
			Model model,
			Criteria criteria,
			int page,
			int problem_level,
			String category_name,
			String search_category,
			String search) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		UserVO user = (UserVO) session.getAttribute("user");
		PageMaker pageMaker = new PageMaker();


		if (search != "") {
			List<CodeBoardVO> CodeBoardList_ = codeBoardService.getCodeBoardListBySearch(user.getUser_id(),
					search_category, search);
			criteria.setPerPageNum(5);
			criteria.setPage(page);
			pageMaker.setCri(criteria);
			pageMaker.setTotalCount(CodeBoardList_.size());

			List<CodeBoardVO> CodeBoardList = codeBoardService.getCodeBoardListBySearch(user.getUser_id(),
					search_category, search, criteria);

			map.put("pageMaker", pageMaker);
			map.put("list", CodeBoardList);

			return map;
		}

		if (problem_level == 0 && category_name.equals("unselected")) {
			criteria.setPerPageNum(5);
			criteria.setPage(page);
			pageMaker.setCri(criteria);

			List<CodeBoardVO> CodeBoardList_ = codeBoardService.getCodeBoardList(user.getUser_id());

			pageMaker.setTotalCount(CodeBoardList_.size());

			List<CodeBoardVO> CodeBoardList = codeBoardService.getCodeBoardList(user.getUser_id(), criteria);

			map.put("pageMaker", pageMaker);
			map.put("list", CodeBoardList);

			return map;
		} else {
			criteria.setPerPageNum(5);
			criteria.setPage(page);
			pageMaker.setCri(criteria);

			List<CodeBoardVO> CodeBoardList = codeBoardService.getCodeBoardList_filter(user.getUser_id(), criteria,
					problem_level, category_name);
			pageMaker.setTotalCount(CodeBoardList.size());

			map.put("pageMaker", pageMaker);
			map.put("list", CodeBoardList);

			return map;
		}

	}

}