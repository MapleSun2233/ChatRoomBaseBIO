package server;

import database.api.DAO;

import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel {
    private String[] columnNames = null;
    private String[] users = null;

    public UserTableModel() {
        this.columnNames = new String[]{"账户"};
        this.users = DAO.queryUsers();
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
