package com.shinhan.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.dto.DoranVO;
import com.shinhan.dto.ProVO;
import com.shinhan.model.DoranService;
import com.shinhan.model.MenuService;


@Controller
@RequestMapping("menu")
public class MenuController {

	@Autowired
	MenuService mService;

	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

	@GetMapping("menu.do")
	public String menu(Model model) {
		List<ProVO> plist = mService.selectAll();
		model.addAttribute("mlist", plist);
		return "menu/menu";
	}
	
	@GetMapping("searchPro.do")
	//@ResponseBody
	public String searchPro(Model model,@RequestParam Map<String, Object> map) throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		ProVO pro = new ProVO();

		String allerJson =  map.get("allerCheckList").toString();
		String[] allerList =  mapper.readValue(allerJson, new TypeReference<String[]>(){});
		for(int i=0;i<allerList.length;i++) {
			System.out.println("알레르기["+i+"] :"+ allerList[i]);	
		}
		if(allerList.length >0) {
			pro.setAllerCheckList(allerList);	
		}
		
		//System.out.println("검색어 :"+(String)map.get("pro_name"));
		//System.out.println("카테고리 :"+(String)map.get("ingre_no"));
		pro.setPro_name((String)map.get("pro_name"));
		pro.setIngre_no((String)map.get("ingre_no"));
		
		List<ProVO> slist = mService.selectSearchPro(pro);
		model.addAttribute("slist", slist);
		//System.out.println("최종 조회리스트 :"+slist.toString());
		
		return "menu/menu_ajax";
	}
	
	// 페이지 styling 용으로 미리 선언
	@GetMapping("menuSpecificReview.do")
	public String menuSpecificReview(Model model) {
		return "menu/menuSpecificReview";
	}
	
	
}
