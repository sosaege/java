package homwork_java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

public class admin extends JFrame {
	JFrame frame;
    private JTextField conditionField;
    private JButton initializeButton, queryButton, executeConditionButton;
    private JTable[] tables;
    private JScrollPane[] scrollPanes;
    private JPanel inputPanels[];
    private JTextField[][] inputFields;
    private JButton[] insertButtons, deleteButtons;
    Connection conn = null;
    Statement stmt = null;
    private String[][] tableSchemas = {
        {"회원아이디", "고객명", "휴대폰번호", "전자메일주소"},
        {"영화번호", "영화명", "상영시간", "상영등급", "감독명", "배우명", "장르", "영화소개", "개봉일", "평점"},
        {"상영관 번호", "상영관사용여부", "가로좌석수", "세로좌석수"},
        {"상영일정번호", "상영시작일", "상영요일", "상영회차", "상영시작시간", "영화_영화번호", "상영관_상영관번호"},
        {"좌석번호", "좌석사용여부", "상영관_상영관 번호"},
        {"예매번호", "결제방법", "결제상태", "결제금액", "결제일자", "회원정보_회원아이디"},
        {"티켓번호", "발권여부", "표준가격", "판매가격", "좌석_좌석번호", "상영관_상영관번호", "상영일정_상영일정번호", "예매정보_예매번호"},
        {"영화번호", "영화명", "상영시간", "영화등급", "감독명", "배우명", "장르", "영화소개", "개봉일", "제작사"}
    };
    

