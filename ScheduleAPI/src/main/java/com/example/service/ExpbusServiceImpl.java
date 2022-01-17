package com.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.dao.CityInfoDao;
import com.example.utill.ApiUrlBuilder;
import com.example.vo.CityInfoVo;
import com.example.vo.expBusVo.ExpBusScInfoVo;
import com.example.vo.expBusVo.ExpBusScVo;
import com.example.vo.expBusVo.ExpBusSetInfoVo;
import com.example.vo.expBusVo.ExpBusTmlInfoVo;

@Service("expbusService")
public class ExpbusServiceImpl implements ExpbusService {

	@Value("${serviceKey}")
	private String key;
	
	@Autowired
	CityInfoDao cityInfoDao;
	
	//도시코드 조회 api URL
	private String cityInfoURL = "http://openapi.tago.go.kr/openapi/service/ExpBusInfoService/getCtyCodeList";
	//고속버스 터미널 조회 api URL
	private String ExpBusTmlInfoUrl = "http://openapi.tago.go.kr/openapi/service/ExpBusInfoService/getExpBusTrminlList";
	//고속버스 스케줄 조회 api URL
	private String getExpBusScInfoUrl = "http://openapi.tago.go.kr/openapi/service/ExpBusInfoService/getStrtpntAlocFndExpbusInfo";
	
	//고속버스 정보 api에서 필요한 도시이름과 도시코드를 가져온다.
	@Override
	public ArrayList<CityInfoVo> getCityCode() throws Exception {
		
		//리턴할 객체
		ArrayList<CityInfoVo> cityInfoList = new ArrayList<CityInfoVo>();
		 
		//구성 정보들
		//apiUrl
		String setUrl = this.cityInfoURL;
		//서비스키
		String serviceKey = this.key;
		//요청방식
		String setRequestMethod = "GET";
		//요청 형식
		String contentType = "application/json";
		//요청한 정보 응답객체
		InputStream is = null;
		
		try {
			
			ApiUrlBuilder aub = new ApiUrlBuilder();
			
			is = aub.ApiUrlBuild(setUrl, serviceKey, setRequestMethod, contentType);
			
			
		} catch (Exception e) {
			throw e;
		}// end
		
		//xml 파싱	
		try {
			
			//DocumentBuilderFactory 객체 생성
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        //DocumentBuilderFactory객체로 DocumentBuilder 객체생성 (팩토리패턴)
	        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	        
	        if(is != null) {	
	        	Document doc = documentBuilder.parse(is);
		        
		        Element element = doc.getDocumentElement();
	  
		        NodeList NodecityCodelist = element.getElementsByTagName("cityCode");
		        NodeList NodecityNamelist = element.getElementsByTagName("cityName");
		        
		       
		            
		        for (int i = 0; i < NodecityCodelist.getLength(); i++) {
		        	// i번째 item 태그를 node에 저장s
		            Node NodecityCode = NodecityCodelist.item(i);
		            Node NodecityName = NodecityNamelist.item(i);
		            
		         
		            String cityCode = NodecityCode.getFirstChild().getNodeValue();
		            String cityName = NodecityName.getFirstChild().getNodeValue();

		            //리턴할 List에 add           	             
		            cityInfoList.add(new CityInfoVo(cityCode, cityName));
		            
		        }//for end
	        	    	
	        }else {
	        	throw new IOException("응답이 없습니다.");
	        }//if end
	        
			
			
		} catch (Exception e) {
			throw e;
		}// end
		
		System.out.println("size" + cityInfoList.size());
		
		return cityInfoList;
		
	}//getCityCode() end
	
	
	
	
	//전국에 있는 고속버스 터미널id와 터미널 이름을 가져온다.
	@Override
	public ArrayList<ExpBusTmlInfoVo> getExpBusTmlInfo() throws Exception {
		
		//리턴할 객체
		ArrayList<ExpBusTmlInfoVo> ExpBusTmlInfoList = new ArrayList<ExpBusTmlInfoVo>();
				
		//api url
		String setUrl = this.ExpBusTmlInfoUrl;
		//서비스키
		String serviceKey = this.key;
		//요청방식
		String setRequestMethod = "GET";
		//요청 형식
		String contentType = "application/json";
		//요청한 정보 응답객체
		InputStream is = null;
		//옵션들
		HashMap<String, String> appendOps = new HashMap<String, String>();
		//한 페이지에서 보여줄 갯수
		appendOps.put("numOfRows", "99999");
		//보여줄 페이지
		appendOps.put("pageNo", "1");
		
		//api에서 정보들 가져오기
		
			ApiUrlBuilder aub = new ApiUrlBuilder();
			
			is = aub.ApiUrlBuild(setUrl, serviceKey, setRequestMethod, contentType, appendOps);
	
			//xml 파싱		
			//DocumentBuilderFactory 객체 생성
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        //DocumentBuilderFactory객체로 DocumentBuilder 객체생성 (팩토리패턴)
	        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	        
	        if(is != null) { 
	        	Document doc = documentBuilder.parse(is);
	        	
	        	Element element = doc.getDocumentElement();
	        	  
		        NodeList NodeterminalIdList = element.getElementsByTagName("terminalId");
		        NodeList NodeterminalNmList = element.getElementsByTagName("terminalNm");
		        
		        for(int i = 0; i < NodeterminalIdList.getLength(); i++) {
		        
		    	 Node NodeterminalId = NodeterminalIdList.item(i);
		         Node NodeterminalNm = NodeterminalNmList.item(i);
		         //api에서 가져온 터미널 id
		         String terminalId = NodeterminalId.getFirstChild().getNodeValue();
		         //api에서 가져온 터미널 이름
	             String terminalNm = NodeterminalNm.getFirstChild().getNodeValue();
	             //리턴할 list에 add
	             ExpBusTmlInfoList.add(new ExpBusTmlInfoVo(terminalId, terminalNm));
	             
		        }//for end
	        	
	        	
	        }else {
	        	throw new IOException("응답이 없습니다.");
	        }//if end
			
		
		return ExpBusTmlInfoList;
			
	}//getExpBusTmlInfo() end



