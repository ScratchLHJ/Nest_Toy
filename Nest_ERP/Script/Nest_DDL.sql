-- =============================
-- 🌐 Calendar Memo + ERP Tables 초기화 스크립트
-- Oracle SQL Developer에서 F5 실행용
-- =============================

BEGIN
    -- 테이블 DROP (의존성 역순)
    EXECUTE IMMEDIATE 'DROP TABLE CALENDAR_MEMO CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE SCHEDULE_UPLOAD CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE SESSION_LOG CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE EMPLOYEE_SALES_CONTRIB CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE TOTAL_SALES_ITEM CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE SALES_ITEM_NAME CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE SALES_ITEM_TYPE CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE TOTAL_SALES_REPORT CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE PAYROLL_POLICY CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE SALARY_TRANSFER_LOG CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE PAYROLL_ITEM CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE PAYROLL CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE EMPLOYEE_BRANCH CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE EMPLOYEE CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE BRANCH CASCADE CONSTRAINTS';

    -- 시퀀스 DROP
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_calendar_memo';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_branch';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_employee';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_employee_branch';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_payroll';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_payroll_item';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_salary_transfer';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_payroll_policy';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_total_sales_report';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_sales_item_type';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_sales_item_name';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_total_sales_item';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_employee_sales_contrib';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_session_log';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_schedule_upload';
END;
/

-- =============================
-- 시퀀스 생성
-- =============================
CREATE SEQUENCE seq_calendar_memo
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
CREATE SEQUENCE seq_branch START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_employee START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_employee_branch START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payroll START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payroll_item START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_salary_transfer START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_payroll_policy START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_total_sales_report START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sales_item_type START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_sales_item_name START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_total_sales_item START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_employee_sales_contrib START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_session_log START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_schedule_upload START WITH 1 INCREMENT BY 1;

-- ========================
-- TABLES (with column comments)
-- ========================

-- CALENDAR_MEMO: 달력 메모 저장 테이블
CREATE TABLE CALENDAR_MEMO (
    memo_id       NUMBER PRIMARY KEY,         -- 메모 고유 ID
    memo_date     DATE NOT NULL,              -- 달력 날짜 (메모가 달력에 표시될 날짜)
    memo_content  VARCHAR2(1000),             -- 메모 내용
    writer_name   VARCHAR2(100),              -- 작성자 이름 (추후 FK 연결 가능)
    created_at    DATE DEFAULT SYSDATE,       -- 메모 작성 시각
    is_deleted    CHAR(1) DEFAULT 'N'         -- 논리 삭제 여부 ('Y' or 'N')
);


-- 2. BRANCH (지점 정보)
CREATE TABLE BRANCH (
    branch_id NUMBER PRIMARY KEY, -- 지점 고유 ID
    branch_name VARCHAR2(100) NOT NULL, -- 지점 이름
    deposit_account VARCHAR2(50), -- 입금 계좌번호
    withdraw_account VARCHAR2(50), -- 출금 계좌번호
    business_number VARCHAR2(50) UNIQUE -- 사업자등록번호
);

-- 1. EMPLOYEE (직원 기본정보)
CREATE TABLE EMPLOYEE (
    employee_id NUMBER PRIMARY KEY, -- 직원 고유 ID
    registered_branch_id NUMBER REFERENCES BRANCH(branch_id), -- 등록된 기본 지점 ID
    employee_code VARCHAR2(50), -- 내부 직원 번호 (식별용)
    name VARCHAR2(50) NOT NULL, -- 이름
    gender VARCHAR2(10), -- 성별
    phone VARCHAR2(20), -- 연락처
    ssn VARCHAR2(20) UNIQUE, -- 주민등록번호
    address VARCHAR2(255), -- 주소
    employment_type VARCHAR2(20), -- 고용 형태 (정규직/프리랜서 등)
    bank_name VARCHAR2(50), -- 입금 은행명
    bank_account VARCHAR2(50), -- 계좌번호
    account_holder VARCHAR2(50) -- 예금주
);

-- 3. EMPLOYEE_BRANCH (지점 소속 이력)
CREATE TABLE EMPLOYEE_BRANCH (
    employee_branch_id NUMBER PRIMARY KEY, -- 직원-지점 소속 ID
    employee_id NUMBER REFERENCES EMPLOYEE(employee_id), -- 직원 ID
    branch_id NUMBER REFERENCES BRANCH(branch_id), -- 지점 ID
    start_date DATE, -- 위촉일
    end_date DATE, -- 해촉일
    assignment_type VARCHAR2(50), -- 위임구분 (PT, 그룹수업 등)
    position VARCHAR2(50), -- 직급
    is_active CHAR(1) DEFAULT 'Y', -- 현재 소속 여부 (Y/N)
    remark VARCHAR2(255) -- 비고
);

-- 4. PAYROLL (월별 페이롤)
CREATE TABLE PAYROLL (
    payroll_id NUMBER PRIMARY KEY, -- 페이롤 ID
    employee_branch_id NUMBER REFERENCES EMPLOYEE_BRANCH(employee_branch_id), -- 소속정보 ID
    payroll_month CHAR(7) NOT NULL, -- 정산 대상 년월 (YYYY-MM)
    base_salary NUMBER DEFAULT 0, -- 기본급
    incentive NUMBER DEFAULT 0, -- 인센티브
    total_allowance NUMBER DEFAULT 0, -- 총 수당합계
    total_deduction NUMBER DEFAULT 0, -- 총 공제합계
    net_payment NUMBER DEFAULT 0, -- 실지급액
    created_at DATE DEFAULT SYSDATE -- 생성일
);

