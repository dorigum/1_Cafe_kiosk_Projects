package service;

import model.Member;
import model.Product;
import repository.CategoryRepository;
import repository.MemberRepository;
import repository.ProductRepository;
import repository.OrderRepository;
import java.util.List;
import java.util.Map;

public class AdminService {
    private ProductRepository productRepository;
    private MemberRepository memberRepository = new MemberRepository();
    private CategoryRepository categoryRepository = new CategoryRepository();
    private OrderRepository orderRepository = new OrderRepository();

    public AdminService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- 상품 관리 ---
    public void registerProduct(int categoryId, String name, int price, String description) {
        productRepository.addProduct(new Product(categoryId, name, price, description));
    }

    public List<Product> getProductList() {
        return productRepository.getAllProducts();
    }

    public void deleteProduct(int id) {
        productRepository.deleteProduct(id);
    }

    // --- 카테고리 관리 ---
    public void addCategory(String name) {
        categoryRepository.addCategory(name);
    }

    public List<String> getCategoryList() {
        return categoryRepository.getAllCategories();
    }

    public void deleteCategory(int id) {
        categoryRepository.deleteCategory(id);
    }

    // --- 회원 관리 ---
    public List<Member> getMemberList() {
        return memberRepository.getAllMembers();
    }

    public void deleteMember(long id) {
        memberRepository.deleteMember(id);
    }

    // --- 주문 관리 ---
    public List<String> getOrderList() {
        return orderRepository.getAllOrders();
    }

    public void cancelOrder(long orderId) {
        if (orderRepository.cancelOrder(orderId)) {
            System.out.println("주문이 취소되었습니다.");
        } else {
            System.out.println("취소 실패: 존재하지 않는 주문이거나 이미 취소된 주문입니다.");
        }
    }

    // --- 통계 기능 ---
    public void showStatistics() {
        System.out.println("\n===== [매출 통계 리포트] =====");
        
        // 1. 총 매출
        int totalSales = orderRepository.getTotalSales();
        System.out.printf("▶ 누적 총 매출액: %,d원\n", totalSales);

        // 2. 일별 매출 추이 (그래프)
        System.out.println("\n[일별 매출 추이 (최근 7일)]");
        Map<String, Integer> dailySales = orderRepository.getDailySales();
        if (dailySales.isEmpty()) {
            System.out.println("- 데이터 없음");
        } else {
            dailySales.forEach((date, sales) -> {
                String bar = "■".repeat(Math.min(20, sales / 1000)); // 1000원당 막대 하나
                System.out.printf("%s | %-20s (%,d원)\n", date, bar, sales);
            });
        }

        // 3. 카테고리별 매출
        System.out.println("\n[카테고리별 매출 현황]");
        Map<String, Integer> categorySales = orderRepository.getSalesByCategory();
        categorySales.forEach((cat, sales) -> 
            System.out.printf("- %-10s: %,d원\n", cat, sales));

        // 4. 인기 메뉴 Top 3
        System.out.println("\n[인기 메뉴 Top 3]");
        List<String> topMenus = orderRepository.getTopSellingMenus();
        topMenus.forEach(menu -> System.out.println("- " + menu));
    }
}
