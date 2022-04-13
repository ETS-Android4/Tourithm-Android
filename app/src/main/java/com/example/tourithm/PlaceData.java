package com.example.tourithm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.example.tourithm.PlaceData_Adapter;

public class PlaceData extends AppCompatActivity {

    ListView lv;
    ArrayList<PlaceData_item> arrayList;
    String tag_name;
    String data;
    ArrayList<String> list = new ArrayList<String>();
    boolean tag[] = new boolean[8];

    /* 관광지명, 관광지 구분, 도로명 주소, 지번 주소,
    위도, 경도, 관광지 소개, 관리기관 전화번호 */
    String[] tag_str = {"trrsrtNm", "trrsrtSe", "rdnmadr", "lnmadr",
            "latitude", "longitude", "trrsrtIntrcn", "phoneNumber"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_list);

        lv = findViewById(R.id.lv);
        arrayList = new ArrayList<>();

        // API 주소
        String api = "http://api.data.go.kr/openapi/tn_pubr_public_trrsrt_api?serviceKey=XhztE1jRQpeISRwY%2BDH%2B5EmZrIS8SYA2yrlky4e0wi2kBCMSddCsg7prvwwKN6%2BsBnzxNBGY7Kp2B1L5SywhFQ%3D%3D&pageNo=0&numOfRows=100&type=xml";

        // API를 이용한 데이터 다운로드 객체
        DownloadWebpageTask task = new DownloadWebpageTask();
        DownloadWebpageTask task2 = new DownloadWebpageTask();
        // 데이터 다운로드 및 처리
        task2.execute(api);

    }


    // API xml에서 데이터 파싱
    private class  DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                String txt = (String) downloadUrl((String) urls[0]);
                return txt;
            } catch (IOException e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            try {
                // XML Pull Parser 객체 생성
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                // 파싱할 문서 설정
                xpp.setInput(new StringReader(result));

                // 현재 이벤트 유형 반환
                int eventType = xpp.getEventType();

                // 이벤트 유형이 문서 마지막이 될 때까지 반복
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    // 문서의 시작인 경우
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    }
                    // START_TAG이면 태그 이름 확인
                    else if (eventType == XmlPullParser.START_TAG) {
                        for (int i=0; i<8; i++) {
                            tag_name = xpp.getName();
                            if(tag[i]==false && tag_name.equals(tag_str[i]))
                                tag[i] = true;
                        }
                    }
                    // 태그 사이의 문자 확인
                    else if (eventType == XmlPullParser.TEXT) {
                        for (int j=0; j<8; j++) {
                            if(tag[j]) {
                                data = xpp.getText();
                                list.add(data);
                                tag[j] = false;
                            }
                        }
                    }

                    // 마침 태그인 경우
                    else if (eventType == XmlPullParser.END_TAG) {

                    }
                    // 다음 이벤트 유형 할당
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                Log.d("[API xml 파싱]", "파싱 실패");
            }

            for (int i=0; i<list.size(); i+=8) {
                PlaceData_item item = new PlaceData_item();
                item.setTrrsrtNm(list.get(i));
                item.setTrrsrtSe(list.get(i+1));
                item.setRdnmadr(list.get(i+2));
                item.setLnmadr(list.get(i+3));
                item.setLatitude(list.get(i+4));
                item.setLongitude(list.get(i+5));
                item.setTrrsrtIntrcn(list.get(i+6));
                item.setPhoneNumber(list.get(i+7));
                arrayList.add(item);
            }

            // DB에 데이터 넣기 ( 최초 1회 시 주석 제거 )
            // PlaceData_Activity.java로 인텐트 보냄
            /*Intent intent = new Intent(getApplicationContext(), PlaceData_Activity.class);
            intent.putExtra("placedata_list", list);
            Log.d("list intent ", list.toString());
            startActivity(intent);
            finish();*/

            // PlaceData_Adapter로 커스텀 리스트뷰 아이템 보냄
            PlaceData_Adapter placeData_adapter = new PlaceData_Adapter(PlaceData.this, arrayList);
            lv.setAdapter(placeData_adapter);

        }

        // API에 해당하는 문서 다운로드
        private String downloadUrl(String api) throws IOException {
            HttpURLConnection conn = null;
            try {
                // 문서를 읽어 텍스트 단위로 버퍼에 저장
                URL url = new URL(api);
                conn = (HttpURLConnection) url.openConnection();

                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));
                // 줄 단위로 읽어 문자로 저장
                String line = null;
                String page = "";
                while ((line = bufreader.readLine()) != null) {
                    page += line;
                }
                // 다운로드 문서 반환
                return page;
            } finally {
                conn.disconnect();
            }
        }
    }

}