	//DB에서 전국 고속버스 터미널 정보 리스트 조회
	@Override
	public ArrayList<ExpBusTmlInfoVo> allExpBusList() {
		
		//리턴할객체
		ArrayList<ExpBusTmlInfoVo> cityExpBusTmlInfoVo = new ArrayList<ExpBusTmlInfoVo>();
		
		ArrayList<ExpBusTmlInfoVo> allEBT =  (ArrayList)this.cityInfoDao.selectExpBusTmlInfo();
		
		//city정보 주입
		for(ExpBusTmlInfoVo EBT : allEBT) {
			
			String subTmId = EBT.getTmId().substring(4, 5);	//터미널 ID의 숫자 첫글자로 구분

			if(subTmId.equals("0")) { EBT.setCity("서울"); }
			if(subTmId.equals("1")) { EBT.setCity("인천/경기"); }
			if(subTmId.equals("2")) { EBT.setCity("강원"); }
			if(subTmId.equals("3")) { EBT.setCity("대전/충남"); }
			if(subTmId.equals("4")) { EBT.setCity("충북"); }
			if(subTmId.equals("5")) { EBT.setCity("광주/전남"); }
			if(subTmId.equals("6")) { EBT.setCity("전북"); }
			if(subTmId.equals("7")) { EBT.setCity("부산/경남"); }
			if(subTmId.equals("8")) { EBT.setCity("대구/경북"); }
			
			
			cityExpBusTmlInfoVo.add(EBT);
					
		}//for end

		return cityExpBusTmlInfoVo;
		
	}//allExpBusList() end
	
