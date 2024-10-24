DROP TABLE JB_USER CASCADE CONSTRAINTS;
DROP TABLE JB_DEPOSIT CASCADE CONSTRAINTS;
DROP TABLE JB_Dtransaction CASCADE CONSTRAINTS;
DROP TABLE JB_PRODUCT CASCADE CONSTRAINTS;
DROP TABLE JB_Ptransaction CASCADE CONSTRAINTS;
DROP TABLE JB_Alarm CASCADE CONSTRAINTS;

DROP SEQUENCE alarmSeq;
DROP SEQUENCE accountSeq;
DROP SEQUENCE productSeq;

CREATE SEQUENCE alarmSeq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE accountSeq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE productSeq START WITH 1 INCREMENT BY 1;

CREATE TABLE JB_User (
    userId VARCHAR2(60) PRIMARY KEY,
    UserPw VARCHAR2(60) NOT NULL,
    userName VARCHAR2(60) NOT NULL,
    userBirth DATE NOT NULL,
    userEmail VARCHAR2(100) NOT NULL,
    userTel VARCHAR2(100) NOT NULL,
    userRoles VARCHAR2(60) NOT NULL,
    userRegDate DATE DEFAULT SYSDATE
);

CREATE TABLE JB_Deposit (
    depositAccount VARCHAR2(100) PRIMARY KEY,
    userId VARCHAR2(60) NOT NULL,
    depositPw NUMBER NOT NULL,
    category VARCHAR2(40) NOT NULL,
    regDate DATE DEFAULT SYSDATE,
    interestRate NUMBER DEFAULT 0.1,
    transactionLimit NUMBER NOT NULL,
    mainAccount VARCHAR(10) NOT NULL,
    depositEnable VARCHAR(10) NOT NULL,
    CONSTRAINT fk_userId FOREIGN KEY (userId) REFERENCES JB_USER(userId)
);



CREATE TABLE JB_Product (
    productAccount VARCHAR2(100) PRIMARY KEY,
    userId VARCHAR2(60) NOT NULL,
    productPw NUMBER NOT NULL,
    category VARCHAR2(40) NOT NULL,
    autoTransferDate NUMBER,
    monthlyPayment NUMBER,
    payment NUMBER,
    regDate DATE DEFAULT SYSDATE,
    expiryDate DATE NOT NULL,
    interestRate NUMBER NOT NULL,
    depositAccount VARCHAR2(100) NOT NULL,
    productEnable VARCHAR2(20) NOT NULL,
    CONSTRAINT fk_pUserId FOREIGN KEY (userId) REFERENCES JB_USER(userId),
    CONSTRAINT fk_dAccount2 FOREIGN KEY (depositAccount) REFERENCES JB_DEPOSIT(depositAccount)
);

CREATE TABLE JB_Dtransaction (
    accountSeq NUMBER PRIMARY KEY,
    depositAccount VARCHAR2(100) NOT NULL,
    userId VARCHAR2(60) NOT NULL,
    updateDate DATE,
    type VARCHAR2(40) NOT NULL,
    memo VARCHAR2(40),
    deltaAmount NUMBER,
    balance NUMBER DEFAULT 0,
    transferAccount VARCHAR2(100),
    CONSTRAINT fk_dAccount FOREIGN KEY (depositAccount) REFERENCES JB_DEPOSIT(depositAccount),    
    CONSTRAINT fk_dUserId FOREIGN KEY (userId) REFERENCES JB_USER(userId)
);

CREATE TABLE JB_Ptransaction (
    productSeq NUMBER PRIMARY KEY,
    productAccount VARCHAR2(100) NOT NULL,
    updateDate DATE,
    type VARCHAR2(40) NOT NULL,
    memo VARCHAR2(40),
    deltaAmount NUMBER,
    balance NUMBER DEFAULT 0,
    CONSTRAINT fk_pAccount FOREIGN KEY (productAccount) REFERENCES JB_PRODUCT(productAccount)
);


CREATE TABLE JB_Alarm (
    alarmSeq NUMBER PRIMARY KEY,
    userId VARCHAR2(60) NOT NULL,
    alarmIsRead VARCHAR2(10) NOT NULL,
    alarmCate VARCHAR2(100),
    alarmCont VARCHAR2(200),
    alarmRegDate DATE,
    CONSTRAINT fk_userId_al FOREIGN KEY (userId) REFERENCES JB_USER(userId)
);

INSERT INTO JB_User (userId, userPw, userName, userBirth, userEmail, userTel, userRoles, userRegDate) 
VALUES ('testID', '$2a$10$McjJtxR8ikZl0/2UT.Lv.usk9lcmj5hGIORSaOwXNGX20QQGpdsAi', '테스트ID', TO_DATE('2024-10-01', 'YYYY-MM-DD'), 'test@test.com', '00000000000', 'USER', TO_DATE('2024-10-05', 'YYYY-MM-DD'));
