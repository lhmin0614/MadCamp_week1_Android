# MadCamp_week1 (앱 이름)

> 이혜민, 강수아, 정이든


|Contacts|Photos|Google Map|
|--|--|--|
|<img src="https://user-images.githubusercontent.com/63537847/124421905-5f18d900-dd9d-11eb-839c-84f6ce84f1ce.png" width="200" height="400">|<img src="https://user-images.githubusercontent.com/63537847/124422205-eb2b0080-dd9d-11eb-8886-4bbd4c79f223.png" width="200" height="400">|cell3|

## Tab1 : 연락처
> 연락처를 저장하고 바로 전화할 수 있습니다. 로컬의 연락처와 연동되어 앱에서 저장하면 로컬 연락처 저장소에도 저장됩니다. 보이는 연락처를 누르면 바로 전화를 걸 수 있습니다. 
> 네이게이션 바를 이용해서 여러 프래그먼트 사이에 이동하는 것이 용이하게 디자인하였습니다. 

- ### 연락처 저장하기 

  <img src="https://user-images.githubusercontent.com/63537847/124422603-802df980-dd9e-11eb-9906-e80cf7c5bd08.png" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/124422659-9b990480-dd9e-11eb-9267-7b4b571c761e.png" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/124422752-d13ded80-dd9e-11eb-99e7-4065a4c99c3b.png" width="200" height="400">
   - 연락처를 local에서 가져오기 위해서 dexter 라이브러리를 사용함. READ_CONTACTS 권한을 요청함
   - query를 사용해서 local에 있는 Phone.CONTENT_URI 받아옴
   - DISPLAY_NAME은 이름, NUMBER는 전화번호, PHONE_URI는 연락처에 저장된 사진
   - Action Bar에 있는 item을 클릭하면 연락처 저장을 위한 alert dialog 뜸
   - Intent를 사용해서 데이터를 전달한 뒤 local 연락처에 전달받은 이름과 전화번호를 저장
   - 다시 연락처 화면으로 돌아가면 저장된 연락처가 포함되어 있음

- ### 전화하기 

  <img src="https://user-images.githubusercontent.com/63537847/124422815-e9157180-dd9e-11eb-9269-173ca6f23cb2.png" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/124422851-f6caf700-dd9e-11eb-9bfc-c1af7e72a00b.png" width="200" height="400">
  - Recycler View로 되어있는 layout을 클릭하면 전화가 걸리는 방식으로 프로그램 구성함
  - 하나의 view를 클릭하게 되면 전화 여부를 물어보는 alert dialog뜸 
  - " 예"를 선택하게 되면 Intent를 사용해서 바로 전화 걸 수 있는 페이지로 넘어감 

- ### 연락처 검색하기 
  
  <img src="https://user-images.githubusercontent.com/63537847/124543740-c9468200-de60-11eb-8610-0b3a69fceaff.png" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/124543744-cc417280-de60-11eb-8ddb-484ca99c4fbc.png" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/124543753-cf3c6300-de60-11eb-9bca-bd99abce04df.png" width="200" height="400">
  - EditText로 검색할 이름 또는 번호를 받아옴
  - EditText에서 받아온 값이 변경될 때마다 사용할 수 있게 TextWatcher사용함
  - Filter를 사용해서 이름을 검색할 때와 번호를 검색하는 경우를 나눠 EditText에서 받아온 내용이 포함된 새로운 리스트를 생성해서 보여줌

## Tab2 : 사진 앨범 
> 사진 앨범 Tab은 Android 기기의 local storage에 존재하는 모든 사진파일을 보여주며, 촬영기능을 통해 새로운 사진을 local storage에 추가할 수 있는 화면입니다. 
- ### 갤러리 페이지
  <img src = "https://user-images.githubusercontent.com/68638211/124426397-f3d30500-dda4-11eb-8f4a-08cfefd7826b.jpg" width = "200" height = "400" >
  
  - Android MediaStore를 사용하여 앱에서 local storage의 파일을 사용함
  - 사진 파일을 읽어오기 위해 사용자에게 READ_EXTERNAL_STORAGE 권한을 요청함
  - local storage에 존재하는 모든 이미지 파일의 uri주소를 반환하는 getAllShownImagesPath 함수를 구현함 
  - uri주소로 접근한 이미지들을 GridView에 추가하여 화면을 구성함
- ### 상세 사진 페이지
  <img src = "https://user-images.githubusercontent.com/68638211/124426143-963eb880-dda4-11eb-95ac-83c1ff9b278f.jpg" width="200" height="400">
  
  - 갤러리 페이지에서 각각의 사진을 누르면 상세 사진 페이지로 이동할 수 있음
  - 사진을 눌렀을 때 사진의 uri 주소를 bundle 형태로 새로운 fragment로 전달함
  - 상세 사진 페이지에서 사진을 다시 한 번 누르면 갤러리 페이지로 돌아갈 수 있음 
- ### 촬영 페이지
  <img src = "https://user-images.githubusercontent.com/68638211/124426654-4f04f780-dda5-11eb-90e1-0b45466ff8db.jpg" width="200" height="400"> <img src = "https://user-images.githubusercontent.com/68638211/124426657-50cebb00-dda5-11eb-98fa-5112a9d914ad.jpg" width="200" height="400"> <img src = "https://user-images.githubusercontent.com/68638211/124426639-49a7ad00-dda5-11eb-8ef8-c52d83ddf4d7.jpg" width="200" height="400">
  - 갤러리 페이지에서 우측 상단 카메라 아이콘을 누르면 촬영 페이지로 이동할 수 있음
  - Android Camera를 사용하여 앱에서 Android 디바이스의 카메라에 접근하여 사용함
  - SurfaceView 객체를 사용하여 카메라 preview화면을 간접적으로 보여줌
  - shoot 버튼을 클릭했을 때 camera.takePicture 메소드를 호출하여 사진을 촬영함
  - 촬영된 사진을 Bitmap 데이터로 변환하여 화면에 표시하고, 새로운 Uri 주소를 할당하여 local storage에 저장함
  - 촬영 페이지에서 다시 갤러리 페이지로 돌아갔을 때 촬영한 사진을 확인할 수 있음

## Tab3 : 구글 맵스 
