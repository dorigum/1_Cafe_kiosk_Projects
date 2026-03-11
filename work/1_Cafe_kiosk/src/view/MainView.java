package view;

import controller.AdminController;
import model.Member;
import repository.ProductRepository;
import service.AdminService;
import service.MemberService;
import java.util.Scanner;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class MainView {
    public static void main(String[] args) {
        // [강제 설정] 콘솔 출력을 UTF-8로 고정
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ProductRepository productRepository = new ProductRepository();
        AdminService adminService = new AdminService(productRepository);
        AdminController adminController = new AdminController(adminService);
        MemberService memberService = new MemberService();

        // [강제 설정] 입력(Scanner)을 UTF-8로 읽도록 고정
        Scanner scanner = new Scanner(System.in, "UTF-8");

        while (true) {
            System.out.println("\n[카페 키오스크 - UTF8 모드]");
            System.out.println("1. 회원 로그인 및 주문 내역 조회");
            System.out.println("2. 비회원 주문 (준비 중)");
            System.out.println("3. 관리자 모드 (상품 관리)");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택: ");
            
            String input = scanner.nextLine(); // 버퍼 문제 방지를 위해 nextLine으로 통일
            int mode;
            try {
                mode = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                continue;
            }

            if (mode == 1) {
                System.out.print("휴대폰 번호 (예: 010-1234-5678): ");
                String phone = scanner.nextLine();
                System.out.print("비밀번호: ");
                String password = scanner.nextLine();

                Member loggedInMember = memberService.login(phone, password);
                if (loggedInMember != null) {
                    System.out.println("\n--- 내 정보 ---");
                    System.out.println(loggedInMember);
                    
                    System.out.println("\n1. 주문 내역 보기");
                    System.out.println("2. 로그아웃");
                    System.out.print("선택: ");
                    String subInput = scanner.nextLine();
                    if ("1".equals(subInput)) {
                        memberService.showOrderHistory(loggedInMember);
                    }
                }
            } else if (mode == 2) {
                System.out.println("준비 중인 서비스입니다.");
            } else if (mode == 3) {
                adminController.run();
            } else if (mode == 0) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
        scanner.close();
    }
}
