package org.dms.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dms.web.domain.CommentsVO;

import org.dms.web.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class RestCommentController {

	@Autowired(required = true)
	private CommentsService commentService;


	@RequestMapping(value = "/{board_id}/comment.read", method = RequestMethod.GET)
	public Map<String, Object> readCommentList(@PathVariable("board_id") int board_id) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		List<CommentsVO> cvo = commentService.readCommentList(board_id);
		int comment_count = commentService.count(board_id);

		map.put("comment", cvo);
		map.put("comment_count", comment_count);
		return map;
	}

	@RequestMapping(value = "/comment.insert", method = RequestMethod.POST)
	public String insertComments(@RequestBody CommentsVO comment) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);
		comment.setComments_upload(ts);
		comment.setComments_content(comment.getComments_content().replace("\r\n", "<br>"));
		commentService.insertComment(comment);
		return "success";
	}

	@RequestMapping(value = "/{board_id}/comment.delete/{comments_id}", method = RequestMethod.DELETE)
	public String deleteComments(@PathVariable("board_id") int board_id, @PathVariable("comments_id") int comments_id)
			throws Exception {
		commentService.deleteComment(comments_id);
		return "success";

	}

}
