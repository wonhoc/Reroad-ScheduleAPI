package com.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import com.example.vo.trainVo.CityTrainStInfoVo;
import com.example.vo.trainVo.TrainScInfoVo;
import com.example.vo.trainVo.TrainScVo;
import com.example.vo.trainVo.TrainSetInfoVo;
import com.example.vo.trainVo.TrainStInfoVo;

@Service("trainService")
public class TrainServiceImpl implements TrainService {
	
	@Value("${serviceKey}")
	private String key;
	
	//도시코드 조회 api URL
	private String cityInfoURL = "http://openapi.tago.go.kr/openapi/service/TrainInfoService/getCtyCodeList?";
	//시/도별 기차역 목록조회 api URL
	private String cityStInfoURL = "http://openapi.tago.go.kr/openapi/service/TrainInfoService/getCtyAcctoTrainSttnList";
	//출도착지기반 열차 스케줄조회 api URL
	private String trainScURL = "http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo";

	@Autowired
	private CityInfoDao cityInfoDao;
	
	
	
	//기차정보 API에서 도시의 정보와 코드를 가져온다.
	@Override
	public ArrayList<CityInfoVo> getCityCode() throws IOException {
		//리턴할 객체
		 ArrayList<CityInfoVo> cityInfoList = new ArrayList<CityInfoVo>();
		
		//구성 정보들
		String setUrl = this.cityInfoURL;
		String serviceKey = this.key;

	 	StringBuilder urlBuilder = new StringBuilder("");
	 	
	 	//url
	 	urlBuilder.append(setUrl);
        //서비스키
	 	urlBuilder.append(URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey);
        
        URL url = new URL(urlBuilder.toString());
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //요청방식
        conn.setRequestMethod("GET");
        //요청 형식
        conn.setRequestProperty("Content-type", "application/json");
        
        System.out.println(conn.getResponseCode());

        
   
	       //XML파싱
	        try {
			//DocumentBuilderFactory 객체 생성
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        //DocumentBuilderFactory객체로 DocumentBuilder 객체생성 (팩토리패턴)
	        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	        
	        //api에서 응답한 InputStream객체
	        InputStream is = conn.getInputStream();
	        
	        Document doc = documentBuilder.parse(is);
	        
	        Element element = doc.getDocumentElement();
  
	        NodeList NodecityCodelist = element.getElementsByTagName("citycode");
	        NodeList NodecityNamelist = element.getElementsByTagName("cityname");
	        
	        for (int i = 0; i < NodecityCodelist.getLength(); i++) {
	        	// i번째 item 태그를 node에 저장
	            
	
	            Node NodecityCode = NodecityCodelist.item(i);
	            Node NodecityName = NodecityNamelist.item(i);
	            
	         
	            String cityCode = NodecityCode.getFirstChild().getNodeValue();
	            String cityName = NodecityName.getFirstChild().getNodeValue();

	            //리턴할 List에 add           	             
	            cityInfoList.add(new CityInfoVo(cityCode, cityName));
	            
	        }//for end
	                        	
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					conn.disconnect();
				}// end
	        
	        return cityInfoList;
        
	}//getCityCode() end
	
