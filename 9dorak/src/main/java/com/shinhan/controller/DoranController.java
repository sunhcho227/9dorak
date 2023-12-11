package com.shinhan.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shinhan.dto.CommentVO;
import com.shinhan.dto.DCommentVO;
import com.shinhan.dto.DoranVO;
import com.shinhan.dto.DoranlikeVO;
import com.shinhan.dto.MemVO;
import com.shinhan.model.DoranService;

@Controller
@RequestMapping("doran")
public class DoranController {

	@Autowired
	DoranService dService;

	private static final Logger logger = LoggerFactory.getLogger(DoranController.class);

	@GetMapping("doran.do")
	public String doran(@RequestParam(name = "orderBy", defaultValue = "latest") String orderBy, Model model,
			HttpSession session) {
		List<DoranVO> dlist;
		System.out.println(orderBy);
		dlist = dService.selectAll();
		model.addAttribute("dlist", dlist);

		MemVO memId = (MemVO) session.getAttribute("loginmem");

		if (memId == null) {
			System.out.println("아이디가 없습니다");
			model.addAttribute("memPoint", "로그인하고 포인트를 얻으세요");
		} else {
			System.out.println(memId);
			System.out.println("백에서 받은 아이디:" + memId.getMem_id());
			System.out.println("백에서 받은 포인트:" + memId.getMem_point());
			System.out.println("그 사람의 레벨 : " + memId.getMem_grade());

		}
		return "doran/doran";
	}

	@GetMapping("/doranFilter.do")
	public String doran2(@RequestParam(name = "orderBy", defaultValue = "latest") String orderBy, Model model) {
		List<DoranVO> dlist;
		if ("views".equals(orderBy)) {
			dlist = dService.selectAllByView();
		} else if ("latest".equals(orderBy)) {
			dlist = dService.selectAll();
		} else {
			dlist = dService.selectAllByDlike();
		}
		model.addAttribute("dlist", dlist);
		return "doran/doran_ajax";
	}

	// 도란도란을 위한 데이터인지, 내가 쓴 글인지 확인
	@GetMapping("/doranFor.do")
	public String doranFor(@RequestParam(name = "dataFor", defaultValue = "doran") String dataFor, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		List<DoranVO> dlist;
		System.out.println("DateFor:" + dataFor);
		if (dataFor.equals("myDoran")) {
			MemVO memId = (MemVO) session.getAttribute("loginmem");
			dlist = dService.selectAllForMe(memId.getMem_id());
		} else {
			dlist = dService.selectAll();
		}
		if (dlist.size() == 0) {
			System.out.println("해당하는 데이터가 없습니다 ~ 글을 써 이 사람아");
		}

		model.addAttribute("dlist", dlist);
		return "doran/doran_ajax";
	}

	@GetMapping("doranUpload.do")
	public String doranUpload(Model model) {
		System.out.println("업로드 페이지로 이동");
		return "doran/doranUpload";
	}

	// 내가 쓴 글일 때, 조회 메소드
	@GetMapping("/doranFilterForMe.do")
	public String doranFilterForMe(@RequestParam(name = "orderBy", defaultValue = "latest") String orderBy, Model model,
			HttpSession session) {
		List<DoranVO> dlist;
		MemVO memVO = (MemVO) session.getAttribute("loginmem");
		String memId = memVO.getMem_id();
		
		if ("views".equals(orderBy)) {
			dlist = dService.selectAllByViewForMe(memId);
		} else if ("latest".equals(orderBy)) {
			dlist = dService.selectAllForMe(memId);
		} else {
			dlist = dService.selectAllByDlikeForMe(memId);
		}
		model.addAttribute("dlist", dlist);
		return "doran/doran_ajax";
	}
	
