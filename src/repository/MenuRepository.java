package repository;

import model.Menu;

import java.util.List;

public interface MenuRepository {
    void addMenu(Menu menu);

    List<Menu> getAllMenus();

    Menu findById(long id);

    void deleteMenu(long id);
}