-- 5. PAYROLL_ITEM (수당/공제 항목)
CREATE TABLE PAYROLL_ITEM (
    item_id NUMBER PRIMARY KEY, -- 항목 ID
    payroll_id NUMBER REFERENCES PAYROLL(payroll_id), -- 소속 페이롤 ID
    item_type VARCHAR2(10) CHECK (item_type IN ('수당', '공제')), -- 항목구분
    item_name VARCHAR2(100), -- 항목 이름 (예: 팀 커미션)
    amount NUMBER, -- 금액
    remark VARCHAR2(255) -- 비고
);

-- 6. SALARY_TRANSFER_LOG (급여이체내역)
CREATE TABLE SALARY_TRANSFER_LOG (
    transfer_id NUMBER PRIMARY KEY, -- 이체 ID
    payroll_id NUMBER REFERENCES PAYROLL(payroll_id), -- 관련 페이롤 ID
    employee_name VARCHAR2(50), -- 직원명 (실제 송금 대상)
    bank_name VARCHAR2(50), -- 입금 은행
    bank_account VARCHAR2(50), -- 입금 계좌번호
    salary_amount NUMBER, -- 급여금액
    sender_note VARCHAR2(255), -- 보내는 쪽 메모
    receiver_note VARCHAR2(255), -- 받는 쪽 메모
    transfer_date DATE -- 이체일
);

-- 7. PAYROLL_POLICY (페이롤 정책)
CREATE TABLE PAYROLL_POLICY (
    policy_id NUMBER PRIMARY KEY, -- 정책 ID
    effective_from DATE NOT NULL, -- 적용 시작일
    effective_to DATE, -- 적용 종료일
    rule_basis VARCHAR2(255), -- 정책 계산 기준 설명
    policy_description VARCHAR2(255) -- 정책 설명
);

-- 8. TOTAL_SALES_REPORT (월간 매출 보고서)
CREATE TABLE TOTAL_SALES_REPORT (
    report_id NUMBER PRIMARY KEY, -- 보고서 ID
    branch_id NUMBER REFERENCES BRANCH(branch_id), -- 대상 지점
    report_month CHAR(7) NOT NULL, -- 대상 월
    created_at DATE DEFAULT SYSDATE, -- 생성일
    created_by VARCHAR2(50) -- 작성자
);

-- 9. SALES_ITEM_TYPE (매출 항목 분류)
CREATE TABLE SALES_ITEM_TYPE (
    type_id NUMBER PRIMARY KEY, -- 유형 ID
    type_name VARCHAR2(50) CHECK (type_name IN ('수업', '결제', '환불', '기타')) -- 유형 이름
);

-- 10. SALES_ITEM_NAME (항목명 마스터)
CREATE TABLE SALES_ITEM_NAME (
    item_name_id NUMBER PRIMARY KEY, -- 항목명 ID
    type_id NUMBER REFERENCES SALES_ITEM_TYPE(type_id), -- 분류 ID
    item_name VARCHAR2(100) NOT NULL, -- 항목명 (예: PT, 요가)
    display_order NUMBER -- 표시순서
);

-- 11. TOTAL_SALES_ITEM (월별 항목별 매출 집계)
CREATE TABLE TOTAL_SALES_ITEM (
    sales_item_id NUMBER PRIMARY KEY, -- 매출 항목 ID
    report_id NUMBER REFERENCES TOTAL_SALES_REPORT(report_id), -- 보고서 ID
    item_name_id NUMBER REFERENCES SALES_ITEM_NAME(item_name_id), -- 항목명 ID
    amount NUMBER, -- 매출 금액
    remark VARCHAR2(255) -- 비고
);

-- 12. EMPLOYEE_SALES_CONTRIB (직원별 매출기여)
CREATE TABLE EMPLOYEE_SALES_CONTRIB (
    contribution_id NUMBER PRIMARY KEY, -- 기여 ID
    report_id NUMBER REFERENCES TOTAL_SALES_REPORT(report_id), -- 보고서 ID
    employee_id NUMBER REFERENCES EMPLOYEE(employee_id), -- 직원 ID
    item_name_id NUMBER REFERENCES SALES_ITEM_NAME(item_name_id), -- 항목명 ID
    amount NUMBER, -- 기여 금액
    rank NUMBER, -- 내부 랭킹
    remark VARCHAR2(255) -- 비고
);

-- 13. SESSION_LOG (수업 세션 기록)
CREATE TABLE SESSION_LOG (
    session_id NUMBER PRIMARY KEY, -- 세션 ID
    employee_id NUMBER REFERENCES EMPLOYEE(employee_id), -- 담당 직원 ID
    session_date DATE, -- 수업일자
    session_type VARCHAR2(50), -- 수업유형 (PT, GX 등)
    image_filename VARCHAR2(100), -- 저장된 이미지 파일명
    file_path VARCHAR2(255), -- 저장경로
    remark VARCHAR2(255) -- 비고
);

-- 14. SCHEDULE_UPLOAD (스케줄 이미지 업로드)
CREATE TABLE SCHEDULE_UPLOAD (
    schedule_id NUMBER PRIMARY KEY, -- 업로드 ID
    employee_id NUMBER REFERENCES EMPLOYEE(employee_id), -- 담당 직원 ID
    branch_id NUMBER REFERENCES BRANCH(branch_id), -- 해당 지점 ID
    schedule_month CHAR(7) NOT NULL, -- 해당 월
    image_filename VARCHAR2(100), -- 이미지 파일명
    file_path VARCHAR2(255), -- 저장 경로
    remark VARCHAR2(255) -- 비고
);