	@PostMapping("quickUpload.do")
	public String quickUpload(@RequestBody Map<String, String> requestData, Model model, HttpSession session) {
	    String title = requestData.get("title");
	    String content = requestData.get("content");

	    int dsize = dService.selectAll().size();
	    MemVO memVO = (MemVO) session.getAttribute("loginmem");
	    DoranVO doran = new DoranVO();
	    String memId = memVO.getMem_id();
	    doran.setDoran_no(dsize + 1);
	    doran.setDoran_title(title);
	    doran.setDoran_cont(content);
	    doran.setDoran_view(0);
	    doran.setMem_id(memId);
	    doran.setDoran_image(null);
	    dService.insertDoran(doran);
	    return "redirect:/doran/doran.do";
	}


	@PostMapping("doranUpload.do")
	public String handleDoranUpload(DoranVO doran, @RequestParam MultipartFile singleFile, HttpServletRequest request,
			HttpSession session) {

		int dsize = dService.selectAll().size();
		String path = request.getSession().getServletContext().getRealPath("resources");
		System.out.println("path : " + path);
//		String root = path + "\\uploadFiles" ;
		String root = path + "\\upload";

		File file = new File(root);

		// 만약 uploadFiles 폴더가 없으면 생성해라 라는뜻
		if (!file.exists())
			file.mkdirs();

		// 업로드할 폴더 설정
		String originFileName = singleFile.getOriginalFilename();
		String ext = "";
		int lastIndex = originFileName.lastIndexOf("."); // 확장자
		if (lastIndex != -1) { // 확장자가 없다면
			ext = originFileName.substring(lastIndex);
		}

		// ext를 이용한 나머지 로직 수행
		String ranFileName = UUID.randomUUID().toString() + ext; // 랜덤값으로 파일의 이름을 준다
		File changeFile = new File(root + "\\" + ranFileName);

		// 파일업로드
		try {
			singleFile.transferTo(changeFile);
			System.out.println("파일 업로드 성공");
		} catch (IllegalStateException | IOException e) {
			System.out.println("파일 업로드 실패");
			e.printStackTrace();
		}

		MemVO memVO = (MemVO) session.getAttribute("loginmem");
		String memId = memVO.getMem_id();
		doran.setDoran_no(dsize + 1);
		doran.setDoran_title(doran.getDoran_title());
		doran.setDoran_cont(doran.getDoran_cont());
		doran.setDoran_view(0);
		doran.setMem_id(memId);
		doran.setDoran_image(ranFileName);
		dService.insertDoran(doran);
		return "redirect:/doran/doran.do";
	}

	@GetMapping("doranFeedDetail.do")
	public String doranFeedDetail(Model model) {

		// 나중에 클릭하면 넘어온 파라미터 게시물 넘버로 바꿔줘
		DoranVO doran = dService.selectDoran_no(14);
		// 현재 1번 게시글에 대한 댓글 정보 가져오기 test
		List<CommentVO> comments = dService.selectAllCommentAbout(1);
		model.addAttribute("doran", doran);
		model.addAttribute("comments", comments);
		logger.info(comments.toString());
		return "doran/doranFeedDetail";
	}

	@RequestMapping(value = "doranLikeUpdate.do", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doranLikeUpdate(@RequestParam int doran_no, String mem_id) {
		System.out.println("doranLikeUpdate");
		System.out.println(doran_no + " " + mem_id);
		DoranlikeVO dlike = new DoranlikeVO();
		dlike.setDoran_no(doran_no);
		dlike.setMem_id(mem_id);
		System.out.println(dlike);
		// 이미 좋아요 된 것이면 취소하고 없으면 insert 한다
		if (dService.selectDoranLikeBy(dlike) > 0) {
			System.out.println("좋아요 삭제 ~");
			dService.deleteDoranLike(dlike);
			return "삭제";
		} else {
			System.out.println("좋아요 추가!");
			dService.insertDoranLike(dlike);
			return "추가";
		}
	}
	
	@GetMapping("doranSearch.do")
	public String selectSearchDoran(@RequestParam String keyword, Model model) {
		List<DoranVO> dlist = dService.selectSearchDoran(keyword);
		model.addAttribute("dlist", dlist);
		return "doran/doran_ajax";
	}
	
}
