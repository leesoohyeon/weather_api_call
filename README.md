# API를 활용하여 현재위치 날씨정보 불러오기
**사용언어 : Java** <br>
**개발 Tool : Android Studio**<br>
**사용스킬 : 공공데이터 OPEN API, 카카오 REST API,Retrofit, LocationManager**<br>
**blog : https://su-hyun07.tistory.com/16**
<br><br>
# Application Version
- **minSdkVersion : 21**
- **targetSdkVersion : 33**
<br><br>
# 앱 실행화면
<div align="center">
<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/36aff50a-91ac-454e-9b26-778610ece759" width="180px" height="320px"> 
<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/86480d8d-aabb-45ce-8462-21b65ffc4496" width="180px" height="320px"> 
<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/e3711d3b-dd63-475c-bd67-590aa6eae39c" width="180px" height="320px"> 
</div>

# 설명

- ### SKY(하늘상태)

<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/6e62c04c-d81d-4a07-bc61-50f869172358" width="450px" height="320px"> 
<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/82a7f71f-0eba-405a-9afc-c968186088fe" width="350px" height="320px"><br><br>

**- JSON 형태로 받아온 코드 값에 대하여 하늘 상태를 표시합니다.**
  
<br><br>

- ### Bast_time(단기예보 조회시간)
  
<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/0810feba-2117-418a-bbb1-ff014f8fa563" width="300px" height="400px"> 
<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/3cc1cfb9-3d98-44b4-b4fe-4200897fcb99" width="500px" height="400px"><br>

**- 현재 시간을 가져와서 공공데이터 API의 Base_time에 맞취어 시간을 변환합니다.**

<br><br>

- ### 기온별 옷차림

<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/3b153f14-3612-4f84-b143-12964bdf4943" width="500px" height="600px"> 
<img src="https://github.com/leesoohyeon/weather_api_call/assets/88640008/9dd483fe-e810-4045-8b32-54a7f3cecc2f" width="300px" height="400px"><br>

**- 사진과 같이 JSON형태로 받아온 기온값에 대하여 옷추천 리스트를 출력합니다.**
