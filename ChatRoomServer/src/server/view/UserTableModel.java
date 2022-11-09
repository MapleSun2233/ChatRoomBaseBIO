package server.view;

import database.UserDao;

import javax.swing.table.AbstractTableModel;

/**
 * 用户表视图
 */
public class UserTableModel extends AbstractTableModel {
    private final String[] columnNames;
    private final String[] users;

    public UserTableModel() {
        this.columnNames = new String[]{"账户"};
        this.users = UserDao.listUsernames();
    }

    @Override
    public int getRowCount() {
        return this.users.length;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.users[rowIndex];
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }
}
