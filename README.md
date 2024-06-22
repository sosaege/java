
![image](https://github.com/sosaege/java/assets/171452515/1cc8c148-3684-4eeb-8e3a-4325acda364f)

프로그램의 Main 함수를 시작하면
로그인 화면에서 시작하며,
아이디 비밀번호 user1/user1 으로 
유저 인터페이스를,
아이디 비밀번호 root/1234로
관리자 인터페이스에 접속할 수 있다.
잘못된 아이디, 비밀번호를 입력할 시,
오류창이 뜨며, 어떤 인터페이스에도 접속되지 않는다. 

   
![image](https://github.com/sosaege/java/assets/171452515/139f5b03-2ca1-4224-a5a5-3ec7e5b6df0d)

![image](https://github.com/sosaege/java/assets/171452515/9dd3eeff-7335-4c3a-ba49-c5bd8d4defef)

![image](https://github.com/sosaege/java/assets/171452515/22a88def-532d-486f-b9e8-dd113ae19705)

![image](https://github.com/sosaege/java/assets/171452515/cb453680-b727-4cc3-93a9-4d9f3128ff72)

 
![image](https://github.com/sosaege/java/assets/171452515/68cbc6b0-2258-4e6c-8d54-7016b1366e14)

![image](https://github.com/sosaege/java/assets/171452515/423de413-56d5-4476-8ce5-ef15916102e4)


관리자 인터페이스에 접속 시, 다음과 같은 화면이 나타나며,
프로그램 테스팅 시, 반드시 관리자가 초기화를 진행해야 한다
 
 
데이터 베이스 초기화 완료 후, 조회/재조회 버튼 클릭 시, 모든 데이터 베이스가 릴레이션 별로
나타나며, 각 릴레이션 아래에는 데이터를 입력할 수 있는 창이 존재한다.


삭제를 누르면 입력 돼있던 값이 사라지며, 입력 확인을 누르면 데이터가 입력된다.
만약 데이터 입력 조건을 만족하지 못하면, 예외처리가 발생한다.

   
![image](https://github.com/sosaege/java/assets/171452515/0e6aec22-732b-4af7-b54b-88803814a1a2)
![image](https://github.com/sosaege/java/assets/171452515/91862038-7b51-4a93-b6af-e2e3107fd373)
![image](https://github.com/sosaege/java/assets/171452515/43fdbfb6-ee7b-4453-a012-3abae4fc0f09)

![image](https://github.com/sosaege/java/assets/171452515/7f884156-617d-49d7-bbb0-1fa63eba952a)

(입력이 완료 돼 추가된 데이터.)

조건식 실행 같은 경우에는
DELETE 또는 UPDATE쿼리문에 한해 조건식을 실행하고
제약조건을 만족하지 않거나 오류가 발생하면 메시지를 출력한다.
  

 
![image](https://github.com/sosaege/java/assets/171452515/43da1405-79f8-46c6-bb22-8b05e327e838)
![image](https://github.com/sosaege/java/assets/171452515/ce74a3fb-8442-4ee3-8c29-a7d7d325fce3)
![image](https://github.com/sosaege/java/assets/171452515/b0847865-d2ec-4212-9391-779233009fff)

DELETE의 경우, 왜래키에 의해 삭제가 안 되는 경우가 많으므로
가장 영향이 없는 티켓으로 테스트를 진행함.


UPDATE 회원정보 SET 휴대폰번호 = '010-9999-9999' WHERE 회원아이디 = 'hpark02'; 실행시
 
![image](https://github.com/sosaege/java/assets/171452515/7123e5ec-66aa-48f7-a84a-a3b16bc500ff)

전화번호가 업데이트 되는 것을 볼 수 있음.





![image](https://github.com/sosaege/java/assets/171452515/3f2cb235-18fc-4473-95ff-fc8ee868f747)


유저 아이디로 로그인 시,
영화 조회와 예매 조회 버튼을 통해
원하는 기능을 테스트할 수 있음.


![image](https://github.com/sosaege/java/assets/171452515/97aa404b-4e64-479b-8dba-cc99a31422bb)









 
영화 조회 버튼을 누를 시, 영화 조회 화면이 나타나며,
영화명, 감독명, 배우명, 장르 중 원하는 것들로 검색이 가능하고,
아무것도 없는 상태에서 조회를 누를 시 모든 영화가 나타남.

원하는 영화를 클릭 후, 영화 소개 버튼 누를 시 해당 영화의 상세설명이 나타남 
 
![image](https://github.com/sosaege/java/assets/171452515/23d066ac-24ce-4afc-8316-cb5a29e253d0)

![image](https://github.com/sosaege/java/assets/171452515/3a79fbae-abb0-4c39-bf58-fc0058d18b87)

![image](https://github.com/sosaege/java/assets/171452515/a137c383-ea4e-4316-aa83-f06d4ce3f818)

(상영 시간을 선택 안 하고 예매버튼 누를 시)



원하는 영화 클릭 후, 예매 버튼 클릭 시, 예매 인터페이스가 나타나며,
상영 시작일을 선택할 시, 해당 상영 시작일에 해당하는
상영 시작시간을 선택할 수 있음.

![image](https://github.com/sosaege/java/assets/171452515/ceb1fee7-af04-45cc-8a8a-0cf34f8a572e)

 
모두 선택 후 예매 버튼을 누를 시, 인원수에 맞게 좌석을 선택할 수 있으며,
이미 예매 돼있는 좌석은 붉은 색으로 나타나 선택이 불가능함.

![image](https://github.com/sosaege/java/assets/171452515/8be12a65-2b70-4453-810d-07f6d96211a5)
(좌석을 적게 선택할 시)               
![image](https://github.com/sosaege/java/assets/171452515/77a45e0e-c859-4ec3-a856-c5d33f03e020)
     

![image](https://github.com/sosaege/java/assets/171452515/9e4316df-eb8f-470e-bd03-1eddadb57453)
(예매 성공)
예매에 성공하거나, 로그인 화면에서 예매 확인 버튼을 클릭할 시,
 
![image](https://github.com/sosaege/java/assets/171452515/facaac8e-54cc-4dae-b901-b5abd4cb374f)

![image](https://github.com/sosaege/java/assets/171452515/16acbbd3-7e2c-4fd2-a523-3f51d5a8cba8)


다음과 같은 예매 확인 화면이 나타나고,
조회를 클릭하면 테이블이 나타남.
상세설명을 확인할 테이블을 클릭 시, 오른쪽에 관련된 상세 릴레이션 들을 확인할 수 있음.

   
원하는 테이블을 클릭하고, 예매 정보 삭제 버튼을 클릭 시,
원하는 예매정보와 티켓정보가 함께 삭제됨.
 
 
![image](https://github.com/sosaege/java/assets/171452515/46c971f7-89e8-4456-9f8b-92a8fc656ac9)

 
![image](https://github.com/sosaege/java/assets/171452515/022949ce-64c4-4d11-af12-237dc5042030)

관리자 페이지에서 예매정보와 티켓 테이블이 함께 삭제된 것을 확인할 수 있음.
원하는 테이블을 클릭하고, 예매 변경 버튼 클릭시,
 
![image](https://github.com/sosaege/java/assets/171452515/057f59f7-7a90-45a0-99f3-a9ae328918fc)

원하는 영화를 선택할 수 있고,
  
![image](https://github.com/sosaege/java/assets/171452515/fc989a97-0f1d-41ad-9c8d-e569932c22c8)
![image](https://github.com/sosaege/java/assets/171452515/207c4ab0-346f-4b98-8417-a29f3fc4d7cc)

동일하게 예매 완료 시, 
 
예매정보가 변경됨.
![image](https://github.com/sosaege/java/assets/171452515/44adae8c-2441-403e-af69-c7df296e3a4f)





상영일정 변경 버튼 클릭 시,
   
![image](https://github.com/sosaege/java/assets/171452515/65782053-c366-4206-a67d-e53d07ba5f4f)
![image](https://github.com/sosaege/java/assets/171452515/185ad957-e1f4-461b-add5-5c3e24509d4d)

동일하게 예매를 진행하고,
 
![image](https://github.com/sosaege/java/assets/171452515/b23755be-fc05-44cd-8f96-246d59780b37)

좌석번호가 변경된 것을 확인할 수 있음.
