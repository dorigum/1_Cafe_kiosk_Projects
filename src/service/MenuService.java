package service;

import model.Member;
import model.Menu;
import model.MenuOption;
import model.OptionGroup;
import model.OrderItem;

import java.util.List;

public interface MenuService {
    List<Menu> getMenusByCategory(String categoryName);

    List<OptionGroup> getOptionGroups(Menu menu);

    List<MenuOption> getOptionsByGroup(OptionGroup optionGroup);

    int placeOrder(List<OrderItem> orderItems, Member member);
}