	//api에서 버스 스케줄정보 가져오기
	@Override
	public ExpBusScVo getExpBusSc(ExpBusSetInfoVo settings) throws Exception {
		System.out.println(settings.toString());
		//리턴할 객체
		ExpBusScVo expBusSc = new ExpBusScVo();	
		//스케줄 정보가 담긴 List
		ArrayList<ExpBusScInfoVo> expBusScInfo = new ArrayList<ExpBusScInfoVo>();
		//해당 옵션으로 검색했을 때 나오는 전체 행의 갯수
		String totalCnt;
		//페이지번호
		String pageNo;
		//한줄에 보여줄 레코드의 갯수
		String numofRows;
		
		//api 구성정보들
		//url
		String setUrl = this.getExpBusScInfoUrl;
		//서비스키
		String serviceKey = this.key;
		//요청방식
		String setRequestMethod = "GET";
		//요청 형식
		String contentType = "application/json";
		//요청한 정보 응답객체
		InputStream is = null;
		//옵션들
		HashMap<String, String> appendOps = new HashMap<String, String>();
		//한페이지 결과수
		appendOps.put("numOfRows", "10");
		//페이지 번호
		appendOps.put("pageNo", settings.getPageNo());
		//출발 터미널  ID
		appendOps.put("depTerminalId", settings.getDepTerminalId());
		//도착 터미널  ID
		appendOps.put("arrTerminalId", settings.getArrTerminalId());
		//출발일
		appendOps.put("depPlandTime",settings.getDepPlandTime());
		
		//api에서 정보 가져오기
		ApiUrlBuilder aub = new ApiUrlBuilder();
		
		is = aub.ApiUrlBuild(setUrl, serviceKey, setRequestMethod, contentType, appendOps);
		
		//XML파싱
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 
		 DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		
		 if(is != null) { 
	        	Document doc = documentBuilder.parse(is);
	        	
	        	Element element = doc.getDocumentElement();
	        		        	  
		        NodeList NodechargeList = element.getElementsByTagName("charge");
		        NodeList NodearrPlandTimeList = element.getElementsByTagName("arrPlandTime");
		        NodeList NodedepPlandTimeList = element.getElementsByTagName("depPlandTime");
		        NodeList NodegradeNmList = element.getElementsByTagName("gradeNm");
  
		        totalCnt = element.getElementsByTagName("totalCount").item(0).getFirstChild().getNodeValue();
		        pageNo = element.getElementsByTagName("pageNo").item(0).getFirstChild().getNodeValue();
		        numofRows = element.getElementsByTagName("numOfRows").item(0).getFirstChild().getNodeValue();

			        for(int i = 0; i < NodearrPlandTimeList.getLength(); i++) {
			        	
			        	Node Nodecharge = NodechargeList.item(i);
				        Node NodearrPlandTime = NodearrPlandTimeList.item(i);
				        Node NodedepPlandTime = NodedepPlandTimeList.item(i);
				        Node NodegradeNm = NodegradeNmList.item(i);
				        
				        //api에서 가져온 기본운임
				        String charge = Nodecharge.getFirstChild().getNodeValue();
				        //api에서 가져온 출발시간
				        String arrPlandTime = NodearrPlandTime.getFirstChild().getNodeValue();
				        //api에서 가져온 도착시간
				        String depPlandTime = NodedepPlandTime.getFirstChild().getNodeValue();
				        //api에서 가져온 버스의 종류
				        String gradeNm = NodegradeNm.getFirstChild().getNodeValue();
				        
				        
				        //소요시간 계산
				      //출발시간
				        int depplandtimeH = Integer.parseInt(depPlandTime.substring(8, 10));
				        int depplandtimeM = Integer.parseInt(depPlandTime.substring(10, 12));
				        
				        //도착시간
				        int arrplandtimeH = Integer.parseInt(arrPlandTime.substring(8, 10));
				        int arrplandtimeM = Integer.parseInt(arrPlandTime.substring(10, 12));
				        
				        int h = (((arrplandtimeH  * 60) + arrplandtimeM) - ((depplandtimeH * 60) + depplandtimeM)) / 60;
						int m = (((arrplandtimeH  * 60) + arrplandtimeM) - ((depplandtimeH * 60) + depplandtimeM)) % 60;
				        
						String spanTime = "";
						
						if(h>0) {
							 spanTime = h + "시간" + m + "분";
						}else {
							 spanTime = m + "분";
						}//if end
						
				        

				        //list에 add
				        expBusScInfo.add(new ExpBusScInfoVo(arrPlandTime, charge, depPlandTime, gradeNm, spanTime));
				        
				        
				       
			        }//for end
	        	    	
	        }else {
	        	throw new IOException("응답이 없습니다.");
	        }//if end
		 
		 System.out.println("size : " + expBusScInfo.size());
	        
	     //리턴할 객체에 set
		 expBusSc.setScList(expBusScInfo);
		 expBusSc.setNumOfRows(numofRows);
		 expBusSc.setPageNo(pageNo);
		 expBusSc.setTotalCnt(totalCnt);
		 expBusSc.setDepPlandTime(settings.getDepPlandTime());
		 
		 

		return expBusSc;
		
	}//getExpBusSc() end

}// class end
