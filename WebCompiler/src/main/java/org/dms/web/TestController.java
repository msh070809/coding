package org.dms.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dms.web.domain.CategoryVO;
import org.dms.web.domain.ChatVO;
import org.dms.web.domain.CodeBoardVO;
import org.dms.web.domain.Criteria;
import org.dms.web.domain.PageMaker;
import org.dms.web.domain.ProblemVO;
import org.dms.web.domain.UserVO;
import org.dms.web.service.CategoryService;
import org.dms.web.service.ChatService;
import org.dms.web.service.CodeBoardService;
import org.dms.web.service.ProblemService;
import org.dms.web.service.TestcaseService;
import org.dms.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @Autowired(required = true)
    UserService userService;

    @Autowired(required = true)
    CategoryService categoryService;

    @Autowired(required = true)
    ProblemService problemService;

    @Autowired(required = true)
    TestcaseService testcaseService;

    @Autowired(required = true)
    CodeBoardService codeBoardService;

    @Autowired(required = true)
    private ChatService chatService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String problemListGet(Locale locale, Model model, Criteria cri) throws Exception {
        List<CategoryVO> cvo = categoryService.readCategoryList();
        List<ProblemVO> pvo = problemService.readProblemList(cri);

        PageMaker pageMaker = new PageMaker();
        cri.setPerPageNum(8);
        pageMaker.setCri(cri);
        pageMaker.setTotalCount(problemService.ProblemCount());

        model.addAttribute("category", cvo);

        model.addAttribute("problem", pvo);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("cri", cri);
        return "problemList";
    }

    @RequestMapping(value = "/chattest", method = RequestMethod.GET)
    public String chattest(Locale locale, Model model, HttpSession session) throws Exception {
        UserVO user = (UserVO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "chat_test";
    }

    @RequestMapping(value = "/chat/read.do", method = RequestMethod.GET)
    @ResponseBody
    public List<ChatVO> getChatList(Locale locale, Model model, int problem_id) throws Exception {

        List<ChatVO> cvo = chatService.readChatList(problem_id);

        for (ChatVO tmp : cvo) {
            tmp.setChat_content(tmp.getChat_content().replace("\r\n", "<br>"));
        }
        return cvo;
    }

    @RequestMapping(value = "/requestObject")
    @ResponseBody
    public UserVO simpleWithObject(HttpServletRequest request, @RequestParam String problem_level,
            @RequestParam String category_name) throws Exception {

        return userService.readUser("1");
    }

    @ResponseBody
    @RequestMapping(value = "idCheck.do", produces = "application/String; charset=utf-8")
    public String idCheck(String id, HttpSession session) {

        String str = "";
        return str;
    }

    @RequestMapping(value = "problemList.do")
    @ResponseBody
    public Map<String, Object> problemList(int problem_level, String category_name, HttpSession session, Locale locale)
            throws Exception {

        if (problem_level == 0 && category_name.equals("unselected")) {
            Map<String, Object> map = new HashMap<String, Object>();
            List<ProblemVO> vo = problemService.readProblemList();

            map.put("list", vo);
            return map;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            List<ProblemVO> vo = problemService.readProblemList(problem_level, category_name);

            map.put("list", vo);
            return map;
        }

    }

    @ResponseBody
    @RequestMapping(value = "updateUser.do", method = { RequestMethod.GET, RequestMethod.POST })

    public Map<String, Object> updateUser(@RequestBody Map<String, Object> map) {

        Map<String, Object> rmap = new HashMap<String, Object>();
        rmap.put("name", "占쎌뵬筌욑옙筌랃옙");
        rmap.put("age", "24");

        return rmap;
    }

    @RequestMapping(value = "/monaco")
    public String getMonaco() {
        return "monaco_page";
    }

    @RequestMapping(value = "/code_ref")
    public String getCodeRef() {
        return "code_ref";
    }

    @RequestMapping(value = "/problem_ref")
    public String getProblemRef() {
        return "problem_ref";
    }

    @RequestMapping(value = "/signin")
    public String getSignin() {
        return "signin";
    }

    @RequestMapping(value = "/codeBoard")
    public String getCodeBoard(HttpSession session, Model model, Criteria criteria) throws Exception {
        UserVO user = (UserVO) session.getAttribute("user");
        List<CodeBoardVO> codeBoardList = codeBoardService.getCodeBoardList(user.getUser_id(), criteria);

        model.addAttribute("codeBoardList", codeBoardList);
        return "test_view";
    }

}