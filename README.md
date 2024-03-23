<img width="206" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/4f29e42f-0143-47c0-b045-c4620666c9ea"> <img width="206" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/6e5b1c9d-72aa-48cb-9241-1dbf064c73b5"> <br>
<br/>


### 📋 프로젝트 개요

> **프로젝트명** : 지금어디 <br/> **개발 기간** : 2023년 07월 ~ 2024년 01월 (7개월) <br/> **분류** : 팀 프로젝트 <br/> **팀 구성** : 프론트엔드 2명, 백엔드 2명, 디자인 1<br/>
<br>
<img width="206" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/07bbf61f-9085-4a97-834d-c96ea167590a"> <img width="207" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/de641260-553c-493f-bb18-34cfca6260cd"> <img width="207" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/73e3f89d-bfa3-4ab9-9b0a-42231304c47c"> <img width="206" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/c4cf9505-8f8b-43b4-a144-c67aa37f38b3">
<br/>
<br/>
<br/>

### 🧑‍🤝‍🧑 팀 정보

| 이름   | 역할                            |
| ------ | ------------------------------- |
| 박종훈 | BE 개발 |
| 송인준 | BE 개발 |
| 유준성 | FE 개발 |
| 김찬휘 | FE 개발 |
| 이서영 | 디자인|

<br/>
<br/>
<br/>

### 🔍 기획 배경

> 모두가 약속에서 늦지 않도록 방지하기 위한 앱을 개발

실제 구글 플레이스토어에 배포하여 실용화될 수 있도록 개발

##### 우리의 Needs

- 약속을 잡으면 같이 일정을 공유해야 할 필요가 있다.
- 자체 친구 기능을 통해 일정에 초대된 모든 사람들의 실시간 위치를 파악해야할 필요가 있다.

##### 솔루션

" 친구와 일정 공유 "

- 일정 제목, 날짜, 약속 장소, 간단 메모를 통해 약속을 잡을 수 있다.
- GPS 기반의 위치 정보를 공유하게 되므로 푸시 알림으로 일정 수락여부를 판단한다.

" 친구 기능 "

- 위치 정보를 상대방에게 보여줘야 하는 취약점을 보완하기 위해 자체 친구 기능을 통해 보안성을 강화한다.
- 앱 자체의 친구 기능을 통해 일정에 초대하여 공유할 수 있다.

<br/>
<br/>
<br/>

### 🎨 기능 상세 및 구현 화면

#### 1) 회원 정보 관련
##### 이메일 인증 기능 구현
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/15cf3c5e-a891-40cf-8219-ffb271403e5e"> <img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/808e8c8e-3465-4ece-afc1-dabbdced684b"> <br>

##### 아이디 찾기, 비밀번호 재설정 구현
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/9cc919c4-9ac7-4b1e-9804-ec6ee5075085"> <img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/2b213aa4-7476-4bd2-811a-46c4b7b2d4bb">
<br>
<br>

#### 2) 홈 화면(일정)
##### 일정 시간이 지난 일정의 경우 회색으로 지난 일정 처리
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/9c2ea4f3-e1a9-4c39-b0ac-fea1c880c408"> <img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/4d6caa3d-0307-4833-84f3-2a1601dd87f6"> <img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/c7ced598-e0e9-4239-a279-9fe756d121a6">
<br>
<br>

#### 3) 일정 생성(일정 정보 입력 -> 친구 추가 -> 장소 설정)
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/a1bb5bce-fedc-4f1a-b0ce-743edb8d94b2"> 
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/b9c25efb-3b46-4b97-a757-26112c262d4e"> 
<br>
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/05f34604-6d97-4dd2-8ccc-c875185c9601"> 
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/2f94782b-01a3-4259-b973-cb5266dabad4">
<br>
<br>

#### 4) 일정 상세보기 및 GPS 기반 친구 위치 확인
##### 일정에 초대된 사람들의 위치를 항상 볼 수 있게 되면 문제가 있다고 판단되어 <br>약속시간 1시간 전부터 끝날때까지만 위치 정보를 파악할 수 있도록 구현
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/05c70ead-b3f1-4ba2-87e8-13fb034318fa"> <img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/7befcade-33bf-4223-bc54-8a52386a9f86">
<br>
<br>

#### 5) 친구 기능
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/d2e169dd-7c54-4df3-a8ac-e049041729f4"> <img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/35b02445-c4b9-4362-8e27-509ea9acd6f4"> <img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/cfaf6017-08bd-4a08-b8cd-fa72033996bd">
<br>
<br>

#### 6) 친구 그룹
##### 자주 만나는 친구들을 그룹을 설정하여 일정에 친구를 추가할 때 그룹으로 추가할 수 있도록 구현
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/26f7eb74-4d1f-4119-a2ef-027dedf8eb0e"> 
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/d3a89731-e1b0-422c-8b52-66b423a6246f">
<br>
<img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/ee15bb55-08d8-4b8f-b1d2-40edc9f73622"> <img width="209" height="350" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/85f263be-f6d4-47c6-964b-82ca11b95552">
<br>
<br>

#### 7) 마이페이지 및 사이드 바
##### 일정 초대 및 친구 추가시 FIrebase 기반 FCM 푸시알림을 통한 알림 구현
<img width="154" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/91fe805b-1dd0-4e2f-a8ce-2f5249a250e1"> <img width="154" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/b369ca97-6112-4aa4-9c02-6ffda0060c5d"> <img width="154" alt="image" src="https://github.com/WhereAreYouPJ/.github/assets/103410386/10c6de9a-9ad8-4c81-a49b-7a3e7097fdbf">

<br/>
<br/>
<br/>
