# java-chess

체스 미션 저장소

## 우아한테크코스 코드리뷰

- [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)

## 체스 기능 요구사항

### 1단계

- [x] 체스판에서 말의 위치 값은 가로 위치는 왼쪽부터 a ~ h이고, 세로는 아래부터 위로 1 ~ 8로 구현한다.
- [x] 말은 종류와 색으로 구성되어 있다.
- [x] 보드에서 말들을 초기 위치로 생성한다.
- [x] 검정말은 대문자, 흰색말은 소문자로 표현한다.
    - [x] 말이 없는 칸은 .으로 표현한다.
- [x] 게임 시작은 start, 종료는 end 명령이다.
    - [x] start, end가 아닌 입력은 예외 처리한다.

### 2단계

- [x] source 위치에 말이 없으면 예외를 발생시킨다.
- [x] source 와 target은 같을 수 없다.
- [x] source의 기물을 target으로 옮길 수 있다.
- [x] 폰은 직진으로 한칸 이동할 수 있다.
    - [x] 색에 따라 직진 방향이 다르다.
    - [x] 초기위치에서는 직진으로 두칸까지 이동할 수 있다.
    - [x] 위쪽 좌우 대각선에 상대 기물이 있으면 이동ㅎ할 수 있다.
- [x] 나이트는 상하좌우 두칸 직진 후 좌우로 이동할 수 있다.
- [x] 룩은 상하좌우로 칸 제한 없이 직진할 수 있다.
- [x] 비숍은 상하 대각선으로 칸 제한 없이 이동할 수 있다.
- [x] 퀸은 룩과 비숍의 이동방법을 모두 갖는다.
- [x] 킹은 상하좌우 한칸, 위 아래 대각선 한칸씩 움직일 수 있다.
- [x] 목적지에 내 기물이 있으면 예외를 발생한다.
- [x] 이동하려는 경로에 (목적지 제외) 다른 기물이 있으면 목적지로 이동할 수 없다.
    - [x] 나이트는 해당 안됨
- [x] 사용자의 입력에 따라 말이 이동한다.
- [x] 사용자의 입력에 따라 게임이 시작하거나 종료될 수 있다.

### 3단계

- [x] King이 잡혔을 때 게임을 종료해야 한다.
- [x] 체스 게임은 현재 남아 있는 말에 대한 점수를 구할 수 있어야 한다.
    - [x] queen은 9점, rook은 5점, bishop은 3점, knight는 2.5점이다.
    - [x] pawn의 기본 점수는 1점이다. 하지만 같은 세로줄에 같은 색의 폰이 있는 경우 1점이 아닌 0.5점을 준다.
- [x] 각 진영의 점수를 비교하여 현재의 승패를 구한다.
- [x] 수를 번갈아가면서 둔다.

### 4단계

- [X] 체스 초기상태 웹뷰에 띄우기
- [x] input form 으로 move 명령어 적용하기 (뷰하고 백앤드하고 통합)
    - [x] move 명령 시 예외 메시지 뷰로 던져주기
- [x] 클릭으로 move 하는 기능 (Rest API)
    - [x] move 명령 시 예외 메시지 뷰로 던져주기
    - [ ] response 에 게임 종료 여부 추가
    - [ ] 게임 종료 시 게임이 종료됐다는 alert 띄우기
- [ ] status 버튼 만들기
- [ ] 체스판, 말 이미지 적용
- [ ] 종료버튼 만들기
