package com.project.javabank.dto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 자동이체 에러 시 에러로그 파일 저장 클래스
public class autoTransferLogger {
	
	//파일 경로
	private static final String LOG_FILE_PATH = System.getProperty("java.io.tmpdir") + "/autoTransfer_errors.log"; // C:\Users\MIN\AppData\Local\Temp
	
	public static void logFailedTransfer(String depositAccount, int balance, int payment) {
        // 현재 시간 추가 (YYYY-MM-DD HH:mm:ss 형식)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = String.format("[%s] 계좌번호: %s, 잔액: %d원, 필요 금액: %d원 - 자동이체 실패\n",
                                          timestamp, depositAccount, balance, payment);

        // 로그 파일에 기록
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(logMessage);
            System.out.println("로그 저장 성공: " + logMessage);  // 확인용 콘솔 출력
        } catch (IOException e) {
            System.err.println("로그 저장 실패: " + e.getMessage());
        }
    }
}