	//API에서 인자로 주어진 도시의 기차역 정보를 가져온다.
	@Override
	public ArrayList<TrainStInfoVo> getTrainStInfo(String cityCode) throws Exception {
		
		//리턴할 객체
		ArrayList<TrainStInfoVo> TrainStInfo = new ArrayList<TrainStInfoVo>();
		//api url
		String setUrl = this.cityStInfoURL;
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
		//검색할 도시이름
		appendOps.put("cityCode", cityCode);
		
		//api에서 정보들 가져오기
		try {
		ApiUrlBuilder aub = new ApiUrlBuilder();
		
		is = aub.ApiUrlBuild(setUrl, serviceKey, setRequestMethod, contentType, appendOps);

		} catch (Exception e) {
			throw e;
		}
		
		//xml파싱
		try {
			
		//DocumentBuilderFactory 객체 생성
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //DocumentBuilderFactory객체로 DocumentBuilder 객체생성 (팩토리패턴)
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        
        if(is != null) { 
        	Document doc = documentBuilder.parse(is);
        	
        	Element element = doc.getDocumentElement();
        	  
	        NodeList NodeStIdList = element.getElementsByTagName("nodeid");
	        NodeList NonodeStNameList = element.getElementsByTagName("nodename");
	        
	        for(int i = 0; i < NodeStIdList.getLength(); i++) {
	        
	    	 Node NodeStId = NodeStIdList.item(i);
	         Node NonodeStName = NonodeStNameList.item(i);
	         //api에서 가져온 역 id
	         String StId = NodeStId.getFirstChild().getNodeValue();
	         //api에서 가져온 역 이름
             String StName = NonodeStName.getFirstChild().getNodeValue();
             //리턴할 list에 add
             TrainStInfo.add(new TrainStInfoVo (StId, StName, cityCode));
             
	        }//for end
        	
        	
        }else {
        	throw new IOException("응답이 없습니다.");
        }//if end
		} catch (Exception e) {
			throw e;
		}//end
		
		return TrainStInfo;
		
	}//getTrainStInfo() end
	
	//도시별 역정보를 return
	@Override
	public ArrayList<CityTrainStInfoVo> getCityTrainStInfo() {
		
		System.out.println("info begin");
		
		//리턴할 객체
		ArrayList<CityTrainStInfoVo> cityTrainStInfoList = new ArrayList<CityTrainStInfoVo>();
	
		//도시 정보만 들어있는 객체
		ArrayList<CityInfoVo> cityInfoList = new ArrayList<CityInfoVo>();
		
		//DB에서 도시정보만 조회
		cityInfoList = (ArrayList)cityInfoDao.selectCityInfo();
		
		//도시코드로 역정보 조회
		for(CityInfoVo cityInfo :  cityInfoList) {
			//도시코드
			String cityCode = cityInfo.getCityCode();
			//도시이름
			String cityName = cityInfo.getCityName();
			//조회
			ArrayList<TrainStInfoVo> trainStInfoList = (ArrayList)cityInfoDao.selectTrainStinfo(cityCode);
			
			//도시이름과 역정보가 담긴 객체
			CityTrainStInfoVo cityTrainStInfo = new CityTrainStInfoVo(cityName, trainStInfoList);
			//리턴할 객체에 add
			cityTrainStInfoList.add(cityTrainStInfo);
			
		}//for end

		return cityTrainStInfoList;
		
	}//getCityTrainStInfo() end
	