    public admin() {
        setTitle("Database Management");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel topPanel = new JPanel();
        initializeButton = new JButton("초기화");
        queryButton = new JButton("조회/재조회");
        conditionField = new JTextField(30);
        executeConditionButton = new JButton("조건식 실행");
        
        executeConditionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeCondition();
            }
        });
        topPanel.add(initializeButton);
        topPanel.add(queryButton);
        topPanel.add(conditionField);
        topPanel.add(executeConditionButton);

        mainPanel.add(topPanel);

        tables = new JTable[7];
        frame = new JFrame("Database Tables");
        scrollPanes = new JScrollPane[7];
        inputPanels = new JPanel[7];
        insertButtons = new JButton[7];
        deleteButtons = new JButton[7];
        inputFields = new JTextField[8][];
        setVisible(true);
        for (int i = 0; i < 7; i++) {
            tables[i] = new JTable(new NonEditableTableModel());
            scrollPanes[i] = new JScrollPane(tables[i]);
            mainPanel.add(scrollPanes[i]);

            inputPanels[i] = new JPanel();
            insertButtons[i] = new JButton("입력 확인");
            deleteButtons[i] = new JButton("삭제");

            inputFields[i] = new JTextField[tableSchemas[i].length];
            for (int j = 0; j < tableSchemas[i].length; j++) {
                inputFields[i][j] = new JTextField(tableSchemas[i][j], 10);
                inputPanels[i].add(inputFields[i][j]);
            }
            inputPanels[i].add(insertButtons[i]);
            inputPanels[i].add(deleteButtons[i]);
            mainPanel.add(inputPanels[i]);

            final int tableIndex = i;
            insertButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    insertData(tableIndex);
                }
            });
            deleteButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	clearInputFields(tableIndex);
                }
            });
        }

        add(mainScrollPane);

        initializeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
        			Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
        			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root","1234"); // JDBC 연결

        			System.out.println("DB 연결 완료");
        			
        			stmt = conn.createStatement(); // SQL문 처리용 Statement 객체 생성
					
					stmt.executeUpdate("DROP DATABASE IF EXISTS db1;");
					stmt.executeUpdate("create database db1;");
					stmt.executeUpdate("grant all privileges on db1.* to root@localhost with grant option;");
					stmt.executeUpdate("grant all privileges on db1.* to user1@localhost with grant option;");
					stmt.executeUpdate("commit;");
					stmt.executeUpdate("USE db1;");
					stmt.executeUpdate("SET SQL_SAFE_UPDATES = 0;");
					stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `db1`.`영화` (\r\n"
							+ "  `영화번호` INT NOT NULL,\r\n"
							+ "  `영화명` VARCHAR(45) NOT NULL,\r\n"
							+ "  `상영시간` TIME NOT NULL,\r\n"
							+ "  `상영등급` VARCHAR(10) NOT NULL,\r\n"
							+ "  `감독명` VARCHAR(45) NOT NULL,\r\n"
							+ "  `배우명` VARCHAR(45) NOT NULL,\r\n"
							+ "  `장르` VARCHAR(45) NOT NULL,\r\n"
							+ "  `영화소개` VARCHAR(300) NOT NULL,\r\n"
							+ "  `개봉일` DATE NOT NULL,\r\n"
							+ "  `평점` VARCHAR(45) NOT NULL,\r\n"
							+ "  PRIMARY KEY (`영화번호`),\r\n"
							+ "  UNIQUE INDEX `영화 번호_UNIQUE` (`영화번호` ASC) VISIBLE,\r\n"
							+ "  UNIQUE INDEX `영화 명_UNIQUE` (`영화명` ASC) VISIBLE)\r\n"
							+ "ENGINE = InnoDB;\r\n");
							
					stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `db1`.`상영관` (\r\n"
							+ "  `상영관번호` INT NOT NULL,\r\n"
							+ "  `상영관사용여부` TINYINT NOT NULL,\r\n"
							+ "  `가로좌석수` INT NOT NULL,\r\n"
							+ "  `세로좌석수` INT NOT NULL,\r\n"
							+ "  PRIMARY KEY (`상영관번호`),\r\n"
							+ "  UNIQUE INDEX `상영관 번호_UNIQUE` (`상영관번호` ASC) VISIBLE)\r\n"
							+ "ENGINE = InnoDB;\r\n");
					
					stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `db1`.`회원정보` (\r\n"
							+ "  `회원아이디` VARCHAR(45) NOT NULL,\r\n"
							+ "  `고객명` VARCHAR(45) NOT NULL,\r\n"
							+ "  `휴대폰번호` VARCHAR(45) NOT NULL,\r\n"
							+ "  `전자메일주소` VARCHAR(45) NOT NULL,\r\n"
							+ "  UNIQUE INDEX `회원 아이디_UNIQUE` (`회원아이디` ASC) VISIBLE,\r\n"
							+ "  UNIQUE INDEX `전자메일 주소_UNIQUE` (`전자메일주소` ASC) VISIBLE,\r\n"
							+ "  UNIQUE INDEX `휴대폰 번호_UNIQUE` (`휴대폰번호` ASC) VISIBLE,\r\n"
							+ "  PRIMARY KEY (`회원아이디`))\r\n"
							+ "ENGINE = InnoDB;\r\n");
					
					stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `db1`.`예매정보` (\r\n"
							+ "  `예매번호` INT NOT NULL,\r\n"
							+ "  `결제방법` VARCHAR(45) NOT NULL,\r\n"
							+ "  `결제상태` TINYINT NOT NULL,\r\n"
							+ "  `결제금액` INT NOT NULL,\r\n"
							+ "  `결제일자` DATE NOT NULL,\r\n"
							+ "  `회원정보_회원아이디` VARCHAR(45) NOT NULL,\r\n"
							+ "  PRIMARY KEY (`예매번호`),\r\n"
							+ "  UNIQUE INDEX `예매번호_UNIQUE` (`예매번호` ASC) VISIBLE,\r\n"
							+ "  INDEX `fk_예매정보_회원정보1_idx` (`회원정보_회원아이디` ASC) VISIBLE,\r\n"
							+ "  CONSTRAINT `fk_예매정보_회원정보1`\r\n"
							+ "    FOREIGN KEY (`회원정보_회원아이디`)\r\n"
							+ "    REFERENCES `db1`.`회원정보` (`회원아이디`)\r\n"
							+ "    ON DELETE NO ACTION\r\n"
							+ "    ON UPDATE NO ACTION)\r\n"
							+ "ENGINE = InnoDB;\r\n");
					
					stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `db1`.`좌석` (\r\n"
							+ "  `좌석번호` INT NOT NULL,\r\n"
							+ "  `좌석사용여부` TINYINT NOT NULL,\r\n"
							+ "  `상영관_상영관번호` INT NOT NULL,\r\n"
							+ "  PRIMARY KEY (`좌석번호`),\r\n"
							+ "  INDEX `fk_좌석_상영관1_idx` (`상영관_상영관번호` ASC) VISIBLE,\r\n"
							+ "  UNIQUE INDEX `좌석 번호_UNIQUE` (`좌석번호` ASC) VISIBLE,\r\n"
							+ "  CONSTRAINT `fk_좌석_상영관1`\r\n"
							+ "    FOREIGN KEY (`상영관_상영관번호`)\r\n"
							+ "    REFERENCES `db1`.`상영관` (`상영관번호`)\r\n"
							+ "    ON DELETE NO ACTION\r\n"
							+ "    ON UPDATE NO ACTION)\r\n"
							+ "ENGINE = InnoDB;\r\n");
					
					stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `db1`.`상영일정` (\r\n"
							+ "  `상영일정번호` INT NOT NULL,\r\n"
							+ "  `상영시작일` DATE NOT NULL,\r\n"
							+ "  `상영요일` VARCHAR(1) NOT NULL,\r\n"
							+ "  `상영회차` INT NOT NULL,\r\n"
							+ "  `상영시작시간` TIME NOT NULL,\r\n"
							+ "  `영화_영화번호` INT NOT NULL,\r\n"
							+ "  `상영관_상영관번호` INT NOT NULL,\r\n"
							+ "  INDEX `fk_상영일정_영화_idx` (`영화_영화번호` ASC) VISIBLE,\r\n"
							+ "  INDEX `fk_상영일정_상영관1_idx` (`상영관_상영관번호` ASC) VISIBLE,\r\n"
							+ "  PRIMARY KEY (`상영일정번호`),\r\n"
							+ "  UNIQUE INDEX `상영일정번호_UNIQUE` (`상영일정번호` ASC) VISIBLE,\r\n"
							+ "  CONSTRAINT `fk_상영일정_영화`\r\n"
							+ "    FOREIGN KEY (`영화_영화번호`)\r\n"
							+ "    REFERENCES `db1`.`영화` (`영화번호`)\r\n"
							+ "    ON DELETE NO ACTION\r\n"
							+ "    ON UPDATE NO ACTION,\r\n"
							+ "  CONSTRAINT `fk_상영일정_상영관1`\r\n"
							+ "    FOREIGN KEY (`상영관_상영관번호`)\r\n"
							+ "    REFERENCES `db1`.`상영관` (`상영관번호`)\r\n"
							+ "    ON DELETE NO ACTION\r\n"
							+ "    ON UPDATE NO ACTION)\r\n"
							+ "ENGINE = InnoDB;\r\n");
					
					stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `db1`.`티켓` (\r\n"
							+ "  `티켓번호` INT NOT NULL,\r\n"
							+ "  `발권여부` TINYINT NOT NULL,\r\n"
							+ "  `표준가격` INT NOT NULL,\r\n"
							+ "  `판매가격` INT NOT NULL,\r\n"
							+ "  `좌석_좌석번호` INT NOT NULL,\r\n"
							+ "  `상영관_상영관번호` INT NOT NULL,\r\n"
							+ "  `상영일정_상영일정번호` INT NOT NULL,\r\n"
							+ "  `예매정보_예매번호` INT NOT NULL,\r\n"
							+ "  PRIMARY KEY (`티켓번호`),\r\n"
							+ "  INDEX `fk_티켓_좌석1_idx` (`좌석_좌석번호` ASC) VISIBLE,\r\n"
							+ "  INDEX `fk_티켓_상영관1_idx` (`상영관_상영관번호` ASC) VISIBLE,\r\n"
							+ "  UNIQUE INDEX `좌석_좌석 번호_UNIQUE` (`좌석_좌석번호` ASC) VISIBLE,\r\n"
							+ "  INDEX `fk_티켓_상영일정1_idx` (`상영일정_상영일정번호` ASC) VISIBLE,\r\n"
							+ "  UNIQUE INDEX `티켓 번호_UNIQUE` (`티켓번호` ASC) VISIBLE,\r\n"
							+ "  INDEX `fk_티켓_예매 정보1_idx` (`예매정보_예매번호` ASC) VISIBLE,\r\n"
							+ "  CONSTRAINT `fk_티켓_좌석1`\r\n"
							+ "    FOREIGN KEY (`좌석_좌석번호`)\r\n"
							+ "    REFERENCES `db1`.`좌석` (`좌석번호`)\r\n"
							+ "    ON DELETE NO ACTION\r\n"
							+ "    ON UPDATE NO ACTION,\r\n"
							+ "  CONSTRAINT `fk_티켓_상영관1`\r\n"
							+ "    FOREIGN KEY (`상영관_상영관번호`)\r\n"
							+ "    REFERENCES `db1`.`상영관` (`상영관번호`)\r\n"
							+ "    ON DELETE NO ACTION\r\n"
							+ "    ON UPDATE NO ACTION,\r\n"
							+ "  CONSTRAINT `fk_티켓_상영일정1`\r\n"
							+ "    FOREIGN KEY (`상영일정_상영일정번호`)\r\n"
							+ "    REFERENCES `db1`.`상영일정` (`상영일정번호`)\r\n"
							+ "    ON DELETE NO ACTION\r\n"
							+ "    ON UPDATE NO ACTION,\r\n"
							+ "  CONSTRAINT `fk_티켓_예매 정보1`\r\n"
							+ "    FOREIGN KEY (`예매정보_예매번호`)\r\n"
							+ "    REFERENCES `db1`.`예매정보` (`예매번호`)\r\n"
							+ "    ON DELETE NO ACTION\r\n"
							+ "    ON UPDATE NO ACTION)\r\n"
							+ "ENGINE = InnoDB;\r\n");
					
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('jkim01', '김지훈', '010-1234-5678', 'jkim01@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('hpark02', '박현우', '010-2345-6789', 'hpark02@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('clee03', '이채은', '010-3456-7890', 'clee03@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('kwang04', '황민수', '010-4567-8901', 'kwang04@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('ychoi05', '최유리', '010-5678-9012', 'ychoi05@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('sjung06', '정서윤', '010-6789-0123', 'sjung06@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('khan07', '한지민', '010-7890-1234', 'khan07@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('dbae08', '배도현', '010-8901-2345', 'dbae08@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('nlee09', '이성민', '010-9012-3456', 'nlee09@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('hseo10', '서현아', '010-0123-4567', 'hseo10@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('mjang11', '장민준', '010-1234-5670', 'mjang11@example.com');");
					stmt.executeUpdate("INSERT INTO `회원정보` VALUES('user1', '사용자', '010-2345-6781', 'user1@example.com');");
					
					stmt.executeUpdate("INSERT INTO 영화 VALUES(1, '나쁜 녀석들', '01:21:00', '15세', '빌랄 팔라','윌스미스','액션', '마이애미 강력반 최고의 콤비 마이크(윌 스미스)와 마커스(마틴 로렌스). 가족 같은 하워드(조 판토리아노) 반장이 사망 전 마약 카르텔 조직의 비리에 연루되었다는 뉴스 속보를 접하게 된다. ','2023-01-01','4.5');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(2, '원더랜드', '01:22:00', '15세', '김태용','탕웨이','드라마','언제 어디서든 다시 만날 수 있습니다. 죽은 사람을 인공지능으로 복원하는 원더랜드 서비스가 일상이 된 세상','2023-02-02','4.6');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(3, '설계자', '01:23:00', '15세', '이요섭','강동원','범죄','정말 우연이라고 생각해요? 의뢰받은 청부 살인을 사고사로 조작하는 설계자 영일(강동원) 그의 설계를 통해 우연한 사고로 조작된 죽음들이 실은 철저하게 계획된 살인이라는 것을 아무도 알지 못한다. ','2023-03-03','4.7');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(4, '그녀가 죽었다', '01:24:00', '15세', '김세휘','변요한','미스터리','나쁜 짓은 절대 안 해요. 그냥 보기만 하는 거예요. 고객이 맡긴 열쇠로 그 집에 들어가 남의 삶을 훔쳐보는 취미를 지닌 공인중개사 구정태. 편의점 소시지를 먹으며 비건 샐러드 사진을 포스팅하는 SNS 인플루언서 한소라에게 흥미를 느끼고 관찰하기 시작한다. ','2023-04-04','4.9');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(5, '범죄도시4','01:25:00', '12세', '허명행','마동석','액션','신종 마약 사건 3년 뒤, 괴물형사 마석도(마동석)와 서울 광수대는 배달앱을 이용한 마약 판매 사건을 수사하던 중 수배 중인 앱 개발자가 필리핀에서 사망한 사건이 대규모 온라인 불법 도박 조직과 연관되어 있음을 알아낸다.','2023-05-05','4.3');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(6, '하이큐', '01:26:00', '전체관람가', '미즈나카 스스무','무라세 아유므','애니메이션','봄철 고교 배구대회 1회전과 2회전에서 우승 후보를 차례로 꺾은 카라스노 고등학교는 마침내 3회전에서 인연의 라이벌 네코마 고등학교와 맞붙게 된다. ','2023-06-06','4.5');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(7, '가필드', '01:27:00', '전체관람가', '마크 딘달','크리스 프렛','애니메이션','세상귀찮 집냥이, 바쁘고 험난한 세상에 던져졌다! 집사 존과 반려견 오디를 기르며 평화로운 나날을 보내던 집냥이 가필드. 어느 날, 험악한 길냥이 무리에게 납치당해 냉혹한 거리로 던져진다.','2023-07-07','4.2');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(8, '명탐정 코난', '01:28:00', '전체관람가', '이시하라 슌스케','코난','애니메이션','괴도 1412호, 이름하여 괴도 KID 암호가 섞인 예고장이 보내진 그곳에는 월하의 마술사, 괴도 키드가 나타난다.','2023-08-08','4.1');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(9, '태극기 휘날리며', '01:29:00', '15세', '강제규','장동건','전쟁','풍족하진 않지만 가족들과 열심히 살아가던 형제 진태와 진석. 그러나 1950년 6월 어느날, 한반도에 전쟁이 일어났다는 호외가 배포되고, 두 형제는 갑작스레 전쟁터로 끌려간다.','2023-09-09','4.1');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(10, '금지된 장난', '01:30:00', '15세', '나카다 히데오','하시모토 칸나','공포','평범한 회사원 나오토는 아내 미유키, 아들 하루토와 행복하고 단란한 생활을 보내며 이보다 더 완벽할 수 없다고 생각한다. 그러나, 어느 날 미유키가 갑작스런 교통사고로 세상을 떠나게 되고 부자는 실의에 빠진다. ','2023-10-10','4.0');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(11, '혹성탈출', '01:31:00', '15세', '웨스 볼','오웬 티그','액션','인류의 시대는 끝났고, 세상의 주인이 바뀌었다! 진화한 유인원과 퇴화된 인간들이 살아가는 땅. 유인원 리더 프록시무스는 완전한 군림을 위해 인간들을 사냥하며 자신의 제국을 건설한다. ','2023-11-11','4.1');");
					stmt.executeUpdate("INSERT INTO 영화 VALUES(12, '창가의 토토', '01:32:00', '전체관람가', '야쿠와 신스케','오노 리리아나','애니메이션','남들과 조금 다르다는 이유로 다니던 초등학교에서 쫓겨나게 된 토토는 엄격한 규율로 가르치는 이전 학교와 달리, 있는 그대로의 토토를 품어주는 새로운 학교로 가게 된다. ','2023-12-12','4.2');");


					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (1, 1, 10, 5);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (2, 1, 9, 6);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (3, 1, 8, 7);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (4, 1, 7, 8);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (5, 1, 6, 9);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (6, 1, 5, 10);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (7, 1, 6, 9);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (8, 1, 7, 8);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (9, 1, 8, 7);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (10, 1, 9, 6);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (11, 1, 8, 7);");
					stmt.executeUpdate("INSERT INTO 상영관 (`상영관번호`, `상영관사용여부`, `가로좌석수`, `세로좌석수`) VALUES (12, 1, 7, 8);");
					

					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(1, '2024-01-01', '일', '1', '12:00:00', '1', '1');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(2, '2024-01-02', '월', '2', '14:00:00', '2', '2');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(3, '2024-01-03', '화', '3', '16:00:00', '3', '3');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(4, '2024-01-04', '수', '4', '18:00:00', '4', '4');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(5, '2024-01-05', '목', '5', '20:00:00', '5', '5');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(6, '2024-01-06', '금', '6', '10:00:00', '6', '6');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(7, '2024-01-07', '토', '1', '12:00:00', '7', '7');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(8, '2024-01-08', '일', '2', '14:00:00', '8', '8');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(9, '2024-01-09', '월', '3', '16:00:00', '9', '9');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(10, '2024-01-10', '화', '4', '18:00:00', '10', '10');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(11, '2024-01-11', '수', '5', '20:00:00', '11', '11');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(12, '2024-01-12', '목', '6', '10:00:00', '12', '12');");

					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(13, '2024-01-01', '일', '1', '14:00:00', '1', '1');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(14, '2024-01-02', '월', '2', '16:00:00', '2', '2');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(15, '2024-01-03', '화', '3', '18:00:00', '3', '3');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(16, '2024-01-04', '수', '4', '20:00:00', '4', '4');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(17, '2024-01-05', '목', '5', '22:00:00', '5', '5');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(18, '2024-01-06', '금', '6', '23:00:00', '6', '6');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(19, '2024-01-07', '토', '1', '02:00:00', '7', '7');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(20, '2024-01-08', '일', '2', '04:00:00', '8', '8');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(21, '2024-01-09', '월', '3', '06:00:00', '9', '9');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(22, '2024-01-10', '화', '4', '08:00:00', '10', '10');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(23, '2024-01-11', '수', '5', '10:00:00', '11', '11');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(24, '2024-01-12', '목', '6', '12:00:00', '12', '12');");
					
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(25, '2024-01-02','월', '1', '12:00:00', '1', '1');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(26, '2024-01-03','화', '2', '14:00:00', '2', '2');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(27, '2024-01-04','수', '3', '16:00:00', '3', '3');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(28, '2024-01-05','목', '4', '18:00:00', '4', '4');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(29, '2024-01-06','금', '5', '20:00:00', '5', '5');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(30, '2024-01-07','토', '6', '10:00:00', '6', '6');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(31, '2024-01-08','일', '1', '12:00:00', '7', '7');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(32, '2024-01-09','월', '2', '14:00:00', '8', '8');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(33,  '2024-01-10', '화','3', '16:00:00', '9', '9');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(34, '2024-01-11', '수', '4', '18:00:00', '10', '10');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(35, '2024-01-12', '목', '5', '20:00:00', '11', '11');");
					stmt.executeUpdate("INSERT INTO 상영일정 VALUES(36, '2024-01-13', '금', '6', '10:00:00', '12', '12');");


					for (int i = 1; i <= 50; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (1000 + i) + ", 0, 1);");
					for (int i = 1; i <= 54; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (2000 + i) + ", 0, 2);");
					for (int i = 1; i <= 56; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (3000 + i) + ", 0, 3);");
					for (int i = 1; i <= 56; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (4000 + i) + ", 0, 4);");
					for (int i = 1; i <= 54; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (5000 + i) + ", 0, 5);");
					for (int i = 1; i <= 50; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (6000 + i) + ", 0, 6);");
					for (int i = 1; i <= 54; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (7000 + i) + ", 0, 7);");
					for (int i = 1; i <= 56; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (8000 + i) + ", 0, 8);");
					for (int i = 1; i <= 56; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (9000 + i) + ", 0, 9);");
					for (int i = 1; i <= 54; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (10000 + i) + ", 0, 10);");
					for (int i = 1; i <= 56; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (11000 + i) + ", 0, 11);");
					for (int i = 1; i <= 56; i++)stmt.executeUpdate("INSERT INTO 좌석 VALUES(" + (12000 + i) + ", 0, 12);");



					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 1001");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 2002");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 3003");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 4004");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 5005");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 6006");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 7007");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 8008");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 9009");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 10010");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 11011");
					stmt.executeUpdate("UPDATE db1.좌석 SET `좌석사용여부` = 1 WHERE `좌석번호` = 12012");

					
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(1, '카드', 1, 15000, '2024-01-01',  'jkim01');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(2, '현금', 1, 15000, '2024-01-02',  'hpark02');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(3, '카드', 1, 15000, '2024-01-03',  'clee03');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(4, '현금', 1, 15000, '2024-01-04',  'kwang04');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(5, '카드', 1, 15000, '2024-01-05',  'ychoi05');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(6, '현금', 1, 15000, '2024-01-06',  'sjung06');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(7, '카드', 1, 15000, '2024-01-07',  'khan07');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(8, '현금', 1, 15000, '2024-01-08',  'dbae08');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(9, '카드', 1, 15000, '2024-01-09',  'nlee09');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(10, '현금', 1, 15000, '2024-01-10', 'hseo10');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(11, '카드', 1, 15000, '2024-01-11', 'mjang11');");
					stmt.executeUpdate("INSERT INTO `예매정보` VALUES(12, '현금', 1, 15000, '2024-01-12', 'user1');");


					
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(1, 1, 15000, 15000, 1001, 1, 1, 1);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(2, 1, 15000, 15000, 2002, 2, 2, 2);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(3, 1, 15000, 15000, 3003, 3, 3, 3);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(4, 1, 15000, 15000, 4004, 4, 4, 4);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(5, 1, 15000, 15000, 5005, 5, 5, 5);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(6, 1, 15000, 15000, 6006, 6, 6, 6);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(7, 1, 15000, 15000, 7007, 7, 7, 7);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(8, 1, 15000, 15000, 8008, 8, 8, 8);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(9, 1, 15000, 15000, 9009, 9, 9, 9);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(10, 1, 15000, 15000, 10010, 10, 10, 10);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(11, 1, 15000, 15000, 11011, 11, 11, 11);");
					stmt.executeUpdate("INSERT INTO 티켓 VALUES(12, 1, 15000, 15000, 12012, 12, 12, 12);");

					
					JOptionPane.showMessageDialog(frame, "데이터 베이스 초기화 완료");
				} catch (ClassNotFoundException e2) {
					System.out.println("JDBC 드라이버 로드 오류");
				} catch (SQLException e2) {
					System.out.println("SQL 실행오류");
				} 
                
            }
        });

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String url = "jdbc:mysql://localhost:3306/db1";
                String user = "root";
                String password = "1234";

                String[] tableNames = {
                    "회원정보", "영화", "상영관", "상영일정", "좌석", "예매정보", "티켓"
                };

                String[][] columnNames = {
                    {"회원아이디", "고객명", "휴대폰번호", "전자메일주소"},
                    {"영화번호", "영화명", "상영시간", "상영등급", "감독명", "배우명", "장르", "영화소개", "개봉일", "평점"},
                    {"상영관번호", "상영관사용여부", "가로좌석수", "세로좌석수"},
                    {"상영일정번호", "상영시작일", "상영요일", "상영회차", "상영시작시간", "영화_영화번호", "상영관_상영관번호"},
                    {"좌석번호", "좌석사용여부", "상영관_상영관 번호"},
                    {"예매번호", "결제방법", "결제상태", "결제금액", "결제일자", "회원정보_회원아이디"},
                    {"티켓번호", "발권여부", "표준가격", "판매가격", "좌석_좌석번호", "상영관_상영관번호", "상영일정_상영일정번호", "예매정보_예매번호"}
                };

                try {
                	Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
        			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root","1234"); // JDBC 연결
        			System.out.println("DB 연결 완료");

                    for (int i = 0; i < tables.length; i++) {
                        DefaultTableModel model = (DefaultTableModel) tables[i].getModel();
                        model.setRowCount(0); 
                        model.setColumnIdentifiers(columnNames[i]); 

                        String query = "SELECT * FROM `" + tableNames[i] + "`";
                        ResultSet rs = stmt.executeQuery(query);
                        while (rs.next()) {
                            int columnCount = rs.getMetaData().getColumnCount();
                            Object[] rowData = new Object[columnCount];
                            for (int j = 0; j < columnCount; j++) {
                                rowData[j] = rs.getObject(j + 1);
                            }
                            model.addRow(rowData);
                        }
                    }
                }catch (ClassNotFoundException e1) {
        			System.out.println("JDBC 드라이버 로드 오류");
        		} catch (SQLException e1) {
        			System.out.println("DB 연결 오류");
        		}
            }
        });

        executeConditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCondition();
            }
        });
    }
    private void insertData(int tableIndex) {
        String url = "jdbc:mysql://localhost:3306/db1";
        String user = "root";
        String password = "1234";
        String[] tableNames = {
                "회원정보", "영화", "상영관", "상영일정", "좌석", "예매정보", "티켓"
            };

        // Check if any input field is empty
        for (JTextField field : inputFields[tableIndex]) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "모든 필드를 채워주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root","1234"); // JDBC 연결

			System.out.println("DB 연결 완료");
			
			stmt = conn.createStatement(); // SQL문 처리용 Statement 객체 생성

            StringBuilder query = new StringBuilder("INSERT INTO `" + tableNames[tableIndex] + "` VALUES(");
            for (int i = 0; i < inputFields[tableIndex].length; i++) {
                query.append("'").append(inputFields[tableIndex][i].getText()).append("'");
                if (i < inputFields[tableIndex].length - 1) {
                    query.append(", ");
                }
            }
            query.append(");");
            stmt.executeUpdate(query.toString());
            JOptionPane.showMessageDialog(frame, "Data inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error inserting data: " + e.getMessage());
        } catch (ClassNotFoundException e) {
			System.out.println("JDBC 드라이버 로드 오류");
        }
    }
    private void clearInputFields(int tableIndex) {
        for (JTextField field : inputFields[tableIndex]) {
            field.setText("");
        }
    }
    class NonEditableTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
    

    private void executeCondition() {
        String url = "jdbc:mysql://localhost:3306/db1";
        String user = "root";
        String password = "1234";
        String condition = conditionField.getText().trim();

        try {
        	Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root","1234"); // JDBC 연결
			System.out.println("DB 연결 완료");

            if (condition.toLowerCase().contains("delete")) {
                stmt.executeUpdate(condition);
                JOptionPane.showMessageDialog(frame, "Delete query executed successfully!");
            } else if (condition.toLowerCase().contains("update")) {
                stmt.executeUpdate(condition);
                JOptionPane.showMessageDialog(frame, "Update query executed successfully!");
            } else {
                throw new IllegalArgumentException("Only DELETE and UPDATE queries are allowed.");
            }

        } catch (SQLException e) {
        	System.out.println("DB 연결 오류");
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error executing query: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
        } catch (ClassNotFoundException e) {
			System.out.println("JDBC 드라이버 로드 오류");
        }
    }
}
