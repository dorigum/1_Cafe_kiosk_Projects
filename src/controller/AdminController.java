package controller;

import model.Member;
import model.Menu;
import model.Category;
import model.Order;
import service.AdminService;
import java.util.Scanner;
import java.util.List;

public class AdminController {
    private final AdminService adminService;
    private final Scanner scanner;

    public AdminController(AdminService adminService) {
        if (adminService == null) {
            throw new IllegalArgumentException("AdminService cannot be null");
        }
        this.adminService = adminService;
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    public void run() {
        while (true) {
            System.out.println("\n===== [관리자 통합 관리 모드] =====");
            System.out.println("1. 카테고리 관리 (CRUD)");
            System.out.println("2. 메뉴 관리 (CRUD)");
            System.out.println("3. 회원 관리 (조회/삭제)");
            System.out.println("4. 주문 관리 (취소)");
            System.out.println("5. 매출 통계 및 그래프");
            System.out.println("0. 메인 메뉴로 돌아가기");
            System.out.print("선택: ");
            
            String choiceStr = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해 주세요.");
                continue;
            }

            switch (choice) {
                case 1: categoryManagement(); break;
                case 2: menuManagement(); break;
                case 3: memberManagement(); break;
                case 4: orderManagement(); break;
                case 5: adminService.showStatistics(); break;
                case 0: return;
                default: System.out.println("잘못된 선택입니다.");
            }
        }
    }

    // --- 주문 관리 ---
    private void orderManagement() {
        while (true) {
            System.out.println("\n--- [전체 주문 목록] ---");
            try {
                List<Order> orders = adminService.getOrderList();
                if (orders.isEmpty()) {
                    System.out.println("주문 내역이 없습니다.");
                } else {
                    orders.forEach(order -> System.out.println(order));
                }
                
                System.out.println("\n1. 주문 취소 | 0. 뒤로");
                System.out.print("선택: ");
                String sub = scanner.nextLine();

                if ("1".equals(sub)) {
                    System.out.print("취소할 주문 ID: ");
                    try {
                        long orderId = Long.parseLong(scanner.nextLine());
                        adminService.cancelOrder(orderId);
                    } catch (NumberFormatException e) {
                        System.out.println("올바른 주문 ID(숫자)를 입력해 주세요.");
                    }
                } else if ("0".equals(sub)) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("주문 관리 중 오류 발생: " + e.getMessage());
                break;
            }
        }
    }

    // --- 카테고리 관리 ---
    private void categoryManagement() {
        while (true) {
            System.out.println("\n--- [카테고리 관리] ---");
            try {
                List<Category> categories = adminService.getCategoryList();
                if (categories.isEmpty()) {
                    System.out.println("카테고리가 없습니다.");
                } else {
                    categories.forEach(cat -> System.out.println(cat));
                }

                System.out.println("\n1. 추가 | 2. 삭제 | 0. 뒤로");
                System.out.print("선택: ");
                String sub = scanner.nextLine();

                if ("1".equals(sub)) {
                    System.out.print("새 카테고리명: ");
                    String name = scanner.nextLine();
                    if (name != null && !name.trim().isEmpty()) {
                        adminService.addCategory(name.trim());
                    } else {
                        System.out.println("카테고리 이름을 입력해 주세요.");
                    }
                } else if ("2".equals(sub)) {
                    System.out.print("삭제할 카테고리 ID: ");
                    try {
                        int catId = Integer.parseInt(scanner.nextLine());
                        adminService.deleteCategory(catId);
                    } catch (NumberFormatException e) {
                        System.out.println("올바른 ID(숫자)를 입력해 주세요.");
                    }
                } else if ("0".equals(sub)) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("카테고리 관리 중 오류 발생: " + e.getMessage());
                break;
            }
        }
    }

    // --- 메뉴 관리 ---
    private void menuManagement() {
        while (true) {
            System.out.println("\n--- [메뉴 관리] ---");
            try {
                List<Menu> menus = adminService.getMenuList();
                if (menus.isEmpty()) {
                    System.out.println("메뉴가 없습니다.");
                } else {
                    menus.forEach(menu -> System.out.println(menu));
                }

                System.out.println("\n1. 등록 | 2. 삭제 | 0. 뒤로");
                System.out.print("선택: ");
                String sub = scanner.nextLine();

                if ("1".equals(sub)) {
                    try {
                        System.out.println("\n[현재 카테고리 목록]");
                        adminService.getCategoryList().forEach(cat -> System.out.println(cat));
                        
                        System.out.print("카테고리 ID: ");
                        int catId = Integer.parseInt(scanner.nextLine());
                        System.out.print("메뉴명: ");
                        String name = scanner.nextLine();
                        System.out.print("가격: ");
                        int price = Integer.parseInt(scanner.nextLine());
                        System.out.print("설명: ");
                        String desc = scanner.nextLine();
                        
                        adminService.registerMenu(catId, name, price, desc);
                        System.out.println("메뉴가 등록되었습니다.");
                    } catch (NumberFormatException e) {
                        System.out.println("잘못된 숫자 형식입니다. 다시 입력해 주세요.");
                    }
                } else if ("2".equals(sub)) {
                    System.out.print("삭제할 메뉴 ID: ");
                    try {
                        long menuId = Long.parseLong(scanner.nextLine());
                        adminService.deleteMenu(menuId);
                    } catch (NumberFormatException e) {
                        System.out.println("올바른 ID(숫자)를 입력해 주세요.");
                    }
                } else if ("0".equals(sub)) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("메뉴 관리 중 오류 발생: " + e.getMessage());
                break;
            }
        }
    }

    // --- 회원 관리 ---
    private void memberManagement() {
        while (true) {
            System.out.println("\n--- [회원 관리] ---");
            try {
                List<Member> members = adminService.getMemberList();
                if (members.isEmpty()) {
                    System.out.println("회원 정보가 없습니다.");
                } else {
                    members.forEach(m -> System.out.println(m));
                }

                System.out.println("\n1. 삭제 | 0. 뒤로");
                System.out.print("선택: ");
                String sub = scanner.nextLine();

                if ("1".equals(sub)) {
                    System.out.print("삭제할 회원 ID: ");
                    try {
                        long memberId = Long.parseLong(scanner.nextLine());
                        adminService.deleteMember(memberId);
                    } catch (NumberFormatException e) {
                        System.out.println("올바른 ID(숫자)를 입력해 주세요.");
                    }
                } else if ("0".equals(sub)) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("회원 관리 중 오류 발생: " + e.getMessage());
                break;
            }
        }
    }
}