	//열차 스케줄을 조회
	@Override
	public TrainScVo getTrainSc(TrainSetInfoVo settings) throws Exception {
		//스케줄 정보들이 담겨있는 리턴할 객체
		TrainScVo trainSc = new TrainScVo();
		//스케줄 정보들
		ArrayList<TrainScInfoVo> cityInfos = new ArrayList<TrainScInfoVo>();
		//해당 옵션으로 검색했을때 나오는 레코드의 수
		String totalCnt;
		//페이지 번호
		String pageNo;
		//한줄에 보여줄 레코드의 갯수
		String numofRows;
		
		
		//api url
		String setUrl = "http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo";
		//serviceKey
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
		//출발 기차역 ID
		appendOps.put("depPlaceId", settings.getDepPlaceId());
		//도착 기차역 ID
		appendOps.put("arrPlaceId", settings.getArrPlaceId());
		//출발일
		appendOps.put("depPlandTime",settings.getDepPlandTime());
		
		
		//api에서 정보 가져오기
		try {
			ApiUrlBuilder aub = new ApiUrlBuilder();
			
			is = aub.ApiUrlBuild(setUrl, serviceKey, setRequestMethod, contentType, appendOps);

			} catch (Exception e) {
				throw e;
			}//end
		
		//xml 파싱		
		try {
			
				//DocumentBuilderFactory 객체 생성
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        //DocumentBuilderFactory객체로 DocumentBuilder 객체생성 (팩토리패턴)
		        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		        
		        if(is != null) { 
		        	Document doc = documentBuilder.parse(is);
		        	
		        	Element element = doc.getDocumentElement();
		        	  
			        NodeList NodeadultchargeList = element.getElementsByTagName("adultcharge");
			        NodeList NodearrplandtimeList = element.getElementsByTagName("arrplandtime");
			        NodeList NodedepplandtimeList = element.getElementsByTagName("depplandtime");
			        NodeList NodetraingradenameList = element.getElementsByTagName("traingradename");
			        NodeList NodetrainnoList = element.getElementsByTagName("trainno");
			        
			        totalCnt = element.getElementsByTagName("totalCount").item(0).getFirstChild().getNodeValue();
			        pageNo = element.getElementsByTagName("pageNo").item(0).getFirstChild().getNodeValue();
			        numofRows = element.getElementsByTagName("numOfRows").item(0).getFirstChild().getNodeValue();
			        
				        for(int i = 0; i < NodeadultchargeList.getLength(); i++) {
				        	
				        	Node Nodeadultcharge = NodeadultchargeList.item(i);
					        Node Nodearrplandtime = NodearrplandtimeList.item(i);
					        Node Nodedepplandtime = NodedepplandtimeList.item(i);
					        Node Nodetraingradename = NodetraingradenameList.item(i);
					        Node Nodetrainno = NodetrainnoList.item(i);
				        
					    	
					        //api에서 가져온 기본운임
					        String adultcharge = Nodeadultcharge.getFirstChild().getNodeValue();
					        //api에서 가져온 출발시간
					        String arrplandtime = Nodearrplandtime.getFirstChild().getNodeValue();
					        //api에서 가져온 도착시간
					        String depplandtime = Nodedepplandtime.getFirstChild().getNodeValue();
					        //api에서 가져온 열차의 종류
					        String traingradename = Nodetraingradename.getFirstChild().getNodeValue();
					        //api에서 가져온 열차번호
					        String trainno = Nodetrainno.getFirstChild().getNodeValue();
					        
					        //소요시간계산 
					        
					        //출발시간
					        int depplandtimeH = Integer.parseInt(depplandtime.substring(8, 10));
					        int depplandtimeM = Integer.parseInt(depplandtime.substring(10, 12));
					        
					        //도착시간
					        int arrplandtimeH = Integer.parseInt(arrplandtime.substring(8, 10));
					        int arrplandtimeM = Integer.parseInt(arrplandtime.substring(10, 12));
					        
					        int h = (((arrplandtimeH  * 60) + arrplandtimeM) - ((depplandtimeH * 60) + depplandtimeM)) / 60;
							int m = (((arrplandtimeH  * 60) + arrplandtimeH) - ((depplandtimeH * 60) + depplandtimeM)) % 60;
					        
							String spanTime = h + "시간" + m + "분";
					        
					        
					        //정보들 List에 add
					        cityInfos.add(new TrainScInfoVo(adultcharge, arrplandtime, depplandtime, traingradename, trainno, spanTime));

				        }//for end
		        	    	
		        }else {
		        	throw new IOException("응답이 없습니다.");
		        }//if end
		        
		        
		        //리턴할 객체에 set
		        trainSc.setScList(cityInfos);
		        trainSc.setTotalCnt(totalCnt);
		        trainSc.setPageNo(pageNo);
		        trainSc.setNumOfRows(numofRows);
		        trainSc.setDepPlandTime(settings.getDepPlandTime());
	        
			} catch (Exception e) {	
				throw e;
			}//end

		return trainSc;
		
	}//getTrainSc() end
	

}//class end
